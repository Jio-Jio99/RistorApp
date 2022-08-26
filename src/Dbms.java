import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Dbms {
    private static String prendi_nomi_tabelle = "select table_name from information_schema.tables where table_schema = 'public' AND table_type = 'BASE TABLE';";
    //Per selezionare la tabella fare replace("!", nome_tabella)
    private static String colonne_tabella = "select column_name, column_default, data_type, character_maximum_length from information_schema.columns where table_name = '!';";

    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData rsmd;
    private static final String DIV = Menu.ANSI_BLUE + "\t-\t" + Menu.ANSI_RESET;
    private Scanner scan;
    private ArrayList<String> lista_tabelle;

    public Dbms(Connection connection) throws SQLException {
        statement = connection.createStatement();
        scan = new Scanner(System.in);
        resultSet = statement.executeQuery(prendi_nomi_tabelle);
        lista_tabelle = new ArrayList<String>();

        while(resultSet.next())
            lista_tabelle.add(resultSet.getString(1));
    }

    //METODI PER IL DATABASE
    public void insert() throws SQLException{
        String column;
        StringBuilder tabella;
        StringBuilder valori = new StringBuilder().append("(");
        int flag = 0;
        String query = "insert into x values y";
        String input = "";

        System.out.print("\n-Digitare la tabella in cui inserire: ");
        do{
            tabella = new StringBuilder(scan.nextLine().strip().toLowerCase().replaceAll(" ", "_"));
            flag = check_tabella(tabella.toString());
        }
        while(flag == 1);

        if(flag == -1)
            return;

        sql_result(colonne_tabella.replace("!", tabella));
        tabella.append("(");

        while (resultSet.next()){
            column =  resultSet.getString(1);
            tabella.append(column).append(", ");
            System.out.print("\t-Inserire " + column + ": ");
            input = scan.nextLine();

            column = resultSet.getString(3);

            if (!(column.equals("integer") || column.equals("smallint") || column.equals("float") || column.equals("real")))
                input = '\'' + input + '\'';

            valori.append(input).append(", ");
        }

        tabella.delete(tabella.length()-2, tabella.length()).append(")");
        valori.delete(valori.length()-2, valori.length()).append(");");

        statement.executeUpdate(query.replace("x", tabella).replace("y", valori));
        System.out.println(Menu.ANSI_RED + "\t\t" + "Inserito" + Menu.ANSI_RESET);
    }

    public void update() throws  SQLException{
        String query = "update x set y where z;";
        int flag = 0;
        StringBuilder tabella = new StringBuilder();
        String set = "";
        String condizione = "";

        System.out.print("\n-Digitare la tabella in cui aggiornare i dati: ");
        do{
            tabella = new StringBuilder(scan.nextLine().strip().toLowerCase().replaceAll(" ", "_"));
            flag = check_tabella(tabella.toString());
        }
        while(flag == 1);

        if(flag == -1)
            return;

        System.out.print("\t-Inserire le colonne con i nuovi valori: ");
        set = scan.nextLine().strip();

        System.out.print("\t-Inserire la condizione per l'aggiornamento: ");
        condizione = scan.nextLine().strip();

        query = query.replace("x",tabella).replace("y", set).replace("z",condizione);
        statement.executeUpdate(query);
        System.out.println(Menu.ANSI_RED + "\n\t\tAggiornamento effettuato" + Menu.ANSI_RESET);
    }

    public void delete() throws SQLException{
        String query = "delete from x where y";
        StringBuilder tabella = new StringBuilder();
        String condizione = "";
        int flag;

        System.out.print("\n-Digitare la tabella in cui eliminare: ");
        do{
            tabella = new StringBuilder(scan.nextLine().strip().toLowerCase().replaceAll(" ", "_"));
            flag = check_tabella(tabella.toString());
        }
        while(flag == 1);

        if(flag == -1)
            return;

        System.out.print("\t-Inserire la condizione (colonna = valore_per_cui_eliminare): ");
        condizione = scan.nextLine().strip();

        query = query.replace("x", tabella).replace("y", condizione);
        statement.executeUpdate(query);
        System.out.println(Menu.ANSI_RED + "\t\tEliminato" +  Menu.ANSI_RESET);
    }

    public void operazioni() {

    }

    public void select() throws SQLException {
        String tabella = "";
        String query = "select * from x";
        int flag = 0;
        System.out.println(Menu.ANSI_BLUE + "  Tabelle disponibili:" + Menu.ANSI_RESET);
        lista_tabelle.stream().forEachOrdered(x -> System.out.println("\t\t- " + x));

        do{
            System.out.print("Inserire la tabella da visualizzare: ");
            tabella = scan.nextLine().strip().toLowerCase().replaceAll(" ", "_");
            flag = check_tabella(tabella);
        }
        while(flag == 1);

        if(flag == -1)
            return;

        sql_result(query.replace("x", tabella));
        System.out.println("\n" + result_toString());
    }

    private Integer check_tabella(String nome){
        if(nome.compareTo("exit") == 0)
            return -1;

        if(lista_tabelle.stream().anyMatch(x -> x.compareTo(nome) == 0))
            return 0;
        else
            System.out.println("\tAttenzione! Il nome della tabella da lei inserita o non esiste o non è stato digitato correttamente! Riprovare");

        return  1;
    }

    public void sql_result(String query) throws SQLException {
        resultSet = statement.executeQuery(query);
        rsmd = resultSet.getMetaData();
    }

    private String result_toString() throws SQLException {
        int columnsNumber = rsmd.getColumnCount();
        String finale = "\t";
        int r = 0;

        for (int i = 1; i <= columnsNumber; i++) {
            finale += Menu.ANSI_BLUE + rsmd.getColumnName(i).toUpperCase().replace("_", " ");
            if(i != columnsNumber)
                finale += DIV;
        }

        finale += Menu.ANSI_RESET + "\n";
        while (resultSet.next()) {
            r++;
            finale += r + ")\t";

            for (int i = 1; i <= columnsNumber; i++){
                finale += resultSet.getString(i);
                if(i != columnsNumber)
                    finale += DIV;
            }

            finale += "\n";
        }

        return finale;
    }

    public void fine(){
        scan.close();
    }

}
