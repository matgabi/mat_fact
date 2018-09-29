package model;

import model.interfaces.InvoiceInfoInterface;

import java.io.Serializable;

public class InvoiceInfo implements InvoiceInfoInterface, Serializable {

    private String seria;
    private String nr;
    private String nrAvize;
    private String ora;
    private String scadenta;

    public String getSeria() {
        return seria;
    }

    public void setSeria(String seria) {
        this.seria = seria;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getNrAvize() {
        return nrAvize;
    }

    public void setNrAvize(String nrAvize) {
        this.nrAvize = nrAvize;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getScadenta() {
        return scadenta;
    }

    public void setScadenta(String scadenta) {
        this.scadenta = scadenta;
    }

    @Override
    public String toString() {
        return "InvoiceInfo{" +
                "seria='" + seria + '\'' +
                ", nr='" + nr + '\'' +
                ", nrAvize='" + nrAvize + '\'' +
                ", ora='" + ora + '\'' +
                ", scadenta='" + scadenta + '\'' +
                '}';
    }
}
