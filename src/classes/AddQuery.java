package classes;

import menu.Menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddQuery {

    public void creer_livraison() throws SQLException, NullPointerException {
        PreparedStatement create = Menu.con.prepareStatement("create table livraison(n_bon_l char(10) not null, date date, n_client char(10) not null," +
                "primary key(n_bon_l), foreign key(n_client) references client(n_client))");
        create.executeUpdate();
    }

    public void ajouter_article(String numArticle, String libelle, int prixUni, int stockMin) throws SQLException {
        PreparedStatement create = Menu.con.prepareStatement("insert into article values(?,?,?,?)");
        create.setString(1, numArticle);
        create.setString(2, libelle);
        create.setInt(3, prixUni);
        create.setInt(4, stockMin);

        create.executeUpdate();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        create = Menu.con.prepareStatement("insert into stock values(?,?,?,?,?)");
        create.setString(1, numArticle);
        create.setString(2, formatter.format(date));
        create.setInt(3, 0);
        create.setInt(4, 0);
        create.setInt(5, 0);

        create.executeUpdate();
    }

    public void ajouter_detailApp(String nbonA, String nArticle, int qteA) throws SQLException, NullPointerException {
        ResultSet rs;
        String date = null;
        PreparedStatement create = Menu.con.prepareStatement("insert into detail_app values (?,?,?)");
        create.setString(1, nbonA);
        create.setString(2, nArticle);
        create.setInt(3, qteA);
        create.executeUpdate();

        PreparedStatement create2 = Menu.con.prepareStatement("select date from approvisiont where nbonA = ?");
        create2.setString(1, nbonA);
        rs = create2.executeQuery();
        while (rs.next()) {
            date = rs.getString("date");
        }

        create = Menu.con.prepareStatement("insert into stock values (?,?,?,?,?)");
        create.setString(1, nArticle);
        create.setString(2, date);
        create.setInt(3, qteA);
        create.setInt(4, 0);
        create.setInt(5, qteA);
    }


}
