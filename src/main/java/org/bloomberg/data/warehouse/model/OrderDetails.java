package org.bloomberg.data.warehouse.model;

/**
 * Created by Bhupendra.Singh on 10/28/2017.
 */
public class OrderDetails {

    private String orderId;

    private String orderingCurrency;

    private String toCurrency;

    private String dealTime;

    private String dealAmount;

    private boolean invalid;

    public OrderDetails(String[] p) {
        super();
        try {
            setOrderId(p[0]);
            setOrderingCurrency(p[1]);
            setToCurrency(p[2]);
            setDealTime(p[3]);
            setDealAmount(p[4]);
        } catch (Exception e) {
            setInvalid(true);
        }
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        if (orderId.isEmpty()) {
            this.invalid = true;
        }
        this.orderId = orderId;
    }

    public String getOrderingCurrency() {
        return orderingCurrency;
    }

    public void setOrderingCurrency(String orderingCurrency) {
        if (orderingCurrency.isEmpty()) {
            this.invalid = true;
        }
        this.orderingCurrency = orderingCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        if (toCurrency.isEmpty()) {
            this.invalid = true;
        }
        this.toCurrency = toCurrency;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        if (dealTime.isEmpty()) {
            this.invalid = true;
        }
        this.dealTime = dealTime;
    }

    public String getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(String dealAmount) {
        if (dealAmount.isEmpty()) {
            this.invalid = true;
        }
        this.dealAmount = dealAmount;
    }
}
