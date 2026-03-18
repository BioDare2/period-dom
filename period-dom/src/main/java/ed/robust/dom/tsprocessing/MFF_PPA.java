/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.dom.tsprocessing;

/**
 *
 * @author Zielu
 */
public class MFF_PPA extends GenericPPAResult {
    private static final long serialVersionUID = 10L;
    
    double AIC;

    public MFF_PPA() {
        super();
    }
    
    public MFF_PPA(PPA ppa) {
        super(ppa);
    }
    
    /*
    @Override
    public double getAmplitude() {
        return getPPAMethodSpecific().getAmplitude();
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    @Override
    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        this.period = period;
    }

    @Override
    public double getPhase() {
        return phase;
    }

    public void setPhase(double phase) {
        this.phase = phase;
    }

    @Override
    public TimeSeries getFit() {
        return fit;                
    }

    public void setFit(TimeSeries fit) {
        this.fit = fit;
    }
    
     * 
     */
    public double getAIC() {
        return AIC;
    }
    
    public void setAIC(double AIC) {
        this.AIC = AIC;
        this.setMethodError(AIC);
        //setGERR(AIC);
    }

    /*
    public boolean getDFit() {
        return dFit;
    }

    public void setDFit(boolean dFit) {
        this.dFit = dFit;
    }
    */
    
    public GenericPPAResult toGenericPPAResult() {
        GenericPPAResult gen = new GenericPPAResult(PPAMethodSpecific, PPAByFirstPeak, PPAByAvgMax, PPAByFit);
        gen.setCircadian(circadian);
        gen.setFit(fit);
        gen.setIgnored(ignored);
        gen.setMessage(message);
        gen.setMethodVersion(methodVersion);
        gen.setNeedsAttention(needsAttention);
        gen.setProcessingTime(processingTime);
        return gen;
    }    
    
}
