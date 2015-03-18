package com.clover.fda.hellfire03.host;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.clover.fda.hellfire03.menu.ScMenu;
import com.clover.fda.hellfire03.parameter.Param;
import com.clover.fda.hellfire03.tlv.TAG;
import com.clover.fda.hellfire03.tlv.TLV;
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

    protected static Context context;


    static protected byte[] eftRequest;

    static String companyNo;
    static String storeNo;
    static String posNo;
    static boolean valid_triple;


    /**
     * getTriple  Method fetch the often used and constant variables companyNo, storeNo, posNo
     */
    public static void getTriple(Context cntx) {

        context = cntx;

        SharedPreferences bkse_ini = PreferenceManager.getDefaultSharedPreferences(context);

        companyNo = bkse_ini.getString(Param.COMPANYNO, "?");
        storeNo   = bkse_ini.getString(Param.STORENO, "?");
        posNo     = bkse_ini.getString(Param.POSNO, "?");

        if(companyNo.compareTo("?") == 0 || storeNo.compareTo("?") == 0 || posNo.compareTo("?") == 0) valid_triple = false;
        else valid_triple = true;
    }

    /**
     * Method buildEftRequest
     * @param menuItemId menu item id
     * @return send byte array
     */
     public static byte[] buildEftRequest(long menuItemId){

         byte[] e0;

         SharedPreferences bkse_ini = PreferenceManager.getDefaultSharedPreferences(context);
         SharedPreferences.Editor editor = bkse_ini.edit();

         TLV tlv = new TLV();
         // build E0
         e0 = tlv.createTag(TAG.MENU_ITEM_ID, Utils.longToByteArray4(menuItemId));

         String requestIdStr = bkse_ini.getString(Param.REQUEST_ID, "0000");
         Integer requestId = new Integer(requestIdStr);
         requestId++;
         editor.putString(Param.REQUEST_ID, requestId.toString());

         editor.commit();

         e0 = tlv.appendTag(e0, TAG.REQUEST_ID, Utils.integerToByteArray2(requestId));

         if(menuItemId != ScMenu.STARTUP_MESSAGE){
             e0 = tlv.appendTLV(e0, TAG.COMPANY_NO, Utils.hexStringToBytes(companyNo));
             e0 = tlv.appendTLV(e0, TAG.STORE_NO, Utils.hexStringToBytes(storeNo));
             e0 = tlv.appendTLV(e0, TAG.POS_NO, Utils.hexStringToBytes(posNo));
         }

         e0 = tlv.appendTag(e0, TAG.PRINTER_READY, Utils.integerToByteArray1(1));

         eftRequest = tlv.createTag(TAG.E0_ROOT, e0);

         // build E1
         byte[] e1;
         String language = bkse_ini.getString(Param.LANGUAGE, "en");
         e1 = tlv.createTLV(TAG.LANGUAGE_MERCHANT, Utils.stringToBytes(language));
         String currency = bkse_ini.getString(Param.CURRENCY, "USD");
         e1 = tlv.appendTLV(e1, TAG.CURRENCY, Utils.hexStringToBytes(currency));
         if(valid_triple == false){
             String terminalId = bkse_ini.getString(Param.TERMINAL_ID,"");
             e1 = tlv.appendTLV(e1, TAG.TERMINAL_ID, Utils.stringToBytes(terminalId));
         }
          String currency_exp = bkse_ini.getString(Param.CURRENCY_EXP, "0");
         e1 = tlv.appendTLV(e1, TAG.CURRENCY_EXP, Utils.hexStringToBytes(currency_exp));

         eftRequest = tlv.appendTLV(eftRequest, TAG.E1_ROOT, e1);

         // build E3
         byte[] e3;

         if(valid_triple == false) {
             String t_snr = bkse_ini.getString(Param.T_SNR, "");
             e3 = tlv.createTLV(TAG.T_SNR, Utils.stringToBytes(t_snr));
             String pp_vendor = bkse_ini.getString(Param.PP_VENDOR, "");
             e3 = tlv.appendTLV(e3, TAG.PP_VENDOR, Utils.stringToBytes(pp_vendor));
         }
         else{

             String pp_vendor = bkse_ini.getString(Param.PP_VENDOR, "");
             e3 = tlv.createTLV(TAG.PP_VENDOR, Utils.stringToBytes(pp_vendor));
         }

         String timer_version =bkse_ini.getString(Param.TIMER_VERSION, "");
         e3 = tlv.appendTLV(e3, TAG.TIMER_VERSION, Utils.stringToBytes(timer_version));

         String printer_width = bkse_ini.getString(Param.PRINTER_WIDTH, "");
         e3 = tlv.appendTLV(e3, TAG.PRINTER_WIDTH, Utils.hexStringToBytes(printer_width));

         String capabilities = bkse_ini.getString(Param.CAPABILITIES, "");
         e3 = tlv.appendTLV(e3, TAG.CAPABILITIES, Utils.hexStringToBytes(capabilities));

         String setup_version = bkse_ini.getString(Param.SETUP_VERSION, "");
         e3 = tlv.appendTLV(e3, TAG.SETUP_VERSION, Utils.stringToBytes(setup_version));

         String local_version = bkse_ini.getString(Param.LOCAL_VERSION, "");
         e3 = tlv.appendTLV(e3, TAG.LOCAL_VERSION, Utils.stringToBytes(local_version));

         String ecr_version = bkse_ini.getString(Param.ECR_VERSION, "");
         e3 = tlv.appendTLV(e3, TAG.ECR_VERSION, Utils.hexStringToBytes(ecr_version));

        eftRequest = tlv.appendTLV(eftRequest, TAG.E3_ROOT, e3);
        Log.d("eftRequest:", Utils.bytesToHex(eftRequest));

        return eftRequest;
    }

    public static byte[] sendReceiveHost() throws IOException {
        InputStream is = null;
        OutputStream os = null;


        //String help = "E025DF010400030002DF02020001DF030400000666DF040400000001DF050454047754DF060101E10EDF39026465DF27020978DF280102E341DF80080846443130305F414EDF80090400000000DF80100124DF80110400000000DF800108444646445F303035DF8012083030303030303032DF80050400950001";

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

        //byte[] sendBytes = Utils.addHostMsgLen(Utils.hexStringToBytes(help));
        byte[] sendBytes = Utils.addHostMsgLen(eftRequest);
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
        int bufferLen = is.read(buffer);



        socket.close();
//        sslsocket.close();
        byte[] eftResponse = new byte[bufferLen];
        System.arraycopy(buffer, 0, eftResponse, 0, bufferLen);
        return eftResponse;
    }

}
