package livraison;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import menu.Menu;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifierLiv implements Initializable {

    private ResultSet rs;

    public ObservableList<String> listarticles = FXCollections.observableArrayList();

    public ObservableList<String> listnum = FXCollections.observableArrayList();

    @FXML
    JFXTextField quant;
    @FXML
    Label article;
    @FXML
    Label numBon;



   /* public void chargerArticles(){
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
            String query = "select distinct n_bon_l from livraison";
            rs = Main.st.executeQuery(query);
            while(rs.next()){
                listnum.add(rs.getString("n_bon_l"));
            }
        }catch(Exception ex){
            System.out.println("error: " + ex);
        }
        numBon.setItems(listnum);
        numBon.getSelectionModel().selectFirst();
    }*/

    public int chercherStock(String n_article, String date) throws SQLException {
        ResultSet rs;
        int temp = 0;
        PreparedStatement create = Menu.con.prepareStatement("select stock from stock where n_article = ? and date <= ? order by date desc");
        create.setString(1, n_article);
        create.setString(2, date);
        rs = create.executeQuery();
        while (rs.next()){
            temp = rs.getInt("stock");
            break;
        }
        return temp;
    }

    public Boolean testStockNeg(String n_article, String date, int liv)throws SQLException{
        Boolean temp = true;
        ResultSet rs;
        ArrayList<Integer> stocks = new ArrayList();
        PreparedStatement create = Menu.con.prepareStatement("select stock from stock where n_article = ? and date >= ?");
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
    public void modifier(javafx.event.ActionEvent event) throws IOException, SQLException {
        int newStockDiff = Integer.valueOf(quant.getText()) - GererLiv.selectedItem.getQte_l();
        String datetockage = GererLiv.selectedItem.getDate_liv();
        int cs = chercherStock(article.getText(), datetockage);
        Boolean test = testStockNeg(GererLiv.selectedItem.getNum(), GererLiv.selectedItem.getDate_liv(), newStockDiff);

        try{
            if((cs >= newStockDiff) && test) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                alert.setTitle("Confirmation");
                alert.setContentText("Etes-vous sur de vouloir apporter ces modificatons ?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    PreparedStatement create = Menu.con.prepareStatement("update detail_liv set qte_l = ? where (n_bon_l = ? and n_article = ?)");
                    create.setInt(1, Integer.valueOf(quant.getText()));
                    create.setString(2, numBon.getText());
                    create.setString(3, article.getText());
                    create.executeUpdate();

                    create = Menu.con.prepareStatement("update stock set stock = stock - ? where (n_article = ? and date >= ?)");
                    create.setInt(1, newStockDiff);
                    create.setString(2, article.getText());
                    create.setString(3, GererLiv.selectedItem.getDate_liv());
                    create.executeUpdate();

                    create = Menu.con.prepareStatement("update stock set qte_l = ? where (n_article = ? and date = ?)");
                    create.setInt(1, Integer.valueOf(quant.getText()));
                    create.setString(2, article.getText());
                    create.setString(3, GererLiv.selectedItem.getDate_liv());
                    create.executeUpdate();

                    GererLiv.selectedItem = null;
                    ((Node) event.getSource()).getScene().getWindow().hide();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Erreur");
                alert.setContentText("La quantité disponible pour cet article n'est pas suffisante pour effectuer cette livraison");

                Optional<ButtonType> result = alert.showAndWait();
            }

        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

      //  chargerArticles();

      //  chargerNumBon();

        numBon.setText(GererLiv.selectedItem.getNumBon());
        article.setText(GererLiv.selectedItem.getNum());
        quant.setText(String.valueOf(GererLiv.selectedItem.getQte_l()));
    }
}
