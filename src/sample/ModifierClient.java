package sample;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifierClient implements Initializable {
    @FXML
    JFXTextField nom;
    @FXML
    JFXTextField adr;
    @FXML
    JFXTextField tel;
    @FXML
    JFXTextField fax;



    @FXML
    public void modifier(javafx.event.ActionEvent event) throws IOException {
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Confirmation");
            alert.setContentText("Etes-vous sur de vouloir apporter ces modificatons ?");

            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK){
                PreparedStatement create = Main.con.prepareStatement("update client set nom_c = ?, adresse = ?, telephone = ?, fax = ? where n_client = ?");
                create.setString(1,nom.getText());
                create.setString(2,adr.getText());
                create.setString(3,tel.getText());
                create.setString(4,fax.getText());
                create.setString(5,Sample5.selectedItem.getNumcli());
                create.executeUpdate();

                Sample5.selectedItem = null;
                ((Node)event.getSource()).getScene().getWindow().hide();
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        nom.setText(Sample5.selectedItem.getNomcli());
        adr.setText(Sample5.selectedItem.getAdresse());
        tel.setText((Sample5.selectedItem.getTel()));
        fax.setText(Sample5.selectedItem.getFax());
    }
}
