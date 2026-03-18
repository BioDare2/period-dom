/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.dom.tsprocessing;

/**
 *
 * @author tzielins
 */
public class MESA_PPA extends GenericPPAResult {
    private static final long serialVersionUID = 10L;
    
    double M;
    int code;
    
    MESA_PPA() {
        super();
    }
    
    public MESA_PPA(PPA ppa) {
        super(ppa);
    }
    
    public MESA_PPA(PPA byFit,PPA byFirstPeak,PPA byAvg) {
        super(byFit, byFirstPeak, byAvg, byFit);
    }

    public double getM() {
        return M;
    }

    public void setM(double M) {
        this.M = M;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    
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
