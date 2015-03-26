package com.clover.fda.hellfire03.host;

import com.clover.fda.hellfire03.tlv.TAG;
import com.clover.fda.hellfire03.tlv.TLV;
import com.clover.fda.hellfire03.util.Utils;

/**
 * Created by firstdata on 24.03.15.
 */
public class PinPad {
    public static byte[] sendReceivePP(Elements resp){
        byte[] ret;

        TLV tlv = new TLV();
        byte[] val = new byte[1];
        val[0] = 0x00;
        byte[] ppTag = tlv.createTag(TAG.PP_PERIPHERAL, val);
        byte[] baRet = Utils.integer2ByteArray(TAG.PP_RESPONSE);
        ret = Utils.AppendByteArray(ppTag, baRet);
        val[0] = 0x00;
        ret = Utils.AppendByteArray(ret, val);		// Length

        return ret;
    }
}
