package connexion;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
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
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion stock?serverTimezone=UTC", "root", mdp.getText());
            Statement st = con.createStatement();

            ((Node)event.getSource()).getScene().getWindow().hide();
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../menu/menu.fxml"));
            stage.setTitle("Menu Principal");
            stage.setScene(new Scene(root));
            stage.show();

            con.close();

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
