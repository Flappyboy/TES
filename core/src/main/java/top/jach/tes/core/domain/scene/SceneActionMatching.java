package top.jach.tes.core.domain.scene;

import top.jach.tes.core.domain.action.Action;
import top.jach.tes.core.matching.NToMMatchingWithPriority;

public class SceneActionMatching extends NToMMatchingWithPriority<Scene, Class<? extends Action>> {
    private static SceneActionMatching ourInstance = new SceneActionMatching();

    public static SceneActionMatching getInstance() {
        return ourInstance;
    }

    private SceneActionMatching() {
    }
}
