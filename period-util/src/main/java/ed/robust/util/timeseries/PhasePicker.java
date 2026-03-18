/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.util.timeseries;

import ed.robust.dom.data.TimeSeries;
import ed.robust.dom.data.Timepoint;
import ed.robust.dom.tsprocessing.PPA;
import ed.robust.dom.util.Pair;
import ed.robust.util.cosfit.LSCosFitter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;


/**
 *
 * @author tzielins
 */
public class PhasePicker {
    
    protected static final int INTERPOLATION_CUTOFF = 1024;
    //static final double PERIOD_EXPANSION = 2.6;
    static final double PERIOD_EXPANSION_FACT = 1.11;
    
    /**
     * Finds phase and amplitude in the data using 'PBFP = Phase By First Peak' approach. Phase is defined as the time of highest data peak in the first
     * cycle of data, amplitude as half of the distance between values of highest peak and lowest valley.
     * <p>
     * Data are smoothly interpolated with resolution of 0.1 hours, then the first period length of data is analysed
     * and the highest peak and lower valley are found in this data subset. If the borders are raising(falling) above(below) the 
     * highest and lowest peak, additional few hours of data are analysed (PERIOD_EXPANSION constant). This will assure that if the peak
     * is at the very start of the data it will be captured in the beginning of the second cycle. If no peaks can be found than 
     * MAX (or MIN) of this data subset are used instead. 
     * <p>Phase is reported as related to 0 in the range 0 - Period, so the phase 1 means that data peaks at time 1. 
     * @param data to be analysed for its phase and amplitude in the first cycle
     * @param expPeriod period length in the data which determines the first cycle of data == window of interest
     * @return PPA with period, phase and amplitude set, phase as time of highest peak relative to 0, amplitude as half the distance between 
     * peak and the valley, phase is in the range [0,period).
     */
    public static PPA findPPAByFirstPeak(TimeSeries data, double expPeriod) {
        
        if (data.isEmpty()) throw new IllegalArgumentException("Data cannot be empty");
        if (expPeriod <= 0) throw new IllegalArgumentException("Expected period must be > 0");
        
        if (data.size() == 1) {
            Timepoint theOne = data.getFirst();
            double phase = theOne.getTime()%expPeriod;
            if (phase < 0) phase+=expPeriod;
            double amp = 0;
            return new PPA(expPeriod, phase, amp);
        }
        
        double length = PERIOD_EXPANSION_FACT*expPeriod;
        double dEnd = data.getFirst().getTime()+length;
        if (dEnd < data.getLast().getTime()) dEnd = data.getLast().getTime();
        
        
        data = data.subSeries(data.getFirst().getTime(), dEnd);
        
        //if (data.size() < 3) throw new IllegalArgumentException("Too litle points in the data during first cycle");
        
        DataSource source = new SplineTSInterpolator(data, ROUNDING_TYPE.CENTY);
        
        return findPPAByFirstPeak(source,expPeriod);
    }
    
    /**
     * Finds phase and amplitude in the data using 'PBFP = Phase By First Peak' approach. Phase is defined as the time of highest data peak in the first
     * cycle of data, amplitude as half of the distance between values of highest peak and lowest valley.
     * <p>
     * Data are retrieved with resolution of 0.1 hours, then the first period length of data is analysed
     * and the highest peak and lower valley are found in this data subset. If the borders are raising(falling) above(below) the 
     * highest and lowest peak, additional few hours of data are analysed (PERIOD_EXPANSION constant). This will assure that if the peak
     * is at the very start of the data it will be captured in the beginning of the second cycle. If no peaks can be found than 
     * MAX (or MIN) of this data subset are used instead. 
     * <p>Phase is reported as related to 0 in the range 0 - Period, so the phase 1 means that data peaks at time 1. 
     * @param data data source with that contains timeseries to be analysed, it should be a 'smooth' data source
     * so that the peaks will be nice and no platoes, a spline one is recommended
     * @param expPeriod period length in the data which determines the first cycle of data == window of interest
     * @return PPA with period, phase and amplitude set, phase as time of highest peak relative to 0, amplitude as half the distance between 
     * peak and the valley, phase is in the range [0,period).
     */
    public static PPA findPPAByFirstPeak(DataSource data,double expPeriod) {
        
        if (expPeriod <= 0) throw new IllegalArgumentException("Expected period must be > 0");
        
        double length = PERIOD_EXPANSION_FACT*expPeriod;
        double dEnd = data.getFirstTime()+length;
        
        if (dEnd > data.getLastTime()) dEnd = data.getLastTime();
        
        
        List<Timepoint> points = data.getTimepoints(data.getFirstTime(),dEnd, 0.1, ROUNDING_TYPE.DECY);

        //System.out.println(""+points);
        double duration = data.getLastTime()-data.getFirstTime();
        if (duration < expPeriod) {
            return findPPAByMinMax(points,expPeriod);
        }
        
        
        
        double stop = data.getFirstTime()+expPeriod;
        int stopIndex = findIndexOf(stop,points);
        Pair<Timepoint,Timepoint> highestAndLowestPeaks = TimeSeriesOperations.findHighestLowestPeaks(points.subList(0,stopIndex+1));
        
        Timepoint highest = highestAndLowestPeaks.getLeft();
        Timepoint lowest = highestAndLowestPeaks.getRight();
        double highestV = highest != null ? highest.getValue() : -Double.MAX_VALUE;
        double lowestV = lowest != null ? lowest.getValue() : Double.MAX_VALUE;
        
        //System.out.println("Highest: "+highest);
        //System.out.println("Lowest: "+lowest);
        
        //lets check if the borders were not raising/falling if so try to go through longer points
        double firstV = points.get(0).getValue();
        double stopV = points.get(stopIndex).getValue();
        
        boolean searchWiderPeaks = (firstV >= highestV || stopV >= highestV);
        boolean searchWiderValleys = (firstV <= lowestV || stopV <= lowestV);
        
        if (searchWiderPeaks || searchWiderValleys) highestAndLowestPeaks = TimeSeriesOperations.findHighestLowestPeaks(points);
        if (searchWiderPeaks) highest = highestAndLowestPeaks.getLeft();
        if (searchWiderValleys) lowest = highestAndLowestPeaks.getRight();
        
        //System.out.println("Highest: "+highest);
        //System.out.println("Lowest: "+lowest);
        
        
        if (highest == null || lowest == null) {
            TimeSeries tmp = new TimeSeries();
            tmp.addAll(points.subList(0,stopIndex+1));
            Pair<Timepoint,Timepoint> minMax = TimeSeriesOperations.getMinMax(tmp);
            if (highest == null) highest = minMax.getRight();
            if (lowest == null) lowest = minMax.getLeft();
        }
        
        double phase = highest.getTime() % expPeriod;
        
        double amp = (highest.getValue()-lowest.getValue())/2;
        
        phase = SmartDataRounder.round(phase,expPeriod);
        amp = SmartDataRounder.round(amp);
        
        return new PPA(expPeriod,phase,amp);
    }
    

