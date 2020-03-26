/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.robust.dom.tsprocessing;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "n",
    "mean",
    "stdErr",
    "stdDev",
    "median",
    "min",
    "max",
    "sum",
    "kurtosis",
    "skewness"
})
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Statistics implements Serializable {
    
        private static final long serialVersionUID = 10L;

	@XmlAttribute
	protected long n;
	@XmlAttribute
	protected double mean;
	@XmlAttribute
	protected double stdErr;
	@XmlAttribute
        protected double stdDev;
	@XmlAttribute
	protected Double median;
	@XmlAttribute
	protected Double min;
	@XmlAttribute
	protected Double max;
	@XmlAttribute
	protected Double sum;	
	@XmlAttribute(name="kur")
	protected Double kurtosis;
	@XmlAttribute(name = "skew")
	protected Double skewness;

    public double getKurtosis() {
        if (kurtosis == null) return Double.NaN;
        return kurtosis;
    }

    public void setKurtosis(double kurtosis) {
        this.kurtosis = kurtosis;
    }

    public double getMax() {
        if (max == null) return Double.NaN;
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getMedian() {
        if (median == null) return Double.NaN;
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public double getMin() {
        if (min == null) return Double.NaN;
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public long getN() {
        return n;
    }

    public void setN(long n) {
        this.n = n;
    }

    public double getSkewness() {
        if (skewness == null) return Double.NaN;
        return skewness;
    }

    public void setSkewness(double skewness) {
        this.skewness = skewness;
    }

    public double getStdDev() {
        return stdDev;
    }

    public void setStdDev(double stdDev) {
        this.stdDev = stdDev;
    }

    public double getStdErr() {
        return stdErr;
    }

    public void setStdErr(double stdErr) {
        this.stdErr = stdErr;
    }

    public double getSum() {
        if (sum == null) return Double.NaN;
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
       
        
        
    public double getVariance() {
        return Math.pow(stdDev, 2);
    }
        
}
