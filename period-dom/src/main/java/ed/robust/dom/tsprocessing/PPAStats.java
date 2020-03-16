/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.dom.tsprocessing;

import static ed.robust.dom.tsprocessing.PhaseType.ByAvgMax;
import static ed.robust.dom.tsprocessing.PhaseType.ByFirstPeak;
import static ed.robust.dom.tsprocessing.PhaseType.ByFit;
import static ed.robust.dom.tsprocessing.PhaseType.ByMethod;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author tzielins
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "biolDescId",
    "environmentId",
    "memberDataId",
    "rawId",
    "periodStats",
    "phaseStatsByMethod",
    "phaseStatsByFit",
    "phaseStatsByFirstPeak",
    "phaseStatsByAvgMax",
    "ampStatsByMethod",
    "ampStatsByFit",
    "ampStatsByFirstPeak",
    "ampStatsByAvgMax",
    "GOFStats",
    "joinedErrorStats",
    "phaseStatsByMethodCirc",
    "phaseStatsByFitCirc",
    "phaseStatsByFirstPeakCirc",
    "phaseStatsByAvgMaxCirc"
    
})
public class PPAStats {


	@XmlAttribute(name="biolId")
	long biolDescId;
	@XmlAttribute(name="envId")
	long environmentId;
        
	@XmlAttribute(name="memberDataId")
        long memberDataId;
	@XmlAttribute(name="rawId")
        long rawId;

	@XmlElement(name="periodStats")
        WeightedStat periodStats;
        
	@XmlElement(name="phByMethod")
        WeightedStat phaseStatsByMethod;
        
	@XmlElement(name="phByFit")
        WeightedStat phaseStatsByFit;
        
	@XmlElement(name="phByFP")
        WeightedStat phaseStatsByFirstPeak;
        
	@XmlElement(name="phByAM")
        WeightedStat phaseStatsByAvgMax;
        
	@XmlElement(name="ampByMethod")
        WeightedStat ampStatsByMethod;
        
	@XmlElement(name="ampByFit")
        WeightedStat ampStatsByFit;
        
	@XmlElement(name="ampByFP")
        WeightedStat ampStatsByFirstPeak;
        
	@XmlElement(name="ampByAM")
        WeightedStat ampStatsByAvgMax;
        
	@XmlElement(name="GOF")
        Statistics GOFStats;
	@XmlElement(name="jERR")
        Statistics joinedErrorStats;
        
	@XmlElement(name="phByMethodCirc")
        WeightedStat phaseStatsByMethodCirc;
        
	@XmlElement(name="phByFitCirc")
        WeightedStat phaseStatsByFitCirc;
        
	@XmlElement(name="phByFPCirc")
        WeightedStat phaseStatsByFirstPeakCirc;
        
	@XmlElement(name="phByAMCirc")
        WeightedStat phaseStatsByAvgMaxCirc;

        
        public Statistics getPeriod(WeightingType weighting) {
            return periodStats.getStat(weighting);
        }
        
        public Statistics getPhase(PhaseType phaseType,WeightingType weighting) {
            return getPhaseStats(phaseType).getStat(weighting);
        }
        
        public Statistics getPhaseCirc(PhaseType phaseType,WeightingType weighting) {
            return getPhaseStatsCirc(phaseType).getStat(weighting);
        }
        
        
        public Statistics getAmplitude(PhaseType phaseType,WeightingType weighting) {
            return getAmpStats(phaseType).getStat(weighting);
        }

	public WeightedStat getPhaseStats(PhaseType type) {
	    switch (type) {
		case ByFit: return getPhaseStatsByFit();
		case ByFirstPeak: return getPhaseStatsByFirstPeak();
		case ByAvgMax: return getPhaseStatsByAvgMax();
                case ByMethod: return getPhaseStatsByMethod();
                default: throw new IllegalArgumentException("Unsuported type: "+type);
	    }
	}

	public void setPhaseStats(WeightedStat stat,PhaseType type) {
            if (stat == null) throw new IllegalArgumentException("Stats cannot be null");
	    switch (type) {
		case ByFit: setPhaseStatsByFit(stat); break;
		case ByFirstPeak: setPhaseStatsByFirstPeak(stat); break;
		case ByAvgMax: setPhaseStatsByAvgMax(stat); break;
		case ByMethod:  setPhaseStatsByMethod(stat); break;
                default: throw new IllegalArgumentException("Unsuported type: "+type);
	    }
	}
        
	public WeightedStat getPhaseStatsCirc(PhaseType type) {
	    switch (type) {
		case ByFit: return getPhaseStatsByFitCirc();
		case ByFirstPeak: return getPhaseStatsByFirstPeakCirc();
		case ByAvgMax: return getPhaseStatsByAvgMaxCirc();
                case ByMethod: return getPhaseStatsByMethodCirc();
                default: throw new IllegalArgumentException("Unsuported type: "+type);
	    }
	}

	public void setPhaseStatsCirc(WeightedStat stat,PhaseType type) {
            if (stat == null) throw new IllegalArgumentException("Stats cannot be null");
	    switch (type) {
		case ByFit: setPhaseStatsByFitCirc(stat); break;
		case ByFirstPeak: setPhaseStatsByFirstPeakCirc(stat); break;
		case ByAvgMax: setPhaseStatsByAvgMaxCirc(stat); break;
		case ByMethod:  setPhaseStatsByMethodCirc(stat); break;
                default: throw new IllegalArgumentException("Unsuported type: "+type);
	    }
	}
        
