package sample;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class AjouterFour implements Initializable {
    @FXML
    JFXTextField num;
    @FXML
    JFXTextField nom;
    @FXML
    JFXTextField adr;
    @FXML
    JFXTextField tel;
    @FXML
    JFXTextField fax;



    @FXML
    public void ajouter(javafx.event.ActionEvent event) throws IOException {

        try{
            PreparedStatement create = Main.con.prepareStatement("insert into fournisseur values(?,?,?,?,?)");
            create.setString(1,num.getText());
            create.setString(2,nom.getText());
            create.setString(3,adr.getText());
            create.setString(4,tel.getText());
            create.setString(5,fax.getText());

            create.executeUpdate();

        }catch(Exception ex){
            System.out.println(ex);
        }

        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
