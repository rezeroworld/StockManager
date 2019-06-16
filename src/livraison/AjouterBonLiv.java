package livraison;

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
import java.util.ResourceBundle;

public class AjouterBonLiv implements Initializable {

    private ResultSet rs;

    public ObservableList<String> listcli = FXCollections.observableArrayList();

    @FXML
    JFXComboBox<String> cli;
    @FXML
    JFXTextField numBon;
    @FXML
    JFXDatePicker date;

    public void chargerCli(){
        try{
            String query = "select distinct n_client from client";
            rs = Menu.st.executeQuery(query);
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
    public void ajouter(javafx.event.ActionEvent event) throws IOException {

        try{
            PreparedStatement create = Menu.con.prepareStatement("insert into livraison values(?,?,?)");
            create.setString(1,numBon.getText());
            create.setString(2,date.getValue().toString());
            create.setString(3,cli.getSelectionModel().getSelectedItem());

            create.executeUpdate();

        }catch(Exception ex){
            System.out.println(ex);
        }

        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        chargerCli();

    }
}
