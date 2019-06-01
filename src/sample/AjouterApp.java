package sample;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AjouterApp implements Initializable {

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
        ResultSet rs;
        try{
            String query = "select distinct n_bon_a, date from approvisiont";
            rs = Main.st.executeQuery(query);
            while(rs.next()){
                Bon b = new Bon(rs.getString("n_bon_a"), rs.getString("date"));
                listnum2.add(b);
                listnum.add(rs.getString("n_bon_a"));
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
        PreparedStatement create = Main.con.prepareStatement("select stock from stock where n_article = ? and date < ? order by date desc");
        create.setString(1, n_article);
        create.setString(2, date);
        rs = create.executeQuery();
        while (rs.next()){
            temp = rs.getInt("stock");
            break;
        }
        return temp;
    }

  /*  public int chercherStockNum(String n_article, String n_bon) throws SQLException {
        ResultSet rs;
        String date = null;
        int temp = 0;
        PreparedStatement create = Main.con.prepareStatement("select date from approvisiont where n_bon_a = ?");
        create.setString(1, n_bon);
        rs = create.executeQuery();
        while (rs.next()){
            date = rs.getString("date");
        }

        create = Main.con.prepareStatement("select stock from stock where n_article = ? and date <= ? order by date desc");
        create.setString(1, n_article);
        create.setString(2, date);
        rs = create.executeQuery();
        while (rs.next()){
            temp = rs.getInt("stock");
            break;
        }
        return temp;
    }

    public String chercherDate(String n_bon_a) throws SQLException {
        String temp = null;
        PreparedStatement create = Main.con.prepareStatement("select date from approvisiont where n_bon_a = ?");
        create.setString(1, n_bon_a);
        rs = create.executeQuery();
        while (rs.next()){
            temp = rs.getString("date");
            break;
        }
        return temp;
    }*/

    @FXML
    public void ajouter(javafx.event.ActionEvent event) throws IOException,SQLException {
        String datetockage = listnum2.get(listnum.indexOf(numBon.getSelectionModel().getSelectedItem())).getDate();
        int cs = chercherStock(article.getSelectionModel().getSelectedItem(), datetockage);
        try{
            PreparedStatement create = Main.con.prepareStatement("insert into detail_app values(?,?,?)");
            create.setString(1, numBon.getSelectionModel().getSelectedItem());
            create.setString(2,article.getSelectionModel().getSelectedItem());
            create.setInt(3,Integer.valueOf(quant.getText()));
            create.executeUpdate();

            create = Main.con.prepareStatement("insert into stock values(?,?,?,?,?)");
            create.setString(1, article.getSelectionModel().getSelectedItem());
            create.setString(2,datetockage);
            create.setInt(3, Integer.valueOf(quant.getText()));
            create.setInt(4,0);
            create.setInt(5, cs + Integer.valueOf(quant.getText()));
            create.executeUpdate();

            create = Main.con.prepareStatement("update stock set stock = stock + ? where (n_article = ? and date > ?)");
            create.setInt(1, Integer.valueOf(quant.getText()));
            create.setString(2, article.getSelectionModel().getSelectedItem());
            create.setString(3, datetockage);
            create.executeUpdate();

        }catch(Exception ex){
            System.out.println(ex);
        }

        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        chargerArticles();

        chargerNumBon();

    }
}
