package top.jach.tes.plugin.tes.code.git.gitlab;

import lombok.Data;

import java.util.List;

@Data
public class Issue {
    String title;
    String description;
    String state;
    String createdTime;
    String updatedTime;
    String severity;
    List<String> dtss;
}
