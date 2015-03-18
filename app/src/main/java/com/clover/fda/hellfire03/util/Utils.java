package com.clover.fda.hellfire03.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

public class Utils {

	final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	//final static byte ACK = 0x06;
	//final static byte NAK = 0x15;

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
		    hexChars[j * 2] = hexArray[v >>> 4];
		    hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String bytesToHex(byte[] bytes, int iLen) {
        byte[] baRet = new byte[iLen];
        System.arraycopy(bytes, 0, baRet, 0, iLen);
        return bytesToHex(baRet);
	}

    public static byte[] stringToBytes(String s){
        int len = s.length();
        byte[] data = new byte[len];
        for (int i = 0; i < len; i ++) {
            data[i] = (byte)s.charAt(i);
            //data[i] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;

    }

    public static byte[] hexStringToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

	public static void Sleep(long lTime) {
		try {Thread.sleep(lTime);} catch (InterruptedException e) {}
		return;
	}

	public static byte[] makeCommando(byte[] baSend) {

        byte[] baRet = new byte[4 + baSend.length];

        baRet[0] = 0;
        baRet[1] = 0;
        baRet[2] = (byte)(baSend.length / 256);
        baRet[3] = (byte)(baSend.length % 256);

        System.arraycopy(baSend, 0, baRet, 4, baSend.length);
        return baRet;
	}

    public static byte[] addHostMsgLen(byte[] baSend) {

        int len = baSend.length +2;

        byte[] baRet = new byte[len];

        baRet[0] = (byte)(len / 256);
        baRet[1] = (byte)(len % 256);

        System.arraycopy(baSend, 0, baRet, 2, baSend.length);
        return baRet;
    }

	public static int GetCommandoLen(byte[] baReceive) {

        if (baReceive.length < 4)                       return 0;
        if (baReceive[0] != 0 || baReceive[1] != 0)     return 0;

        return (baReceive[2] & 0x000000FF) * 256 + (baReceive[3] & 0x000000FF);
	}


    public static byte[] AppendByteArray(byte[] baFirst, byte[] baAppend) {

    	try {
	    	byte[] baRet = new byte[baFirst.length + baAppend.length];

    		System.arraycopy(baFirst,  0, baRet, 0, 				baFirst.length);
    		System.arraycopy(baAppend, 0, baRet, baFirst.length, 	baAppend.length);

	    	return baRet;
    	} catch (Exception e) {
    		return null;
    	}
    }



	public static String repeat(String s, int n) {
	    if(s == null) {
	        return null;
	    }
	    final StringBuilder sb = new StringBuilder();
	    for(int i = 0; i < n; i++) {
	        sb.append(s);
	    }
	    return sb.toString();
	}

    public static byte[] HEX2Byte(String hexString) {
    	try {
	    	if (!IsValidHEX(hexString))
	    		return null;

	    	byte[] baReturn = new byte[hexString.length()/2];

	        for (int i = 0; i < hexString.length(); i+=2) {
	            String str = hexString.substring(i, i+2);
	            baReturn[i/2] =(byte)Integer.parseInt(str, 16);
	        }
	    	return baReturn;
    	}
    	catch (Exception e) {
    		return null;
    	}
    }

    public static String Byte2Hex(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
        	int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static Boolean IsValidHEX(String hexString) {
        if (hexString.length() % 2 == 1 || hexString.length() == 0)
        	return false;

        String strHex = "0123456789abcdefABCDEF";

        for (int i = 0; i < hexString.length(); i++) {
            String str = hexString.substring(i, i+1);
        	if (strHex.indexOf(str) < 0)
        		return false;
        }
    	return true;
    }

    public static Boolean IsValidInteger(String iString) {

        String strInteger = "0123456789";

        for (int i = 0; i < iString.length(); i++) {
            String str = iString.substring(i, i+1);
        	if (strInteger.indexOf(str) < 0)
        		return false;
        }
    	return true;
    }

    public static void Sleep(int iMS) {
    	try{
    		for (int i=0; i<(iMS / 100); i++) {
	    		Thread.sleep(100);
    		}
    	}
    	catch(InterruptedException ie){
    		return;
    	}
    }

    public static String PadRight(String strText, Integer iLen) {
		return String.format("%1$-" + iLen.toString() + "s", strText);
    }

    public static String PadLeft(String strText, Integer iLen) {
		return String.format("%1$" + iLen.toString() + "s", strText);
    }

    public static void writeFile__(Context context, String fileName, String fileContent){
        try{
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            try{
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.write(fileContent.getBytes());
                os.close();
                fos.close();
            }
            catch (IOException ioe){
                ioe.printStackTrace();
            }
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
    }

    public static void writeFile(Context context, String fileName, String fileContent) {
        FileOutputStream  out  =  null;
        try {
            out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            out.write(fileContent.getBytes());
            out.close();
        } catch  (Exception  e) {
            Log.e("UTILS", "writeFile error=  ", e);
        } finally {
        }
    }

    public static String readFile(Context context, String fileName) {
        FileInputStream in = null;
        try {
            in = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            return sb.toString();
        } catch  (Exception  e) {
            Log.e("UTILS", "readFile error=  ", e);
        } finally {
        }
        return "";
    }


	public static byte[] DecToBCDArray(long num) {
		int digits = 0;

		long temp = num;
		while (temp != 0) {
			digits++;
			temp /= 10;
		}

		int byteLen = digits % 2 == 0 ? digits / 2 : (digits + 1) / 2;
		boolean isOdd = digits % 2 != 0;

		byte bcd[] = new byte[byteLen];

		for (int i = 0; i < digits; i++) {
			byte tmp = (byte) (num % 10);

			if (i == digits - 1 && isOdd)
				bcd[i / 2] = tmp;
			else if (i % 2 == 0)
				bcd[i / 2] = tmp;
			else {
				byte foo = (byte) (tmp << 4);
				bcd[i / 2] |= foo;
			}

			num /= 10;
		}

		for (int i = 0; i < byteLen / 2; i++) {
			byte tmp = bcd[i];
			bcd[i] = bcd[byteLen - i - 1];
			bcd[byteLen - i - 1] = tmp;
		}

		return bcd;
	}

	public static byte[] DecToBCDAmount(long num) {
		int digits = 12;
 		int byteLen = digits/2;

		byte bcd[] = new byte[byteLen];

		for (int i = 0; i < digits; i++) {
			byte tmp = (byte) (num % 10);

			if (i % 2 == 0)
				bcd[i / 2] = tmp;
			else {
				byte foo = (byte) (tmp << 4);
				bcd[i / 2] |= foo;
			}

			num /= 10;
		}

		for (int i = 0; i < byteLen / 2; i++) {
			byte tmp = bcd[i];
			bcd[i] = bcd[byteLen - i - 1];
			bcd[byteLen - i - 1] = tmp;
		}

		return bcd;
	}

	public static String BCDtoString(byte bcd) {
		StringBuffer sb = new StringBuffer();
		byte high = (byte) (bcd & 0xf0);
		high >>>= (byte) 4;
				high = (byte) (high & 0x0f);
				byte low = (byte) (bcd & 0x0f);
				sb.append(high);
				sb.append(low);
				return sb.toString();
	}

	public static String BCDtoString(byte[] bcd) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < bcd.length; i++) {
			sb.append(BCDtoString(bcd[i]));
		}

		return sb.toString();
	}

    /**
     * longToByteArray delivers in any case 4 Bytes for a long
     * @param lDec value to convert
     * @return bytes array
     */
    public static byte[] longToByteArray4(long lDec){
        byte[] myBytes = new byte[4];

        myBytes[0] = (byte)(lDec >> 24);
        myBytes[1] = (byte)(lDec >> 16);
        myBytes[2] = (byte)(lDec >> 8);
        myBytes[3] = (byte)(lDec);
        return myBytes;
    }

    public static byte[] long2ByteArray (long lDec){
        try {
            String strHex = Long.toHexString(lDec);
            if (strHex.length() % 2 == 1)
                strHex = "0" + strHex;
            return HEX2Byte(strHex);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * integerToByteArray delivers 2 bytes in any case for an integer
     * @param iDec value to convert
     * @return byte array
     */
    public static byte[] integerToByteArray3(int iDec){
        byte[] myBytes = new byte[3];

        myBytes[0] = (byte)(iDec >> 16);
        myBytes[1] = (byte)(iDec >> 8);
        myBytes[2] = (byte)(iDec);
        return myBytes;
    }
    public static byte[] integerToByteArray2(int iDec){
        byte[] myBytes = new byte[2];

        myBytes[0] = (byte)(iDec >> 8);
        myBytes[1] = (byte)(iDec);
        return myBytes;
    }
    public static byte[] integerToByteArray1(int iDec){
        byte[] myBytes = new byte[1];

        myBytes[0] = (byte)(iDec);
        return myBytes;
    }

    public static byte[] integer2ByteArray(int iDec) {
    	try {
			String strHex = Integer.toHexString(iDec);
			if (strHex.length() % 2 == 1)
				strHex = "0" + strHex;
			return HEX2Byte(strHex);
    	} catch (Exception e) {
    		return null;
    	}
    }

    public static byte[] ConvertAmount(String strAmount) {
    	try {
	    	if (!ValidAmount(strAmount))
	    		return null;

	    	strAmount = strAmount.replaceAll(",", ".");
	    	int iPoint = strAmount.indexOf(".");
	    	if (iPoint < 0 ) {
	    		strAmount += ".00";
	    	} else if (iPoint == strAmount.length() - 1) {
	    		strAmount += "00";
	    	} else if (iPoint == strAmount.length() - 2) {
	    		strAmount += "0";
	    	} else if (iPoint < strAmount.length() - 3) {
	    		strAmount = strAmount.substring(0, iPoint + 3);
	    	}
	    	strAmount = strAmount.replace(".", "");
	    	long lAmount = Long.parseLong(strAmount);
	    	return Utils.DecToBCDAmount(lAmount);
    	} catch (Exception e) {
    		return null;
    	}

    }

    public static boolean ValidAmount(String strAmount) {
        String strDec = "0123456789,.";
        int iPointCount = 0;

        for (int i = 0; i < strAmount.length(); i++) {
            char ch = strAmount.charAt(i);

            if (strDec.indexOf(ch) < 0)
        		return false;

        	if (ch == '.' || ch == ',') {
        		iPointCount++;
	        	if (iPointCount > 1)
	        		return false;
        	}

        }

    	return true;
    }

    public static String FilterString(String strText) {
    	StringBuilder strOut = new StringBuilder();

	    for(int i = 0; i < strText.length(); i++) {
	    	char chr = strText.charAt(i);
	    	if (chr >= 30)
	    		strOut.append(strText.charAt(i));
	    	else
	    		strOut.append('.');
	    }

    	return strOut.toString();
    }

    public static int Str2Int(String strInt) {
		int iInt = 0;
    	try {
    		iInt = Integer.parseInt(strInt);
		} catch (Exception e) {
			iInt = 0;
		}
    	return iInt;
    }

/*
    public static byte[] GetDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
		String strDate = dateFormat.format(new Date());
		return HEX2Byte(strDate);
    }

    public static byte[] GetTime() {
		DateFormat timeFormat = new SimpleDateFormat("HHmmss");
		String strTime = timeFormat.format(new Date());
		return HEX2Byte(strTime);
    }
*/

}
