/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.dom.tsprocessing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 *
 * @author tzielins
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class GenericPPAResult extends PPAResult {
        private static final long serialVersionUID = 10L;


	@XmlElement(name="method")
	protected PPA PPAMethodSpecific;
	
	@XmlElement(name="pbam")
	protected PPA PPAByAvgMax;
	
	@XmlElement(name="pbf")
	protected PPA PPAByFit;
	
	@XmlElement(name="pbfp")
	protected PPA PPAByFirstPeak;
    

    public GenericPPAResult() {
        this(new PPA());
    }
    
    public GenericPPAResult(double period, double peak, double amplitude) {
        this(new PPA(period,peak,amplitude));
    }
    
    public GenericPPAResult(PPA ppa) {
    	this(ppa,ppa,ppa,ppa);
    }
    
    public GenericPPAResult(PPA method,PPA phaseByFirstPeak,PPA phaseByAvgMax,PPA phaseByFit) {
    	this.PPAMethodSpecific = method;
    	this.PPAByAvgMax= phaseByAvgMax;
    	this.PPAByFit = phaseByFit;
    	this.PPAByFirstPeak = phaseByFirstPeak;
    }

    
    
    


        @Override
        public PPA getPPAMethodSpecific() {
		return PPAMethodSpecific;
	}

	public void setPPAMethodSpecific(PPA pPAMethodSpecific) {
		PPAMethodSpecific = pPAMethodSpecific;
	}

        @Override
	public PPA getPPAByAvgMax() {
		return PPAByAvgMax;
	}

	public void setPPAByAvgMax(PPA pPAByMax) {
		PPAByAvgMax = pPAByMax;
	}

        @Override
	public PPA getPPAByFit() {
		return PPAByFit;
	}

	public void setPPAByFit(PPA pPAByFit) {
		PPAByFit = pPAByFit;
	}

        @Override
	public PPA getPPAByFirstPeak() {
		return PPAByFirstPeak;
	}

	public void setPPAByFirstPeak(PPA pPAByFirstPeak) {
		PPAByFirstPeak = pPAByFirstPeak;
	}

    
    
}
