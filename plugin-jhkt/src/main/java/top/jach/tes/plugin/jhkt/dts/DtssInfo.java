package top.jach.tes.plugin.jhkt.dts;

import top.jach.tes.plugin.tes.code.bug.BugsInfo;

public class DtssInfo extends BugsInfo<Dts> {

    public static DtssInfo createInfo(Long reposId){
        DtssInfo info = new DtssInfo();
        info.initBuild();
        info.setReposId(reposId);
        return info;
    }
}
