package top.jach.tes.plugin.tes.code.bug;

import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.impl.domain.element.Element;
import top.jach.tes.core.impl.domain.element.ElementsInfo;

import java.util.*;

public class BugsInfo<B extends Bug> extends ElementsInfo<B> {
    Long reposId;

    private List<B> bugs = new ArrayList<>();

    private Map<String, B> nameBugMap = new HashMap<>();

    public static BugsInfo createInfo(Long reposId){
        BugsInfo info = new BugsInfo();
        info.initBuild();
        info.setReposId(reposId);
        return info;
    }

    public BugsInfo<B> addBugs(B... bugs){
        this.addBugs(Arrays.asList(bugs));
        return this;
    }

    public BugsInfo<B> addBugs(List<B> bugs){
        for (B b :
                bugs) {
            nameBugMap.put(b.getElementName(), b);
        }
        this.bugs.addAll(bugs);
        return this;
    }

    public Long getReposId() {
        return reposId;
    }

    public BugsInfo setReposId(Long reposId) {
        this.reposId = reposId;
        return this;
    }

    public List<B> getBugs() {
        return new ArrayList<>(bugs);
    }

    public BugsInfo<B> setBugs(List<B> bugs) {
        this.bugs.clear();
        this.nameBugMap.clear();
        this.addBugs(bugs);
        return this;
    }

    @Override
    public B getElementByName(String elementName) {
        return nameBugMap.get(elementName);
    }

    @Override
    public Iterator<B> iterator() {
        return bugs.iterator();
    }
}
