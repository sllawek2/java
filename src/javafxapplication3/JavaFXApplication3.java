/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication3;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.sql.*;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Admin
 */
public class JavaFXApplication3 extends Application {

    @Override
    public void start(Stage primaryStage) {

//        m_primaryStage = primaryStage;
        //       m_menu = utworzSceneMenu();
        formularzDodawania = new Formularz("Dodaj", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (formularzDodawania.pesel.getText().isEmpty()
                        || formularzDodawania.imie.getText().isEmpty()
                        || formularzDodawania.nazwisko.getText().isEmpty()
                        || formularzDodawania.nrKonta.getText().isEmpty()
                        || formularzDodawania.stanowisko.getText().isEmpty()) {

                } else {
                    boolean ret = baza.dodaj(formularzDodawania.pesel.getText(),
                            formularzDodawania.imie.getText(),
                            formularzDodawania.nazwisko.getText(),
                            formularzDodawania.nrKonta.getText(),
                            formularzDodawania.stanowisko.getText());
                    if (ret) {
                        formularzDodawania.statusArea.setText("Pomyslnie dodano do bazy");
                    } else {
                        formularzDodawania.statusArea.setText("Error");
                    }
                }

            }
        });
        formularzSzukania = new Formularz("Szukaj", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String wynik = baza.szukaj(formularzSzukania.pesel.getText(),
                        formularzSzukania.imie.getText(),
                        formularzSzukania.nazwisko.getText(),
                        formularzSzukania.nrKonta.getText(),
                        formularzSzukania.stanowisko.getText());

                formularzSzukania.statusArea.setText(wynik);

            }
        });

        //zakładka dodawania
        TabPane tabPane = new TabPane();
        Tab tab = new Tab();
        tab.setText("Dodaj pracownika");
        tab.setContent(formularzDodawania.utworzVBox());

        //zakładka szukania
        Tab tab1 = new Tab();
        tab1.setText("Szukaj pracownika");
        tab1.setContent(formularzSzukania.utworzVBox());

        //zakładka usuwania
        TextArea statusUsun = new TextArea();
        statusUsun.setEditable(false);

        TextField peselUsun = new TextField();
        peselUsun.setPromptText("pesel do usuniecia");
        Button usunBtn = new Button();
        usunBtn.setText("Usun");
        usunBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean ret = baza.usun(peselUsun.getText());
                if (ret) {
                    statusUsun.setText("usunieto pomyslnie");
                } else {
                    statusUsun.setText("blad podczas usuwania");
                }
            }
        });
        VBox vUsun = new VBox();
        vUsun.setPadding(new Insets(15, 12, 15, 12));
        vUsun.setSpacing(8);

        vUsun.getChildren().addAll(peselUsun, usunBtn, statusUsun);

        Tab tab2 = new Tab();
        tab2.setText("Usun pracownika");
        tab2.setContent(vUsun);
        tabPane.getTabs().addAll(tab, tab1, tab2);
        Scene root = new Scene(tabPane, 400, 600);

        primaryStage.setTitle("Pracownicy");
        primaryStage.setScene(root);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }
    private Formularz formularzDodawania;
    private Formularz formularzSzukania;
    private BazaDanych baza = new BazaDanych();

    private class Formularz {

        public Formularz(String akcja, EventHandler<ActionEvent> e) {
            pesel.setPromptText("pesel");
            imie.setPromptText("imie");
            nazwisko.setPromptText("nazwisko");
            nrKonta.setPromptText("nrKonta");
            stanowisko.setPromptText("stanowisko");

            this.akcja.setText(akcja);
            this.akcja.setOnAction(e);

            statusArea.setEditable(false);
        }

        public VBox utworzVBox() {
            VBox formularz = new VBox();
            formularz.setPadding(new Insets(15, 12, 15, 12));
            formularz.setSpacing(8);
            formularz.getChildren().addAll(pesel, imie, nazwisko, nrKonta, stanowisko, akcja, statusArea);

            return formularz;
        }
        public Button akcja = new Button();
        public TextField pesel = new TextField();
        public TextField imie = new TextField();
        public TextField nazwisko = new TextField();
        public TextField nrKonta = new TextField();
        public TextField stanowisko = new TextField();
        public TextArea statusArea = new TextArea();
    }

