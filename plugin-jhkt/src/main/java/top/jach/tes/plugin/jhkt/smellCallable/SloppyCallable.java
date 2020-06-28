package top.jach.tes.plugin.jhkt.smellCallable;

import top.jach.tes.core.impl.domain.element.ElementsValue;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.arcsmell.Sloppy.SloppyAction;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author:AdminChen
 * @date:2020/6/11
 * @description:
 */
public class SloppyCallable implements Callable<Map<Integer,ElementsValue>> {
    private MicroservicesInfo microservices;
    private PairRelationsInfo pairRelationsInfoWithoutWeight;
    int thsd;

    public SloppyCallable(MicroservicesInfo microservicesInfo, PairRelationsInfo pairRelationsInfo, int threshold){
        this.microservices=microservicesInfo;
        this.pairRelationsInfoWithoutWeight=pairRelationsInfo;
        this.thsd=threshold;
    }
    @Override
    public Map<Integer,ElementsValue> call() throws Exception {
        //线程运行结果是Map<阈值，AS检测结果>
        //返回的map怎么确定是哪个AS的阈值和对应结果？
        //是否后期在ElementsValue结构中添加阈值的属性，但有些AS检测就是没阈值咋整?
        Map<Integer,ElementsValue> ThresholdResultMap=new HashMap<>();
        ElementsValue evalue;
        int threshold=thsd;//给阈值事先确定一个范围，比如Sloppy的阈值至少大于0
        while(thsd>0){//循环尝试，若得到使结果分布合适的阈值则加入结果集合ThresholdResultMap。但这只是某一次版本的阈值，多版本？
            evalue=SloppyAction.calculateSD(microservices,pairRelationsInfoWithoutWeight,threshold);
            if(isPositive(evalue)){
                ThresholdResultMap.put(threshold,evalue);
            }else{
                threshold=createThsd(thsd);
            }
        }
        return ThresholdResultMap;
    }

    //判断当前运行结果是否符合要求。可是要求是啥？？？找不到一个标准要求
    public boolean isPositive(ElementsValue elementsValue){
        Collection<Double> valueCollection=elementsValue.getValue().values();
        List<Double> values=new ArrayList<>(valueCollection);
        int count=0;
        for(double value:values){
            if(value<1.0){
                count=count+1;
            }
        }
        if(values.size()-count<2){
            return false;
        }else{
            return true;
        }

    }
    //按照某种规律返回阈值的测试值。规律？？
    public int createThsd(int value){

        return --value;
    }


}
