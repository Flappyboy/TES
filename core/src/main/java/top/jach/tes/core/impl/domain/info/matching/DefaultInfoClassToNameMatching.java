package top.jach.tes.core.impl.domain.info.matching;

import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.impl.matching.NToMMatchingWithoutPriority;

import java.util.Set;

public class DefaultInfoClassToNameMatching implements InfoClassToNameMatching {

    NToMMatchingWithoutPriority<Class<? extends Info>, String> matching = new NToMMatchingWithoutPriority();

    @Override
    public Set<String> getNamesByClass(Class<? extends Info> clazz) {
        return matching.NToM(clazz);
    }

    @Override
    public Set<Class<? extends Info>> getClassByName(String name) {
        return matching.MToN(name);
    }

    @Override
    public String descForNameAndClass(String name, Class<? extends Info> clazz) {
        return "";
    }
}
