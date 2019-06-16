package classes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Approvision {
    private final SimpleStringProperty num;
    private final SimpleStringProperty lib;
    private final SimpleStringProperty numBon;
    private final SimpleStringProperty date_app;
    private final SimpleIntegerProperty qte_a;
    private final SimpleStringProperty four;

    public Approvision(String num, String lib, String numBon, String date_app, Integer qte_a, String four) {
        this.num = new SimpleStringProperty(num);
        this.lib = new SimpleStringProperty(lib);
        this.numBon = new SimpleStringProperty(numBon);
        this.date_app = new SimpleStringProperty(date_app);
        this.qte_a = new SimpleIntegerProperty(qte_a);
        this.four = new SimpleStringProperty(four);
    }

    public Approvision(String num, Integer qte_a) {
        this.num = new SimpleStringProperty(num);
        this.lib = null;
        this.numBon = null;
        this.date_app = null;
        this.qte_a = new SimpleIntegerProperty(qte_a);
        this.four = null;
    }

    public String getNum() {
        return num.get();
    }

    public String getLib() throws NullPointerException {
        return lib.get();
    }

    public String getNumBon() throws NullPointerException {
        return numBon.get();
    }

    public String getDate_app() throws NullPointerException {
        return date_app.get();
    }

    public Integer getQte_a() {
        return qte_a.get();
    }

    public String getFour() throws NullPointerException {
        return four.get();
    }
}
