package top.jach.tes.core.domain.action;

import top.jach.tes.core.domain.context.Context;
import top.jach.tes.core.domain.meta.Field;
import top.jach.tes.core.domain.meta.Meta;
import top.jach.tes.core.domain.meta.StringField;

import java.util.ArrayList;
import java.util.List;

public class DemoAction implements Action{
    @Override
    public String getName() {
        return "Demo";
    }

    @Override
    public String getDesc() {
        return "Demo";
    }

    @Override
    public Meta getInputMeta() {
        return () -> {
            List<Field> metas = new ArrayList<>();
            metas.add(new StringField() {
                @Override
                public String getName() {
                    return "String_Name";
                }

                @Override
                public String displayName() {
                    return getName();
                }
            });
            return metas;
        };
    }

    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) {
        context.Logger().info("Demo Start");

        context.Logger().info("Demo End");
        return null;
    }
}
