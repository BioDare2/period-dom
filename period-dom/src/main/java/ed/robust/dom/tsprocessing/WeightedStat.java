/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.dom.tsprocessing;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import com.fasterxml.jackson.annotation.JsonInclude;
import static ed.robust.dom.tsprocessing.WeightingType.ByGOF;
import static ed.robust.dom.tsprocessing.WeightingType.ByJERR;
import static ed.robust.dom.tsprocessing.WeightingType.BySpecERR;
import static ed.robust.dom.tsprocessing.WeightingType.None;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author tzielins
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "orgStat",
    "gofWeighted",
    "jerrWeighted",
    "specWeighted",
})
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeightedStat {
    
    @XmlElement(name="orgStat")
    Statistics orgStat;
    
    @XmlElement(name="gofWeighted")
    Statistics gofWeighted;
    
    @XmlElement(name="jerrWeighted")
    Statistics jerrWeighted;

    @XmlElement(name="specWeighted")
    Statistics specWeighted;
    
    public Statistics getStat(WeightingType type)     {
        switch(type) {
            case None : return getOrgStat();
            case ByGOF : return getGofWeighted();
            case ByJERR: return getJerrWeighted();
            case BySpecERR: return getSpecWeighted();
            default: throw new IllegalArgumentException("Unsupported weighting type: "+type);
        }
    }
    
    public void setStat(Statistics stat,WeightingType type)     {
        if (stat == null) throw new IllegalArgumentException("Stats cannot be null");
        switch(type) {
            case None : setOrgStat(stat); return;
            case ByGOF : setGofWeighted(stat); return;
            case ByJERR: setJerrWeighted(stat); return;
            case BySpecERR: setSpecWeighted(stat); return;
            default: throw new IllegalArgumentException("Unsupported weighting type: "+type);
        }
    }

    public Statistics getOrgStat() {
        return orgStat;
    }

    public void setOrgStat(Statistics orgStat) {
        this.orgStat = orgStat;
    }

    public Statistics getGofWeighted() {
        return gofWeighted;
    }

    public void setGofWeighted(Statistics gofWeighted) {
        this.gofWeighted = gofWeighted;
    }

    public Statistics getJerrWeighted() {
        return jerrWeighted;
    }

    public void setJerrWeighted(Statistics jerrWeighted) {
        this.jerrWeighted = jerrWeighted;
    }

    public Statistics getSpecWeighted() {
        return specWeighted;
    }

    public void setSpecWeighted(Statistics specWeighted) {
        this.specWeighted = specWeighted;
    }
    
    public double getMean(WeightingType weigthing) {
        return getStat(weigthing).getMean();
    }
    
    public double getStdDev(WeightingType weigthing) {
        return getStat(weigthing).getStdDev();
    }
    
    public double getStdErr(WeightingType weigthing) {
        return getStat(weigthing).getStdErr();
    }
    
    public long getN(WeightingType weigthing) {
        return getStat(weigthing).getN();
    }
}
