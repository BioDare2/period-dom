/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.dom.tsprocessing;

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
    "GOF",
    "joinedError"
})
public class PPAStatSummary {
    
	@XmlElement(name="orgStat")
	PPAStat orgStat;
    
	@XmlElement(name="gofWeighted")
	PPAStat gofWeighted;
    
	@XmlElement(name="jerrWeighted")
	PPAStat jerrWeighted;
        
	@XmlElement(name="specWeighted")
	PPAStat specWeighted;
        
	@XmlElement(name="GOF")
        Statistics GOF;
	@XmlElement(name="joinedError")
        Statistics joinedError;
        
    public PPAStat getPPAStat(WeightingType type)     {
        switch(type) {
            case None : return getOrgStat();
            case ByGOF : return getGofWeighted();
            case ByJERR: return getJerrWeighted();
            case BySpecERR: return getSpecWeighted();
            default: throw new IllegalArgumentException("Unsupported weighting type: "+type);
        }
    }
    
    public void setPPAStat(PPAStat stat,WeightingType type)     {
        switch(type) {
            case None : setOrgStat(stat); return;
            case ByGOF : setGofWeighted(stat); return;
            case ByJERR: setJerrWeighted(stat); return;
            case BySpecERR: setSpecWeighted(stat); return;
            default: throw new IllegalArgumentException("Unsupported weighting type: "+type);
        }
    }
    

    public PPAStat getOrgStat() {
        return orgStat;
    }

    public void setOrgStat(PPAStat orgStat) {
        this.orgStat = orgStat;
    }


    public Statistics getGOF() {
        return GOF;
    }

    public void setGOF(Statistics GOF) {
        this.GOF = GOF;
    }

    public Statistics getJoinedError() {
        return joinedError;
    }

    public void setJoinedError(Statistics joinedError) {
        this.joinedError = joinedError;
    }

    public PPAStat getGofWeighted() {
        return gofWeighted;
    }

    public void setGofWeighted(PPAStat gofWeighted) {
        this.gofWeighted = gofWeighted;
    }

    public PPAStat getJerrWeighted() {
        return jerrWeighted;
    }

    public void setJerrWeighted(PPAStat jerrWeighted) {
        this.jerrWeighted = jerrWeighted;
    }

    public PPAStat getSpecWeighted() {
        return specWeighted;
    }

    public void setSpecWeighted(PPAStat specWeighted) {
        this.specWeighted = specWeighted;
    }

        
        
}
