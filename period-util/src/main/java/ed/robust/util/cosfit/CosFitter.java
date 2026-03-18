/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.util.cosfit;

import ed.robust.dom.data.TimeSeries;
import ed.robust.dom.tsprocessing.PPA;
import ed.robust.error.RobustProcessException;

/**
 * Interface for classes that can perform cos fitting to the input data
 * @author tzielins
 */
public interface CosFitter {
   
    /**
     * Fits one cos wave of the given period to the data. Function is of the form Acos(2Pi(x-Phase)/Period)+Offset
     * @param data to which cosinus should be fitted
     * @param period period which cosinus wave should have
     * @return PPA object that contains phase, amplitude and linear offset of the fitted cosinus. Depending on implementation
     * some error values may be provided as well. PPA has also period set, in case it has been change slightly for example due to the rounding.
     * @throws RobustProcessException if fitting cannot be done due to some numerical instability
     */
    public PPA fitCos(TimeSeries data,double period) throws RobustProcessException;
}
