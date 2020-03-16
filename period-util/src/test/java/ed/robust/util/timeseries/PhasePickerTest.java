/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.util.timeseries;

import ed.robust.dom.data.TimeSeries;
import ed.robust.dom.data.Timepoint;
import ed.robust.dom.tsprocessing.PPA;
import ed.robust.error.RobustProcessException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author tzielins
 */
public class PhasePickerTest {
    
    public PhasePickerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testAveragePhases() {
        
        
        System.out.println("averagePhases");
    
        List<Double> phases = Arrays.asList(0.0,1.0,2.0,1.0);
        
        double period = 20;
        
        double expPhase = 1;
        double EPS = 0.0001;
        
        DescriptiveStatistics result = PhasePicker.averagePhases(phases, period);
        
        assertEquals(expPhase, result.getMean(),EPS);
        
        phases = Arrays.asList(9.0,10.0,11.0,10.0);
        expPhase = 10;
        result = PhasePicker.averagePhases(phases, period);
        assertEquals(expPhase, result.getMean(),EPS);
        
        phases = Arrays.asList(19.0,18.0,18.0,17.0);
        expPhase = 18;
        result = PhasePicker.averagePhases(phases, period);
        assertEquals(expPhase, result.getMean() % period,EPS);
        
        phases = Arrays.asList(19.0,1.0,0.0,0.0);
        expPhase = 0;
        result = PhasePicker.averagePhases(phases, period);
        assertEquals(expPhase, result.getMean() % period,EPS);
        
        phases = Arrays.asList(0.0,1.0,18.0,17.0);
        expPhase = 19;
        result = PhasePicker.averagePhases(phases, period);
        assertEquals(expPhase, result.getMean() % period,EPS);
    }
    
    @Test
    public void testFindPPAByAvgMax() throws InterruptedException {
        System.out.println("findPPAByAvgMax");
        
        TimeSeries data = new TimeSeries();
        data.add(5,5);
        data.add(6,7);
        data.add(7,5);
        data.add(8,6);
        data.add(9,5);
        data.add(10,4);
        data.add(11,0);
        data.add(12,3);
        data.add(14,4);
        data.add(15,5);
        data.add(16,7);
        data.add(17,4);
        data.add(18,5);
        
        
        
        
        double expPeriod = 10;
        
        PPA result = PhasePicker.findPPAByAvgMax(data, expPeriod);
        
        double EPS = 0.1;
        
        assertEquals(expPeriod, result.getPeriod(),EPS);
        
        double expPhase = 6;
        assertEquals(expPhase, result.getPhase(),EPS);
        
        double expAmp = (7.0)/2;
        assertEquals(expAmp, result.getAmplitude(),EPS);
        
        expPeriod = 24.6;
        expPhase = 3.5;
        expAmp = 2.4;
        int N = 7*24;
        
        EPS = 0.1;
        
        /*
        data = TSGenerator.makeCos(N, 1, expPeriod, expPhase,expAmp);        
        data = TSGenerator.sum(data,TSGenerator.makeCos(N, 1, expPeriod/3, expPhase,expAmp/3));
        data = TSGenerator.sum(data,TSGenerator.makeCos(N, 1, expPeriod/3*4, expPhase,expAmp/2));
        */
        
        data = TSGenerator.makeDblPulse(N, 1, expPeriod, expPhase,expAmp);
        //data = TSGenerator.addNoise(data, 0.1);
        
        result = PhasePicker.findPPAByAvgMax(data, expPeriod);
        
        System.out.println("Phase: "+result.getPhase()+", PE: "+result.getPhaseError());
        assertEquals(expPeriod, result.getPeriod(),EPS);
        
        assertEquals(expPhase, result.getPhase(),EPS);
        
        assertEquals(expAmp, result.getAmplitude(),EPS);
        
    }
    
