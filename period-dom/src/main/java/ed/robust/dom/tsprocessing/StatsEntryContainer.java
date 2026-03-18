/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ed.robust.dom.tsprocessing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tzielins
 */
@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
public class StatsEntryContainer implements Iterable<StatsEntry>{

    @XmlElement(name="entry")
    List<StatsEntry> entries = new ArrayList<>();

    public List<StatsEntry> getEntries() {
	return entries;
    }

    public void setEntries(List<StatsEntry> entries) {
	this.entries = entries;
    }

    /*
    public void add(StatsEntry entry) {
	entries.add(entry);
    }*/

    @Override
    public Iterator<StatsEntry> iterator() {
	return getEntries().iterator();
    }

    public StatsEntry get(long jobId) {
	for (StatsEntry entry : entries) {
	    if (entry.getJobId()==jobId) return entry;
	}
	return null;
    }

    /*public void remove(StatsEntry entry) {
	entries.remove(entry);
    }*/

    public void put(StatsEntry newEntry) {

	int index = entries.size();
	for (int i = index-1;i>=0;i--) {
	    if (entries.get(i).getJobId() == newEntry.getJobId()) {
		index = i;
		break;
	    }
	}
	if (index < entries.size()) entries.remove(index);
	entries.add(index,newEntry);
    }

}
