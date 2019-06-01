package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Stock {
    private final SimpleStringProperty num;
    private final SimpleStringProperty date;
    private final SimpleIntegerProperty qte_a;
    private final SimpleIntegerProperty qte_l;
    private final SimpleIntegerProperty stock;

    public Stock(String num, String date, Integer qte_a, Integer qte_l, Integer stock) {
        this.num = new SimpleStringProperty(num);
        this.date = new SimpleStringProperty(date);
        this.qte_a = new SimpleIntegerProperty(qte_a);
        this.qte_l = new SimpleIntegerProperty(qte_l);
        this.stock = new SimpleIntegerProperty(stock);
    }

    public String getNum() {
        return num.get();
    }

    public String getDate() {
        return date.get();
    }

    public Integer getQte_a() {
        return qte_a.get();
    }

    public Integer getQte_l() {
        return qte_l.get();
    }

    public Integer getStock() {
        return stock.get();
    }
}