    /**
     * Finds index of the last point which has time value &lt;= stop.
     * Points are assumed to be already sorted by time 
     *
     * @param stop the time for which to look index
     * @param points list of sorted timepoints
     * @return index of the last time point which time value is &lt;=stop. 
     * @throws IllegalArgumentException if stop is smaller than time of the first point
     */
    public static int findIndexOf(double stop, List<Timepoint> points) {
        
        int good = -1;
        for (int i = 0;i<points.size();i++) {
            if (points.get(i).getTime() <= stop) good = i;
            else break;
        }
        if (good < 0) throw new IllegalArgumentException("Stop value: "+stop+" is smaller than time of the first data point");
        return good;
    }
    
    /**
     * Finds the phase and amplitude of data using PBF = 'phase by fit' model, ie by fitting a cos function to the data.
     * For given period the cos function y = Offset+Amp*cos(2PI(x-Phase)/period) that has smallest square error is found and its phase and amplitude are used as the answers.
     * If data are long and sampled more frequently than every hour, then the data are interpoplated with 1 hour step to reduce
     * number of data points and improved performance.
     * <p>Phases are rotated so that are given in the range [0,period) and are matching the peak rather than in typical model (cos+phase) model
     * @param data to be analysed for its phase and amplitude
     * @param expPeriod period length in the data which determines the frequency of cosinues used for fitting
     * @return phase and amplitude of fitted cosinues together with its offset. 
     * The phases are reported relative to 0 in the range [0,priod), so the phase 5 means that cos (data) peaks at time 5, (so the underlying
     * cos is given by cos(x-5).
     */
    public static PPA findPPAByFit(TimeSeries data, double expPeriod) throws InterruptedException {
        
        
        
        if (data.size() > INTERPOLATION_CUTOFF && data.getAverageStep() < 0.9) {
            
            TimeSeriesInterpolator inter = new BinningLinearTSInterpolator(data,ROUNDING_TYPE.CENTY);
            data = new TimeSeries(inter.makeInterpolation(1, ROUNDING_TYPE.DECY));
        }
       if (Thread.interrupted())
            throw new InterruptedException(Thread.currentThread().hashCode() + " have been interupted in findPPAByFit before cos fit");
        
        LSCosFitter cosFitter = new LSCosFitter();
        PPA cosFit = cosFitter.fitCos(data, expPeriod);
        
        
        cosFit.setPhase(SmartDataRounder.round(cosFit.getPhase(),expPeriod));
        cosFit.setAmplitude(SmartDataRounder.round(cosFit.getAmplitude()));
        cosFit.setOffset(SmartDataRounder.round(cosFit.getOffset()));
        return cosFit;
    }
    
