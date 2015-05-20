import Main.Network.Network.SendSocket;
import java.util.Scanner;

public class Client {
    private static String port = "5555";
    private static String host = "localhost";

	public static void main(String[] args) {

		if(args.length > 1) {
            host = args[0];
            port = args[1];
        }

		startClient();
	}

	private static void startClient() {
        Scanner scanner = new Scanner(System.in);

        if (host == null) {
            System.out.print("Game server: ");
            host = scanner.next();
        }
        if (port == null) {
            System.out.print("Game port: ");
            port = scanner.next();
        }

        System.out.print("Enter your name: ");
        String name = scanner.next();
        System.out.print("Symbol for your spaceship: ");
        String symbol = scanner.next();

		SendSocket send = new SendSocket(name, symbol, host, port);
	}
}
