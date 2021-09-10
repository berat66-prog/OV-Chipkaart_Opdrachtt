package OVChipkaart_Opdracht.persistence;

import OVChipkaart_Opdracht.domein.Reiziger;

import java.util.List;

public interface ReizigerDAO {

    public boolean save(Reiziger reiziger);
    public boolean update(Reiziger reiziger);
    public boolean delete(Reiziger reiziger);
    public Reiziger findByid(int id);
    public List<Reiziger> findByGbdatum(String datum);
    public List<Reiziger> findAll();
}
