package top.jach.tes.plugin.jach.code.git.tree;

import org.junit.Test;
import top.jach.tes.dev.app.InfoTool;
import top.jach.tes.dev.app.InputInfoProfiles;
import top.jach.tes.dev.app.TaskTool;
import top.jach.tes.core.domain.action.Action;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.value.FileInfo;
import top.jach.tes.core.domain.info.value.StringInfo;

import java.io.File;

public class GitTreeActionTest {

/*    @Test
    public void execute() {
        // 创建不存在与数据库中的Info，一般是一些参数
        Info repoDir = FileInfo.createInfo(new File("E:\\workspace\\otherproject\\dddsample-core"));
        Info sha = StringInfo.createInfo("master");

        // 然后将这些info存入数据库
        InfoTool.saveInputInfos(repoDir, sha);

        // 创建一些已存在的InfoProfile
//        InfoProfile infoProfile = new InfoProfile(123l, ValueInfo.class);

        InputInfoProfiles infoProfileMap = InputInfoProfiles.InputInfoProfiles()
                .addInfoProfile(GitTreeAction.LOCAL_REPO_DIR, repoDir)
                .addInfoProfile(GitTreeAction.COMMIT_SHA, sha);

        Action action = new GitTreeAction();
//        TaskTool.excuteActionAndSaveInfo(action, infoProfileMap);
    }*/
}