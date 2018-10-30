package org.personalized.dashboard.bootstrap;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.personalized.dashboard.utils.ConfigKeys;

import java.util.Arrays;

/**
 * Created by sudan on 11/5/15.
 */
public class MongoBootstrap {

    private static final MongoDatabase mongoDatabase = MongoBootstrap.init();

    private MongoBootstrap() {

    }

    public static MongoDatabase init() {
        if (mongoDatabase == null) {
            String username = ConfigKeys.MONGO_USERNAME;
            String password = ConfigKeys.MONGO_PASSWORD;
            String dbName = ConfigKeys.MONGO_DBNAME;
            String hostName = ConfigKeys.MONGO_HOSTNAME;
            String portNumber = ConfigKeys.MONGO_PORTNUMBER;
            MongoCredential mongoCredential = MongoCredential.createCredential(username, dbName,
                    password.toCharArray());
            MongoClient mongoClient = new MongoClient(new ServerAddress(hostName, Integer
                    .parseInt(portNumber)), Arrays.asList(mongoCredential));
            return mongoClient.getDatabase(dbName);
        }
        return mongoDatabase;
    }

    public static MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

}
