package model.interfaces;

import java.math.BigDecimal;

public interface ProductInterface {
    public int getId();
    public void setId(int id);
    public String getProductName();
    public void setProductName(String productName);
    public BigDecimal getPrice();
    public void setPrice(BigDecimal price);
    public String getUm();
    public void setUm(String um);
}
