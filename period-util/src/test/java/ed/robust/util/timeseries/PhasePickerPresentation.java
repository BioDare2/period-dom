/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.util.timeseries;

import ed.robust.dom.data.TimeSeries;
import ed.robust.dom.data.Timepoint;
import ed.robust.dom.tsprocessing.PPA;
import ed.robust.error.RobustFormatException;
import ed.robust.error.RobustProcessException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author tzielins
 */
public class PhasePickerPresentation {
    
    public PhasePickerPresentation() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    //@Test
    public void testFindPPAByMax() {
        System.out.println("findPPAByMax");
        
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
        
        double expAmp = (10+0.917667386)/2;
        assertEquals(expAmp, result.getAmplitude(),EPS);
        
        expPeriod = 24.6;
        expPhase = 17;
        expAmp = 2.4;
        int N = 5*24;
        
        EPS = 0.1;
        
        data = TSGenerator.makeCos(N, 1, expPeriod, expPhase,expAmp);        
        data = TSGenerator.makeDblPulse(N, 1, expPeriod, expPhase,expAmp);
        
        result = PhasePicker.findPPAByFirstPeak(data, expPeriod);
        
        assertEquals(expPeriod, result.getPeriod(),EPS);
        
        assertEquals(expPhase, result.getPhase(),EPS);
        
        assertEquals(expAmp, result.getAmplitude(),EPS);
        
    }

    @Test
    public void presentationCases() throws IOException, RobustFormatException, RobustProcessException, InterruptedException
    {
        System.out.println("Presentation casses");
        
        double period = 25;
        double phase = 5;
        double amp = 2;
        int N = 8*24;
        double step = 0.5;
        
        //TimeSeries data1 = TSGenerator.makeDblPulse(N, step, period, phase, amp);
        TimeSeries data1 = TSGenerator.makePulse(N, step, period, phase, amp);
        data1 = TimeSeriesOperations.sum(data1, TSGenerator.makePulse(N, step, period, phase+8, 0.9*amp));
        
        List<TimeSeries> list = new ArrayList<TimeSeries>();
        /*
        for (int i =0;i<10;i++) {
            TimeSeries data2 = TimeSeriesOperations.addWalkingNoise(data1, 0.3,i*System.currentTimeMillis());
            data2 = TimeSeriesOperations.dampen(data2, 0.9);
            list.add(data2);
        }
        
        TimeSeriesFileHandler.saveToText(list, new File("E:/Temp/ff.csv"), ",");
        */
        
        /*
        list = TimeSeriesFileHandler.readFromText(new File("E:/Temp/fbm_in1.csv"), ",");
        
        data1 = list.get(0);
        
        TimeSeries data2 = TimeSeriesOperations.dampen(data1, 0.7);
        
        list.add(TimeSeriesOperations.addTrend(data2, -0.02, 0));
        
        list.add(TimeSeriesOperations.sum(data2, TSGenerator.makeSin(N, step, 0.6*N/step, 0, 6)));
        list.add(TSGenerator.makeSin(N, step, 0.6*N/step, 0, 5));
        TimeSeriesFileHandler.saveToText(list, new File("E:/Temp/fbm_in2.csv"), ",");
         * 
         */
        
        /*
        list = TimeSeriesFileHandler.readFromText(new File("E:/Temp/fbm_in1.csv"), ",");
        
        data1 = list.get(0);
        
        TimeSeriesInterpolator inter = new SplineTSInterpolator(data1,ROUNDING_TYPE.INTEGER);
        data1 = new TimeSeries(inter.makeInterpolation(1,ROUNDING_TYPE.DECY));
        inter = new SplineTSInterpolator(data1);
        list.add(new TimeSeries(inter.makeInterpolation(step,ROUNDING_TYPE.DECY)));
        TimeSeriesFileHandler.saveToText(list, new File("E:/Temp/fbm_tmp.csv"), ",");
         * 
         */
        
        /*
        list = TimeSeriesFileHandler.readFromText(new File("E:/Temp/fbm_in2.csv"), ",");
        data1 = list.get(0);
        
        PPA pbf = PhasePicker.findPPAByFit(data1, period);
        //list.clear();
        //list.add(data1);
        TimeSeries fit = TSGenerator.makeCos(N, step, period, pbf.getPhase(),pbf.getAmplitude());
        fit = TimeSeriesOperations.addTrend(fit, 0, pbf.getOffset());
        list.add(1,fit);
        
        data1 = list.get(2);
        
        pbf = PhasePicker.findPPAByFit(data1, period);
        fit = TSGenerator.makeCos(N, step, period, pbf.getPhase(),pbf.getAmplitude());
        fit = TimeSeriesOperations.addTrend(fit, 0, pbf.getOffset());
        list.add(3,fit);
        
        data1 = list.get(4);
        
        pbf = PhasePicker.findPPAByFit(data1, period);
        fit = TSGenerator.makeCos(N, step, period, pbf.getPhase(),pbf.getAmplitude());
        fit = TimeSeriesOperations.addTrend(fit, 0, pbf.getOffset());
        list.add(5,fit);
        
        TimeSeriesFileHandler.saveToText(list, new File("E:/Temp/fbf_tmp.csv"), ",");
        */
        
        //data1 = TSGenerator.makeWave(N, step, period, phase, amp);
        //data1 = TSGenerator.addNoise(data1,0.3);
        
        list = new ArrayList();        
        //list.add(data1);
        /*for (int i = 0;i<5;i++) {
            TimeSeries t = TimeSeriesOperations.addWalkingNoise(data1, 0.5,i);
            t = TimeSeriesOperations.sum(t,TSGenerator.makeSin(N, step, 1.2*N, 0, 2*amp));
            list.add(t);
        }
        */
        List<TimeSeries> in = TimeSeriesFileHandler.readFromText(new File("E:/Temp/phase_in.csv"), ",");
        for (TimeSeries data : in) {
            list.add(data);
            /*PPA pbf = PhasePicker.findPPAByFit(data, period);
            TimeSeries fit = TSGenerator.makeCos(N, step, period, pbf.getPhase(),pbf.getAmplitude());
            fit = TimeSeriesOperations.addTrend(fit, 0, pbf.getOffset());
            * 
            */
            TimeSeries fit = TimeSeriesOperations.getPolyTrend(data, 3);
            list.add(fit);            
        }
        TimeSeriesFileHandler.saveToText(list, new File("E:/Temp/phase_polytrend.csv"), ",");
        
        /*
        PPA pbf = PhasePicker.findPPAByFit(data1, period);
        TimeSeries fit = TSGenerator.makeCos(N, step, period, pbf.getPhase(),pbf.getAmplitude());
        fit = TimeSeriesOperations.addTrend(fit, 0, pbf.getOffset());
        list.add(fit);  
        TimeSeriesFileHandler.saveToText(list, new File("E:/Temp/fbf_tmp.csv"), ",");
        */
    }

    //@Test
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

    //@Test
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
        
            assertEquals(expPeriod, result.getPeriod(),EPS);
        
            assertEquals(1, result.getPhase(),EPS);
        
            assertEquals(3, result.getAmplitude(),EPS);
   }
}
