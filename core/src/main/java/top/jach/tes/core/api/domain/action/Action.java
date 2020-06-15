package top.jach.tes.core.api.domain.action;

import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;

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
    Meta getInputMeta();//meta用于描述Action输入的数据

    OutputInfos execute(I inputInfos, Context context) throws ActionExecuteFailedException;
}
