/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.util.cosfit;

import ed.robust.error.RobustProcessException;
import ed.robust.util.cosfit.NLLSCosFitter;
import ed.robust.dom.data.TimeSeries;
import ed.robust.dom.data.Timepoint;
import ed.robust.dom.tsprocessing.CosComponent;
import ed.robust.dom.tsprocessing.PPA;
import ed.robust.util.timeseries.TSGenerator;
import ed.robust.util.timeseries.TimeSeriesFileHandler;
import ed.robust.util.timeseries.TimeSeriesOperations;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author tzielins
 */
public class NLLSCosFitterTest {
    
    public NLLSCosFitterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    
    //@Test 
    public void testFitRealData() throws Exception {
        System.out.println("test Real data");
        //List<TimeSeries> list = TimeSeriesFileHandler.readFromText(new File("D:/Performance/inData.csv"), ",",4);
        List<TimeSeries> list = TimeSeriesFileHandler.readFromText(new File("D:/Performance/inData.csv"), ",",4);
        TimeSeries data = list.get(1);
        TimeSeries orgData = data;
        
        data = TimeSeriesOperations.lineDetrended(data);
        
        double period =  26.07;

        NLLSCosFitter instance = new NLLSCosFitter();

        PPA result = instance.fitCos(data, period);
       
        double rotatatedPhase = - (result.getPhase() % result.getPeriod());
        if (rotatatedPhase < 0) rotatatedPhase+=result.getPeriod();
        
               
        System.out.println("P:"+result.getPeriod()+"\tPh:"+result.getPhase()+"\tA:"+result.getAmplitude()+"\tRP:"+rotatatedPhase);
        
        TimeSeries fit = calculateFit(data,result);
        
        result = instance.fitCos(orgData,period);
        TimeSeries fit2 = calculateFit(orgData, result);
        System.out.println("P no trend:"+result.getPeriod()+"\tPh:"+result.getPhase()+"\tA:"+result.getAmplitude()+"\tRP:"+rotatatedPhase);
        
        TimeSeries polyData = TimeSeriesOperations.substract(orgData,TimeSeriesOperations.getPolyTrend(orgData, 3));
        result = instance.fitCos(polyData,period);
        TimeSeries fit3 = calculateFit(orgData, result);
        System.out.println("P poly trend:"+result.getPeriod()+"\tPh:"+result.getPhase()+"\tA:"+result.getAmplitude()+"\tRP:"+rotatatedPhase);
        
        
        list = Arrays.asList(data,fit,orgData,fit2,polyData,fit3);
        
        
        
        TimeSeriesFileHandler.saveToText(list, new File("E:/Temp/fit.csv"), ",");
        
    }
    
    
    @Test
    public void testSingularMatrix() throws IOException, RobustProcessException {
        System.out.println("singular matrix");
        double EPS = 0.1;
        
        double step = 1;
        int N = 120;
        
        double period = 23.5;
        double phase = 5;
        double amp = 2;
        
        TimeSeries data = TSGenerator.makeDblPulse(N, step, period, phase, amp);
        data = TSGenerator.lineDetrended(data);
        //data = TSGenerator.makeStep(N, step, period, phase, amp);
        //data = TSGenerator.makeWave(N, step, period, phase, amp);
        
        //data = TSGenerator.addTrend(data, 0, -TSGenerator.getMeanValue(data));
        
        //TimeSeriesFileHandler.saveToText(data, new File("E:/Temp/in.csv"), ",");
        
        
        NLLSCosFitter instance = new NLLSCosFitter();

        PPA result = instance.fitCos(data, 23.460044802702853);
        
        TimeSeries fit = calculateFit(data,result);
        List<TimeSeries> list = Arrays.asList(data,fit);
        
        //TimeSeriesFileHandler.saveToText(list, new File("E:/Temp/fit-f1.csv"), ",");
        
        assertEquals(period, result.getPeriod(),EPS);
        assertEquals(0, result.getPeriodError(),EPS);
        //assertEquals(-phase, result.getPhase(),EPS);
        assertEquals(0, result.getPhaseError(),EPS);
        //assertEquals(amp, result.getAmplitude(),EPS);
        assertEquals(0, result.getAmplitudeError(),EPS);
        
        System.out.println("P:"+result.getPeriod()+"\tPh:"+result.getPhase()+"\tA:"+result.getAmplitude());
    }
    
    @Test
    public void testFitCos() throws IOException, RobustProcessException {
        System.out.println("fitCos");
        double EPS = 0.1;
        
        double step = 0.5;
        int N = (int) (5*24/step);
        
        double period = 24.3;
        double phase = 5.5;
        double amp = 2.5;
        
        TimeSeries data = TSGenerator.makeCos(N, step, period, phase,amp);
        //data = TSGenerator.makeDblPulse(N, step, period, phase, amp);
        //data = TSGenerator.makeStep(N, step, period, phase, amp);
        //data = TSGenerator.makeWave(N, step, period, phase, amp);
        
        //data = TSGenerator.addTrend(data, 0, -TSGenerator.getMeanValue(data));
        
        //TimeSeriesFileHandler.saveToText(data, new File("E:/Temp/in.csv"), ",");
        
        
        NLLSCosFitter instance = new NLLSCosFitter();

        PPA result = instance.fitCos(data, period);
        
        TimeSeries fit = calculateFit(data,result);
        List<TimeSeries> list = Arrays.asList(data,fit);
        
        //TimeSeriesFileHandler.saveToText(list, new File("E:/Temp/fit.csv"), ",");
        
        assertEquals(period, result.getPeriod(),EPS);
        assertEquals(0, result.getPeriodError(),EPS);
        assertEquals(-phase, result.getPhase(),EPS);
        assertEquals(0, result.getPhaseError(),EPS);
        assertEquals(amp, result.getAmplitude(),EPS);
        assertEquals(0, result.getAmplitudeError(),EPS);
        
        System.out.println("P:"+result.getPeriod()+"\tPh:"+result.getPhase()+"\tA:"+result.getAmplitude());
    }

    protected TimeSeries calculateFit(TimeSeries data, PPA result) {
        
        TimeSeries fit = new TimeSeries();
        
        double TwoPIByPeriod = 2*Math.PI/result.getPeriod();
        double phase = result.getPhase();
        double amp = result.getAmplitude();
        double offset = result.getOffset();
        
        for (Timepoint tp : data) {
            
            double t = tp.getTime();
            fit.add(t,amp*Math.cos(TwoPIByPeriod*(t+phase))+offset);
        }
        return fit;
    }
}
