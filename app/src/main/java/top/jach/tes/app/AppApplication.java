package top.jach.tes.app;

import com.mongodb.MongoClient;
import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import top.jach.tes.core.impl.domain.context.BaseContextFactory;
import top.jach.tes.core.impl.domain.context.log.simple.SimpleLoggerFactory;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.factory.ContextFactory;
import top.jach.tes.core.impl.factory.DefaultInfoRepositoryFactory;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;
import top.jach.tes.core.impl.matching.DefaultNToOneMatchingStrategy;
import top.jach.tes.core.impl.matching.NToOneMatchingStrategy;
import top.jach.tes.core.api.repository.InfoRepository;
import top.jach.tes.plugin.tes.repository.GeneraInfoMongoRepository;

import java.util.Set;

@SpringBootApplication
public class AppApplication {
    @Autowired
    GeneraInfoMongoRepository generaInfoMongoRepository;

    @Autowired
    ILoggerFactory iLoggerFactory;

    @Autowired
    InfoRepositoryFactory infoRepositoryFactory;

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Bean
    ContextFactory contextFactory(){
        return new BaseContextFactory(iLoggerFactory, infoRepositoryFactory);
    }

    @Bean
    GeneraInfoMongoRepository generaInfoMongoRepository(){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        return new GeneraInfoMongoRepository(mongoClient.getDatabase("tes").getCollection("general_info"));
    }

    @Bean
    InfoRepositoryFactory infoRepositoryFactory(){
        DefaultInfoRepositoryFactory factory = new DefaultInfoRepositoryFactory();
        DefaultNToOneMatchingStrategy<Class<? extends Info>, InfoRepository> strategy = new DefaultNToOneMatchingStrategy<>();
        factory.register(new NToOneMatchingStrategy<Class<? extends Info>, InfoRepository>() {
            @Override
            public InfoRepository NToM(Class<? extends Info> aClass) {
                return generaInfoMongoRepository;
            }

            @Override
            public Set<Class<? extends Info>> MToN(InfoRepository infoRepository) {
                return null;
            }
        });

//        strategy.link();
        return factory;
    }

    @Bean
    ILoggerFactory loggerFactory(){
        return new SimpleLoggerFactory();
    }
}
