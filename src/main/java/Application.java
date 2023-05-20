import DAO.ProducteDAO;
import DAO.ProducteDAO_MySQL;
import DAO.SlotDAO;
import DAO.SlotDAO_MySQL;
import model.Producte;
import model.Slot;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {

    private static final ProducteDAO ProducteDAO = new ProducteDAO_MySQL();

    private static final SlotDAO slotDAO = new SlotDAO_MySQL();

    private static final Scanner escaner = new Scanner(System.in);

    private static final ArrayList<String> productesComprats = new ArrayList<>();

    public static void main(String[] args) throws SQLException {

        int opcio;

        do
        {
            mostrarMenu();
            opcio = escaner.nextInt();

            switch (opcio) {
                case 1 -> mostrarMaquina();
                case 2 -> comprarProducte();

                case 10 -> mostrarInventari();
                case 11 -> afegirProductes();
                case 12 -> modificarMaquina();
                case 13 -> mostrarBenefici();

                case -1 -> System.out.println("Sortint del programa...");
                default -> System.out.println("Opció no vàlida");
            }

        } while(opcio != -1);

    }


    private static void modificarMaquina() throws SQLException {

        int opcio;

        do {
            System.out.println("1. Modificar posició");
            System.out.println("2. Modificar stock");
            System.out.println("3. Afegir ranures");
            System.out.println("-1. Sortir");
            System.out.print("Opció : ");

            opcio = escaner.nextInt();

            switch (opcio) {
                case 1 -> modificarPosicio();
                case 2 -> modificarStock();
                case 3 -> afegirRanures();

                case -1 -> System.out.println("Sortint del menú de modificació de màquina...");
                default -> System.out.println("Opció no vàlida.");
            }

        } while (opcio != -1);

    }

    private static void modificarPosicio() throws SQLException {

        ArrayList<Slot> slots = slotDAO.readSlots();

        boolean existeixProducte = false;

        System.out.print("Introdueix la posició on vols canviar el producte : ");
        int posicio = escaner.nextInt();

        for (Slot slot : slots) {
            if (slot.getPosicio() == posicio) {
                existeixProducte = true;
                System.out.print("Introdueix el codi del nou producte : ");
                String codiProducte = escaner.next();
                slot.setCodiProducte(codiProducte);
                slotDAO.updateSlot(slot);
                System.out.printf("\nS'ha substituït el producte a la posició %d amb el producte %s%n", posicio, codiProducte);
                break;
            }
        }
        if (!existeixProducte) {
            System.out.println("\nNo hi ha cap producte en aquesta posició");
        }

    }

    private static void modificarStock() throws SQLException {

        ArrayList<Slot> slots = slotDAO.readSlots();

        boolean existeixProducte = false;

        System.out.print("Introdueix el producte que vols modificar l'estoc : ");
        String codiProducte = escaner.next();

        for (Slot slot : slots) {
            if (slot.getCodiProducte().equals(codiProducte)) {
                System.out.print("Introdueix la nova quantitat : ");
                int quantitat = escaner.nextInt();
                slot.setQuantitat(quantitat);
                slotDAO.updateSlot(slot);
                System.out.printf("\nS'ha modificat l'estoc del producte %s a %d unitats%n", slot.getCodiProducte(), slot.getQuantitat());
                existeixProducte = true;
            }
        }
        if (!existeixProducte) {
            System.out.println("\nNo existeix el producte");
        }

    }

    private static void afegirRanures() throws SQLException {

        System.out.print("Introdueix la posició de la nova ranura (número): ");
        int posicio = escaner.nextInt();

        System.out.print("Introdueix el codi del producte: ");
        String codi_producte = escaner.next();

        System.out.print("Introdueix la quantitat: ");
        int quantitat = escaner.nextInt();


        ArrayList<Slot> slots = slotDAO.readSlots();

        // Verifiquem que no existeixi una ranura amb el mateix codi de producte
        for (Slot slot : slots) {
            if (slot.getCodiProducte().equals(codi_producte)) {
                System.out.println("\nJa existeix una ranura amb aquest producte");
                return;
            } else if (slot.getPosicio() == posicio) {
                System.out.println("\nJa existeix una ranura en aquesta posició");
                return;
            }
        }

        // Verifiquem que existeixi el producte
        ArrayList<Producte> productes = ProducteDAO.readProductes();
        boolean producteExists = false;
        for (Producte producte : productes) {
            if (producte.getCodiProducte().equals(codi_producte)) {
                producteExists = true;
                break;
            }
        }
        if (!producteExists) {
            System.out.printf("\nEl producte amb el codi %s no existeix %n", codi_producte);
            System.out.print("Productes disponibles: ");
            for (Producte producte : productes) {
                System.out.printf("%s, ", producte.getCodiProducte());
            }
            System.out.println();
            return;
        }

        // Creem la ranura
        Slot slot = new Slot();
        slot.setPosicio(posicio);
        slot.setQuantitat(quantitat);
        slot.setCodiProducte(codi_producte);

        try {
            slotDAO.createSlot(slot);
            System.out.printf("\nS'ha afegit una ranura a la posició %d amb el producte %s i quantitat %d %n",
                    posicio, codi_producte, quantitat);
        } catch (SQLException e) {
            System.out.printf("\nNo s'ha pogut afegir la ranura a la posició %d %n", posicio);
            e.printStackTrace();
        }

    }

    private static void afegirProductes() {

        try {
            System.out.println("\n======PRODUCTE======");
            System.out.print("Codi Producte : ");
            String codiProducte = escaner.next();

            Producte producte = ProducteDAO.readProducte(codiProducte); // comprovar si existeix el producte

            if (producte != null) {

                System.out.println("\nEl producte ja existeix en la base de dades:");
                System.out.println(producte);

                // Si existeix, preguntar si es vol actualitzar
                System.out.print("\nVols actualitzar el producte? (S/N) : ");
                String resposta = escaner.next();

                if (resposta.equalsIgnoreCase("S")) {

                    // afegir nom i descripció sense utilitzar espais
                    System.out.print("Nom : ");
                    String nom = escaner.next();

                    System.out.print("Descripció : ");
                    String descripcio = escaner.next();

                    System.out.print("Preu Compra : ");
                    float preuCompra = escaner.nextFloat();

                    System.out.print("Preu Venda : ");
                    float preuVenta = escaner.nextFloat();

                    producte.setNom(nom);
                    producte.setDescripcio(descripcio);
                    producte.setPreuCompra(preuCompra);
                    producte.setPreuVenta(preuVenta);

                    ProducteDAO.updateProducte(producte);

                    System.out.println("\nProducte actualitzat correctament.");
                } else {
                    System.out.println("\nOperació descartada.");
                }
            } else {

                // afegir nom i descripció sense espais
                System.out.print("Nom : ");
                String nom = escaner.next();

                System.out.print("Descripció : ");
                String descripcio = escaner.next();

                System.out.print("Preu Compra : ");
                float preuCompra = escaner.nextFloat();

                System.out.print("Preu Venda : ");
                float preuVenta = escaner.nextFloat();

                Producte nouProducte = new Producte(codiProducte, nom, descripcio, preuCompra, preuVenta);
                ProducteDAO.createProducte(nouProducte);

                System.out.println("\nProducte afegit correctament.");
            }
        } catch (Exception e) {
            System.err.println("\nError al afegir el producte : " + e.getMessage());
        }

    }

    private static void mostrarInventari() {

        try {
            // Agafem tots els productes de la BD i els mostrem
            ArrayList<Producte> productes = ProducteDAO.readProductes();

            System.out.println("Codi      Nom                 Descripció                    Preu Compra   Preu Venta");
            System.out.println("------------------------------------------------------------------------------------");

            // String.format() -> per mostrar els valors amb un format concret
            for (Producte prod : productes) {
                String codi = String.format("%-10s", prod.getCodiProducte());
                String nom = String.format("%-20s", prod.getNom());
                String descripcio = String.format("%-30s", prod.getDescripcio());
                String preuCompra = String.format("%-14s", prod.getPreuCompra());
                String preuVenta = String.format("%-12s", prod.getPreuVenta());

                System.out.println(codi + nom + descripcio + preuCompra + preuVenta);
            }
        } catch (SQLException e) {
            System.out.println("Error al llegir els productes.");
            e.printStackTrace();
        }

    }

    private static void comprarProducte() throws SQLException {

        ArrayList<Slot> slots = slotDAO.readSlots();

        System.out.println("\n================PRODUCTES================");
        System.out.printf("%-12s %-20s %-10s %n", "ID", "Nom", "Preu (€)");
        System.out.println("------------------------------------------");
        for (Slot slot : slots) {
            Producte producte = ProducteDAO.readProducte(slot.getCodiProducte());
            System.out.printf("%-12s %-20s %-10.2f %n", slot.getCodiProducte(), producte.getNom(), producte.getPreuVenta());
        }

        // s'entra per ID
        System.out.print("\nQuin producte vols comprar? (per la seva ID) : ");
        String nomProducte = escaner.next();

        // Comprovar que el producte és vàlid i té estoc
        Producte producte = ProducteDAO.readProducte(nomProducte);
        if (producte == null) {
            System.out.println("\nEl producte no es troba disponible a la màquina.");
            return;
        }

        Slot slot = slotDAO.readSlot(producte.getCodiProducte());
        if (slot == null) {
            System.out.println("\nEl producte no es troba disponible a la màquina.");
            return;
        }
        if (slot.getQuantitat() == 0) {
            System.out.println("\nEl producte està esgotat.");
            return;
        }

        // Vendre el producte i actualitzar la quantitat de la ranura
        slot.setQuantitat(slot.getQuantitat() - 1);
        slotDAO.updateSlot(slot);

        productesComprats.add(slot.getCodiProducte()); // afegim el producte a la llista de productes comprats

        System.out.println("\nProducte comprat correctament.");

    }

    public static void mostrarMaquina() {

        ArrayList<Slot> slots;

        try {
            SlotDAO slotDAO = new SlotDAO_MySQL();
            slots = slotDAO.readSlots();

            System.out.printf("\n%-15s %-25s %s%n", "Posicio", "Producte", "Quantitat disponible");
            System.out.println("===========================================================");

            // Imprimir les dades de cada slot
            for (Slot slot : slots) {
                ProducteDAO producteDAO = new ProducteDAO_MySQL();
                Producte producte = producteDAO.readProducte(slot.getCodiProducte());

                String nomProducte;
                if (producte == null) {
                    nomProducte = "Producte no trobat";
                } else {
                    nomProducte = producte.getNom();
                }

                System.out.printf("%-15s %-25s %s%n", slot.getPosicio(), nomProducte, slot.getQuantitat());
            }
        } catch (SQLException e) {
            System.out.println("S'ha produït un error al accedir a la base de dades.");
            System.out.println(e);
        }

    }

    private static void mostrarMenu() {

        System.out.println("\nMenú de la màquina expenedora");
        System.out.println("=============================");
        System.out.println("Selecciona la operació a realitzar introduïnt el número corresponent: \n");


        //Opcions per client / usuari
        System.out.println("[1] Mostrar Posició / Nom producte / Stock de la màquina");
        System.out.println("[2] Comprar un producte");

        //Opcions per administrador / manteniment
        System.out.println();
        System.out.println("[10] Mostrar llistat productes disponibles (BD)");
        System.out.println("[11] Afegir productes disponibles");
        System.out.println("[12] Assignar productes / stock a la màquina");
        System.out.println("[13] Mostrar benefici");

        System.out.println();
        System.out.println("[-1] Sortir de l'aplicació");

    }

    private static void mostrarBenefici() throws SQLException {

        double beneficiTotal = 0.0;

        System.out.println("========== PRODUCTES COMPRATS ==========");
        System.out.printf("%-20s %-10s %n", "Producte", "Benefici (€)");
        System.out.println("--------------------------------------");

        for (String codiProducte : productesComprats) {
            Producte producte = ProducteDAO.readProducte(codiProducte);
            double preuVenta = producte.getPreuVenta();
            double costCompra = producte.getPreuCompra();
            double beneficiProducte = preuVenta - costCompra;

            System.out.printf("%-20s %-10.2f %n", producte.getNom(), beneficiProducte);
            beneficiTotal += beneficiProducte;
        }

        System.out.println("--------------------------------------");
        System.out.printf("%-20s %-10.2f %n", "Benefici total", beneficiTotal, "€");

    }

}