package ed.robust.dom.tsprocessing;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import ed.robust.dom.data.TimeSeries;
import java.util.Objects;

@XmlSeeAlso({GenericPPAResult.class,FailedPPA.class,FFT_PPA.class})
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class PPAResult extends Result implements Serializable {

    @XmlAttribute(name = "attention")
    @JsonProperty("attention")    
    protected boolean needsAttention;

    @XmlAttribute(name = "circadian")
    @JsonProperty("circ")    
    protected boolean circadian;

    @XmlAttribute(name = "ignored")
    @JsonProperty("ingored")    
    protected boolean ignored;

    @XmlAttribute(name = "method")
    @JsonProperty("methodV")    
    protected String methodVersion;
    
    /*@XmlAttribute(name = "GOF")
    @Deprecated
    protected double GOF;
    
    @XmlAttribute(name = "GERR")
    @Deprecated
    protected double GERR;
    */
    
    /*
    @XmlAttribute(name = "GERR2L")
    protected double GERR2L;
    
    @XmlAttribute(name = "GERR2R")
    protected double GERR2R;
    */
    
    String message;
    
    @XmlElement(name="fit")
    TimeSeries fit;
    
    /**
     * Gets the value of the amplitude.
     * 
     */
    public double getAmplitude() {
    	return getPPAMethodSpecific().getAmplitude();
    }

    /**
     * Gets the value of the period.
     * 
     */
    public double getPeriod() {
    	return getPPAMethodSpecific().getPeriod();
    }

    /**
     * Gets the value of the phase.
     * 
     */
    public double getPhase() {
    	return getPPAMethodSpecific().getPhase();
    }
    
    
    public abstract PPA getPPAByAvgMax();
    
    public abstract PPA getPPAByFirstPeak();
    
    public abstract PPA getPPAByFit();
    
    public abstract PPA getPPAMethodSpecific();

    public PPA getPPA(PhaseType type) {

	switch(type) {
	    case ByFit: return getPPAByFit();
	    case ByFirstPeak: return getPPAByFirstPeak();
	    case ByAvgMax: return getPPAByAvgMax();
            case ByMethod: return getPPAMethodSpecific();
	    default: throw new IllegalArgumentException("Unsuported phase type: "+type);
	}
    }
    
    /**
     * Gets the theoretical fit
     * @return matching fit
     */
    public TimeSeries getFit() {
        return fit;
    }

    public void setFit(TimeSeries fit) {
        this.fit = fit;
    }

    /**
     * Checks if result represents failed job
     * @return true if result represent failed job
     */
    public boolean hasFailed() {
    	return false;
    }
    
    public boolean needsAttention() {
    	return needsAttention;
    }
    
    public void setNeedsAttention(boolean needsAttention) {
		this.needsAttention = needsAttention;
	}
    
    
    public boolean isCircadian() {
		return circadian;
	}

	public void setCircadian(boolean circadian) {
		this.circadian = circadian;
	}

	/**
     * Message associated with this result
     * @return message, default value is empty string
     */
    public String getMessage() {
    	if (message == null) return "";
    	else return message;
    }
    
	
	public void setMessage(String message) {
		this.message = message;
	}

        /*@Deprecated
	public double getGOF() {
		return GOF;
	}*/

        /*
	public void setGOF(double gof) {
		GOF = gof;
	}*/

    public boolean isIgnored() {
	return ignored;
    }

    public void setIgnored(boolean ignored) {
	this.ignored = ignored;
    }

    /*@Deprecated
    public double getGERR() {
        return GERR;
    }

    @Deprecated
    public void setGERR(double GERR) {
        this.GERR = GERR;
    }*/
    
    /*
    public double getGERR2L() {
    return GERR2L;
    }
    public void setGERR2L(double GERR2L) {
    this.GERR2L = GERR2L;
    }
    public double getGERR2R() {
    return GERR2R;
    }
    public void setGERR2R(double GERR2R) {
    this.GERR2R = GERR2R;
    }*/
    
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
    
    
    public String getMethodVersion() {
        if (methodVersion == null) return "";
        return methodVersion;
    }

    public void setMethodVersion(String methodVersion) {
        this.methodVersion = methodVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.needsAttention ? 1 : 0);
        hash = 79 * hash + (this.circadian ? 1 : 0);
        hash = 79 * hash + (this.ignored ? 1 : 0);
        hash = 79 * hash + Objects.hashCode(this.methodVersion);
        hash = 79 * hash + Objects.hashCode(this.message);
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
        if (!super.equals(obj)) {
            return false;
        }
        final PPAResult other = (PPAResult) obj;
        if (this.needsAttention != other.needsAttention) {
            return false;
        }
        if (this.circadian != other.circadian) {
            return false;
        }
        if (this.ignored != other.ignored) {
            return false;
        }
        if (!Objects.equals(this.methodVersion, other.methodVersion)) {
            return false;
        }
        if (!Objects.equals(this.message, other.message)) {
            return false;
        }
        if (!Objects.equals(this.fit, other.fit)) {
            return false;
        }
        return true;
    }

    
	
	
}
