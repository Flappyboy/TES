package top.jach.tes.core.api.factory;

import org.n3r.idworker.Id;

public class IdGenerator {
    public static IdWorker idWorker = new IdWorker(){
        @Override
        public long nextId() {
            return Id.next();
        }
    };
    public static long nextId(){
        return idWorker.nextId();
    }

    public interface IdWorker{
        long nextId();
    }
}
