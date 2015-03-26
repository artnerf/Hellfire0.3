package com.clover.fda.hellfire03.host;

import com.clover.fda.hellfire03.tlv.TLV;
import com.clover.fda.hellfire03.util.Utils;

/**
 * Created by firstdata on 24.03.15.
 */
public class Elements {
    private class E0 {
        private long menuItemId;
        private int requestId;
        private String compnayNo;
        private String storeNo;
        private String posNo;
        private byte printerReady;
    }
    private class E4 {
        private int identify;
        private int timeout;
        private byte[] value;
    }

    private E0 e0;
    private E4 e4;
    private TLV tlv;

    // constructor
    public Elements(){
        e0 = new E0();
        e4 = new E4();
        tlv = new TLV();
    }

    public void setE0(byte[] e0Bytes){
        if(e0Bytes != null){
            byte[] menuItem = tlv.getTagValue(e0Bytes, 0xDF01);
            e0.menuItemId = Utils.bytesToLong(menuItem);
            byte[] requestId = tlv.getTagValue(e0Bytes, 0xDF02);
            e0.requestId = (int)Utils.bytesToLong(requestId);
            byte[] company = tlv.getTagValue(e0Bytes, 0xDF03);
            e0.compnayNo = Utils.bytesToHex(company);
            byte[] storeNo = tlv.getTagValue(e0Bytes, 0xDF04);
            e0.storeNo = Utils.bytesToHex(storeNo);
            byte[] posNo = tlv.getTagValue(e0Bytes, 0xDF05);
            e0.posNo = Utils.bytesToHex(posNo);
            byte[] printer = tlv.getTagValue(e0Bytes, 0xDF06);
            e0.printerReady = printer[0];
        }
    }

    public void setE4(byte[] e4Bytes){
        if(e4Bytes != null) {
            byte[] ident = tlv.getTagValue(e4Bytes, 0xDF8203);
            e4.identify = ident[0];
            e4.value = tlv.getTagValue(e4Bytes, 0xDF8201);
            byte[] timeoutb = tlv.getTagValue(e4Bytes, 0xDF8020);
            if (timeoutb.length == 2) {
                e4.timeout = (timeoutb[0] << 8) + timeoutb[1];
            } else e4.timeout = 9;  // default
        }
    }
    public E4 getE4(){
        return e4;
    }
    public int getE4Identify(){
        return e4.identify;
    }
}
