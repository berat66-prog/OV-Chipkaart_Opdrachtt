package OVChipkaart_Opdracht;

import OVChipkaart_Opdracht.domein.Adres;
import OVChipkaart_Opdracht.domein.OVChipkaart;
import OVChipkaart_Opdracht.domein.Product;
import OVChipkaart_Opdracht.domein.Reiziger;
import OVChipkaart_Opdracht.persistence.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Connection connection;

    public static void main(String[] args){
        ReizigerDAO rdao = new ReizigerDAOPsql(getConnection(connection));
        AdresDAO adao = new AdresDAOPsql(getConnection(connection));
        OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOPsql(getConnection(connection));
        ProductDAO pdao = new ProductDAOPsql(getConnection(connection));
        testReizigerDAO(rdao,ovChipkaartDAO);
        testAdresDAO(adao, rdao);
        testOVCHipkaartDAO(ovChipkaartDAO,rdao);
        testProductDAO(pdao,ovChipkaartDAO);


    }

    public static Connection getConnection(Connection connection){

        String dbURL = "jdbc:postgresql://localhost:5432/ovchip";
        String username = "postgres";
        String password = "Regenboog66";

        try {
            connection = DriverManager.getConnection(dbURL, username, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;

    }

    private static void testReizigerDAO(ReizigerDAO rdao, OVChipkaartDAO ovChipkaartDAO){
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-18";
        Reiziger sietske = new Reiziger(83, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        System.out.println();

        //TEST update Reiziger

        System.out.println("Reizigers voor de update. Let op sietske");

        reizigers = rdao.findAll();
        for(Reiziger r : reizigers){
            System.out.println(r);
        }

        System.out.println();

        sietske = new Reiziger(83, "B", "", "Coskun", java.sql.Date.valueOf(gbdatum));
        rdao.update(sietske);

        System.out.println("Reiziger Sietske na de update:");
        reizigers = rdao.findAll();
        for(Reiziger r: reizigers){
            System.out.println(r);
        }

        System.out.println();

        // TEST reiziger delete
        System.out.println("[TEST] eerst " + reizigers.size() + " reizigers, na rdao.delete():");
        rdao.delete(sietske);

        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + "reizigers\n");

        System.out.println();

        System.out.println("[Test] ReizigerDAO.findByid() geeft de volgende reiziger:");
        Reiziger r = rdao.findByid(5);
        System.out.println(r);
        System.out.println();

        reizigers = rdao.findByGbdatum("2002-12-03");
        System.out.println("[Test] ReizigerDAO.findByGbdatum() geeft de volgende reizigers:");
        for (Reiziger re : reizigers) {
            System.out.println(re);
        }

        System.out.println();

        reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger re : reizigers) {
            System.out.println(re);
        }

        System.out.println();

    }

    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao){

        adao.setRdao(rdao);

        System.out.println("Test adao.findall() geeft de volgende adressen:");
        List<Adres> adressen = adao.findall();

        for(Adres adres : adressen){
            System.out.println(adres);
        }
        System.out.println();

        //nieuwe adres aanmaken en persisteren naar de database
        String gbdatum1 = "1981-03-19";

        Reiziger r = new Reiziger(82, "S", "v", "Boers", java.sql.Date.valueOf(gbdatum1));
        rdao.save(r);
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");


        Adres adres = new Adres();
        adres.setAdres_id(13);
        adres.setPostcode("3527BJ");
        adres.setHuisnummer("307");
        adres.setStraat("Trumanlaan");
        adres.setWoonplaats("Utrecht");

        adres.setReiziger(r);
        adao.save(adres);
        adressen = adao.findall();
        System.out.println(adressen.size() + " adressen\n");

        System.out.println();

        // TEST update adres

        System.out.println("Adressen voor de update:");
        adressen = adao.findall();
        for(Adres adress: adressen){
            System.out.println(adress);
        }

        adres = new Adres();
        adres.setAdres_id(13);
        adres.setPostcode("3527BJ");
        adres.setHuisnummer("307A");
        adres.setStraat("Trumanlaan");
        adres.setWoonplaats("Utrecht");
        adao.update(adres);
        System.out.println("Adres met id 13 na de update:");
        adressen = adao.findall();

        for(Adres adress: adressen){
            System.out.println(adress);
        }
        System.out.println();

        // TEST adres delete
        System.out.println("[TEST] eerst " + adressen.size() + " adressen, na adao.delete():");
        adao.delete(adres);

        adressen = adao.findall();
        System.out.println(adressen.size() + "adressen\n");

        System.out.println();

        // TEST adres vinden bij een gegevens reiziger

        System.out.println("Test adao.findByReiziger() geeft de volgende adres:");

        Reiziger reiziger = rdao.findByid(5);

        System.out.println(adao.findByReiziger(reiziger));

        rdao.delete(r);

        System.out.println();

    }

    private static void testOVCHipkaartDAO(OVChipkaartDAO ovChipkaartDAO, ReizigerDAO reizigerDAO){
        ovChipkaartDAO.setRdao(reizigerDAO);

        System.out.println("Test OVChipkaarDAO.findall() geeft de volgende ovChipkaarten met de bijbehorende reizigers:");
        List<OVChipkaart> ovChipkaarten = ovChipkaartDAO.findall();

        for(OVChipkaart ovChipkaart : ovChipkaarten){
            System.out.println(ovChipkaart);
        }

        System.out.println();

        //OVChipkaart aanmaken en toewijzen aan een Reiziger
        String gbdatum1 = "1999-01-22";

        Reiziger berat = new Reiziger(6, "M.B", "", "Coskun", java.sql.Date.valueOf(gbdatum1));
        reizigerDAO.save(berat);
        System.out.print("[Test] Eerst " + ovChipkaarten.size() + " OVChipkaarten, na OVCipkaart.save() ");

        OVChipkaart ovChipkaart = new OVChipkaart();
        ovChipkaart.setKaart_nummer(1001);
        ovChipkaart.setGeldig_tot(java.sql.Date.valueOf("2021-12-25"));
        ovChipkaart.setKlasse(1);
        ovChipkaart.setSaldo(10.0);
        ovChipkaart.setReiziger(berat);
        ovChipkaartDAO.save(ovChipkaart);

        OVChipkaart ovChipkaart1 = new OVChipkaart();
        ovChipkaart1.setKaart_nummer(1002);
        ovChipkaart1.setGeldig_tot(java.sql.Date.valueOf("2021-11-26"));
        ovChipkaart1.setKlasse(2);
        ovChipkaart1.setSaldo(25.0);
        ovChipkaart1.setReiziger(berat);
        ovChipkaartDAO.save(ovChipkaart1);

        ovChipkaarten = ovChipkaartDAO.findall();
        System.out.println(ovChipkaarten.size() + " OVChipkaarten\n");

        // TEST update adres

        System.out.println("OVChipkaarten voor de update:");
        ovChipkaarten = ovChipkaartDAO.findall();
        for(OVChipkaart ov : ovChipkaarten){
            System.out.println(ov);
        }

        System.out.println();

        ovChipkaart = new OVChipkaart();
        ovChipkaart.setKaart_nummer(1001);
        ovChipkaart.setGeldig_tot(java.sql.Date.valueOf("2021-12-25"));
        ovChipkaart.setKlasse(1);
        ovChipkaart.setSaldo(50.0);
        ovChipkaartDAO.update(ovChipkaart);
        System.out.println("OVChipkaart met kaart_nummer 1001 na de update {let op de saldo!}:");
        ovChipkaarten = ovChipkaartDAO.findall();

        for(OVChipkaart ovChipkaart2 : ovChipkaarten){
            System.out.println(ovChipkaart2);
        }

        System.out.println();


        //TEST FindByReiziger

        Reiziger reiziger = reizigerDAO.findByid(6);
        System.out.println("Test OVChipkaartDA0.findByReiziger() voor reiziger " + reiziger.getVoorletters() + " " + reiziger.getAchternaam()  + " geeft de volgende OVChipkaart(en):");

        ovChipkaarten = ovChipkaartDAO.findByReiziger(reiziger);

        for(OVChipkaart ovChipkaartPerReiziger : ovChipkaarten){
            System.out.println(ovChipkaartPerReiziger);
        }


        System.out.println();

        // TEST OVChipkaart delete
        ovChipkaarten = ovChipkaartDAO.findall();
        System.out.println("[TEST] eerst " + ovChipkaarten.size() + " OVChipkaarten, na OVChipkaartDAO.delete():");
        ovChipkaartDAO.delete(ovChipkaart);
        ovChipkaartDAO.delete(ovChipkaart1);

        ovChipkaarten = ovChipkaartDAO.findall();
        System.out.println(ovChipkaarten.size() + " OVChipkaarten\n");

        System.out.println();


        reizigerDAO.delete(berat);

    }

    private static void testProductDAO(ProductDAO pdao, OVChipkaartDAO odao){
        pdao.setOdao(odao);

        System.out.println("Test OVChipkaarDAO.findall() geeft de volgende ovChipkaarten met de bijbehorende reizigers:");
        List<Product> producten = pdao.findall();

        for(Product product : producten){
            System.out.println(product);
        }

        System.out.println();

        //NIEUWE PRODUCT AANMAKEN EN PERSISTEREN NAAR DE DATABASE
        producten = pdao.findall();
        System.out.print("[Test] Eerst " + producten.size() + " producten, na pdao.save() ");
        Product product = new Product();
        product.setProduct_nummer(7);
        product.setNaam("test");
        product.setBeschrijving("test beschrijving");
        product.setPrijs(29.95);

        OVChipkaart ovChipkaart = odao.findByid(18326);
        OVChipkaart ovChipkaart1 = odao.findByid(35283);

        product.voegOVChipkaartToe(ovChipkaart);
        product.voegOVChipkaartToe(ovChipkaart1);

        pdao.save(product);

        producten = pdao.findall();
        System.out.println(producten.size() + " Producten\n");

        System.out.println();

        // EEN AANPASSING MAKEN AAN EEN BESTAAND PRODUCT
        System.out.println("[TEST] pdao.update");
        System.out.println("Product voor de aanpassing:");
        Product product1 = pdao.findById(7);
        System.out.println(product1);

        System.out.println();

        Product updateProduct = new Product();
        updateProduct.setProduct_nummer(7);
        updateProduct.setNaam("Aangepaste naam");
        updateProduct.setBeschrijving("Aangepaste beschrijving");
        updateProduct.setPrijs(10.00);
        pdao.update(updateProduct);

        System.out.println("Product na de aanpassingen: ");
        updateProduct = pdao.findById(7);
        System.out.println(updateProduct);

        System.out.println();

        //EEN PRODUCT VERWIJDEREN UIT DE DATABASE
        producten = pdao.findall();
        System.out.println("[TEST] eerst " + producten.size() + " producten, na pdao.delete():");

        product.verwijderOVChipkaart(ovChipkaart);
        product.verwijderOVChipkaart(ovChipkaart);
        pdao.delete(product);


        producten = pdao.findall();
        System.out.println(producten.size() + " OVChipkaarten\n");

        System.out.println();

        //SELECTEER DE PRODUCTEN DIE HOREN BIJ EEN OVCHIPKAART
        System.out.println("[TEST] pdao.findByOVChipkaart");

        System.out.println("pdao.findByOVChipkaart geeft de volgende product(en): ");

        OVChipkaart eenOVChipkaart = odao.findByid(35283);
        List<Product> products = pdao.findByOVChipkaart(eenOVChipkaart);

        for (Product p : products){
            System.out.println(p);
        }
        System.out.println();

        //SELECTEER OVCHIPKAARTEN MET EEN  PRODUCT
        System.out.println("[TEST] odao.findByProduct");
        System.out.println("odao.findByProduct geeft de volgende OVChipkaarten(en): ");

        Product eenProduct = pdao.findById(6);
        List<OVChipkaart> ovChipkaarten = odao.findByProduct(eenProduct);

        for(OVChipkaart ov : ovChipkaarten){
            System.out.println(ov);
        }
        System.out.println();




//
//        pdao.delete(product);
//
//
//
//
//        for(OVChipkaart ovChipkaarten : product.getOvChipkaarten()){
//            System.out.println(ovChipkaarten);
//        }


    }



}


