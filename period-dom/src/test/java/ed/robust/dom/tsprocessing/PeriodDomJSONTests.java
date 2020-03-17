/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.dom.tsprocessing;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ed.biodare.data.json.TimeSeriesModule;
import ed.robust.dom.data.TimeSeries;
import ed.robust.dom.data.Timepoint;
import ed.robust.dom.tsprocessing.CosComponent;
import ed.robust.dom.tsprocessing.FFT_PPA;
import ed.robust.dom.tsprocessing.FailedPPA;
import ed.robust.dom.tsprocessing.GenericPPAResult;
import ed.robust.dom.tsprocessing.PPA;
import ed.robust.dom.tsprocessing.PPAResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tzielins
 */
public class PeriodDomJSONTests {
    
    
    ObjectMapper mapper;
    
    @Before
    public void setUp() {
        mapper = configureMapper();
    }
    
    @After
    public void tearDown() {
    }    
    
    @Test
    public void ppaCanBeSavedAndBack() throws Exception {
        
        PPA org = new PPA(24, 0.24, 5, 0.5, 10, 0.1, 1, 0.01, 0.12, 0.13, 0.14);
        
        String json = mapper.writeValueAsString(org);
        // System.out.println(json);
        PPA cpy = mapper.readValue(json, PPA.class);
        
        String json2 = mapper.writeValueAsString(org);
        // System.out.println(json2);
        
        assertEquals(json, json2);
        assertEquals(org, cpy);
        
        org = new PPA(24, 3, 6);
        json = mapper.writeValueAsString(org);
        cpy = mapper.readValue(json, PPA.class);        
        
        json2 = mapper.writeValueAsString(org);
        
        assertEquals(json, json2);
        assertEquals(org, cpy);
    }

    @Test
    public void cosComponentCanBeSavedAndBack() throws Exception {
        
        CosComponent org = new CosComponent(new PPA(24, 0.24, 5, 0.5, 10, 0.1, 1, 0.01, 0.12, 0.13, 0.14)
                , new PPA(24,2,10), new PPA(24,3,10), new PPA(24,4,10));
        
        String json = mapper.writeValueAsString(org);
        //System.out.println(json);
        CosComponent cpy = mapper.readValue(json, CosComponent.class);
        
        String json2 = mapper.writeValueAsString(org);
        //System.out.println(json2);
        
        assertEquals(json, json2);
        assertEquals(org, cpy);
        
        org = new CosComponent(10, 0.1, 25, 0.25, 5, 0.5);
        json = mapper.writeValueAsString(org);
        cpy = mapper.readValue(json, CosComponent.class);        
        
        json2 = mapper.writeValueAsString(org);
        
        assertEquals(json, json2);
        assertEquals(org, cpy);
    }
    
    @Test
    public void fftPPACanBeSavedAndBack() throws Exception {
        
        FFT_PPA org = new FFT_PPA();
        org.addCOS(new CosComponent(new PPA(24, 0.24, 5, 0.5, 10, 0.1, 1, 0.01, 0.12, 0.13, 0.14)
                , new PPA(24,2,10), new PPA(24,3,10), new PPA(24,4,10)));
        
        org.addCOS(new CosComponent(10, 0.1, 25, 0.25, 5, 0.5));
        org.setCircadian(true);

        org.setGOF(0.23);
        org.setIgnored(false);
        org.setMessage("I am messge");
        //org.setMethodError(0.3);
        org.setMethodVersion("NLLS_2");
        org.setNeedsAttention(true);
        org.setProcessingTime(123);
        org.setShift(0.5);
        org.setTrendInterception(1);
        org.setTrendSlope(2);
        
        TimeSeries fit = new TimeSeries();
        fit.add(0, 1);
        fit.add(new Timepoint(1, 2, null, 0.1));
        
        org.setFit(fit);
        
        String json = mapper.writeValueAsString(org);
        //System.out.println(json);
        FFT_PPA cpy = mapper.readValue(json, FFT_PPA.class);
        
        String json2 = mapper.writeValueAsString(org);
        //System.out.println(json2);
        
        assertEquals(json, json2);
        assertEquals(org, cpy);
        
        org = new FFT_PPA();
        org.addCOS(10, 1, 26, 1, 3, 0.5);
        json = mapper.writeValueAsString(org);
        cpy = mapper.readValue(json, FFT_PPA.class);
        assertEquals(org, cpy);
    }
    
