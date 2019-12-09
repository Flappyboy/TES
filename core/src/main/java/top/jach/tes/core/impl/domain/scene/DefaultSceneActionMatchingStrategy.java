package top.jach.tes.core.impl.domain.scene;

import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.impl.matching.DefaultNToMMatchingStrategy;

public class DefaultSceneActionMatchingStrategy extends DefaultNToMMatchingStrategy<Scene, Class<? extends Action>> {
    private DefaultSceneActionMatchingStrategy() {
    }
    public static DefaultSceneActionMatchingStrategy createInstance(){
        return new DefaultSceneActionMatchingStrategy();
    }
}
