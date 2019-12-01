package top.jach.tes.core.domain.scene;

import top.jach.tes.core.domain.action.Action;
import top.jach.tes.core.matching.DefaultNToMMatchingStrategy;

public class DefaultSceneActionMatchingStrategy extends DefaultNToMMatchingStrategy<Scene, Class<? extends Action>> {
    private DefaultSceneActionMatchingStrategy() {
    }
    public static DefaultSceneActionMatchingStrategy createInstance(){
        return new DefaultSceneActionMatchingStrategy();
    }
}
