package top.jach.tes.plugin.jhkt.dts;

import top.jach.tes.plugin.tes.code.bug.BugsInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DtssInfo extends BugsInfo<Dts> {

    public static DtssInfo createInfo(Long reposId){
        DtssInfo info = new DtssInfo();
        info.initBuild();
        info.setReposId(reposId);
        return info;
    }

    public Map<String, Set<String>> repoShasMap(){
        Map<String, Set<String>> repoShasMap = new HashMap<>();
        for (Dts dts :
                getBugs()) {
            if (dts.getRepoShasMap()!=null) {
                for (Map.Entry<String, Set<String>> entry :
                        dts.getRepoShasMap().entrySet()) {
                    Set<String> set = repoShasMap.get(entry.getKey());
                    if (set==null){
                        set = new HashSet<>();
                        repoShasMap.put(entry.getKey(), set);
                    }
                    set.addAll(entry.getValue());
                }
            }
        }
        return repoShasMap;
    }
}
