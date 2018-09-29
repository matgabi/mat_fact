package model.interfaces;


import java.math.BigDecimal;

public interface TvaInterface {

    public int getId();
    public void setId(int id);
    public BigDecimal getValue();
    public void setValue(BigDecimal value);
    public String getDescription();
    public void setDescription(String description);

}
