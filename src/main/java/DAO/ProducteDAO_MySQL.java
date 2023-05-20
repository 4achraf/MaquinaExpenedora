package DAO;


import model.Producte;

import java.sql.*;
import java.util.ArrayList;

public class ProducteDAO_MySQL implements ProducteDAO {

    //Dades de connexió a la base de dades
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_RUTA = "jdbc:mysql://localhost:3306/expenedora";
    private static final String DB_USUARI = "root";
    private static final String DB_CONTRASENYA = "";

    private Connection connexio;

    public ProducteDAO_MySQL() {

        try {
            Class.forName(DB_DRIVER);
            connexio = DriverManager.getConnection(DB_RUTA, DB_USUARI, DB_CONTRASENYA);
            if (connexio == null) {
                System.out.println("No s'ha pogut establir la connexió.");
            }
        } catch (Exception e) {
            System.out.println("S'ha produït un error al accedir a la base de dades.");
            System.out.println(e);
        }

    }

    @Override
    public void createProducte(Producte p) throws SQLException {

        PreparedStatement ps = connexio.prepareStatement("INSERT INTO PRODUCTE VALUES(?, ?, ?, ?, ?)");

        ps.setString(1,p.getCodiProducte());
        ps.setString(2,p.getNom());
        ps.setString(3,p.getDescripcio());
        ps.setFloat(4,p.getPreuCompra());
        ps.setFloat(5,p.getPreuVenta());

        int rowCount = ps.executeUpdate();

        if (rowCount == 1) {
            System.out.println("\nProducte creat correctament");
        } else {
            System.out.println("No s'ha pogut crear el producte");
        }

    }

    @Override
    public Producte readProducte(String codiProducte) throws SQLException {

        PreparedStatement ps = connexio.prepareStatement("SELECT * FROM PRODUCTE WHERE codi_producte = ?");

        ps.setString(1, codiProducte);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Producte p = new Producte();
            p.setCodiProducte(rs.getString(1));
            p.setNom(rs.getString(2));
            p.setDescripcio(rs.getString(3));
            p.setPreuCompra(rs.getFloat(4));
            p.setPreuVenta(rs.getFloat(5));
            return p;
        } else {
            return null;
        }

    }

    @Override
    public ArrayList<Producte> readProductes() throws SQLException {

        ArrayList<Producte> llistaProductes = new ArrayList<>();

        if (connexio != null) {
            PreparedStatement ps = connexio.prepareStatement("SELECT * FROM PRODUCTE");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Producte p = new Producte();
                p.setCodiProducte(rs.getString(1));
                p.setNom(rs.getString(2));
                p.setDescripcio(rs.getString(3));
                p.setPreuCompra(rs.getFloat(4));
                p.setPreuVenta(rs.getFloat(5));
                llistaProductes.add(p);
            }
        }
        return llistaProductes;

    }

    @Override
    public void updateProducte(Producte p) throws SQLException {

        PreparedStatement ps = connexio.prepareStatement("UPDATE PRODUCTE SET NOM=?, DESCRIPCIO=?, PREU_COMPRA=?, PREU_VENTA=? WHERE CODI_PRODUCTE=?");

        ps.setString(1, p.getNom());
        ps.setString(2, p.getDescripcio());
        ps.setFloat(3, p.getPreuCompra());
        ps.setFloat(4, p.getPreuVenta());
        ps.setString(5, p.getCodiProducte());

        ps.executeUpdate();

    }

    @Override
    public void deleteProducte(Producte p) throws SQLException {

    }

    @Override
    public void deleteProducte(String codiProducte) throws SQLException {

    }

}