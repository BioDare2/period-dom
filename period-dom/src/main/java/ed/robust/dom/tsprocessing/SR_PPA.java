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
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 * Wrapper around SR results.
 * @author tzielins
 */
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SR_PPA extends FFT_PPA
{
    private static final long serialVersionUID = 10L;

        @XmlElement(name="band")
        @JsonProperty("band")                
        double bandOptimal;
        @XmlElement(name="bandB")
        @JsonProperty("bandB")                
        double bandBase;
        
                
    public void addCOS(FreqComponent comp) {
        PPA ppa = new PPA(comp.getPeriod(), comp.getPhase(), comp.getAmplitude());
        addCOS(new CosComponent(ppa));
        //CosComponent cos = new CosComponent
        //addCOS(comp.getAmplitude(),0.0,comp.getPeriod(),0.0,comp.getPhase(),0.0);
    }
	


    public double getBandBase() {
        return bandBase;
    }

    public void setBandBase(double bandBase) {
        this.bandBase = bandBase;
    }

    public double getBandOptimal() {
        return bandOptimal;       
    }

    public void setBandOptimal(double bandOptimal) {
        this.bandOptimal = bandOptimal;
    }

    public void filterSignificantCircadian(double periodMin, double periodMax) {
        
        List<CosComponent> accepted = new ArrayList<>();
        double ampTreshold = 0;
        boolean areCircadian = false;
        //boolean attention = false;
        
        for (CosComponent cos : this) {
            if (areCircadian) {
                if (!isCircadian(cos,periodMin,periodMax)) break;
                if (cos.getAmplitude() > ampTreshold) break;
                //attention = true;
            } else {
                if (isCircadian(cos,periodMin,periodMax)) areCircadian = true;
            }
            ampTreshold = cos.getAmplitude()/10;
            accepted.add(cos);
        }
        
        this.setComponents(accepted);
        this.setCircadian(areCircadian);
        this.setNeedsAttention(accepted.size()!=1);
        
    }
    
    private final boolean isCircadian(CosComponent cos,double periodMin,double periodMax) {
        return (cos.getPeriod() >= periodMin && cos.getPeriod() <= periodMax);
    }

    
    public FFT_PPA toFFTPPA() {
        FFT_PPA gen = new FFT_PPA(components);
        gen.setCircadian(circadian);
        gen.setFit(fit);
        gen.setIgnored(ignored);
        gen.setMessage(message);
        gen.setMethodVersion(methodVersion);
        gen.setNeedsAttention(needsAttention);
        gen.setProcessingTime(processingTime);  
        gen.setShift(shift);
        gen.setTrendInterception(trendInterception);
        gen.setTrendSlope(trendSlope);
        return gen;
    }
    
}
