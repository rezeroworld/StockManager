package sample;

import com.jfoenix.controls.JFXComboBox;
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
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifierBonLiv implements Initializable {

    private ResultSet rs;

    public ObservableList<String> listcli = FXCollections.observableArrayList();

    @FXML
    Label numBon;
    @FXML
    Label date;
    @FXML
    JFXComboBox<String> cli;

    public void chargerCli(){
        try{
            String query = "select distinct n_client from client";
            rs = Main.st.executeQuery(query);
            while(rs.next()){
                listcli.add(rs.getString("n_client"));
            }
        }catch(Exception ex){
            System.out.println("error: " + ex);
        }
        cli.setItems(listcli);
        cli.getSelectionModel().selectFirst();
    }


    @FXML
    public void modifier(javafx.event.ActionEvent event) throws IOException {

        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Confirmation");
            alert.setContentText("Etes-vous sur de vouloir apporter ces modificatons ?");

            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK) {
                PreparedStatement create = Main.con.prepareStatement("update livraison set n_client = ? where n_bon_l = ?");
                create.setString(1, cli.getSelectionModel().getSelectedItem());
                create.setString(2, numBon.getText());
                create.executeUpdate();

                GererBonLiv.selectedItem = null;
                ((Node) event.getSource()).getScene().getWindow().hide();
            }

        }catch(Exception ex){
            System.out.println(ex);
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        chargerCli();

        numBon.setText(GererBonLiv.selectedItem.getN_bon());
        date.setText(GererBonLiv.selectedItem.getDate());
        cli.getSelectionModel().select(GererBonLiv.selectedItem.getNumFC());
    }
}
