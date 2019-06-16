package approvision;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import menu.Menu;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AjouterBonApp implements Initializable {

    private ResultSet rs;

    public ObservableList<String> listfour = FXCollections.observableArrayList();

    @FXML
    JFXComboBox<String> four;
    @FXML
    JFXTextField numBon;
    @FXML
    JFXDatePicker date;

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
    public void ajouter(javafx.event.ActionEvent event) throws IOException {

        try{
            PreparedStatement create = Menu.con.prepareStatement("insert into approvisiont values(?,?,?)");
            create.setString(1,numBon.getText());
            create.setString(2,date.getValue().toString());
            create.setString(3,four.getSelectionModel().getSelectedItem());

            create.executeUpdate();

        }catch(Exception ex){
            System.out.println(ex);
        }

        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        chargerFour();

    }
}
