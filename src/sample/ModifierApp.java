package sample;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifierApp implements Initializable {

    private ResultSet rs;

    @FXML
    JFXTextField quant;
    @FXML
    Label article;
    @FXML
    Label numBon;

    /*public int chercherStock(String n_article) throws SQLException {
        ResultSet rs;
        int temp = 0;
        PreparedStatement create = Main.con.prepareStatement("select stock from stock where n_article = ? order by date desc");
        create.setString(1, n_article);
        rs = create.executeQuery();
        while (rs.next()){
            temp = rs.getInt("stock");
            break;
        }
        return temp;
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
    }*/


    @FXML
    public void modifier(javafx.event.ActionEvent event) throws IOException {
        int newStock = Integer.valueOf(quant.getText()) - Sample6.selectedItem.getQte_a();

        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Confirmation");
            alert.setContentText("Etes-vous sur de vouloir apporter ces modificatons ?");

            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK) {
                PreparedStatement create = Main.con.prepareStatement("update detail_app set qte_a = ? where (n_bon_a = ? and n_article = ?)");
                create.setInt(1, Integer.valueOf(quant.getText()));
                create.setString(2, numBon.getText());
                create.setString(3, article.getText());
                create.executeUpdate();

                create = Main.con.prepareStatement("update stock set stock = stock + ? where (n_article = ? and date >= ?)");
                create.setInt(1, newStock);
                create.setString(2, article.getText());
                create.setString(3, Sample6.selectedItem.getDate_app());
                create.executeUpdate();

                create = Main.con.prepareStatement("update stock set qte_a = ? where (n_article = ? and date = ?)");
                create.setInt(1, Integer.valueOf(quant.getText()));
                create.setString(2, article.getText());
                create.setString(3, Sample6.selectedItem.getDate_app());
                create.executeUpdate();

                Sample6.selectedItem = null;
                ((Node) event.getSource()).getScene().getWindow().hide();
            }

        }catch(Exception ex){
            System.out.println(ex);
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        numBon.setText(Sample6.selectedItem.getNumBon());
        article.setText(Sample6.selectedItem.getNum());
        quant.setText(String.valueOf(Sample6.selectedItem.getQte_a()));
    }
}
