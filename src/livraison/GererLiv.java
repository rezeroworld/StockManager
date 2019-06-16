package livraison;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import classes.Livraison;
import menu.Menu;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class GererLiv implements Initializable {

    public ObservableList<Livraison> list = FXCollections.observableArrayList();

    private ResultSet rs;

    @FXML
    private TableView<Livraison> table;
    @FXML
    private TableColumn<Livraison, String> num;
    @FXML
    private TableColumn<Livraison, String> lib;
    @FXML
    private TableColumn<Livraison, String> numBon;
    @FXML
    private TableColumn<Livraison, String> date;
    @FXML
    private TableColumn<Livraison, Integer> quant;
    @FXML
    private TableColumn<Livraison, String> cli;

    @FXML
    private javafx.scene.control.DatePicker dateD;
    @FXML
    private javafx.scene.control.DatePicker dateF;

    public static Livraison selectedItem;

    @FXML
    public void remplir_list() throws SQLException {
        table.getItems().removeAll(table.getItems());
        if (dateD.getValue() == null & dateF.getValue() == null) {
            try {
                PreparedStatement create = Menu.con.prepareStatement("select article.n_article,libelle,livraison.date,detail_liv.qte_l,livraison.n_client, livraison.n_bon_l " +
                        "from article,detail_liv,livraison " +
                        "where livraison.n_bon_l = detail_liv.n_bon_l and detail_liv.n_article = article.n_article");
                rs = create.executeQuery();
            } catch (Exception ex) {
                System.out.println("error: " + ex);
            }
        } else if(dateD.getValue() != null & dateF.getValue() == null) {
            try {
                PreparedStatement create = Menu.con.prepareStatement("select article.n_article,libelle,livraison.date,detail_liv.qte_l,livraison.n_client, livraison.n_bon_l " +
                        "from article,detail_liv,livraison " +
                        "where livraison.date >= ? and livraison.n_bon_l = detail_liv.n_bon_l and detail_liv.n_article = article.n_article");
                create.setString(1,dateD.getValue().toString());
                rs = create.executeQuery();
            } catch (Exception ex) {
                System.out.println("error: " + ex);
            }
        }
        else if(dateD.getValue() == null & dateF.getValue() != null){
            try {
                PreparedStatement create = Menu.con.prepareStatement("select article.n_article,libelle,livraison.date,detail_liv.qte_l,livraison.n_client, livraison.n_bon_l " +
                        "from article,detail_liv,livraison " +
                        "where livraison.date <= ? and livraison.n_bon_l = detail_liv.n_bon_l and detail_liv.n_article = article.n_article");
                create.setString(1,dateF.getValue().toString());
                rs = create.executeQuery();
            } catch (Exception ex) {
                System.out.println("error: " + ex);
            }
        }
        else{
            try {
                PreparedStatement create = Menu.con.prepareStatement("select article.n_article,libelle,livraison.date,detail_liv.qte_l,livraison.n_client, livraison.n_bon_l " +
                        "from article,detail_liv,livraison " +
                        "where (livraison.date between ? and ? ) and livraison.n_bon_l = detail_liv.n_bon_l and detail_liv.n_article = article.n_article");
                create.setString(1,dateD.getValue().toString());
                create.setString(2,dateF.getValue().toString());
                rs = create.executeQuery();
            } catch (Exception ex) {
                System.out.println("error: " + ex);
            }
        }
        while (rs.next()) {
            Livraison a = new Livraison(
                    rs.getString("n_article"),
                    rs.getString("libelle"),
                    rs.getString("n_bon_l"),
                    rs.getString("date"),
                    rs.getInt("qte_l"),
                    rs.getString("n_client"));
            list.add(a);
        }
    }

    @FXML
    public void retourner(javafx.event.ActionEvent event) throws IOException, SQLException {
        Menu.con.close();

        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../menu/menu.fxml"));
        stage.setTitle("Menu Principal");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void ajouter_liv () throws IOException,SQLException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ajouterLiv.fxml"));
        stage.setTitle("Ajouter une Livraison");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        remplir_list();
    }

    @FXML
    public void supprimer_liv () throws IOException,SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmation");
        alert.setContentText("Etes-vous sur de vouloir supprimer cet element ?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            Livraison selectedItem = table.getSelectionModel().getSelectedItem();
            list.remove(selectedItem);
            PreparedStatement create = Menu.con.prepareStatement("delete from detail_liv where (n_bon_l = ? and n_article = ?)");
            create.setString(1, selectedItem.getNumBon());
            create.setString(2, selectedItem.getNum());
            create.executeUpdate();

            create = Menu.con.prepareStatement("delete from stock where (n_article = ? and date = ?)");
            create.setString(1, selectedItem.getNum());
            create.setString(2, selectedItem.getDate_liv());
            create.executeUpdate();

            create = Menu.con.prepareStatement("update stock set stock = stock + ? where (n_article = ? and date >= ?)");
            create.setInt(1, selectedItem.getQte_l());
            create.setString(2, selectedItem.getNum());
            create.setString(3, selectedItem.getDate_liv());
            create.executeUpdate();
        }
    }

    @FXML
    public void modifier_liv () throws IOException,SQLException {
        selectedItem = table.getSelectionModel().getSelectedItem();

        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("modifierLiv.fxml"));
        stage.setTitle("Modifier une Livraison");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        remplir_list();
    }

    @FXML
    public void gerer_BonLiv(javafx.event.ActionEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage3 = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("gererBonLiv.fxml"));
        stage3.setTitle("Gerer les bons de Livraison");
        stage3.setScene(new Scene(root2));
        stage3.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            remplir_list();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        num.setCellValueFactory(new PropertyValueFactory<Livraison, String>("num"));
        lib.setCellValueFactory(new PropertyValueFactory<Livraison, String>("lib"));
        numBon.setCellValueFactory(new PropertyValueFactory<Livraison, String>("numBon"));
        date.setCellValueFactory(new PropertyValueFactory<Livraison, String>("date_liv"));
        quant.setCellValueFactory(new PropertyValueFactory<Livraison, Integer>("qte_l"));
        cli.setCellValueFactory(new PropertyValueFactory<Livraison, String>("cli"));

        table.setItems(list);
    }
}
