package org.bloomberg.data.warehouse.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import org.bloomberg.data.warehouse.model.FileDetails;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.logging.Logger;

/**
 * Created by Bhupendra.Singh on 10/28/2017.
 */
public class MongoDBConfig {
    private static final Logger LOG = Logger.getLogger(MongoDBConfig.class.getName());
    private static final MongoDBConfig INSTANCE = new MongoDBConfig();

    private final Datastore datastore;
    public static final String DB_NAME = "bloomberg";

    private MongoDBConfig() {
        MongoClientOptions mongoOptions = MongoClientOptions.builder().socketTimeout(60000)
                .connectTimeout(1200000).build(); // SocketTimeout: 60s, ConnectionTimeout: 20min
        MongoClient  mongoClient = new MongoClient(new ServerAddress("127.0.0.1", 27017), mongoOptions);
        mongoClient.setWriteConcern(WriteConcern.SAFE);
        datastore = new Morphia().mapPackage(FileDetails.class.getPackage().getName())
                .createDatastore(mongoClient, DB_NAME);
        datastore.ensureIndexes();
        LOG.info("Connection to database '" + DB_NAME + "' initialized");
    }

    public static MongoDBConfig instance() {
        return INSTANCE;
    }

    // Creating the mongo connection is expensive - (re)use a singleton for performance reasons
    // Both the underlying Java driver and Datastore are thread safe
    public Datastore getDatabase() {
        return datastore;
    }
}
