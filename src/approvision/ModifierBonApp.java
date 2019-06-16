package approvision;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import menu.Menu;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifierBonApp implements Initializable {

    private ResultSet rs;

    public ObservableList<String> listfour = FXCollections.observableArrayList();

    @FXML
    Label numBon;
    @FXML
    Label date;
    @FXML
    JFXComboBox<String> four;

    public void chargerFour(){
        try{
            String query = "select distinct n_fournisseur from fournisseur";
            rs = Menu.st.executeQuery(query);
            while(rs.next()){
                listfour.add(rs.getString("n_fournisseur"));
            }
        }catch(Exception ex){
            System.out.println("error: " + ex);
        }
        four.setItems(listfour);
        four.getSelectionModel().selectFirst();
    }


    @FXML
    public void modifier(javafx.event.ActionEvent event) throws IOException {

        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Confirmation");
            alert.setContentText("Etes-vous sur de vouloir apporter ces modificatons ?");

            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK) {
                PreparedStatement create = Menu.con.prepareStatement("update approvisiont set n_fournisseur = ? where n_bon_a = ?");
                create.setString(1, four.getSelectionModel().getSelectedItem());
                create.setString(2, numBon.getText());
                create.executeUpdate();

                GererBonApp.selectedItem = null;
                ((Node)event.getSource()).getScene().getWindow().hide();
            }

        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        chargerFour();

        numBon.setText(GererBonApp.selectedItem.getN_bon());
        date.setText(String.valueOf(LocalDate.parse(GererBonApp.selectedItem.getDate())));
        four.getSelectionModel().select(GererBonApp.selectedItem.getNumFC());
    }
}
