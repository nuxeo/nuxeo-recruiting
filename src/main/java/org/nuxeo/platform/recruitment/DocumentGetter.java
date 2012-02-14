package org.nuxeo.platform.recruitment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.runtime.api.Framework;

public class DocumentGetter extends UnrestrictedSessionRunner {
    private static final Log log = LogFactory.getLog(DocumentGetter.class);

    protected DocumentRef docRef;

    protected DocumentModel doc;

    public static DocumentModel get(DocumentRef docRef) {
        DocumentGetter getter = new DocumentGetter(docRef);
        try {
            getter.runUnrestricted();
            return getter.getDoc();
        } catch (ClientException e) {
            log.debug("Unable to fetch document with id: " + docRef);
            return null;
        }
    }

    protected DocumentGetter(DocumentRef docRef) {
        super(Framework.getLocalService(RepositoryManager.class).getDefaultRepository().getName());
        this.docRef = docRef;
    }

    protected DocumentModel getDoc() {
        return doc;
    }

    @Override
    public void run() throws ClientException {
        DocumentModel doc = session.getDocument(docRef);
        ((DocumentModelImpl) doc).detach(true);
        this.doc = doc;
    }
}
