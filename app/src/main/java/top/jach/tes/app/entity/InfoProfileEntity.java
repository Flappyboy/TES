package top.jach.tes.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.jach.tes.core.domain.action.InputInfos;
import top.jach.tes.core.domain.info.DefaultInputInfos;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.InfoProfile;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@Embeddable
public class InfoProfileEntity {
    private Long id;

    @Column(name = "`name`")
    private String name;

    private Class<? extends Info> clazz;

    public Info toInfo(){
        Info info = new InfoProfile(id, clazz);
        info.setName(name);
        return info;
    }

    public static InfoProfileEntity createEntities(Info info){
        InfoProfileEntity infoProfileEntity = new InfoProfileEntity();
        infoProfileEntity.setId(info.getId());
        infoProfileEntity.setName(info.getName());
        infoProfileEntity.setClazz(info.getInfoClass());
        return infoProfileEntity;
    }

    public static Map<String, Info> entitiesToInfos(Map<String, InfoProfileEntity> infoProfileEntityMap){
        InputInfos inputInfos = new DefaultInputInfos();
        for (Map.Entry<String, InfoProfileEntity> entry :
                infoProfileEntityMap.entrySet()) {
            inputInfos.put(entry.getKey(), entry.getValue().toInfo());
        }
        return inputInfos;
    }
    public static Map<String, InfoProfileEntity> infosToEntities(Map<String, Info> infoMap){
        Map<String, InfoProfileEntity> infoProfileEntityMap = new HashMap<>();
        for (Map.Entry<String, Info> entry :
                infoMap.entrySet()) {
            infoProfileEntityMap.put(entry.getKey(), InfoProfileEntity.createEntities(entry.getValue()));
        }
        return infoProfileEntityMap;
    }
}
