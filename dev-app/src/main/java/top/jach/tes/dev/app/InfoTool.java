package top.jach.tes.dev.app;

import top.jach.tes.core.domain.action.InputInfos;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.value.*;

import java.io.File;
import java.util.Arrays;

public class InfoTool {
    public static void saveInfos(Info... infos){
        Environment.infoService().saveInfos(ProjectTool.DevAppProject(), Arrays.asList(infos));
    }

    public static void saveInputInfos(Info... infos){
        for (Info info :
                infos) {
            info.setName(InputInfos.INFO_NAME);
        }
        Environment.infoService().saveInfos(ProjectTool.DevAppProject(), Arrays.asList(infos));
    }

    public static ValueInfo wrapperValueInfo(Object object){
        if(object instanceof String){
            return StringInfo.createInfo((String)object);
        }else if (object instanceof Integer){
            return IntegerInfo.createInfo((Integer)object);
        }else if (object instanceof Long){
            return LongInfo.createInfo((Long)object);
        }else if (object instanceof File){
            return FileInfo.createInfo((File)object);
        }else if (object instanceof Float){
            return FloatInfo.createInfo((Float)object);
        }else if (object instanceof Double){
            return DoubleInfo.createInfo((Double)object);
        }
        throw new RuntimeException("Object 应该是 ValueInfo 所支持的类型：String, Integer, Long, File, Float, Double");
    }
}
