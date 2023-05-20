package DAO;

import model.Slot;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SlotDAO {

    Slot readSlot(String codiProducte) throws SQLException;

    void createSlot(Slot s) throws SQLException;

    ArrayList<Slot> readSlots() throws SQLException;

    void updateSlot(Slot s) throws SQLException;

}
