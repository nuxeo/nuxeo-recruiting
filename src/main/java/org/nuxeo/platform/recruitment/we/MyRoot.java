/**
 * 
 */

package org.nuxeo.platform.recruitment.we;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ui.web.tag.fn.DocumentModelFunctions;
import org.nuxeo.ecm.platform.ui.web.util.BaseURL;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;
import org.nuxeo.platform.recruitment.RecruitmentService;
import org.nuxeo.runtime.api.Framework;

/**
 * The root entry for the WebEngine module.
 * 
 * @author arnaud
 */
@Path("/recruitment")
@Produces("text/html;charset=UTF-8")
@WebObject(type = "MyRoot")
public class MyRoot extends ModuleRoot {
    private static final Log log = LogFactory.getLog(MyRoot.class);

    @GET
    public Object doIndex() {
        try {
            RecruitmentService service = Framework.getLocalService(RecruitmentService.class);
            return getView("index").arg("jobs", service.getJobs()).arg(
                    "baseUrl", BaseURL.getBaseURL(request));
        } catch (ClientException e) {
            log.warn(e, e);
            return null;
        }
    }

    @GET
    @Path("job/{id}")
    public Object doJobDetail(@PathParam("id")
    String jobId) {
        try {
            RecruitmentService service = Framework.getLocalService(RecruitmentService.class);
            return getView("jobDetail").arg("job", service.getJob(jobId));
        } catch (ClientException e) {
            log.warn(e, e);
            return null;
        }
    }

    @POST
    @Path("job/{id}")
    public Object doJobApply(@PathParam("id")
    String jobId, @FormParam("firstname")
    String firstname, @FormParam("lastname")
    String lastname, @FormParam("email")
    String email) {
        String errorMessage = null;
        DocumentModel doc = null;
        String documentUrl = null;
        try {
            RecruitmentService service = Framework.getLocalService(RecruitmentService.class);
            doc = service.createApplicationForUser(jobId, firstname, lastname,
                    email, ctx.getRequest());
            documentUrl = RecruitmentService.getCASLessUrlForDoc(doc,
                    ctx.getRequest());
        } catch (ClientException e) {
            log.warn(e, e);
            errorMessage = e.getMessage();
        }
        return getView("applicationResult").arg("application", doc).arg(
                "error", errorMessage).arg("applyUrl", documentUrl);
    }
}
