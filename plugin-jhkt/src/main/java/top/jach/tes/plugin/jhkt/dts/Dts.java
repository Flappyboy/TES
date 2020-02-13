package top.jach.tes.plugin.jhkt.dts;

import top.jach.tes.plugin.tes.code.bug.Bug;

import java.util.*;

public class Dts extends Bug {

    // Key->RepoName, Value->shas
    private Map<String, Set<String>> repoShasMap = new HashMap<>();

    public Dts addShas(String repoName, String... newShas){
        Set<String> shas = repoShasMap.get(repoName);
        if(shas == null){
            shas = new HashSet<>();
            repoShasMap.put(repoName, shas);
        }
        shas.addAll(Arrays.asList(newShas));
        return this;
    }

    public Map<String, Set<String>> getRepoShasMap() {
        return repoShasMap;
    }

    public Dts setRepoShasMap(Map<String, Set<String>> repoShasMap) {
        this.repoShasMap = repoShasMap;
        return this;
    }
}
