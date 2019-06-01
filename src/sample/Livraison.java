package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Livraison {
    private final SimpleStringProperty num;
    private final SimpleStringProperty lib;
    private final SimpleStringProperty numBon;
    private final SimpleStringProperty date_liv;
    private final SimpleIntegerProperty qte_l;
    private final SimpleStringProperty cli;

    public Livraison(String num, String lib, String numBon, String date_liv, Integer qte_l, String cli) {
        this.num = new SimpleStringProperty(num);
        this.lib = new SimpleStringProperty(lib);
        this.numBon = new SimpleStringProperty(numBon);
        this.date_liv = new SimpleStringProperty(date_liv);
        this.qte_l = new SimpleIntegerProperty(qte_l);
        this.cli = new SimpleStringProperty(cli);
    }

    public String getNum() {
        return num.get();
    }

    public String getLib() { return lib.get(); }

    public String getNumBon() { return numBon.get(); }

    public String getDate_liv() {
        return date_liv.get();
    }

    public Integer getQte_l() {
        return qte_l.get();
    }

    public String getCli() {
        return cli.get();
    }
}
