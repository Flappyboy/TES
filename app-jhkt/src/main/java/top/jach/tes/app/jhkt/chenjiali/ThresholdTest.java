package top.jach.tes.app.jhkt.chenjiali;

import org.apache.commons.collections.ArrayStack;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.core.impl.domain.element.Element;
import top.jach.tes.core.impl.domain.element.ElementsValue;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.InfoNameConstant;
import top.jach.tes.plugin.jhkt.arcsmell.ui.UiAction;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.jhkt.smellCallable.SloppyCallable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author:AdminChen
 * @date:2020/5/23
 * @description:
 */
//这边只负责供给各种不同的源数据，用线程池来并发计算AS，并拿到各个AS计算得到的结果
public class ThresholdTest {
    public static void main(String[] args){
        //源数据
        MicroservicesInfo microservices = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.MicroservicesForRepos, MicroservicesInfo.class);
        PairRelationsInfo pairRelationsInfo = microservices.callRelationsInfoByTopic(false);
        //存放各个线程运行结果
        List<Map<Integer,ElementsValue>> resuts=new ArrayList<>();
        //新建线程池
        ExecutorService executor=Executors.newFixedThreadPool(6);

        //创建一个线程专门用于Sloppy阈值的计算测试，阈值的初始值为6
        SloppyCallable sloppyCallable=new SloppyCallable(microservices,pairRelationsInfo,6);
        //Map<String, ElementsValue> relsss=new HashMap<>((Map<String,ElementsValue>)executor.submit(sloppyCallable));
        //线程执行后返回的值是Future结构的，需要强转成所需要的Map结构
        resuts.add((Map<Integer, ElementsValue>)executor.submit(sloppyCallable));

        //下一个AS线程
        //执行结果add到results中去......
        //最终results中存储的是所有测试过符合要求的AS检测结果以及对应的阈值。
        // excl输出就可以以这个为源数据来输出

    }


}
