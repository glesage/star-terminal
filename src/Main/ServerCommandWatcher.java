package Main;

import java.util.Scanner;

public class ServerCommandWatcher implements Runnable {
    Scanner scanner;

    public ServerCommandWatcher(){
        scanner = new Scanner(System.in);
    }

    public void run(){
        String input = scanner.nextLine();
        stopServer(input.toLowerCase());
    }

    private void stopServer(String input){
        if(input.equals("stop")){
            System.out.println("Server Shutting Down");
            System.exit(0);
        }else{
            //watches for more input
            System.out.println("Unrecognized input: " + input);
            run();
        }
    }
}
