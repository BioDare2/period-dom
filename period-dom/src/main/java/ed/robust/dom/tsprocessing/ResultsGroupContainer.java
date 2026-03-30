/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ed.robust.dom.tsprocessing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tzielins
 */
@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
public class ResultsGroupContainer implements Iterable<ResultsGroup> {

    List<ResultsGroup> groups = new ArrayList<>();

    public List<ResultsGroup> getGroups() {
	return groups;
    }

    public void add(ResultsGroup group) {
	groups.add(group);
    }

    @Override
    public Iterator<ResultsGroup> iterator() {
	return groups.iterator();
    }



}
