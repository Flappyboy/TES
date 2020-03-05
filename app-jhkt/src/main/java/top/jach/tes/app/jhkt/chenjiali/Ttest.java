package top.jach.tes.app.jhkt.chenjiali;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class Ttest {

    private double[] x;

    private double[] y;
    StandardDeviation standardDeviation =new StandardDeviation();

    public Ttest(double[] x, double[] y) {
        super();
        this.x = x;
        this.y = y;
    }
    public int getXSize() {
        return x==null?0:x.length;
    }
    public int getYSize() {
        return y==null?0:y.length;
    }
    public double getXMean() {
        int n=x.length;
        double sum=0;
        for (double d : x) {
            sum=sum+d;
        }
        return sum/n;
    }
    public double getYMean() {
        int n=y.length;
        double sum=0;
        for (double d : y) {
            sum=sum+d;
        }
        return sum/n;
    }
    public double getStandard(double[] x) {
        return standardDeviation.evaluate(x);
    }
    public double calculateTvalue() {
        double a=getXMean()-getYMean();
        double q1=getStandard(x);
        double q2=getStandard(y);
        double s1=q1*q1;
        double s2=q2*q2;
        double b=Math.sqrt(s1/x.length+s2/y.length);
        return a/b;
    }
    public Double getDegreesOfFreedom() {
        double q1=getStandard(x);
        double q2=getStandard(y);
        double s1=q1*q1;
        double s2=q2*q2;
        double a=(s1/x.length+s2/y.length)*(s1/x.length+s2/y.length);
        double b=((s1/x.length)*(s1/x.length))/(x.length-1)+((s2/y.length)*(s2/y.length))/(y.length-1);
        Double free=a/b;
        return free;
    }
    public double getPValue() {
        double free=getDegreesOfFreedom();
        double t=calculateTvalue();
        TDistribution td=new TDistribution(free);
        double cumulative = td.cumulativeProbability(t);/////原文是cumulative()方法，但TDistribution类中只有
        double p;
        if(t>0) {
            p=(1-cumulative)*2;
        }else {
            p=cumulative*2;
        }
        return p;
    }
}

