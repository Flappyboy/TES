package top.jach.tes.core.domain;

import lombok.Getter;
import top.jach.tes.core.domain.action.Action;
import top.jach.tes.core.domain.info.InputInfo;
import top.jach.tes.core.factory.ResourceFactory;

@Getter
public class Task extends Entity{

    private InputInfo inputInfo;

    private Action action;

    private Status status;

    public static enum Status{
        Draft(),
        Doing(),
        Warn(),
        Complete(),
        Error(),
        Terminate();
    }

    public void execute(){
        action.execute(inputInfo, ResourceFactory.DefaultResource());
    }

    public Task setInputInfo(InputInfo inputInfo) {
        this.inputInfo = inputInfo;
        return this;
    }

    public Task setAction(Action action) {
        this.action = action;
        return this;
    }

    public Task setStatus(Status status) {
        this.status = status;
        return this;
    }
}
