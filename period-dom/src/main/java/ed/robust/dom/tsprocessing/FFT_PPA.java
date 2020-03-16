package ed.robust.dom.tsprocessing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "FFT_PPA")
@XmlAccessorType(XmlAccessType.FIELD)
public class FFT_PPA extends PPAResult implements Iterable<CosComponent>{
        private static final long serialVersionUID = 10L;

	@XmlElement(name="COS", required = true)
	List<CosComponent> components;
	
	double shift;
	@XmlElement(name="slope")
	double trendSlope;
	@XmlElement(name="inter")
	double trendInterception;
	
    
    
	
	public FFT_PPA() {
		components = new ArrayList<>();		
	}
	
	public FFT_PPA(List<CosComponent> components) {
		if (components == null) throw new IllegalArgumentException("COS components list cannot be null");
		this.components = components;
	}
	

	public void addCOS(CosComponent component) {
		components.add(component);
	}
	
	public void addCOS(double amplitude, double amplitudeError, double period,double periodError, double phase, double phaseError) {
		addCOS(new CosComponent(amplitude, amplitudeError, period, periodError, phase, phaseError));
	}
	
	public void setComponents(List<CosComponent> components) {
		if (components == null) throw new IllegalArgumentException("COS components list cannot be null");
		this.components = components;		
	}
	
	public List<CosComponent> getComponents() {
		return components;
	}
	
	public double getShift() {
		return shift;
	}

	public void setShift(double shift) {
		this.shift = shift;
	}
	
	

	public double getTrendSlope() {
		return trendSlope;
	}

	public void setTrendSlope(double trendSlope) {
		this.trendSlope = trendSlope;
	}

	public double getTrendInterception() {
		return trendInterception;
	}

	public void setTrendInterception(double trendInterception) {
		this.trendInterception = trendInterception;
	}

	@Override
	public Iterator<CosComponent> iterator() {
		return components.iterator();
	}

	@Override
	public PPA getPPAByAvgMax() {
		if (components.isEmpty()) throw new IllegalArgumentException("Cannot access ppa on empty cos components");
		return components.get(0).getPPAByAvgMax();
	}
	
	@Override
	public PPA getPPAByFirstPeak() {
		if (components.isEmpty()) throw new IllegalArgumentException("Cannot access ppa on empty cos components");
		return components.get(0).getPPAByFirstPeak();
	}
	

	@Override
	public PPA getPPAByFit() {
		if (components.isEmpty()) throw new IllegalArgumentException("Cannot access ppa on empty cos components");
		return components.get(0).getPPAByFit();
	}

	@Override
	public PPA getPPAMethodSpecific() {
		if (components.isEmpty()) throw new IllegalArgumentException("Cannot access ppa on empty cos components");
		return components.get(0).getPPAMethodSpecific();
	}

        public void propagateErrors() {
            for (CosComponent cos : components) {
                cos.setJoinedError(cos.getPPAMethodSpecific().getJoinedError());
                cos.setMethodError(cos.getPPAMethodSpecific().getMethodError());
            }
        }
        
        @Override
        public void setGOF(double GOF) {

            for (CosComponent cos : components) cos.setGOF(GOF);

        }
    
        @Override
        public void setJoinedError(double joinedError) {
            throw new UnsupportedOperationException("The joined error should not be set globaly for fft results");
        }
        
        /*@Override
        public void setJoinedError(double joinedError) {
            
            for (CosComponent cos : components) cos.setJoinedError(joinedError);

        }*/
        
        @Override
        public void setMethodError(double metError) {
            throw new UnsupportedOperationException("The Method error should not be set globaly for fft results");
        }
        
        /*@Override
        public void setMethodError(double metError) {
            
            for (CosComponent cos : components) cos.setMethodError(metError);
                        
        }*/
        
        @Override
        public void setPowerValue(double powerValue) {
            throw new UnsupportedOperationException("The Power value should not be set globaly for fft results");
        }
        
        @Override
        public void setRandomness(double randomness) {
            throw new UnsupportedOperationException("The randomnes should not be set globaly for fft results");
        }
        
    
        
        
	
}
