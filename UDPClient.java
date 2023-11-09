import java.io.*; 
import java.net.*; 
  
class UDPClient {
	public static void main(String args[]) throws Exception {
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();

		InetAddress IPAddress = InetAddress.getByName("127.0.0.1");

		boolean connectionEstablished = false;

		while(true) {
			byte[] sendData;
			byte[] receiveData = new byte[1024];

			if(connectionEstablished)
				System.out.println("Enter your mathematical question or STOP to terminate the connection.");
			else
				System.out.println("Please enter your username to connect to the server.");

			String sentence = inFromUser.readLine();
			if(sentence.equals("STOP"))
				break;

			sendData = sentence.getBytes();

			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
			clientSocket.send(sendPacket);

			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);

			String modifiedSentence = new String(receivePacket.getData());
			System.out.println("FROM SERVER:" + modifiedSentence);

			connectionEstablished = true;
		}

		System.out.println("Terminating connection with the server");
		clientSocket.close();
	}
}