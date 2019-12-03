package top.jach.tes.plugin.jach.code.git.commit;

import org.junit.Test;
import top.jach.tes.plugin.InfoTool;
import top.jach.tes.plugin.InputInfoProfiles;
import top.jach.tes.plugin.TaskTool;
import top.jach.tes.core.domain.action.Action;
import top.jach.tes.core.domain.action.InputInfos;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.value.StringInfo;

public class GitCommitActionTest {

    @Test
    public void execute() {
        // 创建不存在与数据库中的Info，一般是一些参数
        Info repoPath = StringInfo.createInfo("./repo_path").setName(InputInfos.INFO_NAME);
        // 然后将这些info存入数据库
        InfoTool.saveInfos(repoPath);

        // 创建一些已存在的InfoProfile
//        InfoProfile infoProfile = new InfoProfile(123l, ValueInfo.class);

        InputInfoProfiles infoProfileMap = InputInfoProfiles.InputInfoProfiles()
                .addInfoProfile(GitCommitAction.REPO_PATH, repoPath)
//                .addInfoProfile(infoprofile)
                ;

        Action action = new GitCommitAction();
        TaskTool.excuteActionAndSaveInfo(action, infoProfileMap);
    }
}