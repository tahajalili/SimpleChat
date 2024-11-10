package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  {
	  String message = msg.toString();
	  

	  if (message.startsWith("#login ")) {
	    String loginId = message.substring(7).trim();  
	

	    if (client.getInfo("loginId") != null) {
	        try {
	            client.sendToClient("Error: Already logged in!!!");
	            client.close();
	        } catch (Exception e) {
	            System.out.println("Error: Cant close connection for client!");
	        }
	        return;
	    }
		    
	    client.setInfo("loginId", loginId);
	    System.out.println("Client logged in with ID: " + loginId);
	    try {
	        client.sendToClient("Login successful. Welcome, " + loginId + "!");
	    } catch (Exception e) {
	        System.out.println("Error: Unable to send login acknowledgment to client.");
	    }
	    return;
	}

	   
	    if (client.getInfo("loginId") == null) {
	        try {
	            client.sendToClient("Error: You must login first!!!");
	            client.close();
	        } catch (Exception e) {
	            System.out.println("Error:  Cant close connection for client!!!");
	        }
	        return;
	    }

	    
	    String loginId = (String) client.getInfo("loginId");
	    String prefixedMessage = loginId + ": " + message;
	    System.out.println("Message received: " + message + " from " + loginId);
	    sendToAllClients(prefixedMessage);
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  
  /**
	 * Implements hook method called each time a new client connection is
	 * accepted. The default implementation does nothing.
	 * @param client the connection connected to the client.
	 */
  	@Override
	protected void clientConnected(ConnectionToClient client) {
  		System.out.println("Client "+client.getInetAddress()+" connected to the server.");
  	}

	/**
	 * Implements hook method called each time a client disconnects.
	 * The default implementation does nothing. The method
	 * may be overridden by subclasses but should remains synchronized.
	 *
	 * @param client the connection with the client.
	 */
  	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
  		System.out.println("Client "+client.getInetAddress()+" diconnected from the server.");
  	}
  	
  	/**
	 * Hook method called each time an exception is thrown in a
	 * ConnectionToClient thread.
	 * The method may be overridden by subclasses but should remains
	 * synchronized.
	 *
	 * @param client the client that raised the exception.
	 * @param Throwable the exception thrown.
	 */
  	@Override
    synchronized protected void clientException(ConnectionToClient client, Throwable exception) {

  		String clientAddress = (client.getInetAddress() != null) ? client.getInetAddress().toString() : client.toString();
  	    System.out.println("Client connection closed with exception: " + clientAddress);
  	}
    
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
	  int port = 0; 

	    try{
	      port = Integer.parseInt(args[0]); 
	    }catch(Throwable t)
	    {
	      port = DEFAULT_PORT; 
	    }
		
	    EchoServer sv = new EchoServer(port);
	    ServerConsole console = new ServerConsole(sv);
	    
	    try {
	      sv.listen(); 
	    }catch (Exception ex) 
	    {
	      System.out.println("ERROR - Could not listen for clients!");
	    }          
	    console.accept();
  } 
}
//End of EchoServer class
