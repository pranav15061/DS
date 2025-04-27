import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Process {
    int id;
    boolean active;

    Process(int id) {
        this.id = id;
        this.active = true;
    }
}

public class RingAlgorithm {
    static List<Process> processes = new ArrayList<>();
    static int coordinatorId = -1;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            processes.add(new Process(i + 1));
        }

        coordinatorId = n; // Initially highest id is coordinator
        System.out.println("Initial Coordinator is Process " + coordinatorId);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Crash a process");
            System.out.println("2. Recover a process");
            System.out.println("3. Display active processes");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    crashProcess(sc);
                    break;
                case 2:
                    recoverProcess(sc);
                    break;
                case 3:
                    displayProcesses();
                    break;
                case 4:
                    System.out.println("Exiting simulation.");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void crashProcess(Scanner sc) {
        System.out.print("Enter process id to crash: ");
        int pid = sc.nextInt();

        if (pid > processes.size() || pid < 1) {
            System.out.println("Invalid process id.");
            return;
        }

        Process p = processes.get(pid - 1);
        if (!p.active) {
            System.out.println("Process " + pid + " is already crashed.");
        } else {
            p.active = false;
            System.out.println("Process " + pid + " has crashed.");

            if (pid == coordinatorId) {
                System.out.println("Coordinator crashed! Starting election...");
                startElection();
            }
        }
    }

    static void recoverProcess(Scanner sc) {
        System.out.print("Enter process id to recover: ");
        int pid = sc.nextInt();

        if (pid > processes.size() || pid < 1) {
            System.out.println("Invalid process id.");
            return;
        }

        Process p = processes.get(pid - 1);
        if (p.active) {
            System.out.println("Process " + pid + " is already active.");
        } else {
            p.active = true;
            System.out.println("Process " + pid + " has recovered.");

            if (pid > coordinatorId) {
                System.out.println("Recovered process has higher ID. It will start an election...");
                startElection(pid);
            }
        }
    }

    static void displayProcesses() {
        System.out.println("Active processes:");
        for (Process p : processes) {
            if (p.active) {
                System.out.print(p.id + " ");
            }
        }
        System.out.println("\nCurrent Coordinator: Process " + coordinatorId);
    }

    static void startElection() {
        startElection(-1);
    }

    static void startElection(int initiatorId) {
        int highestId = -1;

        if (initiatorId == -1) {
            // If no specific initiator, pick the lowest active process
            for (Process p : processes) {
                if (p.active) {
                    initiatorId = p.id;
                    break;
                }
            }
        }

        System.out.println("Process " + initiatorId + " initiates election.");

        int currentProcessId = initiatorId;
        int startProcessId = initiatorId;
        while (true) {
            Process nextProcess = getNextActiveProcess(currentProcessId);

            if (nextProcess != null) {
                System.out.println("Process " + currentProcessId + " sends election message to Process " + nextProcess.id);
                currentProcessId = nextProcess.id;

                // If we looped back to the initiator, stop the election
                if (currentProcessId == startProcessId) {
                    break;
                }
            } else {
                break; // No more active processes to send messages to
            }
        }

        // Now decide the highest process
        for (Process p : processes) {
            if (p.active && p.id > highestId) {
                highestId = p.id;
            }
        }

        coordinatorId = highestId;
        System.out.println("Process " + coordinatorId + " becomes the new Coordinator.");
        announceCoordinator();
    }

    static void announceCoordinator() {
        for (Process p : processes) {
            if (p.active && p.id != coordinatorId) {
                System.out.println("Coordinator message sent from Process " + coordinatorId + " to Process " + p.id);
            }
        }
    }

    static Process getNextActiveProcess(int currentProcessId) {
        for (int i = 0; i < processes.size(); i++) {
            if (processes.get(i).active && processes.get(i).id > currentProcessId) {
                return processes.get(i);
            }
        }
        return null;
    }
}
