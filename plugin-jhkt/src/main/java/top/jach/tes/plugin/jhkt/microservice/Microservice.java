package top.jach.tes.plugin.jhkt.microservice;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import top.jach.tes.core.impl.domain.element.Element;

import java.util.*;

@Getter
@Setter
public class Microservice extends Element {
    String path;
    String repoName;
    Long annotationLines;
    Long codeLines;
    //若A服务发布的topics被B服务订阅了，说明A向B传送了数据，认为A依赖B
    Set<String> pubTopics = new HashSet<>();//某微服务发布的topics
    Set<String> subTopics = new HashSet<>();//某微服务订阅的topics。
    Map<String, Integer> subTopicOneOf = new HashMap<>();

    public String getAllPath(){
        String allPath=this.repoName+"/"+this.path;
        return allPath;
    }


    public Microservice setSubTopicOneOf(Map<String, Integer> subTopicOneOf) {
        this.subTopicOneOf = subTopicOneOf;
        return this;
    }

    public Microservice setElementName(String elementName){
        this.elementName = elementName;
        return this;
    }

    public Microservice setPath(String path) {
        this.path = path;
        return this;
    }

    public Microservice setAnnotationLines(Long annotationLines) {
        this.annotationLines = annotationLines;
        return this;
    }

    public Microservice setCodeLines(Long codeLines) {
        this.codeLines = codeLines;
        return this;
    }

    public Microservice setPubTopics(Set<String> pubTopics) {
        this.pubTopics = pubTopics;
        return this;
    }

    public Microservice setSubTopics(Set<String> subTopics) {
        this.subTopics = subTopics;
        return this;
    }
}
