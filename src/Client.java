import Main.Network.Network.SendSocket;

public class Client {
	private static final int PORT = 5555;

	public static void main(String[] args) {

		if(args.length > 1) 		startClient(args[1], Integer.getInteger(args[0]));
		else if(args.length > 0) 	startClient("", Integer.getInteger(args[0]));
		else 						startClient("", PORT);
	}

	private static void startClient(String host, int port) {
		SendSocket send = new SendSocket(host, port);
	}
}
