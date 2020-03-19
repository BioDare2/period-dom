/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.dom.tsprocessing;

/**
 * For spectrum resampling
 * @author tzielins
 */
public class FreqComponent implements Comparable<FreqComponent>{
    
    public static double INF_PERIOD = 10000;
    double frequency;
    double phase;
    double amplitude;

    public FreqComponent() {
        
    }

    public FreqComponent(double frequency, double phase, double amplitude) {
        this.frequency = frequency;
        this.phase = phase;
        this.amplitude = amplitude;
    }

    
    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public double getFrequency() {
        return frequency;
    }
    
    public double getPeriod() {
        if (frequency != 0)
            return 2*Math.PI/frequency;
        else return INF_PERIOD;
    }
    
    public void setPeriod(double period) {
        setFrequency(2*Math.PI/period);
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double getPhase() {
        return phase;
    }

    public void setPhase(double phase) {
        this.phase = phase;
    }

    @Override
    public int compareTo(FreqComponent o) {
        return (int)Math.signum(this.amplitude-o.amplitude);
    }
    
    
}
