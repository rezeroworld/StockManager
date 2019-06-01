package sample;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AjouterLiv implements Initializable {

    private ResultSet rs;

    public ObservableList<String> listarticles = FXCollections.observableArrayList();

    public ObservableList<String> listnum = FXCollections.observableArrayList();
    public ObservableList<Bon> listnum2 = FXCollections.observableArrayList();

    @FXML
    JFXComboBox<String> article;
    @FXML
    JFXComboBox<String> numBon;
    @FXML
    JFXTextField quant;

    public void chargerArticles(){
        try{
            String query = "select distinct n_article from article";
            rs = Main.st.executeQuery(query);
            while(rs.next()){
                listarticles.add(rs.getString("n_article"));
            }
        }catch(Exception ex){
            System.out.println("error: " + ex);
        }
        article.setItems(listarticles);
        article.getSelectionModel().selectFirst();
    }

    public void chargerNumBon(){
        try{
            String query = "select distinct n_bon_l, date from livraison";
            rs = Main.st.executeQuery(query);
            while(rs.next()){
                Bon b = new Bon(rs.getString("n_bon_l"), rs.getString("date"));
                listnum2.add(b);
                listnum.add(rs.getString("n_bon_l"));
            }
        }catch(Exception ex){
            System.out.println("error: " + ex);
        }
        numBon.setItems(listnum);
        numBon.getSelectionModel().selectFirst();
    }

    public int chercherStock(String n_article, String date) throws SQLException {
        ResultSet rs;
        int temp = 0;
        PreparedStatement create = Main.con.prepareStatement("select stock from stock where n_article = ? and date <= ? order by date desc");
        create.setString(1, n_article);
        create.setString(2, date);
        rs = create.executeQuery();
        while (rs.next()){
            temp = rs.getInt("stock");
            break;
        }
        return temp;
    }

    /*public int chercherStock(String n_article) throws SQLException {
        int temp = 0;
        PreparedStatement create = Main.con.prepareStatement("select stock from stock where n_article = ? order by date desc");
        create.setString(1, n_article);
        rs = create.executeQuery();
        while (rs.next()){
            temp = rs.getInt("stock");
            break;
        }
        return temp;
    }*/

    public Boolean testStockNeg(String n_article, String date, int liv)throws SQLException{
        Boolean temp = true;
        ResultSet rs;
        ArrayList<Integer> stocks = new ArrayList();
        PreparedStatement create = Main.con.prepareStatement("select stock from stock where n_article = ? and date >= ?");
        create.setString(1, n_article);
        create.setString(2, date);
        rs = create.executeQuery();
        while (rs.next()){
            stocks.add(rs.getInt("stock"));
        }
        for(int i = 0; i < stocks.size(); i++){
            if(stocks.get(i) < liv){
                temp = false;
                break;
            }
        }
        return temp;
    }

    @FXML
    public void ajouter(javafx.event.ActionEvent event) throws IOException,SQLException {
        String datetockage = listnum2.get(listnum.indexOf(numBon.getSelectionModel().getSelectedItem())).getDate();
        int cs = chercherStock(article.getSelectionModel().getSelectedItem(), datetockage);
        boolean test = testStockNeg(article.getSelectionModel().getSelectedItem(), datetockage, Integer.parseInt(quant.getText()));

        try{
            if(test && (cs >= Integer.valueOf(quant.getText()))) {
                PreparedStatement create = Main.con.prepareStatement("insert into detail_liv values(?,?,?)");
                create.setString(1, numBon.getSelectionModel().getSelectedItem());
                create.setString(2, article.getSelectionModel().getSelectedItem());
                create.setInt(3, Integer.valueOf(quant.getText()));
                create.executeUpdate();

                create = Main.con.prepareStatement("insert into stock values(?,?,?,?,?)");
                create.setString(1, article.getSelectionModel().getSelectedItem());
                create.setString(2, datetockage);
                create.setInt(3, 0);
                create.setInt(4, Integer.valueOf(quant.getText()));
                create.setInt(5, cs - Integer.valueOf(quant.getText()));
                create.executeUpdate();

                create = Main.con.prepareStatement("update stock set stock = stock - ? where (n_article = ? and date > ?)");
                create.setInt(1, Integer.valueOf(quant.getText()));
                create.setString(2, article.getSelectionModel().getSelectedItem());
                create.setString(3, datetockage);
                create.executeUpdate();

                ((Node)event.getSource()).getScene().getWindow().hide();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Erreur");
                alert.setContentText("La quantité disponible pour cet article n'est pas suffisante pour effectuer cette livraison");

                Optional<ButtonType> result = alert.showAndWait();
            }

        }catch(Exception ex){
            System.out.println("error 2: " + ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        chargerArticles();

        chargerNumBon();

    }
}
