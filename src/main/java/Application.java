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
    public static void main(String[] args) {

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

                case -1 -> System.out.println("Bye...");
                default -> System.out.println("Opció no vàlida");
            }

        }while(opcio != -1);

    }


    private static void modificarMaquina() {

        /**
         * Ha de permetre:
         *      - modificar les posicions on hi ha els productes de la màquina (quin article va a cada lloc)
         *      - modificar stock d'un producte que hi ha a la màquina
         *      - afegir més ranures a la màquina
         */
    }

    private static void afegirProductes() {

        /**
         *      Crear un nou producte amb les dades que ens digui l'operari
         *      Agefir el producte a la BD (tenir en compte les diferents situacions que poden passar)
         *          El producte ja existeix
         *              - Mostrar el producte que té el mateix codiProducte
         *              - Preguntar si es vol actualitzar o descartar l'operació
         *          El producte no existeix
         *              - Afegir el producte a la BD
         *
         *     Podeu fer-ho amb llenguatge SQL o mirant si el producte existeix i després inserir o actualitzar
         */


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

    private static void comprarProducte() {

        /**
         * Mínim: es realitza la compra indicant la posició on es troba el producte que es vol comprar
         * Ampliació (0.5 punts): es permet entrar el NOM del producte per seleccionar-lo (abans cal mostrar els
         * productes disponibles a la màquina)
         *
         * Tingueu en compte que quan s'ha venut un producte HA DE QUEDAR REFLECTIT a la BD que n'hi ha un menys.
         * (stock de la màquina es manté guardat entre reinicis del programa)
         */

    }

    private static void mostrarMaquina() {

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

    private static void mostrarBenefici() {

        /** Ha de mostrar el benefici de la sessió actual de la màquina, cada producte té un cost de compra
         * i un preu de venda. La suma d'aquesta diferència de tots productes que s'han venut ens donaran el benefici.
         *
         * Simplement s'ha de donar el benefici actual des de l'últim cop que s'ha engegat la màquina. (es pot fer
         * amb un comptador de benefici que s'incrementa per cada venda que es fa)
         */

        /** AMPLIACIÓ **
         * En entrar en aquest menú ha de permetre escollir entre dues opcions: veure el benefici de la sessió actual o
         * tot el registre de la màquina.
         *
         * S'ha de crear una nova taula a la BD on es vagi realitzant un registre de les vendes o els beneficis al
         * llarg de la vida de la màquina.
         */
    }
}