package fournisseur;

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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import classes.Fournisseur;
import menu.Menu;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class GererFournisseur implements Initializable {

    public ObservableList<Fournisseur> list = FXCollections.observableArrayList();
    private ResultSet rs;

    @FXML
    private TableView<Fournisseur> table;
    @FXML
    private TableColumn<Fournisseur, String> numfour;
    @FXML
    private TableColumn<Fournisseur, String> nomfour;
    @FXML
    private TableColumn<Fournisseur, String> adresse;
    @FXML
    private TableColumn<Fournisseur, String> tel;
    @FXML
    private TableColumn<Fournisseur, String> fax;

    public static Fournisseur selectedItem;

    public void remplir_list() throws SQLException {
        list.remove(0, list.size());
        table.getItems().removeAll(table.getItems());
        String query = "select * from fournisseur";
        rs = Menu.st.executeQuery(query);
        while (rs.next()) {
            Fournisseur f = new Fournisseur(
                    rs.getString("n_fournisseur"),
                    rs.getString("nom_f"),
                    rs.getString("addresse"),
                    rs.getString("telephone"),
                    rs.getString("fax"));
            list.add(f);
        }
    }

    @FXML
    public void editer_nom(TableColumn.CellEditEvent edditedCell) throws SQLException {
        Fournisseur selectedFour = table.getSelectionModel().getSelectedItem();
        selectedFour.setNomfour(edditedCell.getNewValue().toString());
        PreparedStatement create = Menu.con.prepareStatement("update fournisseur set nom_f = ? where n_fournisseur = ?");
        create.setString(1, String.valueOf(edditedCell.getNewValue()));
        create.setString(2, String.valueOf(edditedCell.getTablePosition().getRow() + 1));
        create.executeUpdate();
    }

    @FXML
    public void editer_adr(TableColumn.CellEditEvent edditedCell) throws SQLException {
        Fournisseur selectedFour = table.getSelectionModel().getSelectedItem();
        selectedFour.setAdresse(edditedCell.getNewValue().toString());
        PreparedStatement create = Menu.con.prepareStatement("update fournisseur set addresse = ? where n_fournisseur = ?");
        create.setString(1, String.valueOf(edditedCell.getNewValue()));
        create.setString(2, String.valueOf(edditedCell.getTablePosition().getRow() + 1));
        create.executeUpdate();
    }

    @FXML
    public void editer_tel(TableColumn.CellEditEvent edditedCell) throws SQLException {
        Fournisseur selectedFour = table.getSelectionModel().getSelectedItem();
        selectedFour.setTel(edditedCell.getNewValue().toString());
        PreparedStatement create = Menu.con.prepareStatement("update fournisseur set telephone = ? where n_fournisseur = ?");
        create.setString(1, String.valueOf(edditedCell.getNewValue()));
        create.setString(2, String.valueOf(edditedCell.getTablePosition().getRow() + 1));
        create.executeUpdate();
    }

    @FXML
    public void editer_fax(TableColumn.CellEditEvent edditedCell) throws SQLException {
        Fournisseur selectedFour = table.getSelectionModel().getSelectedItem();
        selectedFour.setFax(edditedCell.getNewValue().toString());
        PreparedStatement create = Menu.con.prepareStatement("update fournisseur set fax = ? where n_fournisseur = ?");
        create.setString(1, String.valueOf(edditedCell.getNewValue()));
        create.setString(2, String.valueOf(edditedCell.getTablePosition().getRow() + 1));
        create.executeUpdate();
    }

    @FXML
    public void retourner(javafx.event.ActionEvent event) throws IOException, SQLException {
        Menu.con.close();

        ((Node) event.getSource()).getScene().getWindow().hide();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../menu/menu.fxml"));
        stage.setTitle("sample");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void supprimer_four() throws SQLException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmation");
        alert.setContentText("Etes-vous sur de vouloir supprimer cet element ?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                Fournisseur selectedItem = table.getSelectionModel().getSelectedItem();
                list.remove(selectedItem);
                PreparedStatement create = Menu.con.prepareStatement("delete from fournisseur where n_fournisseur = ?");
                create.setString(1, selectedItem.getNumfour());
                create.executeUpdate();
            }
        }
    }

    @FXML
    public void editer_four() throws IOException, SQLException {
        selectedItem = table.getSelectionModel().getSelectedItem();

        Stage stage2 = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("modifierFour.fxml"));
        stage2.setTitle("Modifier un Fournisseur");
        stage2.setScene(new Scene(root));
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.showAndWait();
        remplir_list();

    }

    @FXML
    public void ajouter_four() throws IOException, SQLException {
        Stage stage2 = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ajouterFour.fxml"));
        stage2.setTitle("Ajouter un Fournisseur");
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

        numfour.setCellValueFactory(new PropertyValueFactory<>("numfour"));
        nomfour.setCellValueFactory(new PropertyValueFactory<>("nomfour"));
        adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        tel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        fax.setCellValueFactory(new PropertyValueFactory<>("fax"));
        table.setItems(list);

        table.setEditable(true);
        nomfour.setCellFactory(TextFieldTableCell.forTableColumn());
        adresse.setCellFactory(TextFieldTableCell.forTableColumn());
        tel.setCellFactory(TextFieldTableCell.forTableColumn());
        fax.setCellFactory(TextFieldTableCell.forTableColumn());
    }
}
