package OVChipkaart_Opdracht;

import OVChipkaart_Opdracht.domein.Adres;
import OVChipkaart_Opdracht.domein.Reiziger;
import OVChipkaart_Opdracht.persistence.AdresDAO;
import OVChipkaart_Opdracht.persistence.AdresDAOPsql;
import OVChipkaart_Opdracht.persistence.ReizigerDAO;
import OVChipkaart_Opdracht.persistence.ReizigerDAOPsql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Connection connection;



    public static void main(String[] args){
        ReizigerDAO rdao = new ReizigerDAOPsql(getConnection(connection));
        AdresDAO adao = new AdresDAOPsql(getConnection(connection));
        testReizigerDAO(rdao);
        testAdresDAO(adao);


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




    private static void testReizigerDAO(ReizigerDAO rdao){
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

        rdao.update(new Reiziger(78, "K", "van der", "Valk", java.sql.Date.valueOf(gbdatum)));


        System.out.println("[Test] ReizigerDAO.findByid() geeft de volgende reiziger:");
        Reiziger r = rdao.findByid(82);
        System.out.println(r);
        System.out.println();

        reizigers = rdao.findByGbdatum("1981-03-18");
        System.out.println("[Test] ReizigerDAO.findByGbdatum() geeft de volgende reizigers:");
        for (Reiziger re : reizigers) {
            System.out.println(re);
        }

        rdao.delete(sietske);

        System.out.println();

        reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger re : reizigers) {
            System.out.println(re);
        }

        System.out.println();





    }

    private static void testAdresDAO(AdresDAO adao){

        System.out.println("Test adao.findall() geeft de volgende adressen:");
        List<Adres> adressen = adao.findall();

        for(Adres adres : adressen){
            System.out.println(adres);
        }
        System.out.println();
        String gbdatum1 = "1981-03-19";

        Reiziger r = new Reiziger(82, "S", "v", "Boers", java.sql.Date.valueOf(gbdatum1));
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");

        Adres adres = new Adres(13, "3527BJ", "307", "Trumanlaan", "Utrecht");

        adres.setReiziger(r);
        adao.save(adres);
        adressen = adao.findall();
        System.out.println(adressen.size() + " adressen\n");

        System.out.println();

        System.out.println("Test adao.update()");
        adao.update(new Adres(12, "3527HJ", "308", "Trumanlaan", "Utrecht"));

        for(Adres adress : adressen){
            System.out.println(adress);

        }

        System.out.println();

        System.out.println("Test adao.findByReiziger() geeft de volgende adres:");
        String gbdatum = "1981-03-18";

        Reiziger reiziger = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));

        Adres a = adao.findByReiziger(reiziger);

        System.out.println(a);


    }

}


