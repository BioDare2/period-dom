/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.period;

import ed.robust.dom.data.TimeSeries;
import ed.robust.dom.tsprocessing.PPAResult;

/**
 *
 * @author tzielins
 */
public interface PPAAnalyser {
    
    public PPAResult analyse(TimeSeries data, double periodMin, double periodMax) throws InterruptedException, PeriodProcessException;
    
    public String getMethod();
}