    /**
     * Finds the phase and amplitude of data using PBF = 'phase by fit' model, ie by fitting a cos function to the data.
     * For given period the cos function y = Offset+Amp*cos(2PI(x-Phase)/period) that has smallest square error is found and its phase and amplitude are used as the answers.
     * Data points will be generated from the source every hour.
     * <p>Phases are rotated so that are given in the range [0,period) and are matching the peak rather than in typical model (cos+phase) model
     * @param data to be analysed for its phase and amplitude
     * @param expPeriod period length in the data which determines the frequency of cosinues used for fitting
     * @return phase and amplitude of fitted cosinues together with its offset. 
     * The phases are reported relative to 0 in the range [0,period), so the phase 5 means that cos (data) peaks at time 5, (so the underlying
     * cos is given by cos(x-5).
     * @throws RobustProcessException if cos could not be fit due to numerical instability
     */
    public static PPA findPPAByFit(DataSource data, double expPeriod) throws InterruptedException {
        
        TimeSeries ts = new TimeSeries(data.getTimepoints(1, ROUNDING_TYPE.DECY));
        return findPPAByFit(ts,expPeriod);
    }    
    
    
    public static PPA findPPAByAvgMax(TimeSeries data, double expPeriod) throws InterruptedException {
        
        if (data.isEmpty()) throw new IllegalArgumentException("Data cannot be empty");
        if (expPeriod <= 0) throw new IllegalArgumentException("Expected period must be > 0");
        if (data.getDuration() < expPeriod) {
            //throw new IllegalArgumentException("Data must be longer than one period");
            return findPPAByFirstPeak(data, expPeriod);
        }
        
        //DataSource source = new BinningLinearTSInterpolator(data, ROUNDING_TYPE.CENTY);
        DataSource source = new SplineTSInterpolator(data, ROUNDING_TYPE.CENTY);

       if (Thread.interrupted())
            throw new InterruptedException(Thread.currentThread().hashCode() + " have been interupted findPPAByAvgMax while preparing interpolation");
         
        return findPPAByAvgMax(source,expPeriod);
    }
    
    public static PPA findPPAByAvgMax(DataSource data, double expPeriod) throws InterruptedException {
        
        if (expPeriod <= 0) throw new IllegalArgumentException("Expected period must be > 0");
        if (expPeriod < 0.2) {
            return new PPA(expPeriod,0,0);
        }
        
        double rangeStart = data.getFirstTime();
        //double rangeEnd = rangeStart+expPeriod;
        double END = data.getLastTime();
        
        List<Double> maxs = new ArrayList<>();
        DescriptiveStatistics amps = new DescriptiveStatistics();
        
        double dur = expPeriod-0.1;
        while ((rangeStart+expPeriod) <= END) {
            List<Timepoint> points = data.getTimepoints(rangeStart, rangeStart+dur, 0.1, ROUNDING_TYPE.DECY);            
            Pair<Timepoint,Timepoint> minMax = TimeSeriesOperations.getMinMax(points);
            maxs.add(minMax.getRight().getTime()-rangeStart);
            amps.addValue((minMax.getRight().getValue()- minMax.getLeft().getValue())/2);
            //System.out.println("P: "+points.get(0).getTime()+"-"+points.get(points.size()-1).getTime()+", max: "+minMax.getRight().getTime()+", maxS: "+(minMax.getRight().getTime()-rangeStart));
            rangeStart += expPeriod;
            
            if (Thread.interrupted())
                throw new InterruptedException(Thread.currentThread().hashCode() + " have been interupted findPPAByAvgMax while looping through data");
        }
        
        DescriptiveStatistics phases = averagePhases(maxs,expPeriod);
        
        double phase = (data.getFirstTime()+phases.getMean()) % expPeriod;
        double amplitude = amps.getMean();
        
        phase = SmartDataRounder.round(phase,expPeriod);
        amplitude = SmartDataRounder.round(amplitude);

        PPA ppa = new PPA(expPeriod, phase, amplitude);
        ppa.setPhaseError(SmartDataRounder.round(2*phases.getStandardDeviation()));
        ppa.setAmplitudeError(SmartDataRounder.round(2*amps.getStandardDeviation()));
        
        return ppa;
        
    }

    public static DescriptiveStatistics averagePhases(List<Double> phases, double period) {
        
        DescriptiveStatistics orgTimes = new DescriptiveStatistics();
        DescriptiveStatistics shiftedTimes = new DescriptiveStatistics();
        
        double halfPeriod = period/2;
        for (Double phase : phases) {
            orgTimes.addValue(phase);
            if (phase < halfPeriod) shiftedTimes.addValue(phase+period);
            else shiftedTimes.addValue(phase);
        }
        
        if (orgTimes.getStandardDeviation() <= shiftedTimes.getStandardDeviation()) {
            return orgTimes;
        }
        else {
            //System.out.println("Using shifted: "+shiftedTimes.getMean());
            return shiftedTimes;
        }
        
    }

    protected static PPA findPPAByMinMax(List<Timepoint> points, double expPeriod) {
        
        Pair<Timepoint,Timepoint> minMax = TimeSeriesOperations.getMinMax(points);        
   
        Timepoint lowest = minMax.getLeft();
        Timepoint highest = minMax.getRight();
        
        double phase = highest.getTime() % expPeriod;
        
        double amp = (highest.getValue()-lowest.getValue())/2;
        
        phase = SmartDataRounder.round(phase,expPeriod);
        amp = SmartDataRounder.round(amp);
        
        return new PPA(expPeriod,phase,amp);
        
    }
}
