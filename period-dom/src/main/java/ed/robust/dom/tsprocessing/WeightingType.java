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
public enum WeightingType {
 
    None("none"),
    ByGOF("by gof"),
    ByJERR("by err"),
    BySpecERR("by property");
    
    public final String friendlyName;

    private static Map<WeightingType,String> friendlyNames;
    
    public static Map<WeightingType,String> getFriendlyNames() {
        if (friendlyNames == null) {
            Map<WeightingType,String> map = new EnumMap<>(WeightingType.class);
            for (WeightingType type : values())
                map.put(type,type.friendlyName);
            friendlyNames = Collections.unmodifiableMap(map);
        }
        return friendlyNames;        
    }
    
    WeightingType(String friendlyName) {
	this.friendlyName = friendlyName;
    }
    
    public String getFriendlyName() {
	return friendlyName;
    }
    
    
}
