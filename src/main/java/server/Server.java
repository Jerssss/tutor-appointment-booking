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

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
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
            System.out.println("[Server] Shutdown hook triggered");
            stopServer();
            DatabaseConnection.closeConnection();
        }));

        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.println(" █   █ ██▀ █   ▄▀▀ ▄▀▄ █▄ ▄█ ██▀   ▀█▀ ▄▀▄   █   ██▀ ▄▀▄ █▀▄ █▄ █ █ █▀ ▀▄▀\n" +
                " ▀▄▀▄▀ █▄▄ █▄▄ ▀▄▄ ▀▄▀ █ ▀ █ █▄▄    █  ▀▄▀   █▄▄ █▄▄ █▀█ █▀▄ █ ▀█ █ █▀  █ \n");
        System.out.println("Server Commands: [start | stop | exit]");

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "start":
                    if (!running) {
                        String selectedIP = showIPSelectionDialog();
                        if (selectedIP != null) {
                            System.setProperty("java.rmi.server.hostname", selectedIP);
                            startServer();
                        } else {
                            System.out.println("[Server] IP selection cancelled. Server not started.");
                        }
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

    private static String showIPSelectionDialog() {
        JFrame frame = new JFrame("Select Server IP");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(350, 200);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Predefined IPs
        String[] predefinedIPs = {"localhost", "192.168.1.100", "192.168.191.231", "192.168.191.115"};
        JComboBox<String> ipComboBox = new JComboBox<>(predefinedIPs);
        ipComboBox.setSelectedItem("localhost");
        ipComboBox.setPreferredSize(new Dimension(200, 25));

        JTextField customIPField = new JTextField();
        customIPField.setPreferredSize(new Dimension(200, 25));

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setPreferredSize(new Dimension(100, 30));

        // Disable dropdown when typing in text field
        customIPField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = customIPField.getText().trim();
                ipComboBox.setEnabled(text.isEmpty());
            }
        });

        // Select IP Label and ComboBox
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Select IP:"), gbc);

        gbc.gridx = 1;
        panel.add(ipComboBox, gbc);

        // Enter Custom IP Label and TextField
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Enter IP:"), gbc);

        gbc.gridx = 1;
        panel.add(customIPField, gbc);

        // Confirm Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(confirmButton, gbc);

        final String[] selectedIP = {null};

        confirmButton.addActionListener(e -> {
            String customIP = customIPField.getText().trim();
            if (!customIP.isEmpty()) {
                selectedIP[0] = customIP;
            } else {
                selectedIP[0] = (String) ipComboBox.getSelectedItem();
            }
            frame.dispose();
        });

        frame.add(panel);
        frame.setVisible(true);

        // Wait for dialog to close
        try {
            while (frame.isVisible()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }

        return selectedIP[0];
    }

    private static void startServer() {
        new Thread(() -> {
            try {
                // First, check if we can connect to the database
                Connection dbConnection = DatabaseConnection.setCon();
                if (dbConnection == null) {
                    System.err.println("[Server] Could not establish database connection. Server cannot start.");
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
                System.out.println("[Server] RMI Server started successfully on port " + PORT);
                System.out.println("[Server] Server IP Address: " + serverIP);
                System.out.println("[Server] Available RMI Services: " + String.join(", ", registry.list()));
                System.out.println("=====================================================");

                startHeartbeatMonitor();

            } catch (RemoteException | AlreadyBoundException e) {
                System.err.println("[Server ERROR] " + e.getMessage());
                handleServerStartupFailure();
            }
        }).start();
    }

    private static void handleServerStartupFailure() {
        System.out.println("[Server] Server failed to start. Please check the logs for more details.");
    }

    private static void stopServer() {
        if (registry != null) {
            try {
                System.out.println("[Server] Stopping server...");

                for (String name : registry.list()) {
                    try {
                        registry.unbind(name);
                        System.out.println("[Server] Unbound service: " + name);
                    } catch (Exception e) {
                        System.err.println("[Server ERROR] Failed to unbind " + name + ": " + e.getMessage());
                    }
                }

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

                java.rmi.server.UnicastRemoteObject.unexportObject(registry, true);
                System.out.println("[Server] Unexported RMI registry.");

                threadPool.shutdownNow();

                registry = null;
                authService = null;
                studentService = null;
                tutorService = null;
                adminService = null;
                running = false;

                System.gc();
                System.out.println("[Server] Server stopped.");
            } catch (Exception e) {
                System.err.println("[Server ERROR] Could not stop: " + e.getMessage());
                handleServerShutdownFailure();
            }
        } else {
            System.out.println("[Server] Server is already stopped.");
        }
    }

    private static void handleServerShutdownFailure() {
        System.out.println("[Server] Error during server shutdown. Resources may not have been properly released.");
    }

    private static void startHeartbeatMonitor() {
        threadPool.submit(() -> {
            while (running) {
                try {
                    Thread.sleep(30000);
                    System.out.println("[Heartbeat] Server is running. Active clients: " + activeClients.size());
                    if (!activeClients.isEmpty()) {
                        System.out.println("[Heartbeat] Connected: " + String.join(", ", activeClients));
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("[Heartbeat] Monitor interrupted.");
                    handleHeartbeatMonitorInterrupt();
                }
            }
        });
    }

    private static void handleHeartbeatMonitorInterrupt() {
        System.out.println("[Heartbeat] Monitor stopped due to interruption.");
    }

    private static String getServerIP() {
        return System.getProperty("java.rmi.server.hostname", "localhost");
    }
}