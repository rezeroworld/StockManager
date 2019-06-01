package sample;

import javafx.beans.property.SimpleStringProperty;

public class Client2 {
    private final SimpleStringProperty numcli;
    private final SimpleStringProperty nomcli;
    private final SimpleStringProperty adresse;
    private final SimpleStringProperty tel;
    private final SimpleStringProperty fax;

    public Client2(String numcli, String nomcli, String adresse, String tel, String fax){
        super();
        this.numcli = new SimpleStringProperty(numcli);
        this.nomcli = new SimpleStringProperty(nomcli);
        this.adresse = new SimpleStringProperty(adresse);
        this.tel = new SimpleStringProperty(tel);
        this.fax = new SimpleStringProperty(fax);
    }

    public String getNumcli() {
        return numcli.get();
    }

    public String getNomcli() {
        return nomcli.get();
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

    public void setNumcli(String numcli) {
        this.numcli.set(numcli);
    }

    public void setNomcli(String nomcli) {
        this.nomcli.set(nomcli);
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
