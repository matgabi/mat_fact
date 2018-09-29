package model;

import model.interfaces.ProductInterface;
import model.interfaces.TableItem;
import model.interfaces.TvaInterface;
import model.interfaces.UMInterface;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductItem extends TableItem{

    private ProductInterface product;
    private double quantity;
    private UMInterface um; // um
    private BigDecimal discount;
    private TvaInterface tva;

    public ProductItem(){}
    public ProductItem(ProductInterface product, double quantity, UMInterface um, BigDecimal discount, TvaInterface tva) {
        this.product = product;
        this.quantity = quantity;
        this.um = um;
        this.discount = discount;
        this.tva = tva;
    }

    public ProductInterface getProduct() {
        return product;
    }

    public void setProduct(ProductInterface product) {
        this.product = product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public UMInterface getUm() {
        return um;
    }

    public void setUm(UMInterface um) {
        this.um = um;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public TvaInterface getTva() {
        return tva;
    }

    public void setTva(TvaInterface tva) {
        this.tva = tva;
    }

    //getters and setters for the product object
    //--------------------------------------------------------------------------
    public String getProductName() { return product.getProductName(); }
    public void setProductName(String name) { product.setProductName(name); }
    public BigDecimal getProductPrice() { return product.getPrice(); }
    public void setProductPrice(BigDecimal price) { product.setPrice(price); }
    public void setProductDiscountValue(BigDecimal discountValue) { discount = discountValue; }
    //--------------------------------------------------------------------------

    //getters and setters for the tva object
    //--------------------------------------------------------------------------
    public BigDecimal getTvaValue() { return tva.getValue(); }
    public void setTvaValue(BigDecimal newTva) { tva.setValue(newTva); }
    public String getTvaDescription() { return tva.getDescription(); }
    public void setTvaDescription(String newDescription) { tva.setDescription(newDescription); }
    //--------------------------------------------------------------------------

    //getters and setters for the um object
    //--------------------------------------------------------------------------
    public String getUmValue() { return um.getDescription();}
    public void setUmValue(String newValue) { um.setDescription(newValue); }
    //--------------------------------------------------------------------------

    //specialized methods to extract price considering tva and discount
    //--------------------------------------------------------------------------
    public BigDecimal getProductValue()
    {
        BigDecimal value = product.getPrice().multiply(new BigDecimal(quantity));
        return value;
    }

    public BigDecimal getProductTvaValue()
    {
        BigDecimal value = product.getPrice().multiply(new BigDecimal(quantity));
        BigDecimal tvaValue = value.multiply(tva.getValue()).divide(new BigDecimal(100));
        return tvaValue;
    }

    public BigDecimal getProductDiscountValue()
    {
        BigDecimal value = product.getPrice().multiply(new BigDecimal(quantity));
        BigDecimal discountValue = value.multiply(discount).divide(new BigDecimal(100));
        return discountValue;
    }

    public BigDecimal getProductDiscountTvaValue()
    {
        BigDecimal value = product.getPrice().multiply(new BigDecimal(quantity));
        BigDecimal discountValue = value.multiply(discount).divide(new BigDecimal(100));
        BigDecimal tvaDiscountValue = discountValue.multiply(tva.getValue()).divide(new BigDecimal(100));
        return  tvaDiscountValue;
    }
    //--------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ProductItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", um=" + um +
                ", discount=" + discount +
                ", tva=" + tva +
                '}';
    }
}
