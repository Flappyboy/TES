package top.jach.tes.core.simple.domain.action;

import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public abstract class SimpleAction implements Action {
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getDesc() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Meta getInputMeta() {
        return null;
    }

    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        Method[] methods = this.getClass().getMethods();
        Method method = null;
        int matchMax = 0;
        for (Method m :
                methods){
            ActionExcute actionExcute = m.getAnnotation(ActionExcute.class);
            if(actionExcute==null){
                continue;
            }
            System.out.println(actionExcute.toString());
            Parameter[] parameters = m.getParameters();
            int match = 0;
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                Class type = parameter.getType();
                InfoParam infoParam = parameter.getAnnotation(InfoParam.class);
                parameter.isNamePresent();
                System.out.println(parameter.getName());
            }
            if(match > matchMax){
                matchMax = match;
                method = m;
            }
        }

        Object[] args = new Object[3];
        try {
            method.invoke(method, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws ActionExecuteFailedException {
        SimpleAction action = new SimpleAction() {
            @ActionExcute
            public void func(@InfoParam String ssa){

            }
        };
        action.execute(null, null);
    }
}
