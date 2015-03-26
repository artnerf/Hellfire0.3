package com.clover.fda.hellfire03.host;

import com.clover.fda.hellfire03.tlv.TAG;
import com.clover.fda.hellfire03.tlv.TLV;

/**
 * Created by firstdata on 25.03.15.
 */
public class Printer {

    public static byte[] sendReceivePrinter(Elements resp){
        byte[] printerResp = null;

        TLV tlv = new TLV();
        byte[] val = new byte[1];
        val[0] = 0x01;
        printerResp = tlv.createTag(TAG.PP_PERIPHERAL, val);
        val[0] = 0x00;
        printerResp = tlv.appendTag(printerResp, TAG.PP_RESPONSE, val);

        return printerResp;
    }
}
