package top.jach.tes.plugin.jach.code.repo;

import lombok.Data;
import top.jach.tes.core.domain.info.Info;

import java.util.List;

@Data
public class ReposInfo extends Info {
    private List<Long> repoIds;
}
