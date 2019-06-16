package fournisseur;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import menu.Menu;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifierFour implements Initializable {

    @FXML
    JFXTextField nom;
    @FXML
    JFXTextField adr;
    @FXML
    JFXTextField tel;
    @FXML
    JFXTextField fax;


    @FXML
    public void modifier(javafx.event.ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmation");
        alert.setContentText("Etes-vous sur de vouloir apporter ces modificatons ?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                PreparedStatement create = Menu.con.prepareStatement("update fournisseur set nom_f = ?, addresse = ?, telephone = ?, fax = ? where n_fournisseur = ?");
                create.setString(1, nom.getText());
                create.setString(2, adr.getText());
                create.setString(3, tel.getText());
                create.setString(4, fax.getText());
                create.setString(5, GererFournisseur.selectedItem.getNumfour());
                create.executeUpdate();

                GererFournisseur.selectedItem = null;
                ((Node) event.getSource()).getScene().getWindow().hide();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        nom.setText(GererFournisseur.selectedItem.getNomfour());
        adr.setText(GererFournisseur.selectedItem.getAdresse());
        tel.setText((GererFournisseur.selectedItem.getTel()));
        fax.setText(GererFournisseur.selectedItem.getFax());
    }
}
