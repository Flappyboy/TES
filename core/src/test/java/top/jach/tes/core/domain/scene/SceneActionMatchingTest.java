package top.jach.tes.core.domain.scene;

import org.junit.Test;
import top.jach.tes.core.domain.action.DemoAction;

import static org.junit.Assert.*;

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