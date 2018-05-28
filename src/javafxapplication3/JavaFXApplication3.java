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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.*;

/**
 *
 * @author Admin
 */
public class JavaFXApplication3 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button selectBtn = new Button();
        selectBtn.setText("select");
        selectBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
//                System.out.println("Hello World!");
                
                
                select();
            }
        });
        
        Button updateBtn = new Button();
        updateBtn.setText("update");
        updateBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
//                update();
            }
        });
        
        Button insertBtn = new Button();
        insertBtn.setText("update");
        insertBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                insert();
            }
        });
        
        
        StackPane root = new StackPane();
        root.getChildren().add(selectBtn);
//        root.getChildren().add(updateBtn);
//        root.getChildren().add(insertBtn);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
    }
    
    
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
      
      
      while ( rs.next() ) {
         long pesel = rs.getLong("pesel");
         String  imie = rs.getString("imie");
         String  nazwisko = rs.getString("nazwisko");
         String  stanowisko = rs.getString("stanowisko");
         int nrKonta = rs.getInt("nr_konta");

         
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
}
//
// 1. narysować przyciski
// 2. napisać jedną metodę do łączenia się z bazą i ona będzie wywoływana na kliknięcie
// 3. wyświetlanie listy
// 4. do usunięcia trzeba podać numer pesel
// 5. każda akcja powinna wypisąc jakiś komunikat że sie udało albo nie - status bar
//
