package top.jach.tes.app.jhkt.chenjiali;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author:AdminChen
 * @date:2020/6/17
 * @description:
 */

//输入ElementsValue的List，输出这一组数据的分布图表
public class AsDistribute {
    static int effective_number=3;//决定要保留的小数点后有效位数
    static int space =10;//步长 你要分为几个区间

    public static void main(String[] args){

    }

    public static double getRange(List<Double> list){
        Collections.sort(list);//排序 以方便得到最大最小值
        double min=list.get(0);
        double max=list.get(list.size()-1);
        double range=(max-min)/space;
        BigDecimal temp =new BigDecimal(range);
        range=temp.setScale(effective_number, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println("1:排序并计算min,max,range");
        System.out.print("序列min="+min+"\t");
        System.out.print("序列max="+max+"\t");
        System.out.println("序列range="+range+"\t");
        return range;
    }

    public static void getDistribution(List<Double> list){
        double range=getRange(list);

    }

    //处理2个double数相加 避免出现2个相加后等于.013999999999999等情况
      public static double add(double v1, double v2) {
                     BigDecimal b1 = new BigDecimal(Double.toString(v1));
                     BigDecimal b2 = new BigDecimal(Double.toString(v2));
                     return b1.add(b2).doubleValue();
      }
}
