import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.util.logging.*;
import java.util.*;


class UDPServer {
    public static void main(String args[]) throws Exception
    {
        DatagramSocket serverSocket = new DatagramSocket(9876);
        HashMap<InetAddress, String> nameMap = new HashMap<InetAddress, String>();

        Logger logger = Logger.getLogger("MyLog");
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

            String user = nameMap.get(IPAddress);
            if(user == null) {
                nameMap.put(IPAddress, sentence);
                logger.info("Connection established with IP Address " + IPAddress + " with client name " + sentence);

                String ack = "Connection with server successfully established";
                sendData = ack.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);

                logger.info("Sent acknowledgement to " + sentence);
            }

            else {
                logger.info("User " + user + " sent the following message to port #" + port + "\n" + sentence);

                String response = "Temp response";
                sendData = response.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);

                logger.info("Sent the following response to " + user + "\n" + sentence);
            }


        }
    }
}