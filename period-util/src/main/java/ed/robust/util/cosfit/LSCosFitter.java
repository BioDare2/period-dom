/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.util.cosfit;

import ed.robust.dom.data.TimeSeries;
import ed.robust.dom.tsprocessing.PPA;
import ed.robust.dom.util.Pair;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import static org.apache.commons.math3.util.FastMath.*;

/**
 * Implements of cos fitting using linear LS approach. The fiting is based on the equation:
 * <br/>
 * Ccos(x-f) = Acos(x)+Bsin(x)<br/>
 * C^2 = A^2+B^2<br/>
 * f = arctan(-b/a)
 * <p>
 * The data are modeled as Acos(w*x)+Bsin(w*x)+Offset, where w = 2Pi/period,
 * and the W*coefs = X is solved, using QR decomposition, where X is the value vector of the input data,
 * W = [cos][sin][1] at each time point.
 * 
 * @author tzielins
 */
public class LSCosFitter implements CosFitter {

    @Override
    public PPA fitCos(TimeSeries data, double period) {
        
        if (data.isEmpty()) throw new IllegalArgumentException("Input data cannot be emtpy");
        if (period <= 0) throw new IllegalArgumentException("Period must be > 0, got: "+period);
        
        Pair<double[],double[]> timeVals = data.getTimesAndValues();
        double[] times = timeVals.getLeft();
        double[] vals = timeVals.getRight();
        RealMatrix wave = makeWaveMatrix(times,period);
        RealMatrix values = new Array2DRowRealMatrix(vals);
        
        RealMatrix coefs = getWaveCoeficients(wave,values);
        
        return assemblePPA(coefs,period);
        
    }

    /**
     * Creates equation matrix used in LS sovling of the problem. The matrix has in first column
     * values of cos function of given period at each time point, same for sine in the second column, 
     * and columns of 1 as the last one.
     * @param times input time for which matrix value must be constructed
     * @param period the expected period for the cosine components (cos(2Pi/period*time))
     * @return the value matrix for LS solving, [cos][sin][1] where each columns has values for each given timepiont
     */
    protected RealMatrix makeWaveMatrix(double[] times, double period) {
        
        double TwoPIByPeriod = 2*PI/period;
        
        double[][] waves = new double[times.length][3];
        
        for (int i = 0;i<times.length;i++) {
            double x = times[i]*TwoPIByPeriod;
            waves[i][0] = cos(x);
            waves[i][1] = sin(x);            
            waves[i][2] = 1;
        }
        
        return MatrixUtils.createRealMatrix(waves);
    }

    /**
     * Solves the equations wave*coef = values, using LS approach. QR decomposition is used.
     * @param wave characteristic matrix
     * @param values the measured values
     * @return the coeficient that will minimilise the |wave*coef-value|^2
     */
    protected RealMatrix getWaveCoeficients(RealMatrix wave, RealMatrix values) {
        
        DecompositionSolver solver = new QRDecomposition(wave).getSolver();
        return solver.solve(values);
    }

    /**
     * Constructs the results using found coeficients and the expected period.
     * The phase is reported in the range (0-period) and matches the peak position rather than matematical definition,
     * for example for period 24, phase 1 signifies that the data peak at time 1, 25... and phase 17 that peak at time 17,41..
     * @param coefs model paramters, cos, sine,offset
     * @param period the expected, used to scale the phase accordingly
     * @return the fully populated PPA with period, phase and amplitude.
     */
    protected PPA assemblePPA(RealMatrix coefs, double period) {
        
        
        double a = coefs.getEntry(0, 0);
        double b = coefs.getEntry(1, 0);
        
        double amp = sqrt(pow(a,2)+pow(b,2));
        double phase = atan(-b/a)*period/2.0/PI;
        
        if (a < 0) {
            phase-=period/2.0;
        }
        
        phase = -phase;        
        if (phase < 0) phase+=period;
        phase = phase%period;
        
        //phase = -phase+period/2.0;
        //phase = -phase;
        //if (phase < 0) phase+=period/2.0;
        //phase = phase%period;
        //System.out.println(phase+":"+a+":"+b);
        PPA result = new PPA(period, phase, amp);
        result.setOffset(coefs.getEntry(2, 0));
        return result;
        
    }
    
    
    
}
