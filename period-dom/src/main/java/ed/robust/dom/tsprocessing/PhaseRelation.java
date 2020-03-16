/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ed.robust.dom.tsprocessing;

import static ed.robust.dom.tsprocessing.PhaseType.values;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author tzielins
 */
public enum PhaseRelation {

    RelativeToZero("zero"),
    RelativeToWindow("data window");
    
    public final String friendlyName;
    
    private static Map<PhaseRelation,String> friendlyNames;
    
    public static Map<PhaseRelation,String> getFriendlyNames() {
        if (friendlyNames == null) {
            Map<PhaseRelation,String> map = new EnumMap<>(PhaseRelation.class);
            for (PhaseRelation type : values())
                map.put(type,type.friendlyName);
            friendlyNames = Collections.unmodifiableMap(map);
        }
        return friendlyNames;        
    }
    
    PhaseRelation(String friendlyName) {
	this.friendlyName = friendlyName;
    }
    
    public String getFriendlyName() {
	return friendlyName;
    }


    public static double relativePhase(double phase,double period, double phaseRelation) {
        if (phaseRelation == 0) return phase;

        double reference = phaseRelation % period;
        double val = phase - reference;
        if (val < 0) val = val+period;
        return val % period;
        
    }
}
