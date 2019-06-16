package client;

import classes.Client2;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import menu.Menu;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class GererClient implements Initializable {

    public ObservableList<Client2> list = FXCollections.observableArrayList();
    public ObservableList<String> listVilles = FXCollections.observableArrayList();


    private ResultSet rs;


    @FXML
    private TableView<Client2> table;
    @FXML
    private TableColumn<Client2, String> numcli;
    @FXML
    private TableColumn<Client2, String> nomcli;
    @FXML
    private TableColumn<Client2, String> adresse;
    @FXML
    private TableColumn<Client2, String> tel;
    @FXML
    private TableColumn<Client2, String> fax;

    @FXML
    private JFXComboBox<String> villes;

    public static Client2 selectedItem;

    @FXML
    public void remplir_list(){
        list.remove(0,list.size());
        table.getItems().removeAll(table.getItems());

        if (villes.getSelectionModel().getSelectedItem() == "Toutes les villes"){
            try{
                PreparedStatement create = Menu.con.prepareStatement("select * from client");
                rs = create.executeQuery();
                while(rs.next()){
                    Client2 c = new Client2(
                            rs.getString("n_client"),
                            rs.getString("nom_c"),
                            rs.getString("adresse"),
                            rs.getString("telephone"),
                            rs.getString("fax"));
                    list.add(c);
                    table.setItems(list);
                }
            }catch(Exception ex){
                System.out.println("error: " + ex);
            }
        }
        else{
            try{
                PreparedStatement create = Menu.con.prepareStatement("select * from client where adresse = ?");
                create.setString(1,villes.getSelectionModel().getSelectedItem());
                rs = create.executeQuery();
                while(rs.next()){
                    Client2 c = new Client2(
                            rs.getString("n_client"),
                            rs.getString("nom_c"),
                            rs.getString("adresse"),
                            rs.getString("telephone"),
                            rs.getString("fax"));
                    list.add(c);
                    table.setItems(list);
                }
            }catch(Exception ex){
                System.out.println("error: " + ex);
            }
        }
    }

    public void remplir_villes(){
        listVilles.removeAll(listVilles);

        listVilles.add("Toutes les villes");
        try{
            String query = "select distinct adresse from client";
            rs = Menu.st.executeQuery(query);
            while(rs.next()){
                listVilles.add(rs.getString("adresse"));
            }
        }catch(Exception ex){
            System.out.println("error: " + ex);
        }
        villes.setItems(listVilles);
        villes.getSelectionModel().selectFirst();
    }

    @FXML
    public void retourner(javafx.event.ActionEvent event) throws IOException, SQLException {
        Menu.con.close();

        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../menu/menu.fxml"));
        stage.setTitle("sample");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void supprimer_client() throws IOException, SQLException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmation");
        alert.setContentText("Etes-vous sur de vouloir supprimer cet element ?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK){
            Client2 selectedItem = table.getSelectionModel().getSelectedItem();
            list.remove(selectedItem);
            PreparedStatement create = Menu.con.prepareStatement("delete from client where n_client = ?");
            create.setString(1,selectedItem.getNumcli());
            create.executeUpdate();
            remplir_villes();
        }
    }

    @FXML
    public void ajouter_client() throws IOException {
        Stage stage2 = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ajouterClient.fxml"));
        stage2.setTitle("Ajouter un Client");
        stage2.setScene(new Scene(root));
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.showAndWait();
        remplir_villes();
        remplir_list();
    }

    @FXML
    public void editer_client() throws IOException,SQLException{
        selectedItem = table.getSelectionModel().getSelectedItem();

        Stage stage2 = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("modifierClient.fxml"));
        stage2.setTitle("Modifier un Client");
        stage2.setScene(new Scene(root));
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.showAndWait();
        remplir_villes();
        remplir_list();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        remplir_villes();


        remplir_list();


        numcli.setCellValueFactory(new PropertyValueFactory<Client2, String>("numcli"));
        nomcli.setCellValueFactory(new PropertyValueFactory<Client2, String>("nomcli"));
        adresse.setCellValueFactory(new PropertyValueFactory<Client2, String>("adresse"));
        tel.setCellValueFactory(new PropertyValueFactory<Client2, String>("tel"));
        fax.setCellValueFactory(new PropertyValueFactory<Client2, String>("fax"));

    }
}
