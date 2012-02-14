package org.nuxeo.platform.recruitment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.runtime.api.Framework;

public class DocumentCreator extends UnrestrictedSessionRunner {

    protected static final String ACL_KEY = "local-rh";
    
    protected Map<String, Serializable> properties = new HashMap<String, Serializable>();

    protected String name;

    protected String parentPath;

    protected Set<ACE> aces = new HashSet<ACE>();
    
    protected String type;
    
    protected DocumentModel doc;

    public DocumentCreator(String type) {
        super(Framework.getLocalService(RepositoryManager.class).getDefaultRepository().getName());
        this.type = type;
    }

    @Override
    public void run() throws ClientException {
        DocumentModel doc = session.createDocumentModel(type);
        doc.setPathInfo(parentPath, name);
        
        for (String key : properties.keySet()) {
            doc.setPropertyValue(key, properties.get(key));
        }
        
        doc = session.createDocument(doc);
        ACP acp = doc.getACP();
        for (ACE ace : aces) {
            acp.getOrCreateACL(ACL_KEY).add(ace);
        }
        session.setACP(doc.getRef(), acp, true);
        
        doc = session.getDocument(doc.getRef());
        ((DocumentModelImpl)doc).detach(true);
        this.doc = doc;
    }

    public void addACE(ACE ace) {
        this.aces.add(ace);
    }

    public void addProperty(String key, Serializable value) {
        properties.put(key, value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }
    
    public DocumentModel getDocumentModel() {
        return doc;
    }
}
