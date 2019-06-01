package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.management.Notification;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class GererBonApp implements Initializable {

    public ObservableList<Bon> list = FXCollections.observableArrayList();

    private ResultSet rs;

    @FXML
    private TableView<Bon> table;
    @FXML
    private TableColumn<Bon, String> n_bon;
    @FXML
    private TableColumn<Bon, String> date;
    @FXML
    private TableColumn<Bon, String> numFC;

    public static Bon selectedItem;

    @FXML
    public void remplir_list() throws SQLException {

        table.getItems().removeAll(table.getItems());

            try {
                PreparedStatement create = Main.con.prepareStatement("select * from approvisiont");
                rs = create.executeQuery();
            } catch (Exception ex) {
                System.out.println("error: " + ex);
            }

        while (rs.next()) {
            Bon a = new Bon(
                    rs.getString("n_bon_a"),
                    rs.getString("date"),
                    rs.getString("n_fournisseur"));
            list.add(a);
        }
    }

    @FXML
    public void retourner(javafx.event.ActionEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("sample6.fxml"));
        stage.setTitle("sample6");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void ajouter_Bon () throws IOException,SQLException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ajouterBonApp.fxml"));
        stage.setTitle("Ajouter un bon d'approvisionnement");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        remplir_list();
    }

    @FXML
    public void supprimer_Bon () throws IOException,SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmation");
        alert.setContentText("Etes-vous sur de vouloir supprimer cet element ?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            ResultSet rs;
            ArrayList<Approvision> articles = new ArrayList();

            Bon selectedItem = table.getSelectionModel().getSelectedItem();
            list.remove(selectedItem);

            PreparedStatement create = Main.con.prepareStatement("select n_article,qte_a from detail_app where n_bon_a = ?");
            create.setString(1, selectedItem.getN_bon());
            rs = create.executeQuery();
            while (rs.next()) {
                Approvision a = new Approvision(rs.getString("n_article"), rs.getInt("qte_a"));
                articles.add(a);
            }

            create = Main.con.prepareStatement("delete from approvisiont where n_bon_a = ?");
            create.setString(1, selectedItem.getN_bon());
            create.executeUpdate();

            create = Main.con.prepareStatement("delete from stock where (n_article = ? and date = ?)");
            for (int i = 0; i < articles.size(); i++) {
                create.setString(1, articles.get(i).getNum());
                create.setString(2, selectedItem.getDate());
                create.executeUpdate();
            }

            create = Main.con.prepareStatement("update stock set stock = stock - ? where (n_article = ? and date >= ?)");
            for (int i = 0; i < articles.size(); i++) {
                create.setInt(1, articles.get(i).getQte_a());
                create.setString(2, articles.get(i).getNum());
                create.setString(3, selectedItem.getDate());
                create.executeUpdate();
            }
        }
    }

    @FXML
    public void modifier_Bon () throws IOException,SQLException {

        selectedItem = table.getSelectionModel().getSelectedItem();

        Stage stage2 = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("modifierBonApp.fxml"));
        stage2.setTitle("Modifier un bon d'approvisionnement");
        stage2.setScene(new Scene(root));
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.showAndWait();

        remplir_list();
    }

    public int chercherStock(String n_article, String date) throws SQLException {
        ResultSet rs;
        int temp = 0;
        PreparedStatement create = Main.con.prepareStatement("select stock from stock where (n_article = ? and date = ?)");
        create.setString(1, n_article);
        create.setString(2, date);
        rs = create.executeQuery();
        while (rs.next()){
            temp = rs.getInt("stock");
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

        n_bon.setCellValueFactory(new PropertyValueFactory<Bon, String>("n_bon"));
        date.setCellValueFactory(new PropertyValueFactory<Bon, String>("date"));
        numFC.setCellValueFactory(new PropertyValueFactory<Bon, String>("numFC"));

        table.setItems(list);
    }
}
