package top.jach.tes.dev.app;

import com.mongodb.MongoClient;
import org.slf4j.ILoggerFactory;
import top.jach.tes.core.domain.context.BaseContextFactory;
import top.jach.tes.core.domain.context.log.simple.SimpleLoggerFactory;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.factory.ContextFactory;
import top.jach.tes.core.factory.info.DefaultInfoRepositoryFactory;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;
import top.jach.tes.core.matching.DefaultNToOneMatchingStrategy;
import top.jach.tes.core.matching.NToOneMatchingStrategy;
import top.jach.tes.core.repository.InfoRepository;
import top.jach.tes.core.service.infoservice.DefaultInfoService;
import top.jach.tes.core.service.infoservice.InfoService;
import top.jach.tes.dev.app.jach.repository.GeneraInfoMongoRepository;

import java.util.Set;

public class Environment {

    private static GeneraInfoMongoRepository generaInfoMongoRepository;
    private static InfoRepositoryFactory infoRepositoryFactory;
    private static ILoggerFactory iLoggerFactory;
    private static ContextFactory contextFactory;
    private static InfoService infoService;

    public static synchronized InfoService infoService(){
        if(infoService != null){
            return infoService;
        }
        infoService = new DefaultInfoService(contextFactory());
        return infoService;
    }

    public static synchronized ContextFactory contextFactory(){
        if(contextFactory != null){
            return contextFactory;
        }
        contextFactory = new BaseContextFactory(loggerFactory(), infoRepositoryFactory());
        return contextFactory;
    }

    public static synchronized GeneraInfoMongoRepository generaInfoMongoRepository(){
        if(generaInfoMongoRepository != null){
            return generaInfoMongoRepository;
        }
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        generaInfoMongoRepository = new GeneraInfoMongoRepository(mongoClient.getDatabase("tes").getCollection("general_info"));
        return generaInfoMongoRepository;
    }


    public static synchronized InfoRepositoryFactory infoRepositoryFactory(){
        if(infoRepositoryFactory != null){
            return infoRepositoryFactory;
        }
        DefaultInfoRepositoryFactory factory = new DefaultInfoRepositoryFactory();
        DefaultNToOneMatchingStrategy<Class<? extends Info>, InfoRepository> strategy = new DefaultNToOneMatchingStrategy<>();
        factory.register(new NToOneMatchingStrategy<Class<? extends Info>, InfoRepository>() {
            @Override
            public InfoRepository NToM(Class<? extends Info> aClass) {
                return generaInfoMongoRepository();
            }

            @Override
            public Set<Class<? extends Info>> MToN(InfoRepository infoRepository) {
                return null;
            }
        });

//        strategy.link();
        infoRepositoryFactory = factory;
        return infoRepositoryFactory;
    }


    public static synchronized ILoggerFactory loggerFactory(){
        if(iLoggerFactory != null){
            return iLoggerFactory;
        }
        iLoggerFactory = new SimpleLoggerFactory();
        return iLoggerFactory;
    }
}
