/**
 * 
 */

package org.nuxeo.platform.recruitment;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.core.operations.services.UserWorkspace;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoGroup;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.platform.userworkspace.api.UserWorkspaceService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * Service to perform recruitment operations like user creation or jobs listing.
 * Everything is done using an {@link UnrestrictedSessionRunner} as the service
 * should be accessible without any rights.
 * 
 * @author Arnaud Kervern <akervern@nuxeo.com>
 */
public class RecruitmentService extends DefaultComponent {
    private static final Log log = LogFactory.getLog(RecruitmentService.class);

    private static final String PASSWORD_CHARS = "abcdefghijklmnoprstuvxywzABCDEFGHIJKLMNOPRTSUVXYWZ1234567890";

    protected static final String GROUP_APPLY_NAME = "Candi_apply";

    protected static final String JOB_SCHEMA_NAME = "job_container";

    protected static final String JOB_CONTAINER_PARENT = "/";

    protected static final String JOB_CONTAINER_NAME = "jobContainer";

    protected static final String JOB_CONTAINER_PATH = JOB_CONTAINER_PARENT
            + JOB_CONTAINER_NAME;

    /**
     * List all jobs containing into the JobContainer.
     */
    public DocumentModelList getJobs() throws ClientException {
        DocumentModel container = getOrCreateJobContainer();
        DocumentQuery query = new DocumentQuery(
                "Select * from Job where ecm:parentId = '" + container.getId()
                        + "'");
        query.runUnrestricted();
        return query.getDocuments();
    }

    /**
     * @return the default apply group name
     */
    public String getApplyGroupName() {
        return GROUP_APPLY_NAME;
    }

    /**
     * @return the default human resources group name
     */
    public String getHumanResourcesGroupName() {
        return "RH";
    }

    /**
     * Get a simple Document
     */
    public DocumentModel getJob(String jobId) throws ClientException {
        return DocumentGetter.get(new IdRef(jobId));
    }

    /**
     * Create a new application for the user using his firsname, lastname and
     * email to build a username. It creates a new Application document and add
     * ACE to it
     */
    public DocumentModel createApplicationForUser(String jobId,
            String firstname, String lastname, String email)
            throws ClientException {

        // send event BeforeApplicationCreate

        DocumentModel job = DocumentGetter.get(new IdRef(jobId));
        DocumentModel user = createNewApply(job, firstname, lastname, email);

        DocumentCreator creator = new DocumentCreator("Application");
        String applyName = "pouet_"
                + user.getProperty(getUM().getUserSchemaName(),
                        getUM().getUserEmailField());
        creator.setParentPath(job.getPathAsString());
        creator.setName(applyName);
        creator.addProperty("dc:title", applyName);
        creator.addACE(new ACE(user.getId(), SecurityConstants.READ_WRITE, true));
        creator.addACE(new ACE(user.getId(), SecurityConstants.WRITE_SECURITY, true));
        creator.runUnrestricted();

        // Send user password
        log.warn("User: " + user.getId() + " Pwd: "
                + user.getProperty(getUM().getUserSchemaName(), "password"));

        DocumentModel application = creator.getDocumentModel();

        // send event ApplicationCreated event

        return application;
    }

    protected DocumentModel createNewApply(DocumentModel job, String firstname,
            String lastname, String email) throws ClientException {
        String username = generateLogin(job, email);
        if (getUM().searchUsers(username).size() > 0) {
            throw new ClientException("User: " + username + " already exists");
        }

        DocumentModel newUser = getUM().getBareUserModel();
        newUser.setProperty(getUM().getUserSchemaName(),
                getUM().getUserIdField(), generateLogin(job, email));
        newUser.setProperty(getUM().getUserSchemaName(),
                getUM().getUserEmailField(), email);
        newUser.setProperty(getUM().getUserSchemaName(), "password",
                generatePasswd());
        newUser.setProperty(getUM().getUserSchemaName(), "lastName", lastname);
        newUser.setProperty(getUM().getUserSchemaName(), "firstName", firstname);
        newUser = getUM().createUser(newUser);

        log.debug("Create recruitment user with id: " + newUser.getId());

        addApplyToGroup(newUser);

        return newUser;
    }

    protected void addApplyToGroup(DocumentModel newUser) {
        try {
            DocumentModel group = getUM().getGroupModel(GROUP_APPLY_NAME);
            if (group == null) {
                DocumentModel applyGroup = getUM().getBareGroupModel();
                applyGroup.setProperty(getUM().getGroupSchemaName(),
                        getUM().getGroupIdField(), GROUP_APPLY_NAME);
                group = getUM().createGroup(applyGroup);
            }
            // Add new apply member
            List<String> members = (List<String>) group.getProperty(
                    getUM().getGroupSchemaName(),
                    getUM().getGroupMembersField());
            members.add(newUser.getId());
            group.setProperty(getUM().getGroupSchemaName(),
                    getUM().getGroupMembersField(), members);

            // Save changes
            getUM().updateGroup(group);

            log.debug("Member " + newUser.getId() + " added to group "
                    + group.getId());
        } catch (ClientException e) {
            log.warn("Unable to get group for user registration", e);
            log.debug(e, e);
        }
    }

    protected DocumentModel getOrCreateJobContainer() throws ClientException {
        DocumentModel container = DocumentGetter.get(new PathRef(
                JOB_CONTAINER_PATH));
        // Create it if null
        if (container == null) {
            DocumentCreator creator = new DocumentCreator("JobContainer");
            creator.setName(JOB_CONTAINER_NAME);
            creator.setParentPath(JOB_CONTAINER_PARENT);
            creator.addProperty("dc:title", "JobContainer");
            creator.addACE(new ACE(getHumanResourcesGroupName(),
                    SecurityConstants.WRITE_SECURITY, true));
            creator.runUnrestricted();
            container = creator.getDocumentModel();
            log.info("JobContainer created");
        }
        return container;
    }

    protected static UserManager getUM() {
        return Framework.getLocalService(UserManager.class);
    }

    protected static UserWorkspaceService getUW() {
        return Framework.getLocalService(UserWorkspaceService.class);
    }

    protected static String generateLogin(DocumentModel job, String email)
            throws ClientException {
        String jobNr = (String) job.getPropertyValue(JOB_SCHEMA_NAME
                + ":number");
        return String.format("Candi_%s_%s", jobNr, email);
    }

    protected static String generatePasswd() {
        return RandomStringUtils.random(5, PASSWORD_CHARS);
    }
}
