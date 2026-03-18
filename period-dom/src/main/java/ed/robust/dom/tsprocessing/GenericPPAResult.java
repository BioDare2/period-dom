/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.dom.tsprocessing;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 *
 * @author tzielins
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericPPAResult extends PPAResult {
        private static final long serialVersionUID = 10L;


	@XmlElement(name="method")
        @JsonProperty("method")
	protected PPA PPAMethodSpecific;
	
	@XmlElement(name="pbam")
        @JsonProperty("pbam")
	protected PPA PPAByAvgMax;
	
	@XmlElement(name="pbf")
        @JsonProperty("pbf")
	protected PPA PPAByFit;
	
	@XmlElement(name="pbfp")
        @JsonProperty("pbfp")
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.PPAMethodSpecific);
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
        final GenericPPAResult other = (GenericPPAResult) obj;
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
