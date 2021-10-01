package OVChipkaart_Opdracht.persistence;

import OVChipkaart_Opdracht.domein.OVChipkaart;
import OVChipkaart_Opdracht.domein.Product;

import java.util.List;

public interface ProductDAO {
    public boolean save(Product product);
    public boolean update(Product product);
    public boolean delete(Product product);
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart);
    public List<Product> findall();
    public Product findById(int id);
    public void setOdao(OVChipkaartDAO odao);
}
