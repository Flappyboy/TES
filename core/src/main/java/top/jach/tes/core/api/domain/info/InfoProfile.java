package top.jach.tes.core.api.domain.info;

import lombok.ToString;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;

import java.util.Arrays;
import java.util.List;

@ToString
public class InfoProfile extends Info {

    Class<? extends Info> clazz;

    public InfoProfile(Long id, Class<? extends Info> clazz) {
        this.setId(id);
        this.clazz = clazz;
    }

    public Class<? extends Info> getClazz() {
        return clazz;
    }

    public InfoProfile setClazz(Class<? extends Info> clazz) {
        this.clazz = clazz;
        return this;
    }

    @Override
    public void initBuild() {
        throw new RuntimeException("can't init InfoProfile");
    }

    public static InfoProfile createFromInfo(Info info){
        InfoProfile infoProfile = new InfoProfile(info.getId(), info.getInfoClass());
        infoProfile.setId(info.getId()).setCreatedTime(info.getCreatedTime()).setUpdatedTime(info.getUpdatedTime());
        infoProfile.setDesc(info.getDesc()).setStatus(info.getStatus());
        return infoProfile;
    }

    @Override
    public Class<? extends Info> getInfoClass() {
        return clazz;
    }

    public Info toInfoWithDetail(InfoRepositoryFactory infoRepositoryFactory){
        List<Info> result = infoRepositoryFactory.getRepository(getInfoClass()).
                queryDetailsByInfoIds(Arrays.asList(getId()));
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }
}