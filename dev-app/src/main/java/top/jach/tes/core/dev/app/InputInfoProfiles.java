package top.jach.tes.core.dev.app;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.InfoProfile;

import java.util.HashMap;

public class InputInfoProfiles extends HashMap<String, InfoProfile> {
    public static InputInfoProfiles InputInfoProfiles(){
        return new InputInfoProfiles();
    }
    public InputInfoProfiles addInfoProfile(String name, Info info){
        put(name, InfoProfile.createFromInfo(info));
        return this;
    }
}
