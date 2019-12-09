package top.jach.tes.core.impl.domain.scene;

import org.junit.Test;
import top.jach.tes.core.impl.domain.action.DemoAction;

public class SceneActionMatchingTest {

    @Test
    public void testMatch(){
        SceneActionMatching.getInstance().register(
                DefaultSceneActionMatchingStrategy
                        .createInstance()
                        .link(new Scene(), DemoAction.class)
                        .link(new Scene(), DemoAction.class)
        );
    }
}