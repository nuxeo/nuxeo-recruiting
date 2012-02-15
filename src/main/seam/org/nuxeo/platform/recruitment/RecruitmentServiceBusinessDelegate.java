/**
 * 
 */

package org.nuxeo.platform.recruitment;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Unwrap;
import org.nuxeo.runtime.api.Framework;

import org.nuxeo.platform.recruitment.RecruitmentService;

/**
 *
 * This is a simple service wrapper that allow to inject a Nuxeo Runtime service as a Seam component
 *
 */
@Name("recruitmentService")
@Scope(ScopeType.EVENT)
public class RecruitmentServiceBusinessDelegate implements Serializable {

    private static final long serialVersionUID = 1L;

    protected RecruitmentService recruitmentService;

    @Unwrap
    public RecruitmentService getService() throws Exception {
        if (recruitmentService == null) {
            recruitmentService = Framework.getService(RecruitmentService.class);
        }
        return recruitmentService;
    }

}
