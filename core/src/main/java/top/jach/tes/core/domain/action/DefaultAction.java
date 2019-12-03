package top.jach.tes.core.domain.action;

import top.jach.tes.core.domain.context.Context;
import top.jach.tes.core.domain.info.DefaultInputInfos;
import top.jach.tes.core.domain.meta.Meta;

public class DefaultAction implements Action, StatefulAction{

    Action action;

    SaveInfoAction saveAction;

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
        return null;
    }

    @Override
    public OutputInfos execute(InputInfos inputInfo, Context context) {
        OutputInfos outputInfos = action.execute(inputInfo, context);
        if (outputInfos == null){
            return null;
        }
        InputInfos tmp = new DefaultInputInfos();
        for (OutputInfo outputInfo :
                outputInfos.getOutputInfoList()) {
            if(outputInfo.flags().contains(OutputInfo.Flag.SAVE.name())){
                tmp.put(String.valueOf(tmp.size()), outputInfo.getInfo());
            }
        }
        saveAction.execute(tmp, context);
        return null;
    }

    public DefaultAction setSaveAction(SaveInfoAction saveAction) {
        this.saveAction = saveAction;
        return this;
    }

    public DefaultAction setAction(Action action) {
        this.action = action;
        return this;
    }

    @Override
    public String serialize() {
        return StatefulAction.serializeActionToJson(action);
    }

    @Override
    public void deserialization(String action) {
        this.action = StatefulAction.deserializeActionFromJson(action);
    }
}
