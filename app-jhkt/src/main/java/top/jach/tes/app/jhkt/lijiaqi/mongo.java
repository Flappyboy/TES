package top.jach.tes.app.jhkt.lijiaqi;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class mongo {
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient();
        MongoCollection collection = mongoClient.getDatabase("tes_dev").getCollection("general_info");
        collection.updateOne(Filters.eq("id", 400783137831249920l), new Document("name", "MicroserviceForRepos"));
    }
}
