package top.jach.tes.core.impl.domain.scene;

import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.impl.matching.NToMMatchingWithPriority;

public class SceneActionMatching extends NToMMatchingWithPriority<Scene, Class<? extends Action>> {
    private static SceneActionMatching ourInstance = new SceneActionMatching();

    public static SceneActionMatching getInstance() {
        return ourInstance;
    }

    private SceneActionMatching() {
    }
}
