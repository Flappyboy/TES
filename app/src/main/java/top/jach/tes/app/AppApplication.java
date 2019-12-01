package top.jach.tes.app;

import com.mongodb.MongoClient;
import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import top.jach.tes.core.context.log.simple.SimpleLoggerFactory;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.factory.info.DefaultInfoRepositoryFactory;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;
import top.jach.tes.core.matching.DefaultNToOneMatchingStrategy;
import top.jach.tes.core.matching.NToOneMatchingStrategy;
import top.jach.tes.core.repository.InfoRepository;
import top.jach.tes.plugin.jach.repository.GeneraMongoRepository;

import java.util.Set;

@SpringBootApplication
public class AppApplication {
    @Autowired
    GeneraMongoRepository generaMongoRepository;

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Bean
    GeneraMongoRepository generaMongoRepository(){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        return new GeneraMongoRepository(mongoClient.getDatabase("tes").getCollection("general_info"));
    }

    @Bean
    InfoRepositoryFactory infoRepositoryFactory(){
        DefaultInfoRepositoryFactory factory = new DefaultInfoRepositoryFactory();
        DefaultNToOneMatchingStrategy<Class<? extends Info>, InfoRepository> strategy = new DefaultNToOneMatchingStrategy<>();
        factory.register(new NToOneMatchingStrategy<Class<? extends Info>, InfoRepository>() {
            @Override
            public InfoRepository NToM(Class<? extends Info> aClass) {
                return generaMongoRepository;
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
