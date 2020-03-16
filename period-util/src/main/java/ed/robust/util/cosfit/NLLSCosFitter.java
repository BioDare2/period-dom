/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.util.cosfit;

import ed.robust.dom.data.TimeSeries;
import ed.robust.dom.data.Timepoint;
import ed.robust.dom.tsprocessing.PPA;
import ed.robust.dom.util.Pair;
import ed.robust.error.RobustProcessException;
import ed.robust.util.timeseries.TimeSeriesOperations;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;

/**
 * Implementation of cos fitter using non linear square fitting using Gauss Newton algorithm. 
 * It follows the algorithm described by Anne Moore, see the doc folder of the project. 
 * <p>
 * It starts with initial guess of phase as peak, amplitude as (max-min), then make one more guess
 * with phase = phase  +period/4. If the result amplitude is negative it fits one more time with rotated phase and amplitude.
 * 
 * @deprecated use LSCosFitter instead
 * @author tzielins
 */
class NLLSCosFitter implements CosFitter{

    //how long it is going to iteratre before it stops
    int MAX_LOOPS = 20;
    
    //convergance criteria for square error (the ratio must be smaller that this value)
    double CONV = 0.001;
    //convergance criteria for parameters (the ratio between parameters cannot be higher that this)
    double P_CONV = 0.01;
    
    @Override
    public PPA fitCos(TimeSeries data, double period) throws RobustProcessException{
        
        if (data == null || data.size() <1) throw new IllegalArgumentException("Data to fit cos cannot be empty");
        if (period <= 0) throw new IllegalArgumentException("Period cannot be <=0");
        
        Pair<Timepoint,Timepoint> minMax = TimeSeriesOperations.getMinMax(data);
        
        double min = minMax.getLeft().getValue();
        double max = minMax.getRight().getValue();
        
        TimeSeries dayAndHalf = data.subSeries(data.getFirst().getTime(), data.getFirst().getTime()+2*period);
        Pair<Timepoint,Timepoint> dayAndHalfMinMax = TimeSeriesOperations.getMinMax(dayAndHalf);
        
        double phase = (dayAndHalfMinMax.getRight().getTime() % period );
        
        double amp = (max-min)/2;
        double offset = (max - amp);
           
        //System.out.println(amp);
        //System.out.println(offset);
        //System.out.println(phase);
        
        FitParameters fit;
        
        try {
            fit = fitCosParam(data,period,phase,amp,offset);
        } catch (SingularMatrixException e) {
            //System.out.println("Singular matrix in inital guess");
            
            try {
                fit = fitCosParam(data,period,Math.random()*phase,Math.random()*amp,Math.random()*offset);
            } catch(SingularMatrixException e2) {
                throw new RobustProcessException("Cannot fit cos into data, singular matrix exception for both sets of starting guesses");
            }
        }
        
        {
            try {
            FitParameters fit2 = fitCosParam(data,period,(fit.phase+period/4)%period,2*fit.amp,fit.offset);
            if (fit2.SE < fit.SE) {
                fit = fit2;
                //System.out.println("Second guess BETTER");
            }
            } catch (SingularMatrixException e) {
                //System.out.println("Singular matrix in second guess");
            }
            //else System.out.println("Second guess is WORST");
        }
         if (fit.amp < 0) {
            try {
            FitParameters fit2 = fitCosParam(data,period,(fit.phase-period/2)%period,-1*fit.amp,fit.offset);
            if (fit2.amp > 0) fit = fit2;
            else if (fit2.SE < fit.SE) fit = fit2;
            } catch (SingularMatrixException e) {
                //System.out.println("Singular matrix in reversing amp");
            }
        }
       
        if (fit.amp < 0) {
            fit.amp = -fit.amp;
            fit.phase = (fit.phase-fit.period/2)%fit.period;
        }
        
        return fitToPPA(fit);
        
        
    }

    
    /**
     * Perform the actual fitting using the provided starting points
     * @param data data to be fitted
     * @param period demanded period of the cos fitted
     * @param phase initial guess for the phase
     * @param amp initial guess for the amplitude
     * @param offset initial guess for the offset
     * @return parameters of the best fit, there is always a result cause in worse case scenario a inital guess will be returned
     */
    protected FitParameters fitCosParam(TimeSeries data, double period, double phase, double amp, double offset) {
        
        DataPack dataPack = new DataPack(data);
        FitParameters initialGuess = new FitParameters(period, phase, amp, offset);
        
        //we calculate the residues and square error
        setResidues(initialGuess,dataPack);
        
        List<FitParameters> fits = new ArrayList<>();
        fits.add(initialGuess);
        
        FitParameters previousFit = initialGuess;
        FitParameters bestFit = initialGuess;
        FitParameters fallingSE = initialGuess;
        
        //System.out.println("Initial:  ph: "+initialGuess.phase+", amp: "+initialGuess.amp+", SE: "+initialGuess.SE);
        
        //if during iteriation SS increases we increase the factor by which the steps will be divided because maybe we walk too fast (too far)
        int factor = 1;
        for (int i=0;i<MAX_LOOPS;i++) {

            FitParameters newFit = improveFit(previousFit,dataPack,factor);
            
            setResidues(newFit, dataPack);

            //System.out.println("Loop: "+i+" ph: "+newFit.phase+", amp: "+newFit.amp+", SE: "+newFit.SE);
            
            fits.add(newFit);
            
            if (newFit.SE < bestFit.SE) bestFit = newFit;
            
            //if (hasConverged(newFit,previousFit,factor))
            if (hasConverged(newFit,fallingSE,previousFit,factor))
                    break;
            
            //if (newFit.SE < fallingSE.SE) fallingSE = newFit;            
            if (newFit.SE < previousFit.SE) fallingSE = newFit;            
            else {
                //we increase error we jump too far?
                factor++;
                if (factor > 4) factor = 4;
                //System.out.println("Cut the factor "+factor);
                 
                //*/
            }
            
            //if (fallingSE.SE != bestFit.SE) System.out.println("Differen");
            
            previousFit = newFit;
        }
        
        //System.out.println("Best ph: "+bestFit.phase+", amp: "+bestFit.amp+", off: "+bestFit.offset+", SE: "+bestFit.SE);
        return bestFit;
        
    }

