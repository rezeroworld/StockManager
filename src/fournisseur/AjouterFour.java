package fournisseur;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import menu.Menu;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    public void ajouter(javafx.event.ActionEvent event) throws SQLException {

        PreparedStatement create = Menu.con.prepareStatement("insert into fournisseur values(?,?,?,?,?)");
        create.setString(1, num.getText());
        create.setString(2, nom.getText());
        create.setString(3, adr.getText());
        create.setString(4, tel.getText());
        create.setString(5, fax.getText());
        create.executeUpdate();

        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
