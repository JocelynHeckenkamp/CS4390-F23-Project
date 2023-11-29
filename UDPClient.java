import java.io.*; 
import java.net.*; 

// NOTE: This file is based on the UDPClient file given to us in class
class UDPClient {
	public static void main(String args[]) throws Exception {
		// creates buffered reader, datagram socket, & IPAddress
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("127.0.0.1");

		// boolean variable that monitors if the connection was successfully established
		boolean connectionEstablished = false;

		while(true) {
			byte[] sendData;
			byte[] receiveData = new byte[1024];

			// asks the user for a mathematical expression or for a username
			if(connectionEstablished)
				System.out.println("Enter your mathematical question or STOP to terminate the connection.");
			else
				System.out.println("Please enter your username to connect to the server.");

			// reads line and sends packet to server
			String sentence = inFromUser.readLine();
			sendData = sentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
			clientSocket.send(sendPacket);

			// if user wants to stop, do not wait for server response
			// send message anyway to remove user from the server
			if(sentence.equals("STOP"))
				break;

			// reads answer
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			String answer = new String(receivePacket.getData()).trim();

			// outputs the mathematical answer or a confirmation of connection establishment
			if(connectionEstablished)
				System.out.println("Answer: " + answer);
			else {
				System.out.println(answer);
				// only establish the connection if the server confirms the username is not in use
				connectionEstablished = answer.equals("Connection with server successfully established");
			}
		}

		// output a message and close the socket
		System.out.println("Terminating connection with the server");
		clientSocket.close();
	}
}