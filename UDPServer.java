import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.util.logging.*;
import java.util.*;


class UDPServer {
    public static void main(String args[]) throws Exception
    {
        DatagramSocket serverSocket = new DatagramSocket(9876);
        HashMap<Integer, String> portMap = new HashMap<Integer, String>();

        Logger logger = Logger.getLogger("Server Log");
        FileHandler fileHandler = new FileHandler("server.log");
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);


        logger.info("Server running!");

        while(true)
        {
            byte[] receiveData = new byte[1024];
            byte[] sendData;

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            String sentence = new String(receivePacket.getData()).trim();
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            String user = portMap.get(port);
            if(user == null) {
                portMap.put(port, sentence);
                logger.info("Connection established with user " + sentence + "\n" +
                        "User details: IP Address " + IPAddress + " Port #: " + port);

                String ack = "Connection with server successfully established";
                sendData = ack.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);

                logger.info("Sent acknowledgement to " + sentence);
            }

            else {
                logger.info("User " + user + " sent the following message " + "\n" + sentence);

                String response = "nothing";
                double result = 0;

                try {
                    BinaryTree tree = new BinaryTree(sentence);
                    result = tree.calculate(tree.root);
                    response = Double.toString(result);
                } catch (Exception e) {
                    response = "Parsing error. Valid operations: + - * /";
                }

                sendData = response.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);

                logger.info("Sent the following response to " + user + "\n" + response);
            }
        }
    }
}