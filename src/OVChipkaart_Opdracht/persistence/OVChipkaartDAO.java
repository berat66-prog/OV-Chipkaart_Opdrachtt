package OVChipkaart_Opdracht.persistence;


import OVChipkaart_Opdracht.domein.OVChipkaart;
import OVChipkaart_Opdracht.domein.Product;
import OVChipkaart_Opdracht.domein.Reiziger;

import java.util.List;

public interface OVChipkaartDAO {
    void setRdao(ReizigerDAO rdao);
    public boolean save(OVChipkaart ovChipkaart);
    public boolean update(OVChipkaart ovChipkaart);
    public boolean delete(OVChipkaart ovChipkaart);
    public List<OVChipkaart> findByReiziger(Reiziger reiziger);
    public List<OVChipkaart> findall();
    public OVChipkaart findByid(int id);
    public List<OVChipkaart> findByProduct(Product product);
}
