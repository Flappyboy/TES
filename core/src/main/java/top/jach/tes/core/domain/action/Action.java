package top.jach.tes.core.domain.action;

import top.jach.tes.core.domain.context.Context;
import top.jach.tes.core.domain.meta.Meta;

public interface Action<I extends InputInfos> {

    /**
     * 功能名
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
    Meta getInputMeta();

    OutputInfos execute(I inputInfo, Context context);
}
