package DAO;

import model.Producte;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ProducteDAO {

    void createProducte(Producte p) throws SQLException;

    Producte readProducte(String codiProducte) throws SQLException;

    ArrayList<Producte> readProductes() throws SQLException;

    void updateProducte(Producte p) throws SQLException;

    void deleteProducte(Producte p) throws SQLException;

    void deleteProducte(String codiProducte) throws SQLException;

}
