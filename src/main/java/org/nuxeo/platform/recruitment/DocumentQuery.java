package org.nuxeo.platform.recruitment;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.runtime.api.Framework;

public class DocumentQuery extends UnrestrictedSessionRunner {

    protected String query;

    protected DocumentModelList docs;

    public DocumentQuery(String query) {
        super(Framework.getLocalService(RepositoryManager.class).getDefaultRepository().getName());
        this.query = query;
    }

    @Override
    public void run() throws ClientException {
        docs = session.query(query);
        for (DocumentModel doc : docs) {
            ((DocumentModelImpl)doc).detach(true);
        }
    }

    public DocumentModelList getDocuments() {
        return docs;
    }
}
