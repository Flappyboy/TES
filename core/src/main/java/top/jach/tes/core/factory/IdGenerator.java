package top.jach.tes.core.factory;

import org.n3r.idworker.Id;

public class IdGenerator {
    public static long nextId(){
        return Id.next();
    }
}
