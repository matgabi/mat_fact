package model;

import model.interfaces.TableItem;

import java.math.BigDecimal;

public class DiscountItem extends TableItem {

    private String productName = "Discount global";
    private String um = "buc";
    private double quantity = 1;
    private BigDecimal value;
    private BigDecimal tvaValue;

    public DiscountItem(BigDecimal value, BigDecimal tvaValue) {
        this.value = value;
        this.tvaValue = tvaValue;
    }


    @Override
    public String getProductName() {
        return productName;
    }

    @Override
    public void setProductName(String name) {
        productName = name;
    }

    @Override
    public BigDecimal getProductPrice() {
        return null;
    }

    @Override
    public void setProductPrice(BigDecimal price) {

    }

    @Override
    public String getUmValue() {
        return um;
    }

    @Override
    public void setUmValue(String newValue) {
        um = newValue;
    }

    @Override
    public double getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public BigDecimal getProductValue() {
        return value;
    }

    @Override
    public BigDecimal getProductTvaValue() {
        return tvaValue;
    }

    @Override
    public BigDecimal getDiscount() {
        return null;
    }

    @Override
    public BigDecimal getProductDiscountValue() {
        return null;
    }

    @Override
    public void setProductDiscountValue(BigDecimal discountValue) {}

    @Override
    public BigDecimal getProductDiscountTvaValue() {
        return null;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
    public void setTvaValue(BigDecimal tvaValue) {
        this.tvaValue = tvaValue;
    }

}
