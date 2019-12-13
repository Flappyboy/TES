package top.jach.tes.app.mock;

import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.impl.domain.info.value.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class InfoTool {
    public static void saveInfos(Info... infos){
        Environment.infoService.saveInfos(Environment.defaultProject, Arrays.asList(infos));
    }

    public static void saveInputInfos(Info... infos){
        for (Info info :
                infos) {
            info.setName(InputInfos.INFO_NAME);
        }
        Environment.infoService.saveInfos(Environment.defaultProject, Arrays.asList(infos));
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

    public static <I extends Info> I queryLastInfoByNameAndInfoClass(String infoName, Class<I> infoClass){
        List<I> infos = Environment.infoRepositoryFactory.getRepository(infoClass)
                .queryDetailsByInfoAndProjectId(new Info() {
                    @Override
                    public String getName() {
                        return infoName;
                    }
                    @Override
                    public Class<I> getInfoClass() {
                        return infoClass;
                    }
                }, Environment.defaultProject.getId(), PageQueryDto.create(1, 1).setSortField("createdTime"));
        if(infos.size()==0){
            return null;
        }
        return infos.get(0);
    }
}
