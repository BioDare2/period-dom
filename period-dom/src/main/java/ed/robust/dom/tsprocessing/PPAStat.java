/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.dom.tsprocessing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author tzielins
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "period",
    "phase",
    "amplitude"
})
public class PPAStat {
    
        Statistics period;
        Statistics phase;
        Statistics amplitude;
        
        public Statistics getAmplitude() {
            return amplitude;
        }

        public void setAmplitude(Statistics amplitude) {
            this.amplitude = amplitude;
        }


        public Statistics getPeriod() {
            return period;
        }

        public void setPeriod(Statistics period) {
            this.period = period;
        }

        public Statistics getPhase() {
            return phase;
        }

        public void setPhase(Statistics phase) {
            this.phase = phase;
        }

}
