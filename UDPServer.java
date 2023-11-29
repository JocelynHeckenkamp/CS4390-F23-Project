import java.net.*;
import java.util.logging.*;
import java.util.*;

// NOTE: This file is based on the UDPServer file given to us in class
class UDPServer {
    public static void main(String args[]) throws Exception
    {
        // initializes socket
        DatagramSocket serverSocket = new DatagramSocket(9876);

        // initializes hash map which maps a port number to a username
        HashMap<Integer, String> portMap = new HashMap<Integer, String>();

        // initializes logger
        Logger logger = Logger.getLogger("Server Log");
        FileHandler fileHandler = new FileHandler("server.log");
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);
        logger.info("Server running!");

        while(true) {
            byte[] receiveData = new byte[1024];
            byte[] sendData;

            // receives UDP packet
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            // reads message with trimmed whitespace
            String sentence = new String(receivePacket.getData()).trim();

            // obtains IP address and port number
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            // finds username based on port number
            // user will be null if the port is not in the port map
            String user = portMap.get(port);

            // if user has not connected yet
            if(user == null) {
                logger.info("User " + sentence + " is attempting to establish a connection with the server\n" +
                        "User details: IP Address " + IPAddress + " Port Number " + port);

                String response;

                if(portMap.containsValue(sentence)){
                    logger.info("Connection was not established because username " + sentence + " is already in use");
                    response = "Username " + sentence + " is already in use. Please enter another username.";
                }

                else{
                    portMap.put(port, sentence); // enters the port-username pair to the port map
                    // logs that the user connected
                    logger.info("Connection established with user " + sentence);

                    response = "Connection with server successfully established";
                }

                // sends and acknowledgement message to the client
                sendData = response.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);

                // logs that an acknowledgement was sent
                logger.info("Sent the following response: " + response);
            }

            // user has already connected
            else {
                // logs the message the user sent
                logger.info("User " + user + " sent the following message " + "\n" + sentence);

                // if the user wishes to terminate the connection
                if(sentence.equals("STOP")) {
                    // remove port-username from the port map
                    // and log that the user disconnected from the server

                    portMap.remove(port);
                    logger.info("User " + user + " disconnected from the server");
                    continue;
                }

                String response;
                double result;

                try {
                    BinaryTree tree = new BinaryTree(sentence);
                    result = tree.calculate(tree.root);
                    response = Double.toString(result);
                } catch (Exception e) {
                    response = "Parsing error. Valid operations: + - * /";
                }

                // sends the response to the client
                sendData = response.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);

                // logs the response that was sent
                logger.info("Sent the following response to " + user + "\n" + response);
            }
        }
    }
}