package ed.robust.dom.tsprocessing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import ed.robust.dom.data.TimeSeries;

@XmlAccessorType(XmlAccessType.FIELD)
public class FailedPPA extends PPAResult {
        private static final long serialVersionUID = 10L;

	
	public FailedPPA() {
            this(null);
	}

	public FailedPPA(String message) {
            this(message,"");
        }        
        
	public FailedPPA(String message,String methodVersion) {
		this.message = message;
		this.ignored = true;
		this.circadian = false;
		this.needsAttention =false;
                this.methodVersion = methodVersion;
	}
	
	
	@Override
	public double getAmplitude() {
		return 0;
	}

	@Override
	public double getPeriod() {
		return 0;
	}

	@Override
	public double getPhase() {
		return 0;
	}

	@Override
	public TimeSeries getFit() {
		return new TimeSeries();
	}
	
	@Override
	public boolean hasFailed() {
		return true;
	}

	@Override
	public PPA getPPAByAvgMax() {		
		return new PPA();
	}

	@Override
	public PPA getPPAByFirstPeak() {		
		return new PPA();
	}
	
	@Override
	public PPA getPPAByFit() {
		return new PPA();
	}

	@Override
	public PPA getPPAMethodSpecific() {
		return new PPA();
	}
	

}
