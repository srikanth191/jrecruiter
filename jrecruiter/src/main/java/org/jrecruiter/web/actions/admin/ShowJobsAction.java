/*
*	http://www.jrecruiter.org
*
*	Disclaimer of Warranty.
*
*	Unless required by applicable law or agreed to in writing, Licensor provides
*	the Work (and each Contributor provides its Contributions) on an "AS IS" BASIS,
*	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied,
*	including, without limitation, any warranties or conditions of TITLE,
*	NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE. You are
*	solely responsible for determining the appropriateness of using or
*	redistributing the Work and assume any risks associated with Your exercise of
*	permissions under this License.
*
*/
package org.jrecruiter.web.actions.admin;

import java.util.List;

import org.jrecruiter.common.CollectionUtils;
import org.jrecruiter.model.Job;
import org.jrecruiter.web.actions.BaseAction;
import org.jrecruiter.web.interceptor.RetrieveMessages;
import org.jrecruiter.web.interceptor.StoreMessages;
import org.texturemedia.smarturls.ActionName;
import org.texturemedia.smarturls.ActionNames;
import org.texturemedia.smarturls.Result;
import org.texturemedia.smarturls.Results;

import com.opensymphony.xwork2.Preparable;

/**
 * List all the jobs.
 *
 * @author Gunnar Hillert
 * @version $Id$
 *
 */
@ActionNames({
	@ActionName(name="show-jobs-ajax",  method="executeAjaxJobsTable"),
    @ActionName(name="show-jobs",       method="execute"),
    @ActionName(name="delete-jobs",     method="delete"),
    @ActionName(name="downloadLogFile", method="download")
})
@Results(
    {
        @Result(name="inputRedirected", location="show-jobs", type="redirectAction"),
        @Result(name="success",         location="index", type="redirectAction"),
        @Result(name="ajaxJobsTable",   location="/WEB-INF/jsp/admin/joblistTable.jsp")
    }
)
public class ShowJobsAction extends BaseAction implements Preparable {

    /** serialVersionUID. */
    private static final long serialVersionUID = -6536348867574805926L;

    private List<Job> jobs;

    private List<Long> jobsToDelete = CollectionUtils.getArrayList();

    //~~~~~Action Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     *
     */
    @RetrieveMessages
    public String execute() {
        return INPUT;
    }

    public String executeAjaxJobsTable() {
        return "ajaxJobsTable";
    }

    public void prepare() throws Exception {
    	this.jobs = jobService.getUsersJobs(super.getLoggedInUser().getUsername());
    }

    /**
     *  Delete any selected jobs.
     */
    @StoreMessages
    public String delete() {

    	//TODO improve security
    	
         if(!jobsToDelete.isEmpty()){
        	 
         	 int validJobIds = 0;
        	 
             for (Long jobId : jobsToDelete) {
                 if (jobId != null) {
                     jobService.deleteJobForId(jobId);
                     validJobIds++;
                 }
             }
             
             if (validJobIds == 1) {
                 super.addActionMessage(getText("job.delete.success.one", new String[]{String.valueOf(validJobIds)}));
             } else {
                 super.addActionMessage(getText("job.delete.success.multiple", new String[]{String.valueOf(validJobIds)}));
             }
             
         } else {
             super.addActionMessage("No Job Posting was deleted.");
         }
         
         return "inputRedirected";
    }

    //~~~~~Getters and Setters~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
    
    public List<Long> getJobsToDelete() {
        return jobsToDelete;
    }

    public void setJobsToDelete(List<Long> jobsToDelete) {
        this.jobsToDelete = jobsToDelete;
    }
}
