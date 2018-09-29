package model.interfaces;


import java.io.Serializable;
import java.math.BigDecimal;

public abstract class TableItem  implements Serializable {

    public abstract String getProductName();
    public abstract void setProductName(String name);
    public abstract BigDecimal getProductPrice();
    public abstract void setProductPrice(BigDecimal price);

    public abstract String getUmValue();
    public abstract void setUmValue(String newValue);

    public abstract double getQuantity();
    public abstract void setQuantity(double quantity);

    public abstract BigDecimal getProductValue();
    public abstract BigDecimal getProductTvaValue();
    public abstract BigDecimal getDiscount();

    public abstract BigDecimal getProductDiscountValue();
    public abstract void setProductDiscountValue(BigDecimal discountValue);

    public abstract BigDecimal getProductDiscountTvaValue();

}
