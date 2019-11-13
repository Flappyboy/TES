package top.jach.tes.core.domain.action;

import top.jach.tes.core.domain.info.InputInfo;
import top.jach.tes.core.domain.info.OutputInfo;
import top.jach.tes.core.domain.meta.MetaData;
import top.jach.tes.core.resource.Resource;

public interface Action {

    /**
     * 功能名，应全局唯一
     * @return
     */
    String getName();

    /**
     * 该功能的描述
     * @return
     */
    String getDesc();

    /**
     * 描述该功能所需的输入
     * @return
     */
    MetaData getMetaData();

    OutputInfo execute(InputInfo inputInfo, Resource resource);
}
