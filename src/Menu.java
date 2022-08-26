import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final Integer OP = 5;

    private static final String MENU = ANSI_RED + "\t\tBenvenuto in RistorApp!" +  ANSI_RESET;
    private static final String OPZIONI = "Menu:"
            + "\n\t1- Visualizza Dati tabella"
            + "\n\t2- Inserisci nuovi dati"
            + "\n\t3- Elimina dati"
            + "\n\t4- Aggiorna dati"
            + "\n\t5- Altre Operazioni"
            + "\n\t0- Exit"
            + "\nInserire scelta: ";

    private Integer scelta;
    private Scanner scan;

    public Menu(){
        scelta = 0;
        scan = new Scanner((System.in));
    }

    public void avvio() {
        System.out.println(MENU);
        System.out.print(OPZIONI);

        do {
            scelta = controllo_opzioni(scan.nextLine(), OP);
        }
        while (scelta == -1);
    }


    public static Integer controllo_opzioni(String input, Integer op) {
        Integer i = -1;

        try {
            i = Integer.parseInt(input);

            if(0 < i && i > op) {
                System.out.print(ANSI_RED + "\tATTENZIONE OPZIONE INESISTENTE \nReinserire: " + ANSI_RESET);
                return -1;
            }

        }
        catch (Exception e) {
            System.out.print(ANSI_RED + "\tATTENZIONE ERRORE DI INSERIMENTO, " + e.getMessage().toLowerCase() + "\nReinserire: " + ANSI_RESET);
            return -1;
        }
        return i;
    }

    public void operazione_scelta(Dbms database) throws SQLException {
        switch (scelta){
            case 1: database.select();
                break;
            case 2: database.insert();
                break;
            case 3: database.delete();
                break;
            case 4: database.update();
                break;
            case 5: database.operazioni();
                break;
            case 0: return;
            default:
                throw new IllegalStateException("Unexpected value: " + scelta);
        }
    }

    public int getScelta() {
        return scelta;
    }

    public void setScelta(Integer s){
        scelta = s;
    }

    public boolean fine(){
        if (scelta == 0){
            scan.close();
            System.out.print("\n\t\tArrivederci!\n\n\n\n\n");
            return false;
        }

        System.out.print("\033[H\033[2J");
        System.out.flush();
        return true;
    }
}