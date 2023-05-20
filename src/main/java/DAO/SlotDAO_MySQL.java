package DAO;

import model.Slot;

import java.sql.*;
import java.util.ArrayList;

public class SlotDAO_MySQL implements SlotDAO {

    //Dades de connexió a la base de dades
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_RUTA = "jdbc:mysql://localhost:3306/expenedora";
    private static final String DB_USUARI = "root";
    private static final String DB_CONTRASENYA = "";

    private Connection connexio;

    public SlotDAO_MySQL() {

        try {
            Class.forName(DB_DRIVER);
            connexio = DriverManager.getConnection(DB_RUTA, DB_USUARI, DB_CONTRASENYA);
        } catch (Exception e) {
            System.out.println("S'ha produït un error al accedir a la base de dades.");
            System.out.println(e);
        }

    }

    @Override
    public Slot readSlot(String codiProducte) throws SQLException {

        PreparedStatement ps = connexio.prepareStatement("SELECT * FROM SLOT WHERE codi_producte = ?");
        ps.setString(1, codiProducte);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Slot slot = new Slot();

            slot.setPosicio(rs.getInt(1));
            slot.setQuantitat(rs.getInt(2));
            slot.setCodiProducte(rs.getString(3));

            return slot;
        } else {
            return null;
        }

    }

    @Override
    public void createSlot(Slot s) throws SQLException {

        PreparedStatement ps = connexio.prepareStatement("INSERT INTO SLOT VALUES(?, ?, ?)");

        ps.setInt(1, s.getPosicio());
        ps.setInt(2, s.getQuantitat());
        ps.setString(3, s.getCodiProducte());

        int rowCount = ps.executeUpdate();

        if (rowCount == 1) {
            System.out.println("Slot creat correctament");
        } else {
            System.out.println("No s'ha pogut crear el slot");
        }

    }

    public ArrayList<Slot> readSlots() throws SQLException {

        PreparedStatement ps = connexio.prepareStatement("SELECT * FROM SLOT");

        ResultSet rs = ps.executeQuery();

        ArrayList<Slot> slots = new ArrayList<>();

        while (rs.next()) {
            Slot slot = new Slot();

            slot.setPosicio(rs.getInt(1));
            slot.setQuantitat(rs.getInt(2));
            slot.setCodiProducte(rs.getString(3));

            slots.add(slot);
        }
        return slots;

    }

    @Override
    public void updateSlot(Slot s) throws SQLException {

        PreparedStatement ps = connexio.prepareStatement("UPDATE SLOT SET quantitat = ?, posicio = ? WHERE codi_producte = ?");

        ps.setInt(1, s.getQuantitat());
        ps.setInt(2, s.getPosicio());
        ps.setString(3, s.getCodiProducte());

        ps.executeUpdate();

    }

    @Override
    public void deleteSlot(Slot s) throws SQLException {

    }

    @Override
    public void deleteSlot(String codiProducte) throws SQLException {

    }
}
