package org.bloomberg.data.warehouse;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.apache.commons.io.IOUtils;
import org.bloomberg.data.warehouse.config.ApplicationContextConfig;
import org.bloomberg.data.warehouse.config.MongoDBConfig;
import org.bloomberg.data.warehouse.service.WarehouseService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Bhupendra.Singh on 10/28/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfig.class)
public class BloombergTest {

    private Datastore datastore;

    @Autowired
    private WarehouseService warehouseService;

    private static final String MONGO_HOST = "127.0.0.1";
    private static final int MONGO_PORT = 27017;
    private static final String IN_MEM_CONNECTION_URL = MONGO_HOST + ":" + MONGO_PORT;

    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;

    /**
     * Start in-memory Mongo DB process
     */
    @BeforeClass
    public static void setup() throws Exception {
        System.out.println("start setup...");
        MongodStarter runtime = MongodStarter.getDefaultInstance();
        mongodExe = runtime.prepare(new MongodConfig(Version.V2_0_5, MONGO_PORT, Network.localhostIsIPv6()));
        mongod = mongodExe.start();
    }


    /**
     * Get our Mongo DB configration implementation and ensure it's not null.
     */

    @Before
    public void setUp() throws Exception {
        System.out.println("Start setUp()");
        datastore = MongoDBConfig.instance().getDatabase();
        assertNotNull(datastore);
        System.out.println("End setUp()");
    }

    @Test
    public void testAvalidateFileForProcessing() throws IOException {
        String fileName = "ordertest.csv";
        Boolean isProcessed = warehouseService.getByFilename(fileName);
        assertEquals(false, isProcessed);

    }

    @Test
    public void testBpersistCSVFile() throws IOException {
        File file = new File("src/test/resources/ordertest.csv");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/csv", IOUtils.toByteArray(input));
        Long id = warehouseService.processFile(multipartFile);
        System.out.println("ID value=" + id);
        assertNotNull("An id should have been generated when saving the entity", id);
    }

    @Test
    public void testCvalidateAlreadyProcessedFile() throws IOException {
        String fileName = "ordertest.csv";
        Boolean isProcessed = warehouseService.getByFilename(fileName);
        assertEquals(true, isProcessed);

    }


}
