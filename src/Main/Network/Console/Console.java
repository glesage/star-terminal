package Main.Network.Console;


import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by jeff on 5/12/15.
 *
 * Turns out there was so much to do with console input for keyboard presses
 * that this class was created to handle all that nicely.
 */
public class Console {
    private String ttyConfig;

    public Console() {
        try {
            this.ttyConfig = Console.stty("-g");
        } catch (IOException e) {
            System.err.println("Error while reading console input");
        }
        catch (InterruptedException e) {
            System.err.println("Keyboard interrupt");
        }
    }
    public void printLine(String input) {
        // First you gotta set the console into line mode
        try {
            this.reset();
        } catch(IOException e) {
            System.err.println("Error while reading console input");
        }
        catch (InterruptedException e) {
            System.err.println("Keyboard interrupt");
        }

        // Then you can print a line safely
        System.out.println(input);
    }
    public void printChar(char input) {

    }
    public int getLiveASCII() {
        // First you gotta set the console into char buffered mode
        try {
            this.setTerminalToCBreak();
        } catch(IOException e) {
            System.err.println("Error while reading console input");
        }
        catch (InterruptedException e) {
            System.err.println("Keyboard interrupt");
        }

        // Then you can wait for console input
        try {
           return System.in.read();
        } catch(IOException e) {
            System.err.println("Error while reading console input");
        }
        return 0;
    }
    private void reset() throws IOException, InterruptedException {
        Console.stty(this.ttyConfig.trim());
    }
    private void setTerminalToCBreak() throws IOException, InterruptedException {
        Console.stty("-icanon min 1"); // set the console to be character-buffered instead of line-buffered
        stty("-echo"); // disable character echoing
    }

    /**
     *  Execute the stty command with the specified arguments
     *  against the current active terminal.
     */
    private static String stty(final String args) throws IOException, InterruptedException {
        String cmd = "stty " + args + " < /dev/tty";
        return Console.exec(new String[]{"sh", "-c", cmd });
    }

    /**
     *  Execute the specified command and return the output
     *  (both stdout and stderr).
     */
    public static String exec(final String[] cmd) throws IOException, InterruptedException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        Process p = Runtime.getRuntime().exec(cmd);
        int c;
        InputStream in = p.getInputStream();

        while ((c = in.read()) != -1) {
            bout.write(c);
        }

        in = p.getErrorStream();

        while ((c = in.read()) != -1) {
            bout.write(c);
        }

        p.waitFor();

        String result = new String(bout.toByteArray());
        return result;
    }
}