	public WeightedStat getAmpStats(PhaseType type) {
	    switch (type) {
		case ByFit: return getAmpStatsByFit();
		case ByFirstPeak: return getAmpStatsByFirstPeak();
		case ByAvgMax: return getAmpStatsByAvgMax();
                case ByMethod: return getAmpStatsByMethod();
                default: throw new IllegalArgumentException("Unsuported type: "+type);
	    }
	}

	public void setAmpStats(WeightedStat stat,PhaseType type) {
            if (stat == null) throw new IllegalArgumentException("Stats cannot be null");
	    switch (type) {
		case ByFit: setAmpStatsByFit(stat); break;
		case ByFirstPeak: setAmpStatsByFirstPeak(stat); break;
		case ByAvgMax: setAmpStatsByAvgMax(stat); break;
		case ByMethod:  setAmpStatsByMethod(stat); break;
                default: throw new IllegalArgumentException("Unsuported type: "+type);
	    }
	}
        

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

    public WeightedStat getPeriodStats() {
        return periodStats;
    }

    public void setPeriodStats(WeightedStat periodStats) {
        if (periodStats == null) throw new IllegalArgumentException("Stats cannot be null");
        this.periodStats = periodStats;
    }

    public WeightedStat getPhaseStatsByMethod() {
        return phaseStatsByMethod;
    }

    public void setPhaseStatsByMethod(WeightedStat phaseStatsByMethod) {
        this.phaseStatsByMethod = phaseStatsByMethod;
    }

    public WeightedStat getPhaseStatsByFit() {
        return phaseStatsByFit;
    }

    public void setPhaseStatsByFit(WeightedStat phaseStatsByFit) {
        this.phaseStatsByFit = phaseStatsByFit;
    }

    public WeightedStat getPhaseStatsByFirstPeak() {
        return phaseStatsByFirstPeak;
    }

    public void setPhaseStatsByFirstPeak(WeightedStat phaseStatsByFirstPeak) {
        this.phaseStatsByFirstPeak = phaseStatsByFirstPeak;
    }

    public WeightedStat getPhaseStatsByAvgMax() {
        return phaseStatsByAvgMax;
    }

    public void setPhaseStatsByAvgMax(WeightedStat phaseStatsByAvgMax) {
        this.phaseStatsByAvgMax = phaseStatsByAvgMax;
    }

    public WeightedStat getAmpStatsByMethod() {
        return ampStatsByMethod;
    }

    public void setAmpStatsByMethod(WeightedStat ampStatsByMethod) {
        this.ampStatsByMethod = ampStatsByMethod;
    }

    public WeightedStat getAmpStatsByFit() {
        return ampStatsByFit;
    }

    public void setAmpStatsByFit(WeightedStat ampStatsByFit) {
        this.ampStatsByFit = ampStatsByFit;
    }

    public WeightedStat getAmpStatsByFirstPeak() {
        return ampStatsByFirstPeak;
    }

    public void setAmpStatsByFirstPeak(WeightedStat ampStatsByFirstPeak) {
        this.ampStatsByFirstPeak = ampStatsByFirstPeak;
    }

    public WeightedStat getAmpStatsByAvgMax() {
        return ampStatsByAvgMax;
    }

    public void setAmpStatsByAvgMax(WeightedStat ampStatsByAvgMax) {
        this.ampStatsByAvgMax = ampStatsByAvgMax;
    }

    public Statistics getGOF() {
        return GOFStats;
    }

    public void setGOF(Statistics GOFStats) {
        this.GOFStats = GOFStats;
    }

    public Statistics getJoinedError() {
        return joinedErrorStats;
    }

    public void setJoinedError(Statistics joinedErrorStats) {
        this.joinedErrorStats = joinedErrorStats;
    }

    public WeightedStat getPhaseStatsByMethodCirc() {
        return phaseStatsByMethodCirc;
    }

    public void setPhaseStatsByMethodCirc(WeightedStat phaseStatsByMethodCirc) {
        this.phaseStatsByMethodCirc = phaseStatsByMethodCirc;
    }

    public WeightedStat getPhaseStatsByFitCirc() {
        return phaseStatsByFitCirc;
    }

    public void setPhaseStatsByFitCirc(WeightedStat phaseStatsByFitCirc) {
        this.phaseStatsByFitCirc = phaseStatsByFitCirc;
    }

    public WeightedStat getPhaseStatsByFirstPeakCirc() {
        return phaseStatsByFirstPeakCirc;
    }

    public void setPhaseStatsByFirstPeakCirc(WeightedStat phaseStatsByFirstPeakCirc) {
        this.phaseStatsByFirstPeakCirc = phaseStatsByFirstPeakCirc;
    }

    public WeightedStat getPhaseStatsByAvgMaxCirc() {
        return phaseStatsByAvgMaxCirc;
    }

    public void setPhaseStatsByAvgMaxCirc(WeightedStat phaseStatsByAvgMaxCirc) {
        this.phaseStatsByAvgMaxCirc = phaseStatsByAvgMaxCirc;
    }

    public long getMemberDataId() {
        return memberDataId;
    }

    public void setMemberDataId(long memberDataId) {
        this.memberDataId = memberDataId;
    }

    public long getRawId() {
        return rawId;
    }

    public void setRawId(long rawId) {
        this.rawId = rawId;
    }

	


}
