package model;

import model.interfaces.SellerInterface;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sellers")
public class Seller implements SellerInterface, Serializable{

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private int id;
    @Column
    private String name;
    @Column
    private String cui;
    @Column
    private String nr;
    @Column
    private String headquarters;
    @Column
    private String capital;
    @Column
    private String county;
    @Column
    private String account;
    @Column
    private String bank;

    public Seller(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "Seller{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cui='" + cui + '\'' +
                ", nr='" + nr + '\'' +
                ", headquarters='" + headquarters + '\'' +
                ", capital='" + capital + '\'' +
                ", county='" + county + '\'' +
                ", account='" + account + '\'' +
                ", bank='" + bank + '\'' +
                '}';
    }
}