    @Test
    public void fftPPACanBeSavedAndReadFromAbstract() throws Exception {
        
        FFT_PPA org = new FFT_PPA();
        org.addCOS(new CosComponent(new PPA(24, 0.24, 5, 0.5, 10, 0.1, 1, 0.01, 0.12, 0.13, 0.14)
                , new PPA(24,2,10), new PPA(24,3,10), new PPA(24,4,10)));
        
        org.addCOS(new CosComponent(10, 0.1, 25, 0.25, 5, 0.5));
        org.setCircadian(true);

        org.setGOF(0.23);
        org.setIgnored(false);
        org.setMessage("I am messge");
        //org.setMethodError(0.3);
        org.setMethodVersion("NLLS_2");
        org.setNeedsAttention(true);
        org.setProcessingTime(123);
        org.setShift(0.5);
        org.setTrendInterception(1);
        org.setTrendSlope(2);
        
        TimeSeries fit = new TimeSeries();
        fit.add(0, 1);
        fit.add(new Timepoint(1, 2, null, 0.1));
        
        org.setFit(fit);
        
        String json = mapper.writeValueAsString(org);
        //System.out.println(json);
        PPAResult cpy = mapper.readValue(json, PPAResult.class);
        
        String json2 = mapper.writeValueAsString(org);
        //System.out.println(json2);
        
        assertEquals(json, json2);
        assertEquals(org, cpy);
        
    }    
    
    
    @Test
    public void genericPPAResultCanBeSavedAndReadFromAbstract() throws Exception {
        
        GenericPPAResult org = new GenericPPAResult(new PPA(24, 0.24, 5, 0.5, 10, 0.1, 1, 0.01, 0.12, 0.13, 0.14)
                , new PPA(24,2,10), new PPA(24,3,10), new PPA(24,4,10));
        
        org.setCircadian(true);

        org.setGOF(0.23);
        org.setIgnored(false);
        org.setMessage("I am messge");
        org.setMethodError(0.3);
        org.setMethodVersion("NLLS_2");
        org.setNeedsAttention(true);
        org.setProcessingTime(123);
        
        TimeSeries fit = new TimeSeries();
        fit.add(0, 1);
        fit.add(new Timepoint(1, 2, null, 0.1));
        
        org.setFit(fit);
        
        String json = mapper.writeValueAsString(org);
        System.out.println(json);
        PPAResult cpy = mapper.readValue(json, PPAResult.class);
        
        String json2 = mapper.writeValueAsString(org);
        System.out.println(json2);
        
        assertEquals(json, json2);
        assertEquals(org, cpy);
        
    }    
    
    @Test
    public void failedPPACanBeSavedAndReadFromAbstract() throws Exception {
        
        FailedPPA org = new FailedPPA("yes");
        String json = mapper.writeValueAsString(org);
        //System.out.println(json);
        PPAResult cpy = mapper.readValue(json, PPAResult.class);
        
        String json2 = mapper.writeValueAsString(org);
        //System.out.println(json2);
        
        assertEquals(json, json2);
        assertEquals(org, cpy);
        
    }    
    

    
    public static ObjectMapper configureMapper() {
        ObjectMapper mapper = new ObjectMapper();
        DefaultPrettyPrinter  pp = (new DefaultPrettyPrinter())
                .withoutSpacesInObjectEntries()
                .withArrayIndenter(new DefaultPrettyPrinter.NopIndenter())
                .withObjectIndenter(new DefaultIndenter(" ", "\n"));
        mapper.setDefaultPrettyPrinter(pp);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        mapper.registerModule(new TimeSeriesModule());

        return mapper;
    }
}
