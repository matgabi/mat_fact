package model;

import model.interfaces.UMInterface;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "um")
public class UM implements UMInterface, Serializable, Comparable{
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private int id;

    @Column
    private String description;

    public UM(){}

    public UM(String description){
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description ;
    }

    @Override
    public int compareTo(Object o) {
        return this.description.compareTo(((UM)o).description);
    }
}
