package classes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Article2 {
    private final SimpleStringProperty num;
    private final SimpleStringProperty lib;
    private final SimpleIntegerProperty prix;
    private final SimpleIntegerProperty stockmin;
    private final SimpleIntegerProperty stock;
    private final SimpleStringProperty etat;


    public Article2(String num, String lib, Integer prix, Integer stockmin, Integer stock){
        super();
        this.num = new SimpleStringProperty(num);
        this.lib = new SimpleStringProperty(lib);
        this.prix = new SimpleIntegerProperty(prix);
        this.stockmin = new SimpleIntegerProperty(stockmin);
        this.stock = new SimpleIntegerProperty(stock);
        if(stock >= stockmin)this.etat = new SimpleStringProperty("");
        else this.etat = new SimpleStringProperty("Besoin d'aprovisionnement");
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

    public Integer getStock() {
        return stock.get();
    }

    public String getEtat() {
        return etat.get();
    }

}
