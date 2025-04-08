package server;

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

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(1000);

    public static void main(String[] args) {
        // Shutdown hook to ensure that the server is entirely dead on 'exit'
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (running) {
                stopServer();
            }
        }));

        Scanner scanner = new Scanner(System.in);
        System.out.println("WELCOME TO LEARNIFY!!!!!!!!");
        System.out.println("Server Commands: [start | stop | exit]");

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "start":
                    if (!running) {
                        startServer();
                    } else {
                        System.out.println("[Server] Already running.");
                    }
                    break;

                case "stop":
                    if (running) {
                        stopServer();
                    } else {
                        System.out.println("[Server] Not running.");
                    }
                    break;

                case "exit":
                    stopServer();
                    System.out.println("[Server] Shutting down.");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid command. Use: [start | stop | exit]");
            }
        }
    }


    //Starts the RMI server and binds services.
    private static void startServer() {
        // Use a new thread to start the server so that the main thread remains responsive.
        new Thread(() -> {
            try {
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
                String serverIP = getServerIP(); // Display the IP address
                System.out.println("=====================================================");
                System.out.println("[Server] RMI Server started successfully on port " + PORT);
                System.out.println("[Server] Server IP Address: " + serverIP);
                System.out.println("[Server] Available RMI Services: " + String.join(", ", registry.list()));
                System.out.println("=====================================================");

                // Start a heartbeat monitor in a separate thread.
                startHeartbeatMonitor();

            } catch (RemoteException | AlreadyBoundException e) {
                System.err.println("[Server ERROR] " + e.getMessage());
            }
        }).start();
    }

    // Stops the RMI server.
    private static void stopServer() {
        if (registry != null) {
            try {
                System.out.println("[Server] Stopping server...");

                // Unbind all services
                for (String name : registry.list()) {
                    registry.unbind(name);
                    System.out.println("[Server] Unbound service: " + name);
                }

                // Unexport RMI objects
                if (authService != null) {
                    java.rmi.server.UnicastRemoteObject.unexportObject(authService, true);
                    System.out.println("[Server] Unexported authService.");
                }
                if (studentService != null) {
                    java.rmi.server.UnicastRemoteObject.unexportObject(studentService, true);
                    System.out.println("[Server] Unexported studentService.");
                }
                if (tutorService != null) {
                    java.rmi.server.UnicastRemoteObject.unexportObject(tutorService, true);
                    System.out.println("[Server] Unexported tutorService.");
                }
                if (adminService != null) {
                    java.rmi.server.UnicastRemoteObject.unexportObject(adminService, true);
                    System.out.println("[Server] Unexported adminService.");
                }

                // Unexport the registry
                java.rmi.server.UnicastRemoteObject.unexportObject(registry, true);
                System.out.println("[Server] Unexported RMI registry.");

                // Shutdown the thread pool gracefully
                threadPool.shutdownNow();

                // Nullify references
                registry = null;
                authService = null;
                studentService = null;
                tutorService = null;
                adminService = null;
                running = false;

                // Force garbage collection to clean up RMI resources
                System.gc();
                System.out.println("[Server] Server stopped.");
            } catch (Exception e) {
                System.err.println("[Server ERROR] Could not stop: " + e.getMessage());
            }
        } else {
            System.out.println("[Server] Server is already stopped.");
        }
    }

    // Starts a heartbeat monitor that logs server status every 30 seconds.
    private static void startHeartbeatMonitor() {
        threadPool.submit(() -> {
            while (running) {
                try {
                    Thread.sleep(30000); // sleep for 30 seconds
                    // System.out.println("[Heartbeat] Server is running. Active clients: " + connectedClients.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("[Heartbeat] Monitor interrupted.");
                }
            }
        });
    }


    // Gets the server's actual IP address.
    private static String getServerIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "Unknown";
        }
    }

}
