/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.util.cosfit;

import ed.robust.dom.data.TimeSeries;
import ed.robust.dom.tsprocessing.PPA;
import ed.robust.util.timeseries.TSGenerator;
import ed.robust.util.timeseries.TimeSeriesFileHandler;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tzielins
 */
public class LSCosFitterTest {
    
    static final double EPS = 1E-6;
    
    public LSCosFitterTest() {
    }

    
    /**
     * Test of makeFitCos method, of class LSCosFitter.
     */
    @Test
    public void testFitCos() throws IOException {
        System.out.println("fitCos");
        
        int N = 100;
        double step = 1;
        double period = 23.6;
        double phase = 0;
        double amp = 4.1;
        
        TimeSeries fit = TSGenerator.makeCos(N, step, period, phase,amp);
        
        LSCosFitter instance = new LSCosFitter();
        PPA result = instance.fitCos(fit, period);
        
        assertEquals(period, result.getPeriod(),EPS);
        assertEquals(amp, result.getAmplitude(),EPS);
        assertEquals(phase, result.getPhase(),EPS);   
        
        phase = 6;
        fit = TSGenerator.makeCos(N, step, period, phase,amp);
        TimeSeriesFileHandler.saveToText(fit, new File("E:/Temp/cos.csv"), ",");
        
        
        for (phase = 0;phase<period;phase+=1) {
            amp = Math.random()*100+1;
        
            fit = TSGenerator.makeCos(N, step, period, phase,amp);        
            result = instance.fitCos(fit, period);
        
            assertEquals(period, result.getPeriod(),EPS);
            assertEquals(amp, result.getAmplitude(),EPS);
            assertEquals(phase, result.getPhase(),EPS);           
            //System.out.println("P:"+phase+", "+result.getPhase());
        }
        
        
        for (phase = 0;phase<24;phase+=1) {
            amp = Math.random()*10+1;
            period = 24+Math.random()*1;
            fit = TSGenerator.makeCos(N, step, period, phase,amp);        
             
            result = instance.fitCos(fit, period);
        
            assertEquals(period, result.getPeriod(),EPS);
            assertEquals(amp, result.getAmplitude(),0.01*amp);
            assertEquals(phase, result.getPhase(),0.01);           
       }
    }
    

    /**
     * Test of makeFitCos method, of class LSCosFitter.
     */
    @Test
    public void testFitException() throws IOException {
        System.out.println("fitCosException");
        
        int N = 1;
        double step = 1;
        double period = 23.6;
        double phase = 10;
        double amp = 4.1;
        
        
        TimeSeries fit = new TimeSeries();
        LSCosFitter instance = new LSCosFitter();
        try {
            PPA result = instance.fitCos(fit, period);
            fail("Illegal arugment exceptio expected");
        } catch (IllegalArgumentException e) {};
        
        for (N =1;N<5;N++) {
            fit = TSGenerator.makeCos(N, step, period, phase,amp);
            PPA result = instance.fitCos(fit, period);
        
            assertEquals(period, result.getPeriod(),EPS);
            //assertEquals(amp, result.getAmplitude(),EPS);
            assertEquals(phase, result.getPhase(),2);   
        }
        
        N = 12*30*24*60;
        fit = TSGenerator.makeCos(N, 1.0/60.0, period, phase,amp);
        PPA result = instance.fitCos(fit, period);
        
        assertEquals(period, result.getPeriod(),EPS);
        assertEquals(amp, result.getAmplitude(),EPS);
        assertEquals(phase, result.getPhase(),0.1);           
    }
    
   /**
     * Test of makeFitCos method visually, of class LSCosFitter.
     */
    @Test
    public void testFitCosVis() throws IOException {
        System.out.println("fitCosVis");
        
        int N = 100;
        double step = 1;
        double period = 23.6;
        double phase = 0;
        double amp = 4.1;
        
        
        LSCosFitter instance = new LSCosFitter();
        List<TimeSeries> series = new ArrayList<>();
        
        for (phase = 0;phase<24;phase+=1) {
            amp = Math.random()*10+1;
            period = 24+Math.random()*1;
            TimeSeries data = TSGenerator.makePulse(N, step, period, phase,amp);        
             
            PPA result = instance.fitCos(data, period);
        
            assertEquals(period, result.getPeriod(),EPS);
            //assertEquals(amp, result.getAmplitude(),0.01*amp);
            //assertEquals(phase, result.getPhase(),0.05*phase);           
            series.add(data);
            TimeSeries fit = TSGenerator.makeCos(N,step,result.getPeriod(),result.getPhase(),result.getAmplitude());
            //double mean = data.getMeanValue()-fit.getMeanValue();
            fit = TSGenerator.addTrend(fit, 0, result.getOffset());
            series.add(fit);
       }
        
        TimeSeriesFileHandler.saveToText(series, new File("E:/Temp/phase.csv"), ",");
    }
    
    
    /**
     * Test of makeWaveMatrix method, of class LSCosFitter.
     */
    @Test
    public void testMakeWaveMatrix() {
        System.out.println("makeWaveMatrix");
        double[] times = {0,6,12,24};
        double period = 24;
        
        double[][] expWave = {{1,0,1},{0,1,1},{-1,0,1},{1,0,1}};
        
        LSCosFitter instance = new LSCosFitter();
        RealMatrix result = instance.makeWaveMatrix(times, period);
        RealMatrix expResult = MatrixUtils.createRealMatrix(expWave);
        
        assertEquals(3,result.getColumnDimension());
        assertEquals(4,result.getRowDimension());
        
        for (int i =0;i<expResult.getColumnDimension();i++) {
            assertArrayEquals(expResult.getColumn(i), result.getColumn(i),EPS);
        }
    }

    /**
     * Test of getWaveCoeficients method, of class LSCosFitter.
     */
    @Test
    public void testGetWaveCoeficients() {
        System.out.println("getWaveCoeficients");
        double[][] waveM = new double[][]{{0,1},{2,3},{4,5}};
        double[] expX = {1,2};
        double[] B = {0*1+1*2,2*1+3*2,4*1+5*2};
        
        RealMatrix wave = MatrixUtils.createRealMatrix(waveM);
        RealMatrix values = MatrixUtils.createColumnRealMatrix(B);
        
        LSCosFitter instance = new LSCosFitter();
        RealMatrix result = instance.getWaveCoeficients(wave, values);        
        
        assertEquals(1, result.getColumnDimension());
        assertEquals(2,result.getRowDimension());
        double[] resultC = result.getColumn(0);
        
        assertArrayEquals(expX,resultC,EPS);
        
    }

    /**
     * Test of assemblePPA method, of class LSCosFitter.
     */
    @Test
    public void testAssemblePPA() {
        System.out.println("assemblePPA");
        
        
        RealMatrix coefs = MatrixUtils.createColumnRealMatrix(new double[]{1,0,0});
        
        double period = 24;
        LSCosFitter instance = new LSCosFitter();

        double expAmp = 1;
        double expPhase = 0;
        
        PPA result = instance.assemblePPA(coefs, period);
        assertEquals(period, result.getPeriod(),EPS);
        assertEquals(expAmp, result.getAmplitude(),EPS);
        assertEquals(expPhase, result.getPhase(),EPS);
        
        coefs = MatrixUtils.createColumnRealMatrix(new double[]{0,2,1});
        
        expAmp = 2;
        expPhase = period/4;
        
        result = instance.assemblePPA(coefs, period);
        assertEquals(period, result.getPeriod(),EPS);
        assertEquals(expAmp, result.getAmplitude(),EPS);
        assertEquals(expPhase, result.getPhase(),EPS);        
    }
}