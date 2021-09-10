package OVChipkaart_Opdracht.persistence;

import OVChipkaart_Opdracht.domein.Adres;
import OVChipkaart_Opdracht.domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO{
    private Connection conn;
    ReizigerDAO rdao;

    public AdresDAOPsql(Connection connection){
        this.conn = connection;
    }

    @Override
    public boolean save(Adres adres){
        boolean opslaan = false;
        String query = "INSERT INTO adres (adres_id,postcode,huisnummer,straat,woonplaats,reiziger_id) VALUES (?, ?, ?, ?, ?,?)";
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,adres.getAdres_id());
            preparedStatement.setString(2, adres.getPostcode());
            preparedStatement.setString(3, adres.getHuisnummer());
            preparedStatement.setString(4, adres.getStraat());
            preparedStatement.setString(5, adres.getWoonplaats());
            preparedStatement.setInt(6,adres.getReiziger().getId());
            preparedStatement.executeUpdate();
            opslaan = true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return opslaan;
    }

    @Override
    public boolean update(Adres adres){
        boolean result = false;

        String query = "UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?" + " WHERE adres_id= '"+ adres.getAdres_id() + "'";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);


            pstmt.setString(1, adres.getPostcode());
            pstmt.setString(2, adres.getHuisnummer());
            pstmt.setString(3, adres.getStraat());
            pstmt.setString(4, adres.getWoonplaats());

            result = pstmt.executeUpdate() > 0;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public Adres findByReiziger(Reiziger reiziger){
        Adres adres = null;

        String query = "SELECT adres_id, postcode, huisnummer, straat, woonplaats FROM adres WHERE reiziger_id = '"+ reiziger.getId() + "'";

        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int adres_id = rs.getInt("adres_id");
                String postcode = rs.getString("postcode");
                String huisnummer= rs.getString("huisnummer");
                String straat = rs.getString("straat");
                String woonplaats = rs.getString("woonplaats");
                adres = new Adres(adres_id,postcode,huisnummer,straat,woonplaats);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adres;
    }


    public List<Adres> selectAdressen(String query){
        List<Adres> adressen = new ArrayList<>();


        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("adres_id");
                String postcode = rs.getString("postcode");
                String huisnummer= rs.getString("huisnummer");
                String straat = rs.getString("straat");
                String woonplaats = rs.getString("woonplaats");
                adressen.add(new Adres(id,postcode,huisnummer,straat,woonplaats));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adressen;
    }
    @Override
    public List<Adres> findall(){
        return selectAdressen("SELECT * FROM adres");

    }
    @Override
    public boolean delete(Adres adres){
        boolean result = false;

        String query = "DELETE FROM adres WHERE adres_id = ?";

        try{
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1,adres.getAdres_id());
            result = pstmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }
}
