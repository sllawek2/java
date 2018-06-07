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
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
                    formularzDodawania.status.setText("wypełnij wszystkie pola");
                } else {
                    boolean ret = baza.dodaj(formularzDodawania.pesel.getText(),
                            formularzDodawania.imie.getText(),
                            formularzDodawania.nazwisko.getText(),
                            formularzDodawania.nrKonta.getText(),
                            formularzDodawania.stanowisko.getText());
                    if (ret) {
                        formularzDodawania.status.setText("Pomyslnie dodano do bazy");
                    } else {
                        formularzDodawania.status.setText("Error");
                    }
                }

            }
        });
        formularzSzukania = new Formularz("Szukaj", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane wynik = baza.szukaj(formularzSzukania.pesel.getText(),
                        formularzSzukania.imie.getText(),
                        formularzSzukania.nazwisko.getText(),
                        formularzSzukania.nrKonta.getText(),
                        formularzSzukania.stanowisko.getText());
                borderSzukania.setCenter(wynik);

            }
        });

        //zakładka dodawania
        TabPane tabPane = new TabPane();
        Tab tab = new Tab();
        tab.setText("Dodaj pracownika");
        tab.setContent(formularzDodawania.utworzVBox());

        //zakładka szukania
        borderSzukania.setTop(formularzSzukania.utworzVBox());

        Tab tab1 = new Tab();
        tab1.setText("Szukaj pracownika");
        tab1.setContent(borderSzukania);

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
        Scene root = new Scene(tabPane, 800, 600);

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
    BorderPane borderSzukania = new BorderPane();

    private class Formularz {

        public Formularz(String akcja, EventHandler<ActionEvent> e) {
            pesel.setPromptText("pesel");
            imie.setPromptText("imie");
            nazwisko.setPromptText("nazwisko");
            nrKonta.setPromptText("nrKonta");
            stanowisko.setPromptText("stanowisko");

            this.akcja.setText(akcja);
            this.akcja.setOnAction(e);

            status.setEditable(false);
        }

        public VBox utworzVBox() {
            VBox formularz = new VBox();
            formularz.setPadding(new Insets(15, 12, 15, 12));
            formularz.setSpacing(8);
            formularz.getChildren().addAll(pesel, imie, nazwisko, nrKonta, stanowisko, akcja, status);

            return formularz;
        }
        public Button akcja = new Button();
        public TextField pesel = new TextField();
        public TextField imie = new TextField();
        public TextField nazwisko = new TextField();
        public TextField nrKonta = new TextField();
        public TextField stanowisko = new TextField();
        public TextField status = new TextField();
    }

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

        public GridPane szukaj(String pesel, String imie, String nazwisko, String nrKonta, String stanowisko) {
            Connection c = null;
            Statement stmt = null;
            GridPane vLista = new GridPane();
            vLista.setHgap(10);
            vLista.setVgap(10);

            vLista.setPadding(new Insets(10, 10, 10, 10));

            int nrWiersza = 1;

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
                    sql += " imie = '" + imie + "'";
                    dodajAnd = true;
                }
                if (!nazwisko.isEmpty()) {
                    if (dodajAnd) {
                        sql += " and ";
                    } else {
                        sql += " where ";
                    }
                    sql += " nazwisko = '" + nazwisko + "'";
                    dodajAnd = true;
                }
                if (!nrKonta.isEmpty()) {
                    if (dodajAnd) {
                        sql += " and ";
                    } else {
                        sql += " where ";
                    }
                    sql += " nr_konta = " + Long.parseLong(nrKonta);
                    dodajAnd = true;
                }
                if (!stanowisko.isEmpty()) {
                    if (dodajAnd) {
                        sql += " and ";
                    } else {
                        sql += " where ";
                    }
                    sql += " stanowisko = '" + stanowisko + "'";

                }

                stmt.executeQuery(sql);
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    Label label1 = new Label(String.valueOf(rs.getLong("pesel")));
                    Label label2 = new Label(rs.getString("imie"));
                    Label label3 = new Label(rs.getString("nazwisko"));
                    Label label4 = new Label(String.valueOf(rs.getLong("nr_konta")));
                    Label label5 = new Label(rs.getString("stanowisko"));

                    vLista.add(label1, 0, nrWiersza);
                    vLista.add(label2, 1, nrWiersza);
                    vLista.add(label3, 2, nrWiersza);
                    vLista.add(label4, 3, nrWiersza);
                    vLista.add(label5, 4, nrWiersza);

                    nrWiersza++;
                }
                stmt.close();

                c.close();

            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());

            }

            if (nrWiersza == 1)//nie ma żadnego wyniku
            {
                Label status = new Label("zapytanie nic nie zwróciło");
                vLista.add(status, 0, 0, 5, 1);
            } else {
                Label l1 = new Label("pesel");
                Label l2 = new Label("imie");
                Label l3 = new Label("nazwisko");
                Label l4 = new Label("nr konta");
                Label l5 = new Label("stanowisko");
                vLista.add(l1, 0, 0);
                vLista.add(l2, 1, 0);
                vLista.add(l3, 2, 0);
                vLista.add(l4, 3, 0);
                vLista.add(l5, 4, 0);
            }
            return vLista;
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