//    private class Pracownik {
//
//        Pracownik(long pesel, String imie, String nazwisko, long nrKonta, String stanowisko) {
//            this.pesel = pesel;
//            this.imie = imie;
//            this.nazwisko = nazwisko;
//            this.nrKonta = nrKonta;
//            this.stanowisko = stanowisko;
//        }
//
//        long pesel;
//        String imie = new String();
//        String nazwisko = new String();
//        long nrKonta;
//        String stanowisko = new String();
//    }
    private class BazaDanych {

        private String sterownik = new String("org.sqlite.JDBC");
        private String nazwaBazy = new String("jdbc:sqlite:baza_pracownikow.db");

        public boolean dodaj(String pesel, String imie, String nazwisko, String nrKonta, String stanowisko) {
            Connection c = null;
            Statement stmt = null;

            boolean bRet = true;
            try {
                Class.forName(sterownik);
                c = DriverManager.getConnection(nazwaBazy);
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "INSERT INTO pracownicy (pesel,imie,nazwisko,nr_konta,stanowisko) "
                        + "VALUES (" + Long.parseLong(pesel) + ", '" + imie + "', '" + nazwisko + "', " + Long.parseLong(nrKonta) + ", '" + stanowisko + "' );";
                stmt.executeUpdate(sql);

                stmt.close();
                c.commit();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                bRet = false;
            }
            return bRet;
        }

        public String szukaj(String pesel, String imie, String nazwisko, String nrKonta, String stanowisko) {
            Connection c = null;
            Statement stmt = null;
            String wynik = new String();

            try {
                Class.forName(sterownik);
                c = DriverManager.getConnection(nazwaBazy);
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "select pesel,imie,nazwisko,nr_konta,stanowisko"
                        + " from pracownicy ";
                boolean dodajAnd = false;
                if (!pesel.isEmpty()) {
                    sql += "where pesel = " + Long.parseLong(pesel);
                    dodajAnd = true;
                }
                if (!imie.isEmpty()) {
                    if (dodajAnd) {
                        sql += " and ";
                    } else {
                        sql += " where ";
                    }
                    sql += " imie = `" + imie + "`";
                    dodajAnd = true;
                }
                if (!nazwisko.isEmpty()) {
                    if (dodajAnd) {
                        sql += " and ";
                    } else {
                        sql += " where ";
                    }
                    sql += " nazwisko = `" + nazwisko + "`";
                    dodajAnd = true;
                }
                if (!nrKonta.isEmpty()) {
                    if (dodajAnd) {
                        sql += " and ";
                    } else {
                        sql += " where ";
                    }
                    sql += " nrKonta = " + Long.parseLong(nrKonta);
                    dodajAnd = true;
                }
                if (!stanowisko.isEmpty()) {
                    if (dodajAnd) {
                        sql += " and ";
                    } else {
                        sql += " where ";
                    }
                    sql += " stanowisko = `" + stanowisko + "`";

                }

                stmt.executeQuery(sql);
                ResultSet rs = stmt.executeQuery(sql);

                VBox vLista = new VBox();
                vLista.setPadding(new Insets(15, 12, 15, 12));
                vLista.setSpacing(8);

                while (rs.next()) {
                    wynik += rs.getLong("pesel") + " ";
                    wynik += rs.getString("imie") + " ";
                    wynik += rs.getString("nazwisko") + " ";
                    wynik += rs.getString("stanowisko") + " ";
                    wynik += rs.getInt("nr_konta");
                    wynik += "\n";

                }
                stmt.close();

                c.close();

            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());

            }
            return wynik;
        }

        public boolean usun(String pesel) {
            Connection c = null;
            Statement stmt = null;

            boolean bRet = true;
            try {
                Class.forName(sterownik);
                c = DriverManager.getConnection(nazwaBazy);
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "DELETE FROM pracownicy WHERE pesel = " + pesel;
                stmt.executeUpdate(sql);

                stmt.close();
                c.commit();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                bRet = false;
            }
            return bRet;
        }

        private void utworzPolaczenie() {

        }
    }
}
