package sample;

import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class Connexion implements Initializable {

    @FXML
    JFXPasswordField mdp;
    @FXML
    Label mdpfaux;

    @FXML
    public void Connexion(ActionEvent event) throws IOException {

        try{
           // Class.forName("com.mysql.cj.jdbc.Driver");
            Main.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion stock?serverTimezone=UTC", "root", mdp.getText());
            Main.st = Main.con.createStatement();

            ((Node)event.getSource()).getScene().getWindow().hide();
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
            stage.setTitle("Menu Principal");
            stage.setScene(new Scene(root));
            stage.show();

        }catch(Exception ex){
            mdpfaux.setVisible(true);
            mdp.setText("");
            System.out.println("error: " + ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
