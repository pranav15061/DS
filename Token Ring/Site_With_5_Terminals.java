import java.io.*;
import java.net.*;

public class Site {
    private int siteId;
    private int listenPort;
    private String nextIP;
    private int nextPort;
    private boolean hasToken;

    public Site(int siteId, boolean hasToken, int listenPort, String nextIP, int nextPort) {
        this.siteId = siteId;
        this.listenPort = listenPort;
        this.nextIP = nextIP;
        this.nextPort = nextPort;
        this.hasToken = hasToken;
    }

    public void start() {
        // Thread to listen and receive token
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(listenPort)) {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String message = reader.readLine();
                    if ("TOKEN".equals(message)) {
                        System.out.println("[Site " + siteId + "] Received token.");
                        hasToken = true;
                        enterCriticalSection();
                        sendToken();
                    }
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // If this site initially has the token
        if (hasToken) {
            try {
                Thread.sleep(5000); // small delay so all servers start
                enterCriticalSection();
                sendToken();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void enterCriticalSection() {
        System.out.println("[Site " + siteId + "] ==== ENTERING Critical Section ====");
        try {
            Thread.sleep(5000); // simulate some work
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[Site " + siteId + "] ==== EXITING Critical Section ====");
    }

    private void sendToken() {
        hasToken = false;
        try {
            Socket socket = new Socket(nextIP, nextPort);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("TOKEN");
            writer.newLine();
            writer.flush();
            System.out.println("[Site " + siteId + "] Token sent to " + nextIP + ":" + nextPort);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Usage: java Site <siteId> <hasToken> <listenPort> <nextIP> <nextPort>");
            return;
        }

        int siteId = Integer.parseInt(args[0]);
        boolean hasToken = Boolean.parseBoolean(args[1]);
        int listenPort = Integer.parseInt(args[2]);
        String nextIP = args[3];
        int nextPort = Integer.parseInt(args[4]);

        Site site = new Site(siteId, hasToken, listenPort, nextIP, nextPort);
        site.start();
    }
}
