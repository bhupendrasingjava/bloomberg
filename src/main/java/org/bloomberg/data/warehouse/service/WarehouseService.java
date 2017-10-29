package org.bloomberg.data.warehouse.service;

import org.bloomberg.data.warehouse.config.MongoDBConfig;
import org.bloomberg.data.warehouse.model.FileDetails;
import org.bloomberg.data.warehouse.model.OrderDetails;
import org.bloomberg.data.warehouse.model.OrderSummery;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Bhupendra.Singh on 10/28/2017.
 */

@Service
public class WarehouseService {

    private Datastore datastore;
    static Map<String, Integer> map = new HashMap<>();

    public WarehouseService() {
        this.datastore = MongoDBConfig.instance().getDatabase();
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public void setDatastore(Datastore datastore) {
        this.datastore = datastore;
    }

    /**
     * Method to check the uniqueness of file processed
     *
     * @param fileName
     * @return true or false.
     */
    public Boolean getByFilename(String fileName) {
        Boolean isFileExist = false;
        Query<FileDetails> q = this.datastore.createQuery(FileDetails.class).field("fileName").equalIgnoreCase(fileName);
        Long count = this.datastore.getCount(q);
        if (count > 0) {
            isFileExist = true;
        }
        return isFileExist;
    }

    /**
     * This Methos will Process the uploaded File and will save in DB with proper business Validation.
     * @param file
     * @return
     */
    public long processFile(MultipartFile file) {
        FileDetails uploadFile = new FileDetails();
        uploadFile.setFileName(file.getOriginalFilename());
        BufferedReader reader = null;
        long startTime = System.currentTimeMillis();
        try {
            reader = bytesToReader(file.getBytes());
            List<OrderDetails> items = reader.lines().skip(1).map(mapToValidItem).collect(Collectors.toList());
            List<OrderDetails> invalidItems = items.stream().filter(item -> item.isInvalid()).collect(Collectors.toList());
            boolean flag = items.removeAll(invalidItems);
            uploadFile.setOrders(items);
            uploadFile.setInvalidOrders(invalidItems);
            reader.close();
            uploadFile.setOrders(items);
            uploadFile.setInvalidOrders(invalidItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.datastore.save(uploadFile);
        OrderSummery orderSummery = new OrderSummery();
        orderSummery.setCurrencyMap(map);
        this.datastore.save(orderSummery);
        long endTimeReadingCsv = System.currentTimeMillis();
        long totalTime = endTimeReadingCsv - startTime;
        System.out.println("Total time in reading CSV===>" + totalTime);
        return orderSummery.getId();
    }


    public void clearData() {
        this.datastore.delete(datastore.createQuery(FileDetails.class));
        this.datastore.delete(datastore.createQuery(OrderSummery.class));
    }


    private BufferedReader bytesToReader(byte[] content) {

        InputStream is = null;
        BufferedReader bfReader = null;
        try {
            is = new ByteArrayInputStream(content);
            bfReader = new BufferedReader(new InputStreamReader(is));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return bfReader;
    }

    private static Function<String, OrderDetails> mapToValidItem = (line) -> {
        String[] p = line.split(",");
        OrderDetails item = new OrderDetails(p);

        if (!map.containsKey(item.getToCurrency())) {
            map.put(item.getToCurrency(), 1);
        } else {
            Integer count = map.get(item.getToCurrency());
            map.put(item.getToCurrency(), ++count);
        }
        return item;
    };
}
