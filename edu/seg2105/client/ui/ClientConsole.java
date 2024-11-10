package edu.seg2105.client.ui;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String loginId, String host, int port) {
      
      client = new ChatClient(loginId, host, port, this);             
      fromConsole = new Scanner(System.in);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() {
	    try {
	        String message;
	        while (true) {
	            message = fromConsole.nextLine();

	            if (message.startsWith("#")) {
	                // Commands
	                handleCommands(message);
	            } else {
	                // Regular messages
	                client.handleMessageFromClientUI(message);
	            }
	        }
	    } catch (Exception ex) {
	        System.out.println("Unexpected error while reading from console!");
	    }
	}

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  /**
   * This method 
   * @param command from user
   */


  public void handleCommands(String command) {
	    if (command.equals("#quit")) {
	        client.quit();

	    } else if (command.equals("#logoff")) {
	        try {
	            client.closeConnection();
	        } catch (IOException e) {
	            System.out.println("Error logging off: " + e);
	        }

	    } else if (command.startsWith("#sethost")) {
	        String[] strParts = command.split(" ");
	        if (strParts.length > 1) {
	            client.setHost(strParts[1]);
	            System.out.println("Host set to " + strParts[1]);
	        } else {
	            System.out.println("Error: no host specified.");
	        }

	    } else if (command.startsWith("#setport")) {
	        String[] strParts = command.split(" ");
	        if (strParts.length > 1) {
	            try {
	                int port = Integer.parseInt(strParts[1]);
	                client.setPort(port);
	                System.out.println("Port set to " + port);
	            } catch (NumberFormatException e) {
	                System.out.println("Error: Invalid port number.");
	            }
	        } else {
	            System.out.println("Error: no port specified.");
	        }

	    } else if (command.equals("#login")) {
	        if (!client.isConnected()) {
	            try {
	                client.openConnection();
	            } catch (IOException e) {
	                System.out.println("Error logging in: " + e);
	            }
	        } else {
	            System.out.println("Already logged in.");
	        }

	    } else if (command.equals("#gethost")) {
	        System.out.println("Host: " + client.getHost());

	    } else if (command.equals("#getport")) {
	        System.out.println("Port: " + client.getPort());

	    } else {
	        System.out.println("Unknown command.");
	    }
	}

  
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
	  if (args.length < 1) {
	      System.out.println("ERROR - No login ID specified. Connection aborted.");
	      System.exit(1);
	  }  
	  
	  String loginId = args[0];
      String host = "localhost";
      int port = DEFAULT_PORT;

      if (args.length > 1) {
          host = args[1];
      }
      if (args.length > 2) {
          try {
              port = Integer.parseInt(args[2]);
          } catch (NumberFormatException e) {
              System.out.println("ERROR - Invalid port number. Using default.");
          }
      }

      ClientConsole chat = new ClientConsole(loginId, host, port);
      chat.accept();  
  }
}
//End of ConsoleChat class
