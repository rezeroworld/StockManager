package article;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import menu.Menu;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AjouterArticle implements Initializable {
    @FXML
    JFXTextField numArt;
    @FXML
    JFXTextField lib;
    @FXML
    JFXTextField prixUni;
    @FXML
    JFXTextField StockMin;


    @FXML
    public void ajouter(javafx.event.ActionEvent event) throws SQLException {
        PreparedStatement create = Menu.con.prepareStatement("insert into article (n_article,libelle,prix_unit,stock_min) values(?,?,?,?)");
        create.setString(1, numArt.getText());
        create.setString(2, lib.getText());
        create.setInt(3, Integer.valueOf(prixUni.getText()));
        create.setInt(4, Integer.valueOf(StockMin.getText()));
        create.executeUpdate();

        /*  Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");*/
        create = Menu.con.prepareStatement("insert into stock values(?,?,?,?,?)");
        create.setString(1, numArt.getText());
        // create.setString(2,formatter.format(date));
        create.setString(2, "1999-11-11");
        create.setInt(3, 0);
        create.setInt(4, 0);
        create.setInt(5, 0);
        create.executeUpdate();

        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
