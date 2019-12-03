package top.jach.tes.core.domain.info;

import lombok.Getter;
import lombok.ToString;
import top.jach.tes.core.domain.Entity;

@Getter
@ToString
public class InfoProfile extends Info {

    Class<? extends Info> clazz;

    public InfoProfile(Long id, Class<? extends Info> clazz) {
        this.setId(id);
        this.clazz = clazz;
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
}
