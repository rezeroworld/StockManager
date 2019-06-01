package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class Sample6 implements Initializable {

    public ObservableList<Approvision> list = FXCollections.observableArrayList();

    private ResultSet rs;

    @FXML
    private TableView<Approvision> table;
    @FXML
    private TableColumn<Approvision, String> num;
    @FXML
    private TableColumn<Approvision, String> lib;
    @FXML
    private TableColumn<Approvision, String> numBon;
    @FXML
    private TableColumn<Approvision, String> date;
    @FXML
    private TableColumn<Approvision, Integer> quant;
    @FXML
    private TableColumn<Approvision, String> four;
    @FXML
    private javafx.scene.control.DatePicker dateD;
    @FXML
    private javafx.scene.control.DatePicker dateF;

    public static Approvision selectedItem;



    @FXML
    public void remplir_list() throws SQLException {
        table.getItems().removeAll(table.getItems());
        if (dateD.getValue() == null & dateF.getValue() == null) {
            try {
                PreparedStatement create = Main.con.prepareStatement("select article.n_article,libelle,approvisiont.date,detail_app.qte_a,approvisiont.n_fournisseur, approvisiont.n_bon_a " +
                        "from article,detail_app,approvisiont " +
                        "where approvisiont.n_bon_a = detail_app.n_bon_a and detail_app.n_article = article.n_article");
                rs = create.executeQuery();
            } catch (Exception ex) {
                System.out.println("error: " + ex);
            }
        } else if(dateD.getValue() != null & dateF.getValue() == null) {
            try {
                PreparedStatement create = Main.con.prepareStatement("select article.n_article,libelle,approvisiont.date,detail_app.qte_a,approvisiont.n_fournisseur, approvisiont.n_bon_a " +
                        "from article,detail_app,approvisiont " +
                        "where approvisiont.date >= ? and approvisiont.n_bon_a = detail_app.n_bon_a and detail_app.n_article = article.n_article");
                create.setString(1,dateD.getValue().toString());
                rs = create.executeQuery();
            } catch (Exception ex) {
                System.out.println("error: " + ex);
            }
        }
        else if(dateD.getValue() == null & dateF.getValue() != null){
            try {
                PreparedStatement create = Main.con.prepareStatement("select article.n_article,libelle,approvisiont.date,detail_app.qte_a,approvisiont.n_fournisseur, approvisiont.n_bon_a " +
                        "from article,detail_app,approvisiont " +
                        "where approvisiont.date <= ? and approvisiont.n_bon_a = detail_app.n_bon_a and detail_app.n_article = article.n_article");
                create.setString(1,dateF.getValue().toString());
                rs = create.executeQuery();
            } catch (Exception ex) {
                System.out.println("error: " + ex);
            }
        }
        else{
            try {
                PreparedStatement create = Main.con.prepareStatement("select article.n_article,libelle,approvisiont.date,detail_app.qte_a,approvisiont.n_fournisseur, approvisiont.n_bon_a " +
                        "from article,detail_app,approvisiont " +
                        "where (approvisiont.date between ? and ? ) and approvisiont.n_bon_a = detail_app.n_bon_a and detail_app.n_article = article.n_article");
                create.setString(1,dateD.getValue().toString());
                create.setString(2,dateF.getValue().toString());
                rs = create.executeQuery();
            } catch (Exception ex) {
                System.out.println("error: " + ex);
            }
        }
        while (rs.next()) {
            Approvision a = new Approvision(
                    rs.getString("n_article"),
                    rs.getString("libelle"),
                    rs.getString("n_bon_a"),
                    rs.getString("date"),
                    rs.getInt("qte_a"),
                    rs.getString("n_fournisseur"));
            list.add(a);
        }
    }

    @FXML
    public void retourner(javafx.event.ActionEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        stage.setTitle("Menu Principal");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void ajouter_Appro () throws IOException,SQLException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ajouterApp.fxml"));
        stage.setTitle("Ajouter un Approvisionnement");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        remplir_list();
    }

    @FXML
    public void supprimer_Appro () throws IOException,SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmation");
        alert.setContentText("Etes-vous sur de vouloir supprimer cet element ?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            Approvision selectedItem = table.getSelectionModel().getSelectedItem();
            list.remove(selectedItem);

            PreparedStatement create = Main.con.prepareStatement("delete from detail_app where (n_bon_a = ? and n_article = ?)");
            create.setString(1, selectedItem.getNumBon());
            create.setString(2, selectedItem.getNum());
            create.executeUpdate();

            create = Main.con.prepareStatement("delete from stock where (n_article = ? and date = ?)");
            create.setString(1, selectedItem.getNum());
            create.setString(2, selectedItem.getDate_app());
            create.executeUpdate();

            create = Main.con.prepareStatement("update stock set stock = stock - ? where (n_article = ? and date >= ?)");
            create.setInt(1, selectedItem.getQte_a());
            create.setString(2, selectedItem.getNum());
            create.setString(3, selectedItem.getDate_app());
            create.executeUpdate();
        }
    }

    @FXML
    public void modifier_Appro () throws IOException,SQLException {
        selectedItem = table.getSelectionModel().getSelectedItem();

        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("modifierApp.fxml"));
        stage.setTitle("Modifier un Approvisionnement");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        remplir_list();
    }

    @FXML
    public void gerer_BonApp(javafx.event.ActionEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage3 = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("gererBonApp.fxml"));
        stage3.setTitle("Gerer les bons d'approvisionnement");
        stage3.setScene(new Scene(root2));
        stage3.show();
    }

    public String chercherDate(String n_bon_a) throws SQLException {
        String temp = null;
        PreparedStatement create = Main.con.prepareStatement("select date from approvisiont where n_bon_a = ?");
        create.setString(1, n_bon_a);
        rs = create.executeQuery();
        while (rs.next()){
            temp = rs.getString("date");
            break;
        }
        return temp;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            remplir_list();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        num.setCellValueFactory(new PropertyValueFactory<Approvision, String>("num"));
        lib.setCellValueFactory(new PropertyValueFactory<Approvision, String>("lib"));
        numBon.setCellValueFactory(new PropertyValueFactory<Approvision, String>("numBon"));
        date.setCellValueFactory(new PropertyValueFactory<Approvision, String>("date_app"));
        quant.setCellValueFactory(new PropertyValueFactory<Approvision, Integer>("qte_a"));
        four.setCellValueFactory(new PropertyValueFactory<Approvision, String>("four"));

        table.setItems(list);
    }
}
