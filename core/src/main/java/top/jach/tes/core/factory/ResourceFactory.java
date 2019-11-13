package top.jach.tes.core.factory;

import top.jach.tes.core.resource.Resource;

public class ResourceFactory {
    private static Resource resource = new Resource();
    public static Resource DefaultResource(){
        return resource;
    }
}
