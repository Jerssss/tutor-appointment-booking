package server;

import server.database.DatabaseConnection;
import server.services.AdminServiceImpl;
import server.services.AuthServiceImpl;
import server.services.StudentServiceImpl;
import server.services.TutorServiceImpl;
import shared.interfaces.AdminService;
import shared.interfaces.AuthService;
import shared.interfaces.StudentService;
import shared.interfaces.TutorService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 1099;
    private static Registry registry;
    private static boolean running = false;


    private static AuthServiceImpl authService;
    private static StudentServiceImpl studentService;
    private static TutorServiceImpl tutorService;
    private static AdminServiceImpl adminService;

    private static final List<String> activeClients = new CopyOnWriteArrayList<>();
    public static void addActiveClient(String clientId) {
        activeClients.add(clientId);
    }

    public static void removeActiveClient(String clientId) {
        activeClients.remove(clientId);
    }


    private static final ExecutorService threadPool = Executors.newFixedThreadPool(1000);
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[SERVER | "+ new Date()+ "] Shutdown hook triggered");
            stopServer();
            DatabaseConnection.closeConnection();
        }));


        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.println(" █   █ ██▀ █   ▄▀▀ ▄▀▄ █▄ ▄█ ██▀   ▀█▀ ▄▀▄   █   ██▀ ▄▀▄ █▀▄ █▄ █ █ █▀ ▀▄▀\n" +
                           " ▀▄▀▄▀ █▄▄ █▄▄ ▀▄▄ ▀▄▀ █ ▀ █ █▄▄    █  ▀▄▀   █▄▄ █▄▄ █▀█ █▀▄ █ ▀█ █ █▀  █ \n");
        System.out.println("[SERVER | "+ new Date()+ "] Server Commands: [start | stop | exit]");

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "start":
                    if (!running) {
                        startServer();
                    } else {
                        System.out.println("[SERVER | "+ new Date()+ "] Already running.");
                    }
                    break;

                case "stop":
                    if (running) {
                        stopServer();
                    } else {
                        System.out.println("[SERVER | "+ new Date()+ "] Not running.");
                    }
                    break;

                case "exit":
                    stopServer();
                    System.out.println("[SERVER | "+ new Date()+ "] Shutting down.");
                    System.exit(0);
                    break;

                default:
                    System.out.println("[SERVER | "+ new Date()+ "] Invalid command. Use: [start | stop | exit]");
            }
        }
    }

    private static void startServer() {
        new Thread(() -> {
            try {
//                System.setProperty("java.rmi.server.hostname", "192.168.191.231");
//                  uncomment this when using a different terminal and edit the ip

                // First, check if we can connect to the database
                Connection dbConnection = DatabaseConnection.setCon();
                if (dbConnection == null) {
                    System.err.println("[SERVER | "+ new Date()+ "] Could not establish database connection. Server cannot start.");
                    return;
                }

                registry = LocateRegistry.createRegistry(PORT);

                authService = new AuthServiceImpl();
                studentService = new StudentServiceImpl();
                tutorService = new TutorServiceImpl();
                adminService = new AdminServiceImpl();

                registry.bind("authentication", authService);
                registry.bind("student_service", studentService);
                registry.bind("tutor_services", tutorService);
                registry.bind("admin_services", adminService);

                running = true;
                String serverIP = getServerIP();
                System.out.println("=====================================================");
                System.out.println("[SERVER | "+ new Date()+ "] RMI Server started successfully on port " + PORT);
                System.out.println("[SERVER | "+ new Date()+ "] Server IP Address: " + serverIP);
                System.out.println("[SERVER | "+ new Date()+ "] Available RMI Services: " + String.join(", ", registry.list()));
                System.out.println("=====================================================");

                startHeartbeatMonitor();

            } catch (RemoteException | AlreadyBoundException e) {
                System.err.println("[SERVER | "+ new Date()+ "] " + e.getMessage());
                handleServerStartupFailure();
            }
        }).start();
    }

    private static void handleServerStartupFailure() {
        System.out.println("[SERVER | "+ new Date()+ "] Server failed to start. Please check the logs for more details.");
        // Additional handling could include:
        // - Notifying administrators
        // - Logging the failure
        // - Attempting to restart
    }

    private static void stopServer() {
        if (registry != null) {
            try {
                System.out.println("[SERVER | "+ new Date()+ "] Stopping server...");

                for (String name : registry.list()) {
                    try {
                        registry.unbind(name);
                        System.out.println("[SERVER | "+ new Date()+ "] Unbound service: " + name);
                    } catch (Exception e) {
                        System.err.println("[SERVER | "+ new Date()+ "] Failed to unbind " + name + ": " + e.getMessage());
                    }
                }

                if (authService != null) {
                    java.rmi.server.UnicastRemoteObject.unexportObject(authService, true);
                    System.out.println("[SERVER | "+ new Date()+ "] Unexported authService.");
                }
                if (studentService != null) {
                    java.rmi.server.UnicastRemoteObject.unexportObject(studentService, true);
                    System.out.println("[SERVER | "+ new Date()+ "] Unexported studentService.");
                }
                if (tutorService != null) {
                    java.rmi.server.UnicastRemoteObject.unexportObject(tutorService, true);
                    System.out.println("[SERVER | "+ new Date()+ "] Unexported tutorService.");
                }
                if (adminService != null) {
                    java.rmi.server.UnicastRemoteObject.unexportObject(adminService, true);
                    System.out.println("[SERVER | "+ new Date()+ "] Unexported adminService.");
                }

                java.rmi.server.UnicastRemoteObject.unexportObject(registry, true);
                System.out.println("[SERVER | "+ new Date()+ "] Unexported RMI registry.");

                threadPool.shutdownNow();

                registry = null;
                authService = null;
                studentService = null;
                tutorService = null;
                adminService = null;
                running = false;

                System.gc();
                System.out.println("[SERVER | "+ new Date()+ "] Server stopped.");
            } catch (Exception e) {
                System.err.println("[SERVER | "+ new Date()+ "] Could not stop: " + e.getMessage());
                handleServerShutdownFailure();
            }
        } else {
            System.out.println("[SERVER | "+ new Date()+ "] Server is already stopped.");
        }
    }

    private static void handleServerShutdownFailure() {
        System.out.println("[SERVER | "+ new Date()+ "] Error during server shutdown. Resources may not have been properly released.");
        // Additional handling could include:
        // - Forcing resource cleanup
        // - Logging the failure
    }

    private static void startHeartbeatMonitor() {
        threadPool.submit(() -> {
            while (running) {
                try {
                    Thread.sleep(30000);
                    System.out.println("[Heartbeat SERVER | "+ new Date()+ "] Server is running. Active clients: " + activeClients.size());
                    if (!activeClients.isEmpty()) {
                        System.out.println("[Heartbeat SERVER | "+ new Date()+ "] Connected: " + String.join(", ", activeClients));
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("[Heartbeat SERVER | "+ new Date()+ "] Monitor interrupted.");
                    handleHeartbeatMonitorInterrupt();
                }
            }
        });
    }

    private static void handleHeartbeatMonitorInterrupt() {
        System.out.println("[Heartbeat SERVER | "+ new Date()+ "] Monitor stopped due to interruption.");
    }

    private static String getServerIP() {
//        try {
            return "localhost";
//        } catch (UnknownHostException e) {
//            System.err.println("[Server] Could not get server IP address: " + e.getMessage());
//            return "Unknown";
//        }
   }
}