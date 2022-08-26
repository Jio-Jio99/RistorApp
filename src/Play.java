import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

/***
 * Questa classe serve per lo più per gestire eventuali errori del database e fare la connessione in una sola parte
 */
public class Play {
    private static final String PASSWORD = "ciao1999database";
    private static final String USER = "postgres";
    private static final String DATABASE = "jdbc:postgresql://localhost:5432/RistorApp";

    public static void main(String[] args) {
        Dbms database;
        Menu menu = new Menu();
        Scanner continua = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            database = new Dbms(connection);

            do {
                menu.avvio();

                try{
                    menu.operazione_scelta(database);
                }
                catch (SQLException e){
                    System.out.println(Menu.ANSI_RED + "\t\t!OPERAZIONE ANNULLATA!\n" + e.getMessage().replace("\n", " ") + Menu.ANSI_RESET);
                    e.printStackTrace();
                }

                if(menu.getScelta() != 0){
                    System.out.print("Premi invio per andare avanti... ");
                    continua.nextLine();
                }
            }
            while(menu.fine());

            database.fine();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
