package article;

import classes.Article2;
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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class GererArticle implements Initializable {

    public ObservableList<Article2> list = FXCollections.observableArrayList();

    public ArrayList<String> numeros = new ArrayList<>();

    private ResultSet rs;

    public static Article2 selectedItem;

    @FXML
    private TableView<Article2> table;
    @FXML
    private TableColumn<Article2, String> num;
    @FXML
    private TableColumn<Article2, String> lib;
    @FXML
    private TableColumn<Article2, Integer> prix;
    @FXML
    private TableColumn<Article2, Integer> stockmin;
    @FXML
    private TableColumn<Article2, Integer> stock;
    @FXML
    private TableColumn<Article2, String> etat;

    @FXML
    public void remplir_list() throws SQLException {

        numeros.removeAll(numeros);
        table.getItems().removeAll(table.getItems());

        try {
            PreparedStatement create = Menu.con.prepareStatement("select distinct n_article from article");
            rs = create.executeQuery();
            while (rs.next()) {
                numeros.add(rs.getString("n_article"));
            }

            for(int i = 0; i < numeros.size(); i++) {
                PreparedStatement create2 = Menu.con.prepareStatement("select article.n_article, libelle, prix_unit, stock_min, stock from article, stock where article.n_article = ? and article.n_article = stock.n_article order by date desc");
                create2.setString(1, numeros.get(i));
                rs = create2.executeQuery();
                while (rs.next()){
                    Article2 a = new Article2(
                            rs.getString("n_article"),
                            rs.getString("libelle"),
                            rs.getInt("prix_unit"),
                            rs.getInt("stock_min"),
                            rs.getInt("stock"));
                    list.add(a);
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("error: " + ex);
        }

        table.setItems(list);
    }

    @FXML
    public void ajouter_article(javafx.event.ActionEvent event) throws IOException, SQLException {
        Stage stage2 = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ajouterArticle.fxml"));
        stage2.setTitle("Ajouter un article");
        stage2.setScene(new Scene(root));
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.showAndWait();

        remplir_list();
    }

    @FXML
    public void supprimer_article() throws IOException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmation");
        alert.setContentText("Etes-vous sur de vouloir supprimer cet element ?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            Article2 selectedItem = table.getSelectionModel().getSelectedItem();
            list.remove(selectedItem);
            PreparedStatement create = Menu.con.prepareStatement("delete from article where n_article = ?");
            create.setString(1, selectedItem.getNum());
            create.executeUpdate();
        }
    }

    @FXML
    public void editer_article() throws IOException,SQLException{
        selectedItem = table.getSelectionModel().getSelectedItem();

        Stage stage2 = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("modifierArticle.fxml"));
        stage2.setTitle("Modifier un Article");
        stage2.setScene(new Scene(root));
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.showAndWait();

        remplir_list();
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
        list.remove(0,list.size());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            remplir_list();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        num.setCellValueFactory(new PropertyValueFactory<Article2, String>("num"));
        lib.setCellValueFactory(new PropertyValueFactory<Article2, String>("lib"));
        prix.setCellValueFactory(new PropertyValueFactory<Article2, Integer>("prix"));
        stockmin.setCellValueFactory(new PropertyValueFactory<Article2, Integer>("stockmin"));
        stock.setCellValueFactory(new PropertyValueFactory<Article2, Integer>("stock"));
        etat.setCellValueFactory(new PropertyValueFactory<Article2, String>("etat"));
    }
}
