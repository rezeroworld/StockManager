package article;

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

public class ModifierArticle implements Initializable {
    @FXML
    JFXTextField lib;
    @FXML
    JFXTextField prix;
    @FXML
    JFXTextField stockmin;


    @FXML
    public void modifier(javafx.event.ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmation");
        alert.setContentText("Etes-vous sur de vouloir apporter ces modifications ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                PreparedStatement create = Menu.con.prepareStatement("update article set libelle = ?, prix_unit = ?, stock_min = ? where n_article = ?");
                create.setString(1, lib.getText());
                create.setString(2, prix.getText());
                create.setString(3, stockmin.getText());
                create.setString(4, GererArticle.selectedItem.getNum());
                create.executeUpdate();

                GererArticle.selectedItem = null;
                ((Node) event.getSource()).getScene().getWindow().hide();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        lib.setText(GererArticle.selectedItem.getLib());
        prix.setText(String.valueOf(GererArticle.selectedItem.getPrix()));
        stockmin.setText(String.valueOf((GererArticle.selectedItem.getStockmin())));
    }

}