    @Test
    public void testFindPPAByAvgMaxShort() throws InterruptedException {
        System.out.println("findPPAByAvgMaxShort");
        
        TimeSeries data = new TimeSeries();
        double expPeriod = 10;
        double expPhase = 6;
        double expAmp = (7.0)/2;
        
        double EPS = 0.1;
        PPA result;
        
        try {
            result = PhasePicker.findPPAByAvgMax(data, expPeriod);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {};
        
        data.add(1,1);
        result = PhasePicker.findPPAByAvgMax(data, expPeriod);
        expPhase = 1;
        expAmp = 0;
        assertEquals(expPeriod, result.getPeriod(),EPS);     
        assertEquals(expPhase, result.getPhase(),EPS);        
        assertEquals(expAmp, result.getAmplitude(),EPS);
        
        data.add(2,2);
        result = PhasePicker.findPPAByAvgMax(data, expPeriod);
        expPhase = 2;
        expAmp = 0.5;
        assertEquals(expPeriod, result.getPeriod(),EPS);     
        assertEquals(expAmp, result.getAmplitude(),EPS);
        assertEquals(expPhase, result.getPhase(),EPS);        
        
        expPeriod = 0.4;
        result = PhasePicker.findPPAByAvgMax(data, expPeriod);
        expPhase = 0.2;
        expAmp = 0.2;
        assertEquals(expPeriod, result.getPeriod(),EPS);     
        assertEquals(expAmp, result.getAmplitude(),EPS);
        assertEquals(expPhase, result.getPhase(),EPS);        
     
        expPeriod = 0;
        try {
            result = PhasePicker.findPPAByAvgMax(data, expPeriod);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {};
        
        data = TSGenerator.makeDblPulse(100, 1, 23, 7, 2);
        expPeriod = 0.21;
        result = PhasePicker.findPPAByAvgMax(data, expPeriod);
        assertEquals(expPeriod, result.getPeriod(),EPS);     
        //System.out.println("Phase: "+result.getPhase()+", PE: "+result.getPhaseError());
    }

    
    @Test
    public void testFindPPAByFirstPeak() {
        System.out.println("findPPAByFistPeak");
        
        TimeSeries data = new TimeSeries();
        data.add(5,6);
        data.add(6,5);
        data.add(7,5);
        data.add(8,6);
        data.add(9,5);
        data.add(10,4);
        data.add(11,0);
        data.add(13,1);
        data.add(14,4);
        data.add(15,10);
        data.add(16,4);
        data.add(17,4);
        data.add(18,5);
        
        
        
        
        double expPeriod = 10;
        
        PPA result = PhasePicker.findPPAByFirstPeak(data, expPeriod);
        
        double EPS = 0.01;
        
        assertEquals(expPeriod, result.getPeriod(),EPS);
        
        double expPhase = 5;
        assertEquals(expPhase, result.getPhase(),EPS);
        
        double expAmp = (10+0.8973529413689887)/2;
        assertEquals(expAmp, result.getAmplitude(),EPS);
        
        expPeriod = 24.6;
        expPhase = 17;
        expAmp = 2.4;
        int N = 5*24;
        
        EPS = 0.1;
        
        data = TSGenerator.makeDblPulse(N, 1, expPeriod, expPhase,expAmp);
        
        result = PhasePicker.findPPAByFirstPeak(data, expPeriod);
        System.out.println("Phase: "+result.getPhase()+", PE: "+result.getPhaseError());
        
        assertEquals(expPeriod, result.getPeriod(),EPS);
        
        assertEquals(expPhase, result.getPhase(),EPS);
        
        assertEquals(expAmp, result.getAmplitude(),EPS);
        
         
        
    }
    
    @Test
    public void testFindPPAByFirstPeakShortData() {
        System.out.println("findPPAByFistPeak Short Data");
        
        PPA result;        
        double EPS = 0.01;
        
        TimeSeries data = new TimeSeries();
        double expPeriod = 10;
        double expPhase = 0;
        double expAmp = 0;
        
        try {
            result = PhasePicker.findPPAByFirstPeak(data, expPeriod);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {};
        
        data.add(1,1);
        result = PhasePicker.findPPAByFirstPeak(data, expPeriod);
        expPhase = 1;
        expAmp = 0;
        assertEquals(expPeriod, result.getPeriod(),EPS);     
        assertEquals(expPhase, result.getPhase(),EPS);        
        assertEquals(expAmp, result.getAmplitude(),EPS);
        
        data.add(2,2);
        result = PhasePicker.findPPAByFirstPeak(data, expPeriod);
        expPhase = 2;
        expAmp = 0.5;
        assertEquals(expPeriod, result.getPeriod(),EPS);     
        assertEquals(expAmp, result.getAmplitude(),EPS);
        assertEquals(expPhase, result.getPhase(),EPS);        
        
        expPeriod = 0.4;
        result = PhasePicker.findPPAByFirstPeak(data, expPeriod);
        expPhase = 0.2;
        expAmp = 0.2;
        assertEquals(expPeriod, result.getPeriod(),EPS);     
        assertEquals(expAmp, result.getAmplitude(),EPS);
        assertEquals(expPhase, result.getPhase(),EPS);        
     
        expPeriod = 0;
        try {
            result = PhasePicker.findPPAByFirstPeak(data, expPeriod);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {};
        
    }


    @Test
    public void testFindIndexOf() {
        System.out.println("findIndexOf");
        
        TimeSeries data = new TimeSeries();
        data.add(5,1);
        data.add(6,2);
        data.add(7,1);
        data.add(8,0);
        data.add(9,0);
        data.add(10,1);
        
        double stop = 0.0;
        int expResult = 0;
        int result = 0;
        List<Timepoint> points = data.getTimepoints();
        
        stop = 0;
        try {
            result = PhasePicker.findIndexOf(stop, points);
            fail("Exception expected");
        } catch(IllegalArgumentException e) {};
        
        stop = 5;
        expResult = 0;
        result = PhasePicker.findIndexOf(stop, points);
        assertEquals(expResult, result);
        
        stop = 7.5;
        expResult = 2;
        result = PhasePicker.findIndexOf(stop, points);
        assertEquals(expResult, result);
        
        stop = 10;
        expResult = 5;
        result = PhasePicker.findIndexOf(stop, points);
        assertEquals(expResult, result);
        
        stop = 15;
        expResult = 5;
        result = PhasePicker.findIndexOf(stop, points);
        assertEquals(expResult, result);
        
        
    }

    @Test
    public void testFindPPAByFit() throws RobustProcessException, InterruptedException {
        System.out.println("findPPAByFit");
        
        double expPeriod = 25.6;
        double expPhase = 1;
        double expAmp = 2.4;
        int N = 5*24;
        
        double EPS = 0.1;
        
        TimeSeries data = TSGenerator.makeCos(N, 1, expPeriod, expPhase,expAmp);        
        //data = TSGenerator.makeStep(N, 1, expPeriod, expPhase,expAmp);
        
        PPA result = PhasePicker.findPPAByFit(data, expPeriod);
        
        assertEquals(expPeriod, result.getPeriod(),EPS);
        
        assertEquals(expPhase, result.getPhase(),EPS);
        
        assertEquals(expAmp, result.getAmplitude(),EPS);
        
        
		double period = 25.2;
                expPeriod = period;
		double amp = 3;
                double phase = 1;
		
		TimeSeries p1data = TSGenerator.makeCos(N,1, period, phase,amp);			
		
                //if (p1data.length > 0) return p1data;
                
		period = 15;
		amp = 1;
		
		TimeSeries  p2data = TSGenerator.makeCos(N,1, period, 0, amp);
				
		period = 40;
		amp = 0.5;//0.5;
		
		TimeSeries  p3data = TSGenerator.makeCos(N,1, period, 0, amp);
                
                data = TSGenerator.sum(p1data, TSGenerator.sum(p2data,p3data));
                
                result = PhasePicker.findPPAByFit(data, expPeriod);
         System.out.println("Phase: "+result.getPhase()+", PE: "+result.getPhaseError());
       
            assertEquals(expPeriod, result.getPeriod(),EPS);
        
            assertEquals(1, result.getPhase(),EPS);
        
            assertEquals(3, result.getAmplitude(),EPS);
   }
}
