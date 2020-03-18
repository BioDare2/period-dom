/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.period;

import ed.robust.dom.data.TimeSeries;
import ed.robust.dom.data.Timepoint;
import ed.robust.dom.tsprocessing.GenericPPAResult;
import ed.robust.dom.tsprocessing.PPAResult;
import ed.robust.util.timeseries.TSGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tzielins
 */
public class PPAMultiAnalyserTest {
    
    double periodMin = 20;
    double periodMax = 28;
    double EPS = 1E-6;
    
    public PPAMultiAnalyserTest() {
    }


    static class TestingPPAMultiAnalyser extends PPAMultiAnalyser {
        
        public TestingPPAMultiAnalyser(PPAAnalyser analyser, int threads, int defChunk) {
            super(analyser, threads, defChunk);
        }

        public TestingPPAMultiAnalyser() {
            super(new TestingPPAAnalyser(), 1, 10);
        }
        
    }
    
    static class TestingPPAAnalyser implements PPAAnalyser {

        @Override
        public PPAResult analyse(TimeSeries data, double periodMin, double periodMax) throws InterruptedException, PeriodProcessException {
            
            double period = data.getFirst().getValue();
            return new GenericPPAResult(period, 1, 10);
        }

        @Override
        public String getMethod() {
            return "testingAnalyser";
        }
        
    }
    
    TestingPPAMultiAnalyser instance;
    @Before
    public void setUp() {
        
        instance = new TestingPPAMultiAnalyser();
    }

    @Test
    public void testAnalyseDataWorks() throws Exception  {
        
        TimeSeries data = TSGenerator.makeCos(24*5, 1, 24, 5,10);
        data = setValue(data, 24, 0);
        PPAResult res = instance.analyseData(data, periodMin, periodMax);
        
        assertEquals(24.0, res.getPeriod(), 0.1);
        assertEquals(1.0, res.getPhase(), 0.1);
        assertEquals(10.0, res.getAmplitude(), 0.5);
        
        
    }
    
    @Test
    public void testAnalyseDataSubset() throws Exception {
        
        List<TimeSeries> datas = List.of(
        setValue(TSGenerator.makeCos(24*5, 1, 22, 5,10), 22, 0),
        setValue(TSGenerator.makeCos(24*5, 1, 24, 5,10), 24, 0),
        setValue(TSGenerator.makeCos(24*5, 1, 26, 5,10), 26, 0)
        );
        
        List<PPAResult> expected = new ArrayList<>();
        
        for (TimeSeries data : datas)
            expected.add(instance.analyseData(data, periodMin, periodMax));
        
        List<PPAResult> res = instance.analyseDataSubset(datas, periodMin, periodMax, 0, datas.size());
        for (int i = 0; i< expected.size(); i++) {
            assertEquals(expected.get(i).getPeriod(), res.get(i).getPeriod(), EPS);
        }
        
        res = instance.analyseDataSubset(datas, periodMin, periodMax, 2, datas.size());
        assertEquals(1, res.size());
        
        assertEquals(26.0, res.get(0).getPeriod(), 0.1);
    }
    
    @Test
    public void analyseInParrallelWorks() {
        
        List<Double> periods = List.of(24.0, 23.0, 22.0, 21.0, 20.0, 21.0, 22.0, 23.0, 24.0);
        
        List<TimeSeries> datas = periods.stream().map( period -> setValue(TSGenerator.makeCos(24*5, 1, period, 5,10),period,0))                
                .collect(Collectors.toList());
        
        
        List<PPAResult> res = instance.analyse(datas, periodMin, periodMax, 2, 7);
        
        assertEquals(periods.size(), res.size());
        
        for (int i =0; i< periods.size(); i++) {
            assertEquals(periods.get(i), res.get(i).getPeriod(), 0.1);
        }
       
    }

    TimeSeries setValue(TimeSeries data, double val, int ix) {
        
        List<Timepoint> points = data.getTimepoints();
        List<Timepoint> mod = new ArrayList<>(points.size());
        
        mod.addAll(points.subList(0, ix));
        Timepoint p = points.get(ix);
        p = new Timepoint(p.getTime(), val, p.getStdError(), p.getStdDev());
        mod.add(p);
        mod.addAll(points.subList(ix+1, points.size()));
        return new TimeSeries(mod);
    }
        
}
