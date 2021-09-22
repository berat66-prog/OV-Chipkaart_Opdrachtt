package OVChipkaart_Opdracht.persistence;

import OVChipkaart_Opdracht.domein.Reiziger;

import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAO adao;

    public ReizigerDAOPsql(Connection connection){
        this.conn = connection;

    }


    @Override
    public boolean save(Reiziger reiziger){
        boolean opslaan = false;
        String query = "INSERT INTO reiziger (reiziger_id,voorletters,tussenvoegsel,achternaam,geboortedatum) VALUES (?, ?, ?, ?, ?)";
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,reiziger.getId());
            preparedStatement.setString(2, reiziger.getVoorletters());
            preparedStatement.setString(3, reiziger.getTussenvoegsel());
            preparedStatement.setString(4, reiziger.getAchternaam());
            preparedStatement.setDate(5, reiziger.getGeboortedatum());
            preparedStatement.executeUpdate();
            opslaan = true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return opslaan;
    }

    @Override
    public boolean update(Reiziger reiziger){
        boolean result = false;

        String query = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ?" + " WHERE reiziger_id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);


            pstmt.setString(1, reiziger.getVoorletters());
            pstmt.setString(2, reiziger.getTussenvoegsel());
            pstmt.setString(3, reiziger.getAchternaam());
            pstmt.setDate(4, reiziger.getGeboortedatum());
            pstmt.setInt(5, reiziger.getId());

            result = pstmt.executeUpdate() > 0;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }



    @Override
    public List<Reiziger> findAll(){
        List<Reiziger> reizigers = new ArrayList<>();

        String query = "SELECT * FROM reiziger";

        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("reiziger_id");
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel= rs.getString("tussenvoegsel");
                String achternaam = rs.getString("achternaam");
                java.sql.Date geboortedatum = rs.getDate("geboortedatum");
                if(tussenvoegsel == null){
                    tussenvoegsel = "";
                }
                reizigers.add(new Reiziger(id,voorletters,tussenvoegsel,achternaam,geboortedatum));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reizigers;


    }
    @Override
    public Reiziger findByid(int id){
        Reiziger reiziger = null;

        String query = "SELECT reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum FROM reiziger WHERE reiziger_id = ?";

        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int reiziger_id = rs.getInt("reiziger_id");
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel= rs.getString("tussenvoegsel");
                String achternaam = rs.getString("achternaam");
                java.sql.Date geboortedatum = rs.getDate("geboortedatum");
                if(tussenvoegsel == null){
                    tussenvoegsel = "";
                }
                reiziger = new Reiziger(reiziger_id,voorletters,tussenvoegsel,achternaam,geboortedatum);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reiziger;

    }
    @Override
    public List<Reiziger> findByGbdatum(String datum){
        List<Reiziger> reizigers = new ArrayList<>();

        String query = "SELECT reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum FROM reiziger WHERE geboortedatum = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDate(1,java.sql.Date.valueOf(datum));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("reiziger_id");
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel= rs.getString("tussenvoegsel");
                String achternaam = rs.getString("achternaam");
                java.sql.Date geboortedatum = rs.getDate("geboortedatum");
                if(tussenvoegsel == null){
                    tussenvoegsel = "";
                }
                reizigers.add(new Reiziger(id,voorletters,tussenvoegsel,achternaam,geboortedatum));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reizigers;

    }
    @Override
    public boolean delete(Reiziger reiziger){
        boolean result = false;

        String query = "DELETE FROM reiziger WHERE reiziger_id = ?";

        try{
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1,reiziger.getId());
            result = pstmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }


}
