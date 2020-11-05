package processing.server.board;

import processing.ClientBoardState;

import java.io.IOException;

import processing.*;
import processing.boardobject.*;

/**
 * This class implements IServerCommunication interface, hencke defines all
 * the functions required by the client for communication with the BoardServer
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class ServerCommunication {
	
	// variable to store the server address with ip and port
	String serverAddress;
	
	/**
	 * Constructor to set the server address as the object of this
	 * class is created to use this server address for further 
	 * communications in this class.
	 */
	public ServerCommunication() {
		
		/**
		 * setting serverAddress as the full server address which we
		 * need to pass the destination in the send function of networking
		 * module.
		 */
		serverAddress = ClientBoardState.serverIp.toString()
					  + ":"
					  + ClientBoardState.serverPort.toString();
	}

	/**
	 * This function will be called in the start when the client will receive
	 * the port number of the Board Server to get the persistence Board State 
	 * if there from the server.
	 */
	public void getBoardState() {
		
		/**
		 * While sending BoardState request to the server, as it is the starting
		 * communication of the client with the server, we will pass the client
		 * details in the message so that server will save this information for
		 * further use.
		 * 
		 * We are sending username and the client full address
		 */
		String message = ClientBoardState.username.toString()
					   + ":"
					   + ClientBoardState.userIP.toString()
					   + ":"
					   + ClientBoardState.userPort.toString();
		
		/**
		 * Send the BoardState request to the server with the identifier
		 * BoardStateRequest.
		 */
		ClientBoardState.send(serverAddress, message, "BoardStateRequest");
	}
	
	/**
	 * Whenever there is an Object creation or any operation on any BoardObject
	 * we need to pass the object to the other clients, so we need to send this 
	 * object first to the server using this function
	 * 
	 * @param obj BoardObject that we want to send
	 */
	public void sendObject(BoardObject obj) {
	
		// variable to store the message that we want to send
		String message = null;
		
		/** 
		 * First we need to serialize the BoardObject to send it across 
		 * the network using the Serialize class.
		 */
		try {
			message = Serialize.serialize(obj);
		} catch (IOException e) {
			// log the exception
		}
		
		/**
		 * Now send the message to the server at the serverAddress as the
		 * destination address and the serialized object as the message with
		 * the identifier ObjectBoardcast
		 */
		ClientBoardState.send(serverAddress, message, "ObjectBroadcast");
	}
	
	/**
	 * When client closes the program processing module will call this function
	 * to notify the server of stopping the connection
	 */
	public void stopConnection() {
		
		/**
		 * need to pass the username of the client as the message to tell 
		 * the server which client is leaving the connection
		 */
		String message = ClientBoardState.username.toString();
		
		/**
		 * send the message to the server at the serverAddress as the
		 * destination address and the username as the message and identifier
		 * as the StopConnection
		 */
		ClientBoardState.send(serverAddress, message, "StopConnection");
	}
	
}
