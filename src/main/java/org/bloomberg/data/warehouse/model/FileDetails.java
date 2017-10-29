package org.bloomberg.data.warehouse.model;


import org.mongodb.morphia.annotations.*;

import java.util.List;
import java.util.Map;

@Entity("FILES_DETAILS")
public class FileDetails {

    @Id
    private String fileName;

    @Embedded("ORDERS")
    private List<OrderDetails> orders;
    
    @Embedded("INVALID_ORDERS")
    private List<OrderDetails> invalidOrders;
    

	public List<OrderDetails> getInvalidOrders() {
		return invalidOrders;
	}

	public void setInvalidOrders(List<OrderDetails> invalidOrders) {
		this.invalidOrders = invalidOrders;
	}

	public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<OrderDetails> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDetails> orders) {
        this.orders = orders;
    }

}
