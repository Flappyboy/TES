package top.jach.tes.plugin.jhkt.Threshold;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.jach.tes.core.impl.domain.element.ElementsValue;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;

import java.util.List;

/**
 目前来看所有需要进行阈值测试的AS检测都有共同的参数传入，那就是microservicesInfo和pairrelationsInfo。故多线程执行结果是源数据、阈值和执行结果
 */
@Data
@AllArgsConstructor
public class ThresholdResult {
    private MicroservicesInfo microservicesInfo;
    private PairRelationsInfo pairRelationsInfo;
    private List<Double> thrhds;
    private ElementsValue elementsValue;
}
