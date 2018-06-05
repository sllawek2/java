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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Admin
 */
public class JavaFXApplication3 extends Application {

    @Override
    public void start(Stage primaryStage) {

        m_primaryStage = primaryStage;
        m_menu = utworzSceneMenu();
        formularzDodawania = new Formularz("Dodaj", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (formularzDodawania.pesel.getText().isEmpty()
                        || formularzDodawania.imie.getText().isEmpty()
                        || formularzDodawania.nazwisko.getText().isEmpty()
                        || formularzDodawania.nrKonta.getText().isEmpty()
                        || formularzDodawania.stanowisko.getText().isEmpty()) {

                } else {
                    boolean ret =
                    baza.dodaj(new Pracownik(Long.parseLong(formularzDodawania.pesel.getText()),
                            formularzDodawania.imie.getText(),
                            formularzDodawania.nazwisko.getText(),
                            Integer.parseInt(formularzDodawania.nrKonta.getText()),
                            formularzDodawania.stanowisko.getText()));
                    if(ret)
                    {
                        m_primaryStage.setScene(m_menu);
                    }
                }

            }
        });
        formularzSzukania = new Formularz("Szukaj", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                insert();
            }
        });
        formularzEdycji = new Formularz("Zapisz zmiany", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                insert();
            }
        });
        m_scenaDodawania = formularzDodawania.utworzScene();
        m_scenaSzukania = formularzSzukania.utworzScene();
        m_scenaEdycji = formularzEdycji.utworzScene();

        m_primaryStage.setTitle("Pracownicy");
        m_primaryStage.setScene(m_menu);
        m_primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }
    private Formularz formularzDodawania;
    private Formularz formularzSzukania;
    private Formularz formularzEdycji;
    private Stage m_primaryStage;
    private Scene m_menu;
    private Scene m_scenaDodawania;
    private Scene m_scenaSzukania;
    private Scene m_scenaEdycji;
    private BazaDanych baza = new BazaDanych();

    private Scene utworzSceneMenu() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(15, 12, 15, 12));
        vbox.setSpacing(8);

        Button dodajBtn = new Button();
        dodajBtn.setText("Dodaj pracownika");
        dodajBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                m_primaryStage.setScene(m_scenaDodawania);
            }
        });

        Button szukajBtn = new Button();
        szukajBtn.setText("Szukaj pracownika");
        szukajBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                m_primaryStage.setScene(m_scenaSzukania);
            }
        });
        Button edytujBtn = new Button();
        edytujBtn.setText("Edytuj dane pracownika");
        edytujBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                m_primaryStage.setScene(m_scenaEdycji);
            }
        });
        Button usunBtn = new Button();
        usunBtn.setText("Usuń pracownika");
        usunBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                 m_primaryStage.setScene(utworzScene);
            }
        });

        vbox.getChildren().addAll(dodajBtn, szukajBtn, edytujBtn, usunBtn);
        return new Scene(vbox, 300, 250);
    }

    //dostęp do bazy danych   
    public static void select() {
        Connection conn = null;
        Statement stmt = null;
        try {
            System.out.println("Registering database...");
            //STEP 2: Register JDBC driver
            Class.forName("org.sqlite.JDBC");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection("jdbc:sqlite:baza_pracownikow.db");

            //STEP 4: Execute a query
            System.out.println("Creating database...");
            stmt = conn.createStatement();

            String sql = "select * from pracownicy";
            ResultSet rs = stmt.executeQuery(sql);

            VBox vLista = new VBox();
            vLista.setPadding(new Insets(15, 12, 15, 12));
            vLista.setSpacing(8);

            while (rs.next()) {
                long pesel = rs.getLong("pesel");
                String imie = rs.getString("imie");
                String nazwisko = rs.getString("nazwisko");
                String stanowisko = rs.getString("stanowisko");
                int nrKonta = rs.getInt("nr_konta");

                HBox hPola = new HBox();
                Label label = new Label("pesel = " + pesel);
                label.setWrapText(true);
                hPola.getChildren().add(label);

                Label label2 = new Label("imie = " + imie);
                label2.setWrapText(true);
                hPola.getChildren().add(label2);

                vLista.getChildren().add(hPola);
                System.out.println("pesel = " + pesel);
                System.out.println("imie = " + imie);
                System.out.println("nazwisko = " + nazwisko);
                System.out.println("stanowisko = " + stanowisko);
                System.out.println("nr konta = " + nrKonta);
                System.out.println();
            }

            System.out.println("Database created successfully...");
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main

    private class Formularz {

        public Formularz(String akcja, EventHandler<ActionEvent> e) {
            pesel.setPromptText("pesel");
            imie.setPromptText("imie");
            nazwisko.setPromptText("nazwisko");
            nrKonta.setPromptText("nrKonta");
            stanowisko.setPromptText("stanowisko");

            this.akcja.setText(akcja);
            this.akcja.setOnAction(e);
            wroc.setText("Wróć do menu");
            wroc.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    m_primaryStage.setScene(m_menu);
                }
            });
        }

        public Scene utworzScene() {
            VBox formularz = new VBox();
            formularz.setPadding(new Insets(15, 12, 15, 12));
            formularz.setSpacing(8);
            formularz.getChildren().addAll(pesel, imie, nazwisko, nrKonta, stanowisko, akcja, wroc);
            return new Scene(formularz, 300, 250);
        }
        public Button wroc = new Button();
        public Button akcja = new Button();
        public TextField pesel = new TextField();
        public TextField imie = new TextField();
        public TextField nazwisko = new TextField();
        public TextField nrKonta = new TextField();
        public TextField stanowisko = new TextField();
    }

    private class Pracownik {

        Pracownik(long pesel, String imie, String nazwisko, int nrKonta, String stanowisko) {
            this.pesel = pesel;
            this.imie = imie;
            this.nazwisko = nazwisko;
            this.nrKonta = nrKonta;
            this.stanowisko = stanowisko;
        }

        long pesel;
        String imie = new String();
        String nazwisko = new String();
        int nrKonta;
        String stanowisko = new String();
    }

    private class BazaDanych {

        private String sterownik = new String("org.sqlite.JDBC");
        private String nazwaBazy = new String("jdbc:sqlite:baza_pracownikow.db");

        public boolean dodaj(Pracownik p) {
            Connection c = null;
            Statement stmt = null;

            boolean bRet = true;
            try {
                Class.forName(sterownik);
                c = DriverManager.getConnection(nazwaBazy);
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "INSERT INTO pracownicy (pesel,imie,nazwisko,nr_konta,stanowisko) "
                        + "VALUES (" + p.pesel + ", '" + p.imie + "', '" + p.nazwisko + "', " + p.nrKonta + ", '" + p.stanowisko + "' );";
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

        public boolean szukaj(String pesel, String imie, String nazwisko, String nrKonta, String stanowisko) {
            Connection c = null;
            Statement stmt = null;

            boolean bRet = true;
            try {
                Class.forName(sterownik);
                c = DriverManager.getConnection(nazwaBazy);
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "select pesel,imie,nazwisko,nr_konta,stanowisko"
                        + " from pracownicy () "
                        + "where ";
                boolean dodajAnd = false;
                if (!pesel.isEmpty()) {
                    sql += " pesel = " + Long.parseLong(pesel);
                    dodajAnd = true;
                }
                if (!imie.isEmpty()) {
                    if (dodajAnd) {
                        sql += " and ";
                    }
                    sql += " imie = `" + imie + "`";
                    dodajAnd = true;
                }
                if (!nazwisko.isEmpty()) {
                    if (dodajAnd) {
                        sql += " and ";
                    }
                    sql += " nazwisko = `" + nazwisko + "`";
                    dodajAnd = true;
                }
                if (!nrKonta.isEmpty()) {
                    if (dodajAnd) {
                        sql += " and ";
                    }
                    sql += " nrKonta = " + Long.parseLong(nrKonta);
                    dodajAnd = true;
                }
                if (!stanowisko.isEmpty()) {
                    if (dodajAnd) {
                        sql += " and ";
                    }
                    sql += " stanowisko = `" + stanowisko + "`";

                }

                stmt.executeQuery(sql);
                ResultSet rs = stmt.executeQuery(sql);

                VBox vLista = new VBox();
                vLista.setPadding(new Insets(15, 12, 15, 12));
                vLista.setSpacing(8);

                while (rs.next()) {
//                    long pesel = rs.getLong("pesel");
//                    String imie = rs.getString("imie");
//                    String nazwisko = rs.getString("nazwisko");
//                    String stanowisko = rs.getString("stanowisko");
//                    int nrKonta = rs.getInt("nr_konta");

                    HBox hPola = new HBox();
                    Label label = new Label("pesel = " + rs.getLong("pesel"));
                    label.setWrapText(true);
                    hPola.getChildren().add(label);

                    Label label2 = new Label("imie = " + rs.getString("imie"));
                    label2.setWrapText(true);
                    hPola.getChildren().add(label2);

                    vLista.getChildren().add(hPola);
//                    System.out.println("pesel = " + pesel);
//                    System.out.println("imie = " + imie);
//                    System.out.println("nazwisko = " + nazwisko);
//                    System.out.println("stanowisko = " + stanowisko);
//                    System.out.println("nr konta = " + nrKonta);
//                    System.out.println();
                }
             stmt.close();
             
                c.close();

            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                bRet = false;
            }
            return bRet;
        }

        public void edytuj(Pracownik p) {

        }

        public void usun(Pracownik p) {

        }

        private void utworzPolaczenie() {

        }
    }
}

//
// 1. narysować przyciski
// 2. napisać jedną metodę do łączenia się z bazą i ona będzie wywoływana na kliknięcie
// 3. wyświetlanie listy
// 4. do usunięcia trzeba podać numer pesel
// 5. każda akcja powinna wypisąc jakiś komunikat że sie udało albo nie - status bar
//
