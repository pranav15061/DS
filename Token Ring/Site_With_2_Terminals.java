import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Site {
    private static boolean hasToken;
    private static String nextSiteIP;
    private static int listenPort;
    private static int sendPort;

    public Site(boolean hasToken, String nextSiteIP, int listenPort, int sendPort) {
        Site.hasToken = hasToken;
        Site.nextSiteIP = nextSiteIP;
        Site.listenPort = listenPort;
        Site.sendPort = sendPort;
    }

    public void start() {
        // Start a thread to listen for incoming token
        new Thread(() -> listenForToken()).start();

        // If this site has token initially, send it manually
        if (hasToken) {
            try {
                Thread.sleep(2000); // small delay to allow other site to start
                enterCriticalSection();
                passToken();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void listenForToken() {
        try (ServerSocket serverSocket = new ServerSocket(listenPort)) {
            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message = reader.readLine();
                if ("TOKEN".equals(message)) {
                    hasToken = true;
                    enterCriticalSection();
                    passToken();
                }
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void passToken() {
        try (Socket socket = new Socket(nextSiteIP, sendPort)) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("TOKEN\n");
            writer.flush();
            hasToken = false;
            System.out.println("Token passed to " + nextSiteIP + ":" + sendPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enterCriticalSection() {
        System.out.println("\n==== Entering Critical Section ====");
        try {
            Thread.sleep(3000); // Simulate doing some work in CS
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("==== Exiting Critical Section ====\n");
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java Site <hasToken:true/false> <nextSiteIP> <listenPort> <sendPort>");
            return;
        }
        boolean hasToken = Boolean.parseBoolean(args[0]);
        String nextSiteIP = args[1];
        int listenPort = Integer.parseInt(args[2]);
        int sendPort = Integer.parseInt(args[3]);

        Site site = new Site(hasToken, nextSiteIP, listenPort, sendPort);
        site.start();
    }
}
