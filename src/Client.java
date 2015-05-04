import Main.Network.SendSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client {
    private static final int port = 5555;

    public static void main(String[] args) {
		try {
			// input the message from standard input
			BufferedReader reader= new BufferedReader(new InputStreamReader(System.in));
			String message= reader.readLine();
			
			SendSocket client= new SendSocket(port);
			client.communicate(message);
			client.close();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
}
