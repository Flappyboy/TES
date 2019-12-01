package top.jach.tes.core.domain.action;

import org.junit.Test;
import top.jach.tes.core.context.BaseContext;
import top.jach.tes.core.domain.Project;
import top.jach.tes.core.domain.Task;

import static org.junit.Assert.*;

public class DemoActionTest {

    @Test
    public void execute() {
        DemoAction demoAction = new DemoAction();
        demoAction.execute(null, new BaseContext(new Task(new Project())));
    }
}