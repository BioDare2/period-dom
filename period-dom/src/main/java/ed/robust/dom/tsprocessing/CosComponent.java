package ed.robust.dom.tsprocessing;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "COS")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class CosComponent implements Serializable{
        private static final long serialVersionUID = 10L;
        
        private static final DecimalFormat formatter = new DecimalFormat("0.00");

	/**
	 * Stores period, phase, amplitude and potential errors established in the particular analysis method manner
	 */
	@XmlElement(name="method")
        @JsonProperty("method")
        protected PPA PPAMethodSpecific;
	
	/**
	 * Stores period, phase, amplitude which were determined using the phase by avg max method
	 */
	@XmlElement(name="pbam")
        @JsonProperty("pbam")
	protected PPA PPAByAvgMax;
	
	/**
	 * Stores period, phase, amplitude which were determined using the phase by cos fit method
	 */
	@XmlElement(name="pbf")
        @JsonProperty("pbf")
	protected PPA PPAByFit;
	
	/**
	 * Stores period, phase, amplitude which were determined using the phase by first peak
	 */
	@XmlElement(name="pbfp")
        @JsonProperty("pbfp")
	protected PPA PPAByFirstPeak;
	
	    
        public CosComponent() {
            this(new PPA());
        }
    
	public CosComponent(double amplitude, double amplitudeError, double period,
			double periodError, double phase, double phaseError) {
		this(new PPA(period,periodError,phase,phaseError,amplitude,amplitudeError,0,0,(amplitude != 0 ? amplitudeError/amplitude : 1),(amplitude != 0 ? amplitudeError/amplitude : 1),1));
		
	}
	
	public CosComponent(PPA ppa) {
		this(ppa,ppa,ppa,ppa);
	}
	
	public CosComponent(PPA method,PPA phaseByFirstPeak,PPA phaseByAvgMax,PPA phaseByFit) {
            this.PPAMethodSpecific = method;
            this.PPAByAvgMax= phaseByAvgMax;
            this.PPAByFit = phaseByFit;
            this.PPAByFirstPeak = phaseByFirstPeak;
	}

	public PPA getPPA(PhaseType type) {

	    switch(type) {
		case ByFit: return getPPAByFit();
		case ByFirstPeak: return getPPAByFirstPeak();
		case ByAvgMax: return getPPAByAvgMax();
                case ByMethod: return getPPAMethodSpecific();
		default: throw new IllegalArgumentException("Unsuported phase type: "+type);
	    }
	}


	public PPA getPPAMethodSpecific() {
		return PPAMethodSpecific;
	}

	public void setPPAMethodSpecific(PPA pPAMethodSpecific) {
		PPAMethodSpecific = pPAMethodSpecific;
	}

	public PPA getPPAByAvgMax() {
		return PPAByAvgMax;
	}

	public void setPPAByAvgMax(PPA pPAByAvgMax) {
		PPAByAvgMax = pPAByAvgMax;
	}

	public PPA getPPAByFit() {
		return PPAByFit;
	}

	public void setPPAByFit(PPA pPAByFit) {
		PPAByFit = pPAByFit;
	}

	public PPA getPPAByFirstPeak() {
		return PPAByFirstPeak;
	}

	public void setPPAByFirstPeak(PPA pPAByFirstPeak) {
		PPAByFirstPeak = pPAByFirstPeak;
	}

	public double getAmplitude() {
		return getPPAMethodSpecific().amplitude;
	}
	
	
	public double getPeriod() {
		return getPPAMethodSpecific().period;
	}
	
	public double getPhase() {
		return getPPAMethodSpecific().phase;
	}
        
        public void setGOF(double GOF) {
            for (PhaseType type : PhaseType.values()) {
                getPPA(type).setGOF(GOF);
            }
        }
        
        public void setJoinedError(double joinedError) {
            for (PhaseType type : PhaseType.values()) {
                getPPA(type).setJoinedError(joinedError);
            }
        }
        
        public void setMethodError(double metError) {
            for (PhaseType type : PhaseType.values()) {
                getPPA(type).setMethodError(metError);
            }            
        }
        
        public void setPowerValue(double powerValue) {
            for (PhaseType type : PhaseType.values()) {
                getPPA(type).setPowerValue(powerValue);
            }                        
        }
        
        public void setRandomness(double randomness) {
            for (PhaseType type : PhaseType.values()) {
                getPPA(type).setRandomness(randomness);
            }                        
        }
        

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        PPA ppa = getPPAMethodSpecific();
        sb.append("P:").append(formatter.format(ppa.getPeriod()));
        if (ppa.hasPeriodError()) sb.append("(").append(formatter.format(ppa.getPeriodError())).append(")");
        sb.append(" PH:").append(formatter.format(ppa.getPhase()));
        if (ppa.hasPhaseError()) sb.append("(").append(formatter.format(ppa.getPhaseError())).append(")");
        sb.append(" A:").append(formatter.format(ppa.getAmplitude()));
        if (ppa.hasAmplitudeError()) sb.append("(").append(formatter.format(ppa.getAmplitudeError())).append(")");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.PPAMethodSpecific);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CosComponent other = (CosComponent) obj;
        if (!Objects.equals(this.PPAMethodSpecific, other.PPAMethodSpecific)) {
            return false;
        }
        if (!Objects.equals(this.PPAByAvgMax, other.PPAByAvgMax)) {
            return false;
        }
        if (!Objects.equals(this.PPAByFit, other.PPAByFit)) {
            return false;
        }
        if (!Objects.equals(this.PPAByFirstPeak, other.PPAByFirstPeak)) {
            return false;
        }
        return true;
    }

        
}
