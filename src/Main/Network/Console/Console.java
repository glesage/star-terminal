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

    private boolean isUnix = false;

    public Console() {
        String osName = System.getProperty("os.name");
        String osNameMatch = osName.toLowerCase();
        if(osNameMatch.contains("linux")) {
            isUnix = true;
        } else if(osNameMatch.contains("solaris") || osNameMatch.contains("sunos")) {
            isUnix = true;
        } else if(osNameMatch.contains("mac os") || osNameMatch.contains("macos") || osNameMatch.contains("darwin")) {
            isUnix = true;
        } else if(osNameMatch.contains("windows")) {
            System.err.println("Are you ACTUALLY trying this on windows!? It won't play nice...");
        } else {
            System.err.println("Your OS is unknown to us... who ARE you???");
        }

        if (!isUnix) return;

        try {
            this.ttyConfig = Console.stty("-g");
        } catch (IOException e) {
            System.err.println("new(): Error while reading console input");
        }
        catch (InterruptedException e) {
            System.err.println("new(): Keyboard interrupt");
        }
    }
    public void reset() {
        if (!isUnix) return;

        try {
            this.revertConsoleToBackup();
        } catch(IOException e) {
            System.err.println("reset(): Error while reading console input");
        }
        catch (InterruptedException e) {
            System.err.println("reset(): Keyboard interrupt");
        }
    }
    public void printLine(String input) {
        if (isUnix) {
            // First you gotta set the console into line mode
            try {
                this.revertConsoleToBackup();
            } catch(IOException e) {
                System.err.println("printLine(): Error while reading console input");
            }
            catch (InterruptedException e) {
                System.err.println("printLine(): Keyboard interrupt");
            }
        }

        // Then you can print a line safely
        System.out.println(input);
    }
    public int getLiveASCII() {
        if (isUnix) {
            // First you gotta set the console into char buffered mode
            try {
                this.setTerminalToCBreak();
            } catch (IOException e) {
                System.err.println("getLiveASCII(): Error while reading console input");
            } catch (InterruptedException e) {
                System.err.println("getLiveASCII(): Keyboard interrupt");
            }
        }

        // Then you can wait for console input
        try {
           return System.in.read();
        } catch(IOException e) {
            System.err.println("getLiveASCII(): Error while reading console input");
        }
        return 0;
    }
    private void revertConsoleToBackup() throws IOException, InterruptedException {
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
    public void clear() {
        int lines = 14;
        while (lines > 0) {
            System.out.println("\f");
            lines--;
        }
    }
}
