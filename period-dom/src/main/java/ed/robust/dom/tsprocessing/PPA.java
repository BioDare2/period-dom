package ed.robust.dom.tsprocessing;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "PPA")
@XmlAccessorType(XmlAccessType.FIELD)
public class PPA implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 10L;
	
        @XmlAttribute(name = "per")
        double period;
        @XmlAttribute(name = "ph")
        double phase;
        @XmlAttribute(name = "amp")
        double amplitude;
        @XmlAttribute(name = "off")
        double offset;

        @XmlAttribute(name = "perE")
        Double periodError;
        @XmlAttribute(name = "phE")
        Double phaseError;
        @XmlAttribute(name = "ampE")
        Double amplitudeError;
        @XmlAttribute(name = "offE")
        Double offsetError;

        /*@XmlAttribute(name = "err")
        @Deprecated
        Double globalError;*/
        
        @XmlAttribute(name = "jERR")
        Double joinedError;
        
        @XmlAttribute(name = "mERR")
        Double methodError;        
        
        @XmlAttribute(name = "GOF")
        Double GOF;

        @XmlAttribute(name = "powV")
        Double powerValue;
        
        @XmlAttribute(name = "sig")
        Double randomness;
	
	public PPA() {
		this(0,0,0);
	}

	public PPA(double period, double phase, double amplitude) {
		this(period,phase,amplitude,0);
	}
	
	public PPA(double period, double phase, double amplitude, double offset) {
		super();
		this.period = period;
		this.phase = phase;
		this.amplitude = amplitude;
		this.offset = offset;
		
	}
	

	public PPA(double period, double periodError, double phase, double phaseError, double amplitude, double amplitudeError, 
			double offset, double offsetError,double joinedError,double methodError,double GOF) {
		
		this(period,phase,amplitude,offset);
		
		this.periodError = periodError;
		this.phaseError = phaseError;
		this.amplitudeError = amplitudeError;
		this.offsetError = offsetError;
		//this.globalError = globalError;
                this.joinedError = joinedError;
                this.methodError = methodError;
                this.GOF = GOF;
	}

	public double getPeriod() {
		return period;
	}

	public void setPeriod(double period) {
		this.period = period;
	}

	public double getPhase() {
		return phase;
	}
	
	/**
	 * Reports phase relatative to given time instead of 0. For example if phase is 5, period 20, than 
	 * phase relative to 5 is 0, relative to 24 is 1, and relative to 50 is 15.
	 * @param reference value relative to which phase should be reported (the beginning of data window)
	 * @return phase in the range [0,period) relative to reference value.
	 */
	public double getRelativePhase(double reference) {
		
		reference = reference % period;
		double val = phase - reference;
		if (val < 0) val = val+period;
		return val;
	}

	public void setPhase(double phase) {
		this.phase = phase;
	}

	public double getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}

	public double getOffset() {
		return offset;
	}

	public void setOffset(double offset) {
		this.offset = offset;
	}

	public double getPeriodError() {
		if (periodError == null) return 0;
		return periodError;
	}

	public void setPeriodError(double periodError) {
		this.periodError = periodError;
	}

	public double getPhaseError() {
		if (phaseError == null) return 0;
		return phaseError;
	}

	public void setPhaseError(double phaseError) {
		this.phaseError = phaseError;
	}

	public double getAmplitudeError() {
		if (amplitudeError == null) return 0;
		return amplitudeError;
	}

	public void setAmplitudeError(double amplitudeError) {
		this.amplitudeError = amplitudeError;
	}

	public double getOffsetError() {
		if (offsetError == null) return 0;
		return offsetError;
	}

	public void setOffsetError(double offsetError) {
		this.offsetError = offsetError;
	}

        /*@Deprecated
	public double getGlobalError() {
		if (globalError == null) return 1;
		return globalError;
	}*/

    public double getJoinedError() {
        if (joinedError == null) return 1;
        return joinedError;
    }

    public void setJoinedError(double joinedError) {
        this.joinedError = joinedError;
    }

    public double getMethodError() {
        if (methodError == null) return 1;
        return methodError;
    }

    public void setMethodError(double methodError) {
        this.methodError = methodError;
    }

        public double getGOF() {
            if (GOF != null) return GOF;
            return 1;
        }

        public void setGOF(double GOF) {
            this.GOF = GOF;
        }

        public double getPowerValue() {
            if (powerValue != null) return powerValue;
            return 0;
        }

        public void setPowerValue(double powerValue) {
            this.powerValue = powerValue;
        }

        public double getRandomness() {
            if (randomness != null) return randomness;
            return 1;
        }

        public void setRandomness(double randomness) {
            this.randomness = randomness;
        }
        
        

	/*public void setGlobalError(double globalError) {
		this.globalError = globalError;
	}*/
	
	public boolean hasPeriodError() {
		return periodError != null;
	}
	
	public boolean hasPhaseError() {
		return phaseError != null;
	}
	
	public boolean hasAmplitudeError() {
		return amplitudeError != null;
	}
	
	public boolean hasOffsetError() {
		return offsetError != null;
	}
	
        /*@Deprecated
	public boolean hasGlobalError() {
		return globalError != null;
	}*/
        
        public boolean hasJoinedError() {
            return joinedError != null;
        }
        
        public boolean hasMethodError() {
            return methodError != null;
        }
        
        public boolean hasGOF() {
            return GOF != null;
        }

        public boolean hasPowerValue() {
            return powerValue != null;
        }
        
        public boolean hasRandomness() {
            return randomness != null;
        }
        
        
}
