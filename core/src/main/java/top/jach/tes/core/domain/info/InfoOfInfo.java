package top.jach.tes.core.domain.info;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Getter
public class InfoOfInfo<I extends Info> extends Info implements Iterable<I>{
    private List<I> infos = new ArrayList<>();

    @SafeVarargs
    public final InfoOfInfo addInfos(I... infos){
        return addInfos(Arrays.asList(infos));
    }

    public InfoOfInfo addInfos(List<I> infos){
        this.infos.addAll(infos);
        return this;
    }

    @Override
    public Iterator<I> iterator() {
        return infos.iterator();
    }

    public static <I extends Info> InfoOfInfo<I> createInfoOfInfo(List<I> infos){
        InfoOfInfo<I> infoOfInfo = new InfoOfInfo<>();
        infoOfInfo.addInfos(infos);
        return infoOfInfo;
    }

    @SafeVarargs
    public static <I extends Info> InfoOfInfo<I> createInfoOfInfo(I... infos){
        InfoOfInfo<I> infoOfInfo = new InfoOfInfo<>();
        infoOfInfo.addInfos(infos);
        return infoOfInfo;
    }
}
