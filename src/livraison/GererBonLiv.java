package livraison;

import classes.Bon;
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

public class GererBonLiv implements Initializable{

    public ObservableList<Bon> list = FXCollections.observableArrayList();

    private ResultSet rs;

    @FXML
    private TableView<Bon> table;
    @FXML
    private TableColumn<Bon, String> n_bon;
    @FXML
    private TableColumn<Bon, String> date;
    @FXML
    private TableColumn<Bon, String> numFC;

    public static Bon selectedItem;

    @FXML
    public void remplir_list() throws SQLException {
        table.getItems().removeAll(table.getItems());

        try {
            PreparedStatement create = Menu.con.prepareStatement("select * from livraison");
            rs = create.executeQuery();
        } catch (Exception ex) {
            System.out.println("error: " + ex);
        }

        while (rs.next()) {
            Bon a = new Bon(
                    rs.getString("n_bon_l"),
                    rs.getString("date"),
                    rs.getString("n_client"));
            list.add(a);
        }
    }

    @FXML
    public void retourner(javafx.event.ActionEvent event) throws IOException, SQLException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("gererLiv.fxml"));
        stage.setTitle("Gerer les Livraisons");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void ajouter_Bon () throws IOException,SQLException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ajouterBonLiv.fxml"));
        stage.setTitle("Ajouter un bon de Livraison");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        remplir_list();
    }

    @FXML
    public void supprimer_Bon () throws IOException,SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmation");
        alert.setContentText("Etes-vous sur de vouloir supprimer cet element ?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            Bon selectedItem = table.getSelectionModel().getSelectedItem();
            list.remove(selectedItem);
            PreparedStatement create = Menu.con.prepareStatement("delete from livraison where n_bon_l = ?");
            create.setString(1, selectedItem.getN_bon());
            create.executeUpdate();
        }
    }

    @FXML
    public void modifier_Bon () throws IOException,SQLException {
        selectedItem = table.getSelectionModel().getSelectedItem();

        Stage stage2 = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("modifierBonLiv.fxml"));
        stage2.setTitle("Modifier un bon de Livraison");
        stage2.setScene(new Scene(root));
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.showAndWait();

        remplir_list();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            remplir_list();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        n_bon.setCellValueFactory(new PropertyValueFactory<Bon, String>("n_bon"));
        date.setCellValueFactory(new PropertyValueFactory<Bon, String>("date"));
        numFC.setCellValueFactory(new PropertyValueFactory<Bon, String>("numFC"));

        table.setItems(list);
    }
}
