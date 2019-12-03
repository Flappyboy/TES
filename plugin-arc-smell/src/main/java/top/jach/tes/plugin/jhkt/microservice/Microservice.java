package top.jach.tes.plugin.jhkt.microservice;

import lombok.Data;

import java.util.List;

@Data
public class Microservice {
    String repoId;
    String path;
    List<String> pubTopics;
    List<String> subTopics;
}
