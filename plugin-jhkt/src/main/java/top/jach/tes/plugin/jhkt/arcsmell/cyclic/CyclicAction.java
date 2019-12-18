package top.jach.tes.plugin.jhkt.arcsmell.cyclic;

import org.apache.commons.compress.utils.Lists;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.DefaultOutputInfos;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.element.Element;
import top.jach.tes.core.impl.domain.element.ElementsInfo;
import top.jach.tes.core.impl.domain.info.value.StringInfo;
import top.jach.tes.core.impl.domain.meta.InfoField;
import top.jach.tes.core.impl.domain.relation.PairRelation;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;

import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CyclicAction implements Action {
    public static final String Elements_INFO = "elements_info";
    public static final String PAIR_RELATIONS_INFO = "PairRelationsInfo";
    public static final int MAX_NODE_COUNT = 100;
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public Meta getInputMeta() {
        return () -> Arrays.asList(
                InfoField.createField(Elements_INFO).setInfoClass(ElementsInfo.class),
                InfoField.createField(PAIR_RELATIONS_INFO).setInfoClass(PairRelationsInfo.class)
        );
    }
//寻找环回路信息
    private static void findCycle(int v,List<Integer> trace,List<String> result,List<String> nodes,int[][] matrix)
    {
        int j;
        //添加闭环信息
        if((j=trace.indexOf(v))!=-1) {
            StringBuffer sb = new StringBuffer();
            String startNode = nodes.get(trace.get(j));
            while(j<trace.size()) {
                sb.append(nodes.get(trace.get(j))+"-");//最终输出结果为a-b-c-a这样的形式
                j++;
            }
            result.add("cycle:"+sb.toString()+startNode);
            return;
        }
        trace.add(v);
        for(int i=0;i<nodes.size();i++){
            if(matrix[v][i]==1) {
                findCycle(i,trace,result,nodes,matrix);
            }
        }
        trace.remove(trace.size()-1);
    }

//该方法根据元素和元素之间的关系，以此为参数调用方法，输出架构异味
    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        ElementsInfo<Element> elementsInfo = inputInfos.getInfo(Elements_INFO, ElementsInfo.class);
        PairRelationsInfo pairRelationsInfo = inputInfos.getInfo(PAIR_RELATIONS_INFO, PairRelationsInfo.class);
        //构建有向图
        List<Element> nodess = Lists.newArrayList(elementsInfo.iterator());
        List<String> elements=new ArrayList<String>();//存储节点名即可
        for(Element e:nodess){
            elements.add(e.getElementName());
        }
        List<PairRelation> relations = Lists.newArrayList(pairRelationsInfo.getRelations().iterator());
        int [][] matrix=new int[MAX_NODE_COUNT][MAX_NODE_COUNT];//有向图的邻接矩阵
        if(elements.size()>MAX_NODE_COUNT){
            context.Logger().info("elements 超长");
        }
        else{
            //初始化矩阵
            for(PairRelation pr:relations){
                  int startIndex=elements.indexOf(pr.getSourceName());
                  int endIndex=elements.indexOf(pr.getTargetName());
                 // String gp="n";
                  if(startIndex>=0&&endIndex>=0){
                      matrix[startIndex][endIndex]=1;
                    //  gp="y";
                  }
               // context.Logger().info(gp);//打印出来的全都是n，说明矩阵根本没有初始化成功
            }

            // 从出发节点到当前节点的轨迹
            List<Integer> trace =new ArrayList<Integer>();
            //存储要打印输出的环回路
            List<String> result = new ArrayList<>();
            if(matrix.length>0){
                findCycle(0,trace,result,elements,matrix);
            }

            if(result.size()==0){
               result.add("no cycle dependence");
            }
            //打印输出环回路
            for (String string : result) {
                context.Logger().info(string);
            }
        }

        return null;//暂时return null就可以了
    }
}
