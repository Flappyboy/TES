package top.jach.tes.app.jhkt.chenjiali;

public class CorrelationData {
    String sourceAttr;
    String targetAttr;
    Double pValue;//t检验
    Double rValue;//皮尔森分析

    public CorrelationData(String sourceAttr, String targetAttr, double pValue, double rValue) {
        this.sourceAttr=sourceAttr;
        this.targetAttr=targetAttr;
        this.pValue=pValue;
        this.rValue=rValue;
    }
    public String getSourceAttr(){
        return this.sourceAttr;
    }
    public String getTargetAttr(){
        return this.targetAttr;
    }
    public Double getpValue(){
        return this.pValue;
    }
    public Double getrValue(){
        return this.rValue;
    }

}
