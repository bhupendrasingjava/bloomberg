package org.bloomberg.data.warehouse.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Map;

/**
 * Created by Bhupendra.Singh on 10/28/2017.
 */

@Entity("ORDER_SUMMARY")
public class OrderSummery {

    @Id
    protected long id=1l;

    private Map<String , Integer> currencyMap ;

    public Map<String, Integer> getCurrencyMap() {
        return currencyMap;
    }

    public void setCurrencyMap(Map<String, Integer> currencyMap) {
        this.currencyMap = currencyMap;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
