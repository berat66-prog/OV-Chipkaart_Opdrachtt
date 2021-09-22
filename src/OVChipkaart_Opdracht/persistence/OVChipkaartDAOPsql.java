package OVChipkaart_Opdracht.persistence;

import OVChipkaart_Opdracht.domein.OVChipkaart;
import OVChipkaart_Opdracht.domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO{
    private Connection conn;
    private ReizigerDAO rdao;

    public OVChipkaartDAOPsql(Connection connection){
        this.conn = connection;
    }
    @Override
    public boolean save(OVChipkaart ovChipkaart){
        boolean opslaan = false;
        String query = "INSERT INTO ov_chipkaart (kaart_nummer,geldig_tot,klasse,saldo,reiziger_id) VALUES (?, ?, ?, ?, ?)";
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,ovChipkaart.getKaart_nummer());
            preparedStatement.setDate(2, ovChipkaart.getGeldig_tot());
            preparedStatement.setInt(3, ovChipkaart.getKlasse());
            preparedStatement.setDouble(4,ovChipkaart.getSaldo());
            preparedStatement.setInt(5, ovChipkaart.getReiziger().getId());
            preparedStatement.executeUpdate();
            opslaan = true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return opslaan;

    }
    @Override
    public boolean update(OVChipkaart ovChipkaart){
        boolean result = false;

        String query = "UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?" + " WHERE kaart_nummer= ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);


            pstmt.setDate(1, ovChipkaart.getGeldig_tot());
            pstmt.setInt(2, ovChipkaart.getKlasse());
            pstmt.setDouble(3, ovChipkaart.getSaldo());
            pstmt.setInt(4, ovChipkaart.getKaart_nummer());

            result = pstmt.executeUpdate() > 0;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;

    }
    @Override
    public boolean delete(OVChipkaart ovChipkaart){
        boolean result = false;

        String query = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";

        try{
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1,ovChipkaart.getKaart_nummer());
            result = pstmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;

    }



    @Override
    public void setRdao(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    public List<OVChipkaart> selectOVChipkaarten(String query){
        List<OVChipkaart> OVChipkaarten = new ArrayList<>();


        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int kaart_nummer = rs.getInt("kaart_nummer");
                java.sql.Date geldig_tot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                double saldo  = rs.getDouble("saldo");
                int reiziger_id = rs.getInt("reiziger_id");



                OVChipkaart ovChipkaart = new OVChipkaart();
                ovChipkaart.setKaart_nummer(kaart_nummer);
                ovChipkaart.setGeldig_tot(geldig_tot);
                ovChipkaart.setKlasse(klasse);
                ovChipkaart.setSaldo(saldo);
                ovChipkaart.setReiziger(rdao.findByid(reiziger_id));

                OVChipkaarten.add(ovChipkaart);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return OVChipkaarten;
    }
    @Override
    public List<OVChipkaart> findall(){
        return selectOVChipkaarten("SELECT * FROM ov_chipkaart");

    }
    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger){
        List<OVChipkaart> ovChipkaarten = reiziger.getOVChipkaarten();

        String query = "SELECT kaart_nummer, geldig_tot, klasse, saldo FROM ov_chipkaart WHERE reiziger_id = ?";

        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1,reiziger.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int kaart_nummer = rs.getInt("kaart_nummer");
                java.sql.Date geldig_tot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                double saldo = rs.getDouble("saldo");

                OVChipkaart ovChipkaart = new OVChipkaart();

                ovChipkaart.setKaart_nummer(kaart_nummer);
                ovChipkaart.setGeldig_tot(geldig_tot);
                ovChipkaart.setKlasse(klasse);
                ovChipkaart.setSaldo(saldo);

                ovChipkaarten.add(ovChipkaart);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ovChipkaarten;
    }

}
