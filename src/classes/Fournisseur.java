package classes;

import javafx.beans.property.SimpleStringProperty;

public class Fournisseur {
    private final SimpleStringProperty numfour;
    private final SimpleStringProperty nomfour;
    private final SimpleStringProperty adresse;
    private final SimpleStringProperty tel;
    private final SimpleStringProperty fax;

    public Fournisseur(String numfour, String nomfour, String adresse, String tel, String fax){
        super();
        this.numfour = new SimpleStringProperty(numfour);
        this.nomfour = new SimpleStringProperty(nomfour);
        this.adresse = new SimpleStringProperty(adresse);
        this.tel = new SimpleStringProperty(tel);
        this.fax = new SimpleStringProperty(fax);
    }

    public String getNumfour() {
        return numfour.get();
    }

    public String getNomfour() {
        return nomfour.get();
    }

    public String getAdresse() {
        return adresse.get();
    }

    public String getTel() {
        return tel.get();
    }

    public String getFax() {
        return fax.get();
    }

    public void setNumfour(String numfour) {
        this.numfour.set(numfour);
    }

    public void setNomfour(String nomfour) {
        this.nomfour.set(nomfour);
    }

    public void setAdresse(String adresse) {
        this.adresse.set(adresse);
    }

    public void setTel(String tel) {
        this.tel.set(tel);
    }

    public void setFax(String fax) {
        this.fax.set(fax);
    }
}
