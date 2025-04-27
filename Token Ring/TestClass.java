package com.pranav.Mail_Sender;

import java.util.Scanner;

class TokenRing {

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of processes in the ring: ");
        int n = sc.nextInt();
        int token = 0;

        System.out.println("Initializing ring...");
        for (int i = 0; i < n; i++) {
            System.out.print(" " + i);
        }
        System.out.println();

        while (true) {
            System.out.print("\nDo you want to send a message? (yes/no): ");
            String choice = sc.next().toLowerCase();
            if (!choice.equals("yes")) {
                System.out.println("Exiting token ring simulation.");
                break;
            }

            System.out.print("Enter sender process ID (0 to " + (n - 1) + "): ");
            int sender = sc.nextInt();
            System.out.print("Enter receiver process ID (0 to " + (n - 1) + "): ");
            int receiver = sc.nextInt();
            sc.nextLine(); // clear buffer
            System.out.print("Enter the message to be sent: ");
            String message = sc.nextLine();

            System.out.println("\nToken circulation started...");

            // Circulate token to sender
            for (int i = token; i != sender; i = (i + 1) % n) {
                System.out.print(i + " -> ");
            }
            System.out.println(sender);

            // Sender sends the message
            System.out.println(sender + " sending message: '" + message + "'");

            // Forward message to receiver
            for (int i = (sender + 1) % n; i != receiver; i = (i + 1) % n) {
                System.out.println("Message '" + message + "' forwarded by process " + i);
            }

            // Receiver receives the message
            System.out.println("Receiver " + receiver + " received the message: '" + message + "'");

            // Update token holder
            token = sender;
        }

        sc.close();
    }
}
