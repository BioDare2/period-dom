/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ed.robust.dom.tsprocessing;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author tzielins
 */
public enum PhaseType {

    ByMethod("method specific"),
    ByFit("by fit"),
    ByFirstPeak("by first peak"),
    ByAvgMax("by averaged peaks");
    
    public final String friendlyName;

    private static Map<PhaseType,String> friendlyNames;
    
    public static Map<PhaseType,String> getFriendlyNames() {
        if (friendlyNames == null) {
            Map<PhaseType,String> map = new EnumMap<>(PhaseType.class);
            for (PhaseType type : values())
                map.put(type,type.friendlyName);
            friendlyNames = Collections.unmodifiableMap(map);
        }
        return friendlyNames;        
    }
    
    PhaseType(String friendlyName) {
	this.friendlyName = friendlyName;
    }
    
    public String getFriendlyName() {
	return friendlyName;
    }

    

}
