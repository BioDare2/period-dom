/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ed.robust.dom.tsprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author tzielins
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class ResultsGroup implements Iterable<ResultsEntry>, Comparable<ResultsGroup> {

    @XmlAttribute(name="orgId")
    protected String orgId;
    @XmlAttribute(name="biolId")
    protected long biolDescId;
    @XmlAttribute(name="envId")
    protected long environmentId;
    @XmlAttribute
    boolean ignored;
    
    @XmlElement(name="ppa")
    protected List<ResultsEntry> results = new ArrayList<>();

    transient private Map<Long,ResultsEntry> resultsById;
    transient private boolean parsed = false;
    transient private Integer numOrgId;

    public long getBiolDescId() {
	return biolDescId;
    }

    public void setBiolDescId(long biolDescId) {
	this.biolDescId = biolDescId;
    }

    public long getEnvironmentId() {
	return environmentId;
    }

    public void setEnvironmentId(long environmentId) {
	this.environmentId = environmentId;
    }

    public Integer getNumOrgId() {
	if (!parsed) {
	    try {
		numOrgId = Integer.parseInt(orgId);
	    } catch (Exception e) {}
	    parsed = true;
	}
	return numOrgId;
    }


    public String getOrgId() {
	return orgId;
    }

    public void setOrgId(String orgId) {
	this.orgId = orgId;
	parsed = false;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }


    
    public List<ResultsEntry> getResults() {
	return results;
    }

    public void setResults(List<ResultsEntry> results) {
	this.results = results;
	this.resultsById = null;
    }

    public void add(ResultsEntry entry) {
	this.results.add(entry);
	getResultsById().put(entry.getJobId(), entry);
    }

    private Map<Long,ResultsEntry> getResultsById() {
	if (resultsById == null) {
	    resultsById = new HashMap<>();
	    for (ResultsEntry entry : results) resultsById.put(entry.getJobId(),entry);
	}
	return resultsById;
    }

    @Override
    public Iterator<ResultsEntry> iterator() {
	return results.iterator();
    }

    @Override
    public int compareTo(ResultsGroup o) {
	Integer i1 = getNumOrgId();
	Integer i2 = o.getNumOrgId();
	if (i1 != null & i2 != null) return i1.compareTo(i2);

	if (orgId != null) return orgId.compareTo(o.orgId);
	return (o.orgId == null) ? 0 : -1;
    }

    public boolean matches(ResultsGroup other) {
	if (!orgId.equals(other.orgId)) return false;
	if (biolDescId != other.biolDescId) return false;
	if (environmentId != other.environmentId) return false;
	return true;
    }

    public void merge(ResultsGroup other) {

	for (ResultsEntry otherEntry : other) {
	    ResultsEntry mine = getResultsById().get(otherEntry.getJobId());
	    if (mine != null && mine.matches(otherEntry)) mine.merge(otherEntry);
	    else add(otherEntry);
	}
    }
}
