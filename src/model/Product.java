package model;

import model.interfaces.ProductInterface;
import model.interfaces.UMInterface;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product implements ProductInterface, Serializable {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private int id;

    @Column
    private String productName;
    @Column
    private BigDecimal price;
    @Column
    private String um;

    public Product(){}

    public Product(String productName, BigDecimal price, String um) {
        this.productName = productName;
        this.price = price;
        this.um = um;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    @Override
    public String toString() {
        return productName;
    }

}
