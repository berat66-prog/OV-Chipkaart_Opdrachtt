package OVChipkaart_Opdracht.persistence;

import OVChipkaart_Opdracht.domein.Adres;
import OVChipkaart_Opdracht.domein.Reiziger;

import java.util.List;

public interface AdresDAO {

    void setRdao(ReizigerDAO rdao);
    public boolean save(Adres adres);
    public boolean update(Adres adres);
    public boolean delete(Adres adres);
    public Adres findByReiziger(Reiziger reiziger);
    public List<Adres> findall();
}
