package gitExtraction;

import java.io.Console;
import java.util.Scanner;

public class UserConsole {
    static Scanner scanner = new Scanner(System.in);
    static Console console = System.console();

    public static String setURI() {


        System.out.println("Connecting to repository: Enter URI ");
        String URI = scanner.nextLine();

    return URI;
    }

    public static String setUser() {

        System.out.println("Setup credential: Enter username");
        String userName = scanner.nextLine();

    return userName;
    }

    public static char[] setPassword() {

        char[] password = console.readPassword("Enter password: ");

    return password;
    }
}