    /**
     * Calculates the difference between input data and the cosinus difened by the given paramters,
     * it also sums the squares of differences. The resultting residues and square error are stored inside the params object
     * @param params parameters defining cosinus model for which residues should be calculated.
     * Here will be also stored the calculated residues and SE.
     * @param data data with which cosinues should be compared
     */
    protected void setResidues(FitParameters params, DataPack data) {
        
        double[] times = data.times;
        double[] values = data.values;
        double[] residues = new double[times.length];
        
        double TwoPIByPeriod = 2*Math.PI/params.period;
        double offset = params.offset;
        double phase = params.phase;
        double amp = params.amp;
        
        double SE = 0;
        for (int i = 0;i<times.length;i++) {
            
            double t = times[i];
            double val = offset+amp*Math.cos(TwoPIByPeriod*(t+phase));
            double res = values[i]-val;
            residues[i] = res;
            SE+=res*res;
        }
        
        params.residues = residues;
        params.SE = SE;
    }

    /**
     * Converts inner representation into the output PPA object.
     * @param parameters
     * @return 
     */
    protected PPA fitToPPA(FitParameters parameters) {
        
        return new PPA(parameters.period,parameters.phase,parameters.amp,parameters.offset);
    }

    /**
     * Checks if computation has converged. To do so, compares the current square error value (SE) with the last SE which 
     * was still decreasing and if the difference is smaller than CONV*SE than checks if the parameters converged. Parameters are convergend
     * if the difference between new parameters and form previous iterations are smaler P_CONV*PARAM.
     * @param newFit new cos parameters togheters with their SE 
     * @param fallingFit cos fit which has decreasing SE
     * @param previousFit paramters of the previous fit
     * @param factor step decressing factor, by this factor the parameters steps were decreased hence convergance treashold will be also scaled by this
     * @return true if SE has not change much nor the paramters values
     */
    protected boolean hasConverged(FitParameters newFit, FitParameters fallingFit,FitParameters previousFit,int factor) {
        
        //double dif = newFit.SE/previousFit.SE -1;
        double dif = newFit.SE/fallingFit.SE -1;
        dif = Math.abs(dif);
        if (dif > CONV) return false; //the diff is still too large
        
        //if (true) return true;
        //System.out.println("Checking param conv.");
        double ratio = P_CONV/factor;
        //dif is small lets check the params
        if (largerRatio(newFit.offset,previousFit.offset,ratio)) return false;
        if (largerRatio(newFit.amp,previousFit.amp,ratio)) return false;
        if (largerRatio(newFit.phase,previousFit.phase,ratio)) return false;
        
        return true;
    }
    
