/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.period;

/**
 *
 * @author tzielins
 */
public class PeriodProcessException extends Exception {
    
    
    public PeriodProcessException(String msg) {
        super(msg);
    }
    
    public PeriodProcessException(String msg, Throwable err) {
        super(msg, err);
    }
    
    public PeriodProcessException(Throwable err) {
        super(err);
    }
}
