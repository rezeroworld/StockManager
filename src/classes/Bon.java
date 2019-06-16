package classes;

import javafx.beans.property.SimpleStringProperty;

public class Bon {
    private final SimpleStringProperty n_bon;
    private final SimpleStringProperty date;
    private final SimpleStringProperty numFC;

    public Bon(String n_bon, String date, String numFC) {
        this.n_bon = new SimpleStringProperty(n_bon);
        this.date = new SimpleStringProperty(date);
        this.numFC = new SimpleStringProperty(numFC);
    }

    public Bon(String n_bon, String date) {
        this.n_bon = new SimpleStringProperty(n_bon);
        this.date = new SimpleStringProperty(date);
        this.numFC = null;
    }

    public String getN_bon() {
        return n_bon.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getNumFC() throws NullPointerException {
        return numFC.get();
    }
}
