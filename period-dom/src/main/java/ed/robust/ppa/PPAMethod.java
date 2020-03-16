/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.ppa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author tzielins
 */
public enum PPAMethod {
    
    NLLS("FFT NLLS","FFT"),
    MFourFit("MFourFit","MFF"),
    MESA("MESA","MESA"),
    EPR("ER Periodogram","EPR"),
    EPR_ND("ER Periodogram (no DT)","EPR_ND",false),
    LSPR("LS Periodogram","LSPR"),
    SR("Spectrum Resampling","SR"),
    SR_LD("Spectrum Resampling (lin DT)","SR_LD",false),
    FAKE("Fake method","FAKE",false);

    public final String friendlyName;
    public final String shortName;
    public final boolean on;
    
    static List<PPAMethod> onMethods;

    PPAMethod(String friendlyName,String shortName) {
        this(friendlyName,shortName,true);
    }
    
    PPAMethod(String friendlyName,String shortName,boolean on) {
	this.friendlyName = friendlyName;
        this.shortName = shortName;
        this.on = on;
    }
    

    public String getFriendlyName() {
	return friendlyName;
    }

    @Override
    public String toString() {
	return friendlyName;
    }

    public static List<PPAMethod> getOnMethods() {
        if (onMethods == null) {
            List<PPAMethod> list = new ArrayList<>(values().length);
            for (PPAMethod m : values()) if (m.on) list.add(m);
            onMethods = Collections.unmodifiableList(list);
        }
        return onMethods;
    }


}
