package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class menuController implements Initializable {

    @FXML
    public void ajouter_article(javafx.event.ActionEvent event) throws IOException {
            ((Node)event.getSource()).getScene().getWindow().hide();
            Stage stage2 = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("gererArticle.fxml"));
            stage2.setTitle("Gerer les Articles");
            stage2.setScene(new Scene(root));
            stage2.show();
    }

   /* @FXML
    public void supprimer_client(javafx.event.ActionEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage3 = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("sample3.fxml"));
        stage3.setTitle("Supprimer un Client");
        stage3.setScene(new Scene(root2));
        stage3.show();
    }*/

    @FXML
    public void modifier_fournisseur(javafx.event.ActionEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage3 = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("sample4.fxml"));
        stage3.setTitle("Gerer les Fournisseurs");
        stage3.setScene(new Scene(root2));
        stage3.show();
    }

    @FXML
    public void afficher_clients(javafx.event.ActionEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage3 = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("sample5.fxml"));
        stage3.setTitle("Gerer les Clients");
        stage3.setScene(new Scene(root2));
        stage3.show();
    }

    @FXML
    public void afficher_articles(javafx.event.ActionEvent event) throws IOException {

        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage3 = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("sample6.fxml"));
        stage3.setTitle("Gerer les Approvisionnements");
        stage3.setScene(new Scene(root2));
        stage3.show();
    }

    @FXML
    public void afficher_stocks(javafx.event.ActionEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage3 = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("sample7.fxml"));
        stage3.setTitle("Gerer les Stocks");
        stage3.setScene(new Scene(root2));
        stage3.show();
    }

    @FXML
    public void afficher_livraisons(javafx.event.ActionEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage3 = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("gererLiv.fxml"));
        stage3.setTitle("Gerer les Livraisons");
        stage3.setScene(new Scene(root2));
        stage3.show();
    }

    @FXML
    public void retourner(javafx.event.ActionEvent event) throws SQLException {
        Main.con.close();
        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
