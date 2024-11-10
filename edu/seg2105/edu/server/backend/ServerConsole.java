package edu.seg2105.edu.server.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.seg2105.client.common.ChatIF;
import ocsf.server.ConnectionToClient;

public class ServerConsole implements ChatIF {
    private EchoServer server;

    public ServerConsole(EchoServer server) {
        this.server = server;
    }
    
    public void display(String message) {
        System.out.println("SERVER MSG> " + message);
    }

    
    public void accept() {
    	
        BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
        try {
            String input;
            while (true) {
                input = fromConsole.readLine();

                if (input.startsWith("#")) {
                    handleCommands(input);
                } else {
                    server.sendToAllClients("SERVER MSG> " + input);
                    display("Broadcasting: " + input);
                }
            }
        } catch (IOException e) {
            display("Error reading from console: " + e.getMessage());
        }
    }


    public void handleCommands(String command) {
        if (command.equals("#quit")) {
            System.out.println("Server is quitting...");
            System.exit(0);

        } else if (command.equals("#stop")) {
            server.stopListening();
            System.out.println("Server has stopped listening for new clients!");

        } else if (command.equals("#close")) {
            
            server.stopListening();
            Thread[] clientConnections = server.getClientConnections();
            
            for (Thread clientThread : clientConnections) {
                try {
                    ((ConnectionToClient) clientThread).close();
                } catch (IOException e) {
                    System.out.println("Error disconnecting client: " + e.getMessage());
                }
            }

            System.out.println("Server closed all client connections and fully stopped.");

        } else if (command.startsWith("#setport")) {
            String[] parts = command.split(" ");
            
            if (parts.length > 1) {
                try {
                    int newPort = Integer.parseInt(parts[1]);
                    if (!server.isListening() && server.getNumberOfClients() == 0) {
                        server.setPort(newPort);
                        System.out.println("Server port set to: " + newPort);
                    } else {
                        System.out.println("Cannot change port while server is active.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ERROR - Invalid port number.");
                }
            } else {
                System.out.println("ERROR - No port specified.");
            }

        } else if (command.equals("#start")) {
            if (!server.isListening()) {
                try {
                    server.listen();
                    System.out.println("Server started listening for clients.");
                } catch (IOException e) {
                    System.out.println("Error starting server: " + e.getMessage());
                }
            } else {
                System.out.println("ERROR - Server is already listening.");
            }

        } else if (command.equals("#getport")) {
            System.out.println("Current server port: " + server.getPort());
        } else {
            System.out.println("ERROR - Invalid command.");
        }
    }
}