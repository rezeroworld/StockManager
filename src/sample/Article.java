package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Article {
    private final SimpleStringProperty num;
    private final SimpleStringProperty lib;
    private final SimpleIntegerProperty prix;
    private final SimpleIntegerProperty stockmin;

    public Article(String num, String lib, Integer prix, Integer stockmin){
        super();
        this.num = new SimpleStringProperty(num);
        this.lib = new SimpleStringProperty(lib);
        this.prix = new SimpleIntegerProperty(prix);
        this.stockmin = new SimpleIntegerProperty(stockmin);
    }

    public String getNum() {
        return num.get();
    }

    public String getLib() {
        return lib.get();
    }

    public Integer getPrix() {
        return prix.get();
    }

    public Integer getStockmin() {
        return stockmin.get();
    }
}
