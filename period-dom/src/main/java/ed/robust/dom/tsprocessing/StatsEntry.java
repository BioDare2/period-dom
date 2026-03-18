/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ed.robust.dom.tsprocessing;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tzielins
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatsEntry implements Iterable<PPAStats> {

    @XmlAttribute
    long jobId;
    
    @XmlAttribute
    String uuid;
    

    @XmlElement(name="stats")
    List<PPAStats> stats = new ArrayList<>();
    
    public StatsEntry() {};
    
    public StatsEntry(long jobId) {
        this.jobId = jobId;
    }
    
    public StatsEntry(UUID jobId) {
        this.jobId = uuid2long(jobId);
        this.uuid = jobId.toString();
    }    


    public List<PPAStats> getStats() {
	return stats;
    }

    public void setStats(List<PPAStats> stats) {
	this.stats = stats;
    }

    public long getJobId() {
	return jobId;
    }

    public void setJobId(long jobId) {
	this.jobId = jobId;
    }

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid.toString();
    }
    
    

    public void add(PPAStats stat) {
	stats.add(stat);
    }

    @Override
    public Iterator<PPAStats> iterator() {
	return getStats().iterator();
    }

    public static final long uuid2long(UUID id) {
        return id.getLeastSignificantBits()+id.getMostSignificantBits();
    } 

}
