package top.jach.tes.plugin.tes.code.git.commit;

import top.jach.tes.core.domain.action.OutputInfo;
import top.jach.tes.core.domain.context.Context;
import top.jach.tes.core.domain.action.Action;
import top.jach.tes.core.domain.action.InputInfos;
import top.jach.tes.core.domain.action.OutputInfos;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.value.StringInfo;
import top.jach.tes.core.domain.meta.Meta;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GitCommitAction implements Action {
    public static final String REPO_PATH = "repo_path";

    @Override
    public String getName() {
        return "GitCommit";
    }

    @Override
    public String getDesc() {
        return "git commit";
    }

    @Override
    public Meta getInputMeta() {
        return null;
    }

    @Override
    public OutputInfos execute(InputInfos inputInfo, Context context) {
        context.Logger().info(inputInfo.getInfo(REPO_PATH, StringInfo.class).getValue());
        GitCommitInfo info = new GitCommitInfo(Arrays.asList(
                new Commit().setSha("123").setAuthor("hh"),
                new Commit().setSha("1234").setAuthor("ha")
        ));
        info.initBuild();

        return () -> Arrays.asList(new OutputInfo() {
            @Override
            public Info getInfo() {
                return info;
            }

            @Override
            public Set<String> flags() {
                Set<String> flags = new HashSet<>();
                flags.add(Flag.SAVE.name());
                return flags;
            }
        });
    }
}
