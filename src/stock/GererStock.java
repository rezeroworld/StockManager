package stock;

import classes.Stock;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import menu.Menu;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GererStock implements Initializable {

    public ObservableList<Stock> list = FXCollections.observableArrayList();

    private ResultSet rs;

    @FXML
    private TableView<Stock> table;
    @FXML
    private TableColumn<Stock, String> num;
    @FXML
    private TableColumn<Stock, String> date;
    @FXML
    private TableColumn<Stock, Integer> qte_a;
    @FXML
    private TableColumn<Stock, Integer> qte_l;
    @FXML
    private TableColumn<Stock, Integer> stock;


    @FXML
    private JFXDatePicker dateD;
    @FXML
    private JFXDatePicker dateF;

    @FXML
    public void remplir_list() throws SQLException {
        table.getItems().removeAll(table.getItems());
        if (dateD.getValue() == null & dateF.getValue() == null) {
            try {
                PreparedStatement create = Menu.con.prepareStatement("select * from stock");
                rs = create.executeQuery();
            } catch (Exception ex) {
                System.out.println("error: " + ex);
            }
        } else if(dateD.getValue() != null & dateF.getValue() == null) {
            try {
                PreparedStatement create = Menu.con.prepareStatement("select * from stock where date >= ?");
                create.setString(1,dateD.getValue().toString());
                rs = create.executeQuery();
            } catch (Exception ex) {
                System.out.println("error: " + ex);
            }
        }
        else if(dateD.getValue() == null & dateF.getValue() != null){
            try {
                PreparedStatement create = Menu.con.prepareStatement("select * from stock where date <= ?");
                create.setString(1,dateF.getValue().toString());
                rs = create.executeQuery();
            } catch (Exception ex) {
                System.out.println("error: " + ex);
            }
        }
        else{
            try {
                PreparedStatement create = Menu.con.prepareStatement("select * from stock where date between ? and ?");
                create.setString(1,dateD.getValue().toString());
                create.setString(2,dateF.getValue().toString());
                rs = create.executeQuery();
            } catch (Exception ex) {
                System.out.println("error: " + ex);
            }
        }
        while (rs.next()) {
            Stock a = new Stock(
                    rs.getString("n_article"),
                    rs.getString("date"),
                    rs.getInt("qte_a"),
                    rs.getInt("qte_l"),
                    rs.getInt("stock"));
            list.add(a);
        }
    }

    @FXML
    public void retourner(javafx.event.ActionEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../menu/menu.fxml"));
        stage.setTitle("sample");
        stage.setScene(new Scene(root));
        stage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            remplir_list();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        num.setCellValueFactory(new PropertyValueFactory<Stock, String>("num"));
        date.setCellValueFactory(new PropertyValueFactory<Stock, String>("date"));
        qte_a.setCellValueFactory(new PropertyValueFactory<Stock, Integer>("qte_a"));
        qte_l.setCellValueFactory(new PropertyValueFactory<Stock, Integer>("qte_l"));
        stock.setCellValueFactory(new PropertyValueFactory<Stock, Integer>("stock"));

        table.setItems(list);
    }
}
