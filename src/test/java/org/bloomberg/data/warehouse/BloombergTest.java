package org.bloomberg.data.warehouse;

import org.apache.commons.io.IOUtils;
import org.bloomberg.data.warehouse.config.ApplicationContextConfig;
import org.bloomberg.data.warehouse.config.MongoDBConfig;
import org.bloomberg.data.warehouse.service.WarehouseService;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
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
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Bhupendra.Singh on 10/28/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfig.class)
public class BloombergTest {

    private Datastore datastore;

    @Autowired
    private WarehouseService warehouseService;

    /**
     * Get our Mongo DB configration implementation and ensure it's not null.
     */

    @Before
    public void setUp() throws Exception {
        datastore = MongoDBConfig.instance().getDatabase();
        assertNotNull(datastore);
    }

    /**
     * After finishing the test, clean up the database. This is important to
     * allow multiple test runs in combination with unique key constraints.
     */
    @After
    public void tearDown() throws Exception {
       // warehouseService.clearData();
    }

    /**
     * Process CSV file
     */

    @Test
    public void persistCSVFile() throws IOException {
        File file = new File("src/test/resources/order.csv");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/csv", IOUtils.toByteArray(input));
        Long id = warehouseService.processFile(multipartFile);
        assertNotNull("An id should have been generated when saving the entity", id);
    }

    @Test
    public void validateFileForProcessing() throws IOException {
       String fileName = "orders.csv";
       Boolean isProcessed = warehouseService.getByFilename(fileName);
       assertEquals(false, isProcessed);

    }


    /**
     * To test after running orders.csv first time, please comment warehouseService.clearData() and uncomment this test.
     * @throws IOException
     */
    //@Test
    public void validateAlreadyProcessedFile() throws IOException {
        String fileName = "orders.csv";
        Boolean isProcessed = warehouseService.getByFilename(fileName);
        assertEquals(true, isProcessed);

    }
}
