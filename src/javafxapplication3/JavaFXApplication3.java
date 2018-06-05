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
        m_scenaDodawania = utworzSceneDodawania();
        m_scenaSzukania = utworzSceneSzukania();
        m_scenaEdycji = utworzSceneEdycji();
   
        m_primaryStage.setTitle("Lista pracowników");
        m_primaryStage.setScene(m_menu);
        m_primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
    }
    
    private Stage m_primaryStage;
    private Scene m_menu;
    private Scene m_scenaDodawania;
    private Scene m_scenaSzukania;
    private Scene m_scenaEdycji;
    
    
    private VBox utworzFormularz(){
        VBox formularz = new VBox();
        formularz.setPadding(new Insets(15, 12, 15, 12));
        formularz.setSpacing(8);
        
        String[] labelki = {"pesel: ", "imie: ", "nazwisko: ", "adres: ", "stanowisko: "};
        for(String tekst : labelki)
        {
            Label label = new Label(tekst );
            label.setWrapText(true);
            
            TextField textField = new TextField(); 
            
            HBox hBox = new HBox();
            hBox.getChildren().addAll(label, textField);
            formularz.getChildren().add(hBox);
        }
        return formularz;
    }
    
    private Scene utworzSceneMenu(){
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
    
        vbox.getChildren().addAll(dodajBtn,szukajBtn,edytujBtn,usunBtn);
        return new Scene(vbox, 300, 250);
    }
    
    
    //rysowanie scen
    private Scene utworzSceneDodawania(){
        VBox v = utworzFormularz();
        Button b= new Button();
        b.setText("Zapisz");
        b.setOnAction(new EventHandler<ActionEvent>() {         
            @Override
            public void handle(ActionEvent event) {
                 insert();
            }
        });
        
        Button b2= new Button();
        b2.setText("Wróć do menu");
        b2.setOnAction(new EventHandler<ActionEvent>() {         
            @Override
            public void handle(ActionEvent event) {
                m_primaryStage.setScene(m_menu);
            }
        });
        
        v.getChildren().addAll(b, b2);
        return new Scene(v, 300, 250);
    }
    
    private Scene utworzSceneSzukania(){
        VBox v = utworzFormularz();
        Button b1= new Button();
        b1.setText("Szukaj");
        b1.setOnAction(new EventHandler<ActionEvent>() {         
            @Override
            public void handle(ActionEvent event) {
//                 insert();
            }
        });
        
        Button b2= new Button();
        b2.setText("Wróć do menu");
        b2.setOnAction(new EventHandler<ActionEvent>() {         
            @Override
            public void handle(ActionEvent event) {
                m_primaryStage.setScene(m_menu);
            }
        });
        
        v.getChildren().addAll(b1,b2);
        return new Scene(v, 300, 250);
    }
    
    private Scene utworzSceneEdycji(){
        VBox v = utworzFormularz();
        Button b= new Button();
        b.setText("Zapisz");
        b.setOnAction(new EventHandler<ActionEvent>() {         
            @Override
            public void handle(ActionEvent event) {
//                 insert();
            }
        });
        
        Button b2= new Button();
        b2.setText("Wróć do menu");
        b2.setOnAction(new EventHandler<ActionEvent>() {         
            @Override
            public void handle(ActionEvent event) {
                m_primaryStage.setScene(m_menu);
            }
        });
        
        v.getChildren().addAll(b, b2);
        return new Scene(v, 300, 250);
    }
    
    
    
 //dostęp do bazy danych   
    public static void insert() {
        Connection c = null;
      Statement stmt = null;
      
      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:baza_pracownikow.db");
         c.setAutoCommit(false);
         System.out.println("Opened database successfully");

         stmt = c.createStatement();
         String sql = "INSERT INTO pracownicy (pesel,imie,nazwisko,nr_konta,stanowisko) " +
                        "VALUES (88111112345, 'Paul', 'Mac', 123456789, 'bankier' );"; 
         stmt.executeUpdate(sql);

        
         stmt.close();
         c.commit();
         c.close();
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      System.out.println("Records created successfully");
    }
   
    public static void select() {
   Connection conn = null;
   Statement stmt = null;
   try{
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
      
      while ( rs.next() ) {
         long pesel = rs.getLong("pesel");
         String  imie = rs.getString("imie");
         String  nazwisko = rs.getString("nazwisko");
         String  stanowisko = rs.getString("stanowisko");
         int nrKonta = rs.getInt("nr_konta");

         HBox hPola = new HBox();
         Label label = new Label("pesel = " + pesel );
         label.setWrapText(true);
         hPola.getChildren().add(label);
         
                  Label label2 = new Label("imie = " + imie );
         label2.setWrapText(true);
         hPola.getChildren().add(label2);
         
         
         vLista.getChildren().add(hPola);
         System.out.println( "pesel = " + pesel );
         System.out.println( "imie = " + imie );
         System.out.println( "nazwisko = " + nazwisko );
         System.out.println( "stanowisko = " + stanowisko );
         System.out.println( "nr konta = " + nrKonta );
         System.out.println();
      }
      
      System.out.println("Database created successfully...");
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   System.out.println("Goodbye!");
}//end main
   
    private class Pracownik{
        String pesel;
        String imie;
        String nazwisko;
        String adres;
        String stanowisko;
    }
    
    private class BazaDanych{
        public void dodaj(Pracownik p){
            
        }
        public void szukaj(Pracownik p){
            
        }
        
        public void edytuj(Pracownik p){
            
        }
        
        public void usun(Pracownik p){
            
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
