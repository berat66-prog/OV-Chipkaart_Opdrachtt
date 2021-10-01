package OVChipkaart_Opdracht.persistence;

import OVChipkaart_Opdracht.domein.Adres;
import OVChipkaart_Opdracht.domein.OVChipkaart;
import OVChipkaart_Opdracht.domein.Product;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO{

    private Connection conn;
    private OVChipkaartDAO odao;

    public ProductDAOPsql(Connection connection){
        this.conn = connection;
    }

    @Override
    public void setOdao(OVChipkaartDAO odao){
        this.odao = odao;
    }


    @Override
    public boolean save(Product product){
        boolean opslaan = false;

        String query = "INSERT INTO product (product_nummer,naam,beschrijving,prijs) VALUES (?, ?, ?, ?)";
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,product.getProduct_nummer());
            preparedStatement.setString(2, product.getNaam());
            preparedStatement.setString(3, product.getBeschrijving());
            preparedStatement.setDouble(4,product.getPrijs());
            preparedStatement.executeUpdate();


            for(OVChipkaart ovChipkaart : product.getOvChipkaarten()){
                String relatie = "INSERT INTO ov_chipkaart_product VALUES (?,?,?,?)";
                PreparedStatement pstm = conn.prepareStatement(relatie);
                pstm.setInt(1, ovChipkaart.getKaart_nummer());
                pstm.setInt(2, product.getProduct_nummer());
                pstm.setString(3, null);
                pstm.setDate(4, null);
                pstm.executeUpdate();
            }



            opslaan = true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return opslaan;
    }
    @Override
    public boolean update(Product product){
        boolean result = false;

        String query = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ?" + " WHERE product_nummer= ?";
        String queryRelatie = "UPDATE ov_chipkaart_product SET last_update = ?" + "WHERE product_nummer = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setString(1, product.getNaam());
            pstmt.setString(2, product.getBeschrijving());
            pstmt.setDouble(3, product.getPrijs());
            pstmt.setInt(4, product.getProduct_nummer());
            result = pstmt.executeUpdate() > 0;

            PreparedStatement pstmtRelatie = conn.prepareStatement(queryRelatie);
            pstmtRelatie.setDate(1, Date.valueOf(LocalDate.now()));
            pstmtRelatie.setInt(2, product.getProduct_nummer());
            result = pstmtRelatie.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public boolean delete(Product product){
        boolean result = false;

        String relatieQuery = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        String query = "DELETE FROM product WHERE product_nummer = ?";

        try{
            PreparedStatement pstmtRelatie = conn.prepareStatement(relatieQuery);
            pstmtRelatie.setInt(1, product.getProduct_nummer());
            result = pstmtRelatie.executeUpdate() > 0;


            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1,product.getProduct_nummer());
            result = pstmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart){
        List<Product> producten = ovChipkaart.getProducten();

        String query = "SELECT * FROM product p JOIN ov_chipkaart_product ovp ON ovp.product_nummer = p.product_nummer WHERE ovp.kaart_nummer = ?";
        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, ovChipkaart.getKaart_nummer());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int product_nummer = rs.getInt("product_nummer");
                String naam = rs.getString("naam");
                String beschrijving= rs.getString("beschrijving");
                double prijs = rs.getDouble("prijs");


                Product product = new Product();
                product.setProduct_nummer(product_nummer);
                product.setNaam(naam);
                product.setBeschrijving(beschrijving);
                product.setPrijs(prijs);

                producten.add(product);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return producten;
    }

    public List<Product> selectProducten(String query){
        List<Product> producten = new ArrayList<>();

        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int product_nummer = rs.getInt("product_nummer");
                String naam = rs.getString("naam");
                String beschrijving= rs.getString("beschrijving");
                double prijs = rs.getDouble("prijs");


                Product product = new Product();
                product.setProduct_nummer(product_nummer);
                product.setNaam(naam);
                product.setBeschrijving(beschrijving);
                product.setPrijs(prijs);

                producten.add(product);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return producten;
    }

    @Override
    public List<Product> findall(){
        return selectProducten("SELECT * FROM product");
    }

    @Override
    public Product findById(int id){
        Product product = null;
        String query = "SELECT product_nummer, naam, beschrijving, prijs FROM product WHERE product_nummer = ?";

        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int product_nummer = rs.getInt("product_nummer");
                String naam = rs.getString("naam");
                String beschrijving = rs.getString("beschrijving");
                double prijs = rs.getDouble("prijs");

                product = new Product();
                product.setProduct_nummer(product_nummer);
                product.setNaam(naam);
                product.setBeschrijving(beschrijving);
                product.setPrijs(prijs);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

}
