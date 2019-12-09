package top.jach.tes.core.api.domain.info;

import top.jach.tes.core.api.domain.action.OutputInfo;

import java.util.HashSet;
import java.util.Set;

public class DefaultOutputInfo implements OutputInfo {
    Info info;
    Set<String> flags = new HashSet<>();

    public DefaultOutputInfo(Info info) {
        this.info = info;
    }

    public DefaultOutputInfo addFlag(String flag){
        flags.add(flag);
        return this;
    }

    public DefaultOutputInfo(Info info, Set<String> flags) {
        this.info = info;
        this.flags = flags;
    }

    @Override
    public Info getInfo() {
        return info;
    }

    @Override
    public Set<String> flags() {
        return flags;
    }
}