    /**
     * Checks if the difference ratio between two numbers is larger that given trehsold . (l-m)/m &gt; ration. Values close to 0 are 
     * rounded up to zero before comparison. 
     * @param l one value
     * @param m second value by which it is going to be divided (if 0 than if l != 0) method returns true
     * @param ratio treshold with which the ration should be compared
     * @return true if the relative difference between numbers is larger than ratio
     */
    protected boolean largerRatio(double l,double m,double ratio) {
        if (Math.abs(l) < 1E-5) l = 0;
        if (Math.abs(m) < 1E-5) m = 0;
        if (m == 0) return (l!= 0);
        
        double dif = l/m - 1;
        dif = Math.abs(dif);
        return (dif > ratio);
    }

    /**
     * Finds new cos fitting parameters by applying newton method using the derivateves and jacobian as
     * described in word doc and matlab code.
     * @param previousFit previous set of cos parameters which should be improved
     * @param data to be fit the cos with
     * @param factor scalling factor for change of parameters step. The delta in parameters is divided by this 
     * factor to walk more slowly in case we keep going upward
     * @return new set of cos parameters
     */
    protected FitParameters improveFit(FitParameters previousFit,DataPack data,int factor) {
        
        double times[] = data.times;
        
        int size = 3;
        RealMatrix JTR = new Array2DRowRealMatrix(size,1);
        RealMatrix JTJ = new Array2DRowRealMatrix(size,size);
        
        double period = previousFit.period;
        double TwoPIByPeriod = 2*Math.PI/period;
        double phase = previousFit.phase;
        double amp = previousFit.amp;
        double offset = previousFit.offset;
        
        
        for (int n = 0;n<times.length;n++) {
            
            double time = times[n];
            double dif = previousFit.residues[n];
            
            double partialOffset = 1;
            double partialAmp = Math.cos(TwoPIByPeriod*(time+phase));
            double partialPhase = -TwoPIByPeriod*amp*Math.sin(TwoPIByPeriod*(time+phase));
            
            double[] partials = {partialOffset,partialAmp,partialPhase};
            
            for (int i = 0;i<size;i++) {
                JTR.addToEntry(i, 0, dif*partials[i]);
            }   
            
            for (int i = 0;i<size;i++) {
                for (int j = 0;j<size;j++)
                    JTJ.addToEntry(i, j, partials[i]*partials[j]);
            }
            
        }
        
        //System.out.println(JTJ.toString());
        //System.out.println(JTR.toString());
        RealMatrix e = solveMatrix(JTJ,JTR);
        
        offset+= e.getEntry(0, 0)/factor;
        amp+= e.getEntry(1, 0)/factor;
        phase+=e.getEntry(2, 0)/factor;
        phase = phase % period;
        
        return new FitParameters(period, phase, amp, offset);
    }

    /**
     * Solves the Ab = X equastions using the QR decomposition (so the solution does not have to be exact)
     * @param A Matrix
     * @param X Value vector
     * @return coeficiency vector b
     */
    protected RealMatrix solveMatrix(RealMatrix A, RealMatrix X) {
        
        QRDecomposition decomp = new QRDecomposition(A);
        return decomp.getSolver().solve(X);
    }
    
    
    /**
     * Container for cos parameters that are used to fit into data
     * y = offset + amp*cos(2PI(x+phase)/period)
     */
    static protected class FitParameters {
        double period;
        double phase;
        double amp;
        double offset;
        double SE;
        double[] residues;
        
        public FitParameters(double period,double phase,double amp,double offset) {
            this.period = period;
            this.phase = phase;
            this.amp = amp;
            this.offset = offset;
        }
    }
    
    /**
     * Container for timeseries data and their representations as time and values arrays
     */
    static protected class  DataPack {
        TimeSeries data;
        double[] times;
        double[] values;
        
        public DataPack(TimeSeries data) {
            this.data = data;
            Pair<double[],double[]> timeVals = TimeSeriesOperations.extractTimeAndValueTables(data);
            this.times = timeVals.getLeft();
            this.values = timeVals.getRight();            
        }
    }
    
}
