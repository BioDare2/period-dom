/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ed.robust.dom.tsprocessing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author tzielins
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class ResultsEntry {
    
    @XmlAttribute
    public long jobId;
    @XmlAttribute
    public long dataId;
    @XmlAttribute
    public String dataType;
    
    //new attributes to flatten the structure
    @XmlAttribute(name="orgId")
    public String orgId;
    @XmlAttribute(name="rawId")
    public long rawDataId;    
    @XmlAttribute(name="biolId")
    public long biolDescId;
    @XmlAttribute(name="envId")
    public long environmentId;
    @XmlAttribute
    public boolean ignored;
    

    PPAResult result;

    public long getDataId() {
	return dataId;
    }

    public void setDataId(long dataId) {
	this.dataId = dataId;
    }

    public String getDataType() {
	return dataType;
    }

    public void setDataType(String dataType) {
	this.dataType = dataType;
    }

    public long getJobId() {
	return jobId;
    }

    public void setJobId(long jobId) {
	this.jobId = jobId;
    }

    
    

    public PPAResult getResult() {
	return result;
    }

    public void setResult(PPAResult result) {
	this.result = result;
    }

    boolean matches(ResultsEntry other) {
	if (jobId != other.jobId) return false;
	if (dataId != other.dataId) return false;
	if (!dataType.equals(other.dataType)) return false;
	return true;
    }

    void merge(ResultsEntry other) {
	result = other.result;
    }


    
}
