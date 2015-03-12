package com.clover.fda.hellfire03.host;


import com.clover.fda.hellfire03.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import java.net.InetAddress;
import java.net.Socket;

import javax.net.SocketFactory;


/**
 * Created by firstdata on 11.03.15.
 */
public class Host {

//    static protected StringBuffer receiveString = new StringBuffer();
//    static protected StringBuffer sendString = new StringBuffer();


    static public void sendReceiveHost() throws IOException {
        InputStream is = null;
        OutputStream os = null;


        String help = "E025DF010400030002DF02020001DF030400000666DF040400000001DF050454047754DF060101E10EDF39026465DF27020978DF280102E341DF80080846443130305F414EDF80090400000000DF80100124DF80110400000000DF800108444646445F303035DF8012083030303030303032DF80050400950001";

//        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
//        SSLSocket sslsocket = (SSLSocket) factory.createSocket(address, 443);
//        sslsocket.setSoTimeout(30000);

//        String address = "159.253.209.61";   // Brain Behind
        String address = "217.86.142.9";   // Hamburg MIQ Test Host
        SocketFactory factory=(SocketFactory) SocketFactory.getDefault();
        Socket socket=(Socket) factory.createSocket(address, 9013);   // Hamburg MIQ Host Port 9013
       // Socket socket=(Socket) factory.createSocket(address, 80);  // Brain Behind Port 80
        socket.setSoTimeout(30000);


//        sslsocket.startHandshake();
//        os = sslsocket.getOutputStream();

        os = socket.getOutputStream();

//        os.write(sendString.toString().getBytes());

        byte[] sendBytes = Utils.addHostMsgLen(Utils.hexStringToBytes(help));

        os.write(sendBytes);
        os.flush();

//        is = sslsocket.getInputStream();
        is = socket.getInputStream();

//        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
//			StringBuilder resultBuffer = new StringBuilder();
//			int inChar;
//			while ((inChar = isr.read()) != -1) {
//			  resultBuffer.append((char) inChar);
//			}
//        char[] buffer = new char[2000];
//        isr.read(buffer, 0, 2000);

         byte[] buffer = new byte[2000];
        is.read(buffer);

        socket.close();
//        sslsocket.close();
    }


}
