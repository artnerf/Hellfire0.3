package com.clover.fda.hellfire03.tlv;

import android.util.Log;
import com.clover.fda.hellfire03.menu.MenuElement;
import com.clover.fda.hellfire03.util.Utils;

/**
 * TLC class handles all TLV stuff
 */

public class TLV {
	private class Tag {
	    int iTag;
	    int iTagLen;
		boolean bConstructed;

	    void Tag() {
	        iTag = 0;
	        iTagLen = 0;
		    bConstructed = false;
	    }
	}

	private class ValueLen {
	    int iLen;
	    int iLenLen;

	    void ValueLen() {
	        iLen = 0;
	        iLenLen = 0;
	    }
	}

    /**
     * Function fills the menu structure of MenuElements from a given TLV byte stream
     *
     * @param tagList
     * @param menu
     * @return
     */
    public int getTLVMenu(byte[] tagList, MenuElement menu) {
        if (menu == null)
            return -1;

		try {
            int i = 0;
		    Tag tag = null;
		    ValueLen valueLen = null;

			while (i < tagList.length)
			{
		        // get tag
		        tag = getTag (tagList, i, tagList.length);
		        if (tag == null)
		            return -2;

		        // get tag length
		        i += tag.iTagLen;

                // get value
                valueLen = getValueLen(tagList, i, tagList.length);
		        if (valueLen == null)
		            return -3;

		        // get length length
		        i += valueLen.iLenLen;

		    	// get VALUE
		        if (valueLen == null)
		            return -4;

                // check value length
				if (i + valueLen.iLen > tagList.length)
				    return -5;

				if (tag.bConstructed) {
                    // copy local value
                    byte[] baValue = new byte[valueLen.iLen];
                    for(int s=0; s < valueLen.iLen; s++) {
                        baValue[s] = tagList[i++];
                    }

                    if (tag.iTag == 0xFF8022) {                         // menu directory
                        MenuElement menuLocal = new MenuElement();
                        menuLocal.previousMenuElement = menu;
                        int iRet = getTLVMenu(baValue, menuLocal);
                        if (iRet != 0)  return iRet;
                        menu.submenu.add(menuLocal);
                    } else if (tag.iTag == 0xFF8023) {                  // menu item
                        MenuElement menuLocal = new MenuElement();
    					int iRet = getTLVMenu(baValue, menuLocal);
    					if (iRet != 0)  return iRet;
                        menu.submenu.add(menuLocal);
                    }

				} else {
				    long lValue = 0;
                    for(int s=0; s < valueLen.iLen; s++) {
                        lValue = (lValue * 0x0100) + (tagList[i++] & 0x000000FF);
                    }

					if (menu != null) {
                        if          (tag.iTag == 0xDF01)        menu.menu_item_id   = lValue;
                        else if     (tag.iTag == 0xDF8030)      menu.pw_level       = lValue;
                        else if     (tag.iTag == 0xDF8032)      menu.hidden         = lValue;
                        else if     (tag.iTag == 0xDF8033)      menu.number         = lValue;
                        else if     (tag.iTag == 0xDF8046)      menu.item_property  = lValue;
                        else if     (tag.iTag == 0xDF8048)      menu.pre_param_id   = lValue;
                        else if     (tag.iTag == 0xDF8049) {
                            menu.msg_text_id = lValue;
                            menu.msg_text = getMsgText((int)lValue);
                        }
				    }
				}
			}
        } catch (Exception e) {
	    	Log.d("TLV", "getTLVMenu: " + e.toString());
        	return -10;
        }

    	return 0;
    }

    public void listTLVMenu(MenuElement menu, int iLevel, StringBuilder strOut) {
        if (menu.menu_item_id >= 0)
            strOut.append(Utils.PadRight("", iLevel * 4 + 1) + menu.msg_text + " " + "[" + menu.msg_text_id + "] " + "\r\n" );
        else
            strOut.append(Utils.PadRight("", iLevel * 4 + 1) + "* " + menu.msg_text + " " + "[" + menu.msg_text_id + "] " + "\r\n" );

        for (int i=0; i < menu.submenu.size(); i++) {
            listTLVMenu(menu.submenu.get(i), iLevel + 1, strOut);
        }

        return;
    }

    public int getTLV(byte[] tagList, TLVContainer tlv) {
        if (tlv == null)
            return -1;

		try {
            int i = 0;
		    Tag tag = null;
		    ValueLen valueLen = null;

			while (i < tagList.length)
			{
		        // get tag
		        tag = getTag (tagList, i, tagList.length);
		        if (tag == null)
		            return -2;

		        // get tag length
		        i += tag.iTagLen;

                // get value
                valueLen = getValueLen(tagList, i, tagList.length);
		        if (valueLen == null)
		            return -3;

		        // get length length
		        i += valueLen.iLenLen;

		    	// check VALUE
		        if (valueLen == null)
		            return -4;

                // check value length
				if (i + valueLen.iLen > tagList.length)
				    return -5;

                // save TLV infos
                TLVContainer tlvLocal = new TLVContainer ();

                // tag
                tlvLocal.tag = tag.iTag;

				// value
				if (tag.bConstructed || tag.iTag == 0x0000DF53) {
                    // copy value
                    byte[] baValue = new byte[valueLen.iLen];
                    System.arraycopy(tagList, i, baValue , 0, valueLen.iLen);
                    i += valueLen.iLen;

                    int iRet = getTLV(baValue, tlvLocal);
                    if (iRet != 0)
                        return iRet;
				} else {
                    // copy local value
                    tlvLocal.baValue = new byte[valueLen.iLen];
                    System.arraycopy(tagList, i, tlvLocal.baValue, 0, valueLen.iLen);
                    i += valueLen.iLen;
				}

                // insert into list
                tlv.subcontainer.add(tlvLocal);
			}
        } catch (Exception e) {
	    	Log.d("TLV", "loadTLV: " + e.toString());
        	return -10;
        }

    	return 0;
    }

    public void listTLV(TLVContainer tlv, int iLevel, StringBuilder strOut) {
        if (tlv.baValue.length > 0) {
            strOut.append(Utils.PadRight("", iLevel * 4 + 1) + formatTag(tlv.tag) + " " + "[" + Utils.bytesToHex(tlv.baValue) + "] " + "\r\n");
        }
        else {
            strOut.append(Utils.PadRight("", iLevel * 4 + 1) + "* " + formatTag(tlv.tag) + "\r\n" );

            for (int i=0; i < tlv.subcontainer.size(); i++) {
                listTLV(tlv.subcontainer.get(i), iLevel + 1, strOut);
            }
        }

        return;
    }

    public int interpretTLV(byte[] tagList, Integer iLevel, StringBuilder strOut) {

		try {
		    int i = 0;
		    Tag tag = null;
		    ValueLen valueLen = null;
            byte[] baValue = null;

			// get tag + taglength + constructed
			while (i < tagList.length)
			{
		        // get tag
		        tag = getTag (tagList, i, tagList.length);
		        if (tag == null)
		            return -2;

		        // get tag length
		        i += tag.iTagLen;

                // get value
                valueLen = getValueLen(tagList, i, tagList.length);
		        if (valueLen == null)
		            return -3;

		        // get length length
		        i += valueLen.iLenLen;

		    	// get VALUE
		        if (valueLen == null)
		            return -4;

                // check value length
				if (i + valueLen.iLen > tagList.length)
				    return -5;

		    	// get VALUE
				baValue = new byte[valueLen.iLen];
				for(int s=0; s < valueLen.iLen; s++) {
					baValue[s] = tagList[i++];
				}

				if (tag.bConstructed || tag.iTag == 0x0000DF53 ) {
					strOut.append(Utils.PadRight("", iLevel * 4 + 1) + formatTag(tag.iTag) + formatTagName(tag.iTag) + formatLength(tag.iTagLen) + "\r\n" );
	
					interpretTLV(baValue, iLevel + 1, strOut);
				} else {
					strOut.append(Utils.PadRight("", iLevel * 4 + 1) + formatTag(tag.iTag) + formatTagName(tag.iTag) + formatLength(tag.iTagLen) + "=" +
							(isTagDisplay(tag.iTag) ? Utils.FilterString(new String(baValue, "Cp1252")) + " - " : ""   ) +  Utils.Byte2Hex(baValue) + "\r\n");
				}
			}
        } catch (Exception e) {
        	strOut.append(e.toString() + "\r\n");
        }
		
    	return 0;
    }

    public byte[] getTagValue(byte[] tagList, int getTag) {
		int i = 0;
		byte j = 0;
		int tag = 0;
		int tagLength = 0;
		byte[] baValue = null;
		boolean bConstructed = false;

		try {

			// get tag + taglength + constructed
			while (i < tagList.length)
			{
				// constructed tag?
				bConstructed = ((tagList[i] & 0x20) != 0) ? true : false;
				
				// TAG
				if((tagList[i] & 0x1F) == 0x1F)						//two or more byte tag
				{
					tag = (tagList[i++] & 0x000000FF);				// 1. byte
					tag = (tag * 0x100);
					tag = tag + (tagList[i] & 0x000000FF);			// 2. byte
					if((tagList[i] & 0x80) == 0x80)					//another byte follows?
					{
						tag = (tag * 0x100);
						tag = tag + (tagList[++i] & 0x000000FF);	// 3. byte
	
						if((tagList[i] & 0x80) == 0x80)				//another byte follows?
						{
							tag = (tag * 0x100);
							tag = tag + (tagList[++i] & 0x000000FF);// 4. byte
						}
					}
					i++;
				}
				else
				{
					tag = (tagList[i++] & 0x000000FF);
				}
	
		    	// get TLV length
				if((tagList[i] & 0x80) == 0x80)
				{
					for(tagLength=0, j=0; j < (tagList[i] & 0x7F); j++)
					{
						tagLength = (tagLength * 0x0100) + (tagList[i+j+1] & 0x000000FF);
					}
					i=i+j+1;
				}
				else
				{
					tagLength = (tagList[i]  & 0x000000FF);
					i++;
				}
	
		    	// get VALUE
				baValue = new byte[tagLength];
				for(int s=0; s<tagLength; s++) {
					baValue[s] = tagList[i++];
				}
				
				if (getTag == tag) {
					return baValue; 
				} 
	
				if (bConstructed) {
					byte[] baRet = getTagValue(baValue, getTag);
					if (baRet != null) {
						return baRet; 
					}
				}
			}
        } catch (Exception e) {
        	System.out.println("GetTagValue: " + e.toString());
        }
		
    	return null;
    }

    /**
     * Method crateTag delivers a TLV coded value
     * @param iTag
     * @param baValue
     * @return TLV coded value
     */
    public byte[] createTLV(int iTag, byte[] baValue){
        byte[] baRet;
        if (iTag > 0xFFFF)
            baRet = Utils.integerToByteArray3(iTag);
        else if (iTag > 0xFF)
            baRet = Utils.integerToByteArray2(iTag);
        else
            baRet = Utils.integerToByteArray1(iTag);

        baRet = Utils.AppendByteArray(baRet, makeLength(baValue.length));		// Length
        baRet = Utils.AppendByteArray(baRet, baValue);							// Value
        return baRet;
    }


	public byte[] createTag(int iTag, byte[] baValue) {
		
		try {
			byte[] baRet = Utils.integer2ByteArray(iTag);							// Tag
			baRet = Utils.AppendByteArray(baRet, makeLength(baValue.length));		// Length
			baRet = Utils.AppendByteArray(baRet, baValue);							// Value
			
			return baRet; 
	    } catch (Exception e) {
	    	Log.d("TLV", "createTag: " + e.toString());
			return null;
	    }
	}


    /**
     * Method appendTag
     * @param baTLV
     * @param iTag
     * @param baValue
     * @return TLV coded value appended on baTLV
     */
	public byte[] appendTag(byte[] baTLV, int iTag, byte[] baValue) {
		
		try {
			baTLV = Utils.AppendByteArray(baTLV, Utils.integer2ByteArray(iTag));	// Tag
			baTLV = Utils.AppendByteArray(baTLV, makeLength(baValue.length));		// Length
			baTLV = Utils.AppendByteArray(baTLV, baValue);							// Value
			
			return baTLV; 
	    } catch (Exception e) {
	    	Log.d("TLV", "appendTag: " + e.toString());
			return null;
	    }
	
	}

    public byte[] appendTLV(byte[] baTLV, int iTag, byte[] baValue){
        if(iTag > 0xFFFF)
            baTLV = Utils.AppendByteArray(baTLV, Utils.integerToByteArray3(iTag));	// Tag 3 bytes
        else if(iTag > 0xFF)
            baTLV = Utils.AppendByteArray(baTLV, Utils.integerToByteArray2(iTag));	// Tag 2 bytes
        else
            baTLV = Utils.AppendByteArray(baTLV, Utils.integerToByteArray1(iTag));  // Tag 1 byte

        baTLV = Utils.AppendByteArray(baTLV, makeLength(baValue.length));		// Length
        baTLV = Utils.AppendByteArray(baTLV, baValue);							// Value

        return baTLV;
    }

    private Tag getTag (byte[] tagList, int iPos, int iMaxPos) {

        int i = iPos;
        if (i >= iMaxPos)    return null;

        // return value
        Tag tagRet = new Tag();

        // constructed tag?
        tagRet.bConstructed = ((tagList[i] & 0x20) != 0) ? true : false;

        //two or more byte tag
        if((tagList[i] & 0x1F) == 0x1F)
        {
            tagRet.iTag = (tagList[i++] & 0x000000FF);				// 1. byte
            if (i >= iMaxPos)    return null;

            tagRet.iTag = (tagRet.iTag * 0x100);
            tagRet.iTag = tagRet.iTag + (tagList[i] & 0x000000FF);			// 2. byte

            if((tagList[i] & 0x80) == 0x80)					//another byte follows?
            {
                tagRet.iTag = (tagRet.iTag * 0x100);

                if (i + 1 >= iMaxPos)    return null;
                tagRet.iTag = tagRet.iTag + (tagList[++i] & 0x000000FF);	// 3. byte

                if((tagList[i] & 0x80) == 0x80)				//another byte follows?
                {
                    tagRet.iTag = (tagRet.iTag * 0x100);

                    if (i + 1 >= iMaxPos)    return null;
                    tagRet.iTag = tagRet.iTag + (tagList[++i] & 0x000000FF);// 4. byte
                }
            }
            i++;
        }

        // one byte tag
        else
        {
            tagRet.iTag = (tagList[i++] & 0x000000FF);
        }

        tagRet.iTagLen = (i - iPos);
        return tagRet;
    }

    private ValueLen getValueLen(byte[] tagList, int iPos, int iMaxPos) {
        int i = iPos;
        int j = 0;

        if (i >= iMaxPos)    return null;

        // return value
        ValueLen lenValue = new ValueLen();

        // get value length
        if((tagList[i] & 0x80) == 0x80)
        {
            int iLenLen = (tagList[i] & 0x7F);
            if (i + iLenLen >= iMaxPos)    return null;

            for(j=0; j < iLenLen; j++)
            {
                lenValue.iLen = (lenValue.iLen * 0x0100) + (tagList[i+j+1] & 0x000000FF);
            }
            i += (j+1);
        }
        else
        {
            lenValue.iLen = (tagList[i]  & 0x000000FF);
            i++;
        }

        lenValue.iLenLen = (i - iPos);
        return lenValue;
    }

	private byte[] makeLength(int iLen) {
		if (iLen < 0x80) {
			return new byte[] {(byte) iLen };
		} else if (iLen <= 0xFF) {
			return new byte[] {(byte) 0x81, (byte) iLen };
		} else if (iLen < 0xFFFF) {
			return new byte[] {(byte) 0x82, (byte) (iLen >> 8), (byte) (iLen % 256) };
		} else if (iLen < 0xFFFFFF) {
			return new byte[] {(byte) 0x83, (byte) (iLen >> 16), (byte) ((iLen >> 8) % 256), (byte) (iLen % 256) };
		} else  {
			return new byte[] {(byte) 0x84, (byte) (iLen >> 24), (byte) ((iLen >> 16) % 256), (byte) ((iLen >> 8) % 256), (byte) (iLen % 256) };
		}
	}
    
    private String getTagName2(int iTag) {
        switch (iTag)
        {
case  0x0000E0:  return "RIA Attribute";
case  0x0000E1:  return "RIA Parameter";
case  0x0000E2:  return "Smart Client Configuration";
case  0x0000E3:  return "Smart Client Parameter";
case  0x0000E4:  return "PINPad Request";
case  0x0000E5:  return "PINPad Response";
case  0x0000E6:  return "User data from ECR";
case  0x0000E7:  return "User data to ECR";
case  0x0000EF:  return "Message splitting container";
case  0x00DF01:  return "menu_item_id";
case  0x00DF02:  return "requestid";
case  0x00DF03:  return "companyno";
case  0x00DF04:  return "storeno";
case  0x00DF05:  return "posno";
case  0x00DF06:  return "printer-ready";
case  0x00DF07:  return "sessionID";
case  0x00DF08:  return "offline";
case  0x00DF09:  return "time_to_idle";
case  0x00DF10:  return "split_header";
case  0x00DF11:  return "split_body";
case  0x00DF20:  return "account";
case  0x00DF21:  return "add_data";
case  0x00DF22:  return "aid";
case  0x00DF23:  return "amount";
case  0x00DF24:  return "blz";
case  0x00DF25:  return "bonus_points";
case  0x00DF26:  return "card_number";
case  0x00DF27:  return "currency";
case  0x00DF28:  return "currency_exp";
case  0x00DF29:  return "exp_month";
case  0x00DF2A:  return "exp_year";
case  0x00DF2B:  return "note";
case  0x00DF2C:  return "ref_number";
case  0x00DF2D:  return "ref_number_receipt";
case  0x00DF2F:  return "ta_date";
case  0x00DF30:  return "ta_time";
case  0x00DF31:  return "terminal_id";
case  0x00DF32:  return "track1";
case  0x00DF33:  return "track2";
case  0x00DF34:  return "track3";
case  0x00DF35:  return "trace_number";
case  0x00DF36:  return "receipt_number";
case  0x00DF37:  return "amount_total";
case  0x00DF38:  return "language_cardholder";
case  0x00DF39:  return "language_merchant";
case  0x00DF70:  return "pre_param_slot 0";
case  0x00DF71:  return "pre_param_slot 1";
case  0x00DF72:  return "pre_param_slot 2";
case  0x00DF73:  return "pre_param_slot 3";
case  0x00DF74:  return "pre_param_slot 4";
case  0x00DF75:  return "pre_param_slot 5";
case  0x00DF76:  return "pre_param_slot 6";
case  0x00DF77:  return "pre_param_slot 7";
case  0x00DF78:  return "pre_param_slot 8";
case  0x00DF79:  return "pre_param_slot 9";
case  0xDF8001:  return "setup_version";
case  0xDF8002:  return "last_error";
case  0xDF8003:  return "last_error_tag";
case  0xDF8004:  return "last_error_text";
case  0xDF8005:  return "ecr_version";
case  0xDF8006:  return "pp_version";
case  0xDF8007:  return "pp_snr";
case  0xDF8008:  return "pp_vendor";
case  0xDF8009:  return "timer_version";
case  0xDF8010:  return "printer_width";
case  0xDF8011:  return "capabilities";
case  0xDF8012:  return "local_version";
case  0xDF8013:  return "language_merchant";
case  0xDF8014:  return "sc_settings";
case  0xDF8015:  return "task_data";
case  0xDF8020:  return "timeout";
case  0xDF8022:  return "pw_title";
case  0xDF8023:  return "pw_cashier_title";
case  0xDF8024:  return "pw_mer_title";
case  0xDF8025:  return "pw_service_title";
case  0xDF8026:  return "pw_cashier_val";
case  0xDF8027:  return "pw_mer_val";
case  0xDF8028:  return "pw_service_val";
case  0xDF8030:  return "pw_level";
case  0xDF8031:  return "title";
case  0xDF8032:  return "hidden";
case  0xDF8033:  return "number";
case  0xDF8034:  return "timer_ID";
case  0xDF8035:  return "minute";
case  0xDF8036:  return "hour";
case  0xDF8037:  return "day_of_month";
case  0xDF8038:  return "month";
case  0xDF8039:  return "day_of_week";
case  0xDF803A:  return "timer_data";
case  0xDF803B:  return "timer_data_format";
case  0xDF8040:  return "eft_param_tag";
case  0xDF8041:  return "param_type";
case  0xDF8042:  return "param_tag";
case  0xDF8043:  return "param_man";
case  0xDF8044:  return "param_min";
case  0xDF8045:  return "param_max";
case  0xDF8046:  return "item_property";
case  0xDF8047:  return "signature";
case  0xDF8048:  return "pre_param_id";
case  0xDF8049:  return "msg_text_id";
case  0xDF804A:  return "text_table";
case  0xDF804B:  return "pw_value";
case  0xDF8201:  return "request";
case  0xDF8202:  return "response";
case  0xDF8203:  return "peripheral";
case  0xDF8204:  return "code_table";
case  0xDF8205:  return "trx_status";
case  0xDF8206:  return "local_appl_id";
case  0xDF8207:  return "local_appl_on";
case  0xDF8208:  return "header_line";
case  0xDF8209:  return "max_offline_trx";
case  0xDF820A:  return "floor_limit";
case  0xDF820B:  return "account_card";
case  0xDF820C:  return "floor_cur_code";
case  0xDF820D:  return "floor_cur_exp";
case  0xDF8210:  return "ecr_user_data";
case  0xDF8211:  return "ecr_user_data_format";
case  0xFF8020:  return "vendor_data";
case  0xFF8021:  return "menu_property";
case  0xFF8022:  return "menu_directory";
case  0xFF8023:  return "menu_item";
case  0xFF8024:  return "timer";
case  0xFF8025:  return "pre_task";
case  0xFF8026:  return "pre_param";
case  0xFF8027:  return "local_appl";
case  0xFF8028:  return "receipt_header";
case  0xFF8029:  return "secured_text_def";
case  0xFF802A:  return "pw_def";
default: return "????";
        }
    }

    private String getTagName(int iTag) {
        switch (iTag)
        {
case  0x000042:  return   "Issuer Identification Number (IIN)"            ;
case  0x00004F:  return   "Application Identifier (AID) card"             ;
case  0x000050:  return   "Application Label"                             ;
case  0x000057:  return   "Track 2 Equivalent Data"                       ;
case  0x00005A:  return   "Application PAN"                               ;
case  0x000056:  return   "Track1_data"				                      ;
case  0x000061:  return   "Application Template"                          ;
case  0x00006F:  return   "File Control Inf.(FCI) Template"               ;
case  0x000071:  return   "Issuer Script Template 1"                      ;
case  0x000072:  return   "Issuer Script Template 2"                      ;
case  0x000073:  return   "Directory Discretionary Template"              ;
case  0x000077:  return   "Response Message Template Format 2"            ;
case  0x000080:  return   "Response Message Template Format 1"            ;
case  0x000081:  return   "Amount Authorised (Binary)"                    ;
case  0x000082:  return   "Application Interchange Profile"               ;
case  0x000083:  return   "Command Template"                              ;
case  0x000084:  return   "Dedicated File (DF) Name"                      ;
case  0x000086:  return   "Issuer Script Command"                         ;
case  0x000087:  return   "Application Priority Indicator"                ;
case  0x000088:  return   "Short File Identifier (SFI)"                   ;
case  0x000089:  return   "Authorisation Code"                            ;
case  0x00008A:  return   "Authorisation Response Code"                   ;
case  0x00008C:  return   "Card Risk Data Object List 1 (CDOL1)"          ;
case  0x00008D:  return   "Card Risk Data Object List 2 (CDOL2)"          ;
case  0x00008E:  return   "Cardholder Verif. Method (CVM) List"           ;
case  0x00008F:  return   "Certification Authority Public Key Index"      ;
case  0x000090:  return   "Issuer Public Key Certificate"                 ;
case  0x000091:  return   "Issuer Authentication Data"                    ;
case  0x000092:  return   "Issuer Public Key Remainder"                   ;
case  0x000093:  return   "Signed Static Application Data"                ;
case  0x000094:  return   "Application File Locator (AFL)"                ;
case  0x000095:  return   "Terminal Verification Results"                 ;
case  0x000097:  return   "TX Certificate Data Object List (TDOL)"        ;
case  0x000098:  return   "TX Certificate (TC) Hash Value"                ;
case  0x000099:  return   "TX Personal Identification Number (PIN) Data"  ;
case  0x00009A:  return   "TX Date"                                       ;
case  0x00009B:  return   "TX Status Information"                         ;
case  0x00009C:  return   "TX Type"                                       ;
case  0x00009D:  return   "Directory Definition File (DDF) Name"          ;
case  0x0000A5:  return   "File Control Inf.(FCI) Proprietary Template"   ;
case  0x0000E0:  return   "Terminalspezifische Konfigurationsdaten"       ;
case  0x0000E1:  return   "Zahlungssystemspezifische Konfigurationsdaten" ;
case  0x0000E2:  return   "Anwendungsspezifische Konfigurationsdaten"     ;
case  0x0000EE:  return   "Anwendungsspezifische Terminal-Datenobjekte"   ;
case  0x0000F0:  return   "Vorrang-Anwendungen"                           ;
case  0x0000F1:  return   "Identifizierende Magnetstreifendaten"          ;
case  0x0000F8:  return   "Zugeordnete magnetstreifenbasierte Anwendung"  ;
case  0x0000F9:  return   "Präfix ggf. mit Gültigkeitsangabe"             ;
case  0x0000FB:  return   "Konfiguration für kontaktlose EMV-Anwendung"   ;
case  0x005F20:  return "Cardholder Name"                            ;
case  0x005F24:  return "Application Expiration Date"                ;
case  0x005F25:  return "Application Effective Date"                 ;
case  0x005F28:  return "Issuer Country Code"                        ;
case  0x005F2A:  return "TX Currency Code"                           ;
case  0x005F2D:  return "Language Preference"                        ;
case  0x005F30:  return "Service Code"                               ;
case  0x005F34:  return "Application PAN Sequence Number"            ;
case  0x005F36:  return "TX Currency Exponent"                       ;
case  0x005F50:  return "Issuer URL"                                 ;
case  0x005F53:  return "International Bank Account Number (IBAN)"   ;
case  0x005F54:  return "Bank Identifier Code (BIC)"                 ;
case  0x005F55:  return "Issuer Country Code (alpha2 format)"        ;
case  0x005F56:  return "Issuer Country Code (alpha3 format)"        ;
case  0x009F01:  return "Acquirer Identifier"                        ;
case  0x009F02:  return "Amount Authorised(Numeric)"                 ;
case  0x009F03:  return "Amount Other (Numeric)"                     ;
case  0x009F04:  return "Amount Other (Binary)"                      ;
case  0x009F05:  return "Application Discretionary Data"             ;
case  0x009F06:  return "Application Identifier (AID) terminal"      ;
case  0x009F07:  return "Application Usage Control"                  ;
case  0x009F08:  return "Application Version Number"                 ;
case  0x009F09:  return "Application Version Number"                 ;
case  0x009F0B:  return "Cardholder Name Extended"                   ;
case  0x009F0D:  return "Issuer Action Code - Default"               ;
case  0x009F0E:  return "Issuer Action Code - Denial"                ;
case  0x009F0F:  return "Issuer Action Code - Online"                ;
case  0x009F10:  return "Issuer Application Data"                    ;
case  0x009F11:  return "Issuer Code Table Index"                    ;
case  0x009F12:  return "Application Preferred Name"                 ;
case  0x009F13:  return "Last Online Application TX Counter"         ;
case  0x009F14:  return "Lower Consecutive Offline Limit"            ;
case  0x009F15:  return "Merchant Category Code"                     ;
case  0x009F16:  return "Merchant Identifier"                        ;
case  0x009F17:  return "PIN Try Counter"                            ;
case  0x009F18:  return "Issuer Script Identifier"                   ;
case  0x009F1A:  return "Terminal Country Code"                      ;
case  0x009F1B:  return "Terminal Floor Limit"                       ;
case  0x009F1C:  return "Terminal Identification"                    ;
case  0x009F1D:  return "Terminal Risk Management Data"              ;
case  0x009F1E:  return "Interface Device (IFD) Serial Number"       ;
case  0x009F1F:  return "Track 1 Discretionary Data"                 ;
case  0x009F20:  return "Track 2 Discretionary Data"                 ;
case  0x009F21:  return "TX Time"                                    ;
case  0x009F22:  return "Certification Authority Public Key Index"   ;
case  0x009F23:  return "Upper Consecutive Offline Limit"            ;
case  0x009F26:  return "Application Cryptogram"                     ;
case  0x009F27:  return "Cryptogram Information Data"                ;
case  0x009F2D:  return "ICC PIN Encipherment Public Key Certificate";
case  0x009F2E:  return "ICC PIN Encipherment Public Key Exponent"   ;
case  0x009F2F:  return "ICC PIN Encipherment Public Key Remainder"  ;
case  0x009F32:  return "Issuer Public Key Exponent"                 ;
case  0x009F33:  return "Terminal Capabilities"                      ;
case  0x009F34:  return "Cardholder Verif. Method (CVM) Results"     ;
case  0x009F35:  return "Terminal Type"                              ;
case  0x009F36:  return "Application Transaction Counter (ATC)"      ;
case  0x009F37:  return "Unpredictable Number"                       ;
case  0x009F38:  return "Processing Options Data List (PDOL)"        ;
case  0x009F39:  return "Point-of-Service (POS) Entry Mode"          ;
case  0x009F3A:  return "Amount Reference Currency"                  ;
case  0x009F3B:  return "Application Reference Currency"             ;
case  0x009F3C:  return "TX Reference Currency Code"                 ;
case  0x009F3D:  return "TX Reference Currency Exponent"             ;
case  0x009F40:  return "Additional Terminal Capabilities"           ;
case  0x009F41:  return "TX Sequence Counter"                        ;
case  0x009F42:  return "Application Currency Code"                  ;
case  0x009F43:  return "Application Reference Currency Exponent"    ;
case  0x009F44:  return "Application Currency Exponent"              ;
case  0x009F45:  return "Data Authentication Code"                   ;
case  0x009F46:  return "ICC Public Key Certificate"                 ;
case  0x009F47:  return "ICC Public Key Exponent"                    ;
case  0x009F48:  return "ICC Public Key Remainder"                   ;
case  0x009F49:  return "Dynamic Data Auth. Object List (DDOL)"      ;
case  0x009F4A:  return "Static Data Authentication Tag List"        ;
case  0x009F4B:  return "Signed Dynamic Application Data"            ;
case  0x009F4C:  return "ICC Dynamic Number"                         ;
case  0x009F4D:  return "Log Entry"                                  ;
case  0x009F4E:  return "Merchant Name and Location"                 ;
case  0x009F4F:  return "Log Format"                                 ;
case  0x009F53:  return "Transaction Category Code (TCC)"            ;
case  0x009F5A:  return "Application Program Identifier"		     ;
case  0x009F5D:  return "AOSA"					                     ;
case  0x009F66:  return "Terminal Transaction Qualifiers"		     ;
case  0x009F6B:  return "Track2_data"				                 ;
case  0x009F6E:  return "FFI"					                     ;
case  0x009F7C:  return "CED"					                     ;
case  0x00BF0C:  return "File Control Inf.(FCI) Issuer Disc. Data"   ;
case  0x00DF01:  return "Kernel Identifier | Issuer Script Results )";
case  0x00DF02:  return "Status Check Support |Fehlerkennung"        ;
case  0x00DF03:  return "Zero Amount Allowed flag"			         ;
case  0x00DF04:  return "Reader Ctls Trans. Limit | Aktionsdatenobjekt" ;
case  0x00DF05:  return "Reader Contactless Floor Limit"		     ;
case  0x00DF06:  return "Reader CVM Required Limit | ID der MSR Anwendung (MID)";
case  0x00DF07:  return "Terminal Transaction Qualifiers"			   ;
case  0x00DF08:  return "Extended Selection Support Flag"			   ;
case  0x00DF0A:  return "Application Label (Default)"                   ;
case  0x00DF0B:  return "ASI"                                           ;
case  0x00DF0C:  return "DDOL (Default)"                                ;
case  0x00DF0D:  return "TDOL (Default)"                                ;
case  0x00DF0E:  return "Schwellenwert für die zufällige Online-Wahl"   ;
case  0x00DF0F:  return "maximaler Zielprozentsatz für die Onlinewahl"  ;
case  0x00DF10:  return "Zielprozentsatz für die zufällige Online-Wahl" ;
case  0x00DF11:  return "TAC-Ablehnung"                                 ;
case  0x00DF12:  return "TAC-Online"                                    ;
case  0x00DF13:  return "TAC-Default"                                   ;
case  0x00DF14:  return "Floor-Limit für MSR Transaktionen"             ;
case  0x00DF15:  return "Händler-ID"                                    ;
case  0x00DF18:  return "Fall-Back-Parameter"                           ;
case  0x00DF19:  return "Notwendige Datenobjekte der Karte"             ;
case  0x00DF1A:  return "Reaktion bei Karte unterst. Inhaber Auth.nicht";
case  0x00DF1B:  return "Terminal- Konfigurationswährung mit Exponent"  ;
case  0x00DF1C:  return "Terminal- Konfigurationswährung mit Exponent"  ;
case  0x00DF22:  return "Zugelassene spezielle Transaktionen"           ;
case  0x00DF23:  return "Maximale Anzahl an Offline-Transaktionen"      ;
case  0x00DF25:  return "Beleg-Parameter"                               ;
case  0x00DF26:  return "Floor-Limit für chipbasierte Transaktionen"    ;
case  0x00DF27:  return "Einschränkung der 'DF27' '03' 88 Terminalfäh." ;
case  0x00DF28:  return "Einschränkung der 'DF27' '03' 88 Terminalfäh." ;
case  0x00DF29:  return "Kennung des Hash- Algorithmus der CA"          ;
case  0x00DF2A:  return "Konfig. der Produkt-Anzeige und der Sprachen"  ;
case  0x00DF2C:  return "Präfix"                                        ;
case  0x00DF2D:  return "Aktivierungsdatum"                             ;
case  0x00DF2E:  return "Verfalldatum"                                  ;
case  0x00DF2F:  return "Freigabe der vorgezogenen PIN Eingabe"         ;
case  0x00DF30:  return "Kennung des Signatur- Algorithmus der CA "     ;
case  0x00DF31:  return "Modulus des öff. Schlüssels der CA"            ;
case  0x00DF32:  return "Exponent des öff. Schlüssels der CA"           ;
case  0x00DF33:  return "Checksum zur Integritätssicherung4"            ;
case  0x00DF34:  return "RID"                                           ;
case  0x00DF35:  return "Ländercode der Anwendung"                      ;
case  0x00DF39:  return "Journal-DOL"                                   ;
case  0x00DF40:  return "Beleg-DOL für H.belege für Online-TX"          ;
case  0x00DF41:  return "Beleg-DOL für H.belege für aut.Offline- TX"    ;
case  0x00DF42:  return "Beleg-DOL für H.belege für abgelehnte Offl. TX";
case  0x00DF43:  return "Beleg-DOL für K.belege für Online-TX"          ;
case  0x00DF44:  return "Beleg-DOL für K.belege für autor. Offline-TX"  ;
case  0x00DF45:  return "Beleg-DOL für K.belege für abgelehnte Offl. TX";
case  0x00DF46:  return "Notwendige Spur(en) des Magnetstreifens"       ;
case  0x00DF47:  return "Fehlversuch-Parameter"                         ;
case  0x00DF4F:  return "Konfigurationskontrollfeld"                    ;
case  0x00DF51:  return "cmd_head_req"                                  ;
case  0x00DF52:  return "cmd_head_resp"                                 ;
case  0x00DF53:  return "cmd_body"                                      ;
case  0x00DF54:  return "pp_status"                                     ;
case  0x00DF55:  return "debug_info"                                    ;
case  0x00DF56:  return "challenge1"                                    ;
case  0x00DF57:  return "secret1"                                       ;
case  0x00DF58:  return "challenge2"                                    ;
case  0x00DF59:  return "secret2"                                       ;
case  0x00DF5A:  return "auth_status"                                   ;
case  0x00DF5B:  return "trx_status"                                    ;
case  0x00DF5C:  return "key_index"                                     ;
case  0x00DF5D:  return "key_set_nr"                                    ;
case  0x00DF5E:  return "key_algo_id"                                   ;
case  0x00DF5F:  return "trx_type"                                      ;
case  0x00DF60:  return "trx_final_result"                              ;
case  0x00DF61:  return "pin_length"                                    ;
case  0x00DF62:  return "pac"                                           ;
case  0x00DF63:  return "mac"                                           ;
case  0x00DF64:  return "key_info"                                      ;
case  0x00DF65:  return "trx_message"                                   ;
case  0x00DF66:  return "pp_identification"                             ;
case  0x00DF67:  return "pp_reader_type"                                ;
case  0x00DF68:  return "trx_pay_system"                                ;
case  0x00DF69:  return "pp_stat_inf"                                   ;
case  0x00DF6A:  return "amount_tip"                                    ;
case  0x00DF6B:  return "amount_goods_and_services"                     ;
case  0x00DF6C:  return "key_version_nr"				;
case  0x00DF70:  return "card_types"                                    ;
case  0x00DF71:  return "card_direction"                                ;
case  0x00DF72:  return "card_icc_slot"                                 ;
case  0x00DF73:  return "icc_apdu_req"                                  ;
case  0x00DF74:  return "icc_apdu_res"                                  ;
case  0x00DF75:  return "atr"                                           ;
case  0x00DF76:  return "track3_data"                                   ;
case  0x00DF77:  return "track2_data"                                   ;
case  0x00DF78:  return "track1_data"                                   ;
case  0x00DF79:  return "expiration_date"                               ;
case  0x00DF7A:  return "mask_pan"					;
case  0x00DF7B:  return "emv_ctl_params"				;
case  0x00DF7C:  return "emv_ctl_params_paypass_30"			;
case  0x00FF51:  return "message"                                       ;
case  0x00FF52:  return "item_list"                                     ;
case  0x00FF53:  return "candidate"                                     ;
case  0x00FF54:  return "emv_log"                                       ;
case  0x00FF56:  return "follow_up_dspl"                                ;
case  0x00FF57:  return "follow_up_dspl"                                ;
case  0x00FF58:  return "emv_ctl_cfg_in"                                ;
case  0x00FF59:  return "emv_ctl_trx_data"                              ;
case  0x00FF5A:  return "emv_ctl_trx_result"                            ;
case  0x00FF60:  return "encrypt_container_pre_mac"                     ;
case  0x00FF61:  return "mac_container"				                    ;
case  0x00FF62:  return "encrypt_container_port_mac"			        ;
case  0x00FF63:  return "emv_ctl_trx_data_record"			            ;
case  0x00FF64:  return "emv_ctl_drl_set"				                ;
case  0xDF8000:  return "msg_area"                                      ;
case  0xDF8001:  return "msg_text_id"                                   ;
case  0xDF8002:  return "msg_text"                                      ;
case  0xDF8003:  return "msg_code_table"                                ;
case  0xDF8004:  return "msg_signature"                                 ;
case  0xDF8005:  return "layout_id"                                     ;
case  0xDF8006:  return "timeout"                                       ;
case  0xDF8007:  return "beep"                                          ;
case  0xDF8008:  return "msg_type"                                      ;
case  0xDF8009:  return "msg_option"                                    ;
case  0xDF800A:  return "choice"                                        ;
case  0xDF800B:  return "item_pre_sel"                                  ;
case  0xDF800C:  return "item_sel"                                      ;
case  0xDF800D:  return "item_pre_sel_list"                             ;
case  0xDF800E:  return "item_sel_list"                                 ;
case  0xDF800F:  return "sld_min"                                       ;
case  0xDF8100:  return "sld_max"                                       ;
case  0xDF8101:  return "sld_value"                                     ;
case  0xDF8102:  return "in_value_type"                                 ;
case  0xDF8103:  return "in_digit_min"                                  ;
case  0xDF8104:  return "balance_before | in_digit_max"                 ;
case  0xDF8105:  return "balance_after | in_echo"                       ;
case  0xDF8106:  return "in_num_keys"                                   ;
case  0xDF8107:  return "mm_data"                                       ;
case  0xDF8108:  return "mm_slot_nr"                                    ;
case  0xDF8109:  return "color"                                         ;
case  0xDF810A:  return "in_anum_keys"                                  ;
case  0xDF810B:  return "font"                                          ;
case  0xDF810C:  return "kernel_id | in_digit_confirm"                  ;
case  0xDF810D:  return "mm_d_format"				;
case  0xDF810E:  return "list_mode"					;
case  0xDF8117:  return "data_in_cap"				;
case  0xDF8118:  return "cvm_req"					;
case  0xDF8119:  return "cvm_no_req"					;
case  0xDF811F:  return "security_cap"				;
case  0xDF811A:  return "udol"					;
case  0xDF811B:  return "kernel_cfg"					;
case  0xDF811E:  return "cvm_msr_req"				;
case  0xDF812C:  return "cvm_msr_no_req"				;
case  0xDF811C:  return "torn_max_time"				;
case  0xDF811D:  return "torn_max_num"				;
case  0xDF8131:  return "phone_msg"					;
case  0xDF8123:  return "ctl_floorlimit"				;
case  0xDF8124:  return "ctl_trx_limit_no_odcv"			;
case  0xDF8125:  return "ctl_trx_limit_odcv"				;
case  0xDF8126:  return "cvm_req_limit"				;
case  0xDF8200:  return "emv_sel_mode"                                  ;
case  0xDF8201:  return "emv_log_req"                                   ;
case  0xDF8203:  return "emv_hit_list"                                  ;
case  0xDF8204:  return "emv_trx_dol"                                   ;
case  0xDF8205:  return "emv_force_onl"                                 ;
case  0xDF8206:  return "emv_amount_recent"                             ;
case  0xDF8207:  return "emv_cfg_in"                                    ;
case  0xDF8208:  return "emv_host_response_code"                        ;
case  0xDF8209:  return "emv_input_list"                                ;
case  0xDF820A:  return "emv_sel_result"                                ;
case  0xDF820B:  return "amount_conf"                                   ;
case  0xDF820C:  return "emv_ctl_conf_id"                               ;
case  0xDF820D:  return "RFU"					;
case  0xDF820E:  return "emv_ctl_trx_status"				;
case  0xDF820F:  return "emv_ctl_trx_start"				;
case  0xDF8210:  return "emv_ctl_trx_online_response_data"		;
case  0xDF8211:  return "emv_ctl_trx_cvm"				;
case  0xDF8212:  return "emv_ctl_trx_ui_req_on_outcome"		;
case  0xDF8213:  return "emv_ctl_trx_ui_req_on_restart"		;
case  0xDF8214:  return "emv_ctl_trx_data_record_present"		;
case  0xDF8215:  return "emv_ctl_trx_discretionary_data_present"	;
case  0xDF8216:  return "emv_ctl_trx_alter_interface_pref"		;
case  0xDF8217:  return "emv_ctl_trx_receipt"			;
case  0xDF8218:  return "emv_ctl_trx_field_off_request"		;
case  0xDF8219:  return "emv_ctl_trx_removal_timeout"		;
case  0xDF8300:  return "adm_info_dol"                                  ;
case  0xDF8301:  return "snr_ext_key"                                   ;
case  0xDF8302:  return "dll_request"                                   ;
case  0xDF8303:  return "dll_response"                                  ;
case  0xDF8305:  return "app_version"                                   ;
case  0xDF8306:  return "app_name"                                      ;
case  0xDF8307:  return "pp_identification_ext_key"                     ;
case  0xDF8308:  return "app_version_ext_key"                           ;
case  0xDF8309:  return "acq_net_id"                                    ;
case  0xDF830A:  return "apas_version"                                  ;
case  0xDF830B:  return "fraud_events"                                  ;
case  0xDFA000:  return "in_termination_key"                            ;
case  0xDFA001:  return "reset_key_buffer"                              ;
case  0xDFA002:  return "retry_counter"                                 ;
case  0xDFA003:  return "interface_sel"                                 ;
case  0xDFA004:  return "ip_address"                                    ;
case  0xDFA005:  return "ip_port"                                       ;
case  0xDFA006:  return "isdn_prot"                                     ;
case  0xDFA007:  return "isdn_msn"                                      ;
case  0xDFA008:  return "isdn_tei"                                      ;
case  0xDFA009:  return "isdn_x25_no"                                   ;
case  0xDFA00A:  return "isdn_user_data"                                ;
case  0xDFA00B:  return "dial_no"                                       ;
case  0xDFA00C:  return "dial_prefix"                                   ;
case  0xDFA00D:  return "dial_pre_sel"                                  ;
case  0xDFA00E:  return "mdm_dial_type"                                 ;
case  0xDFA00F:  return "mdm_tone_det"                                  ;
case  0xDFA010:  return "mdm_dial_wait"                                 ;
case  0xDFA011:  return "mdm_prot"                                      ;
case  0xDFA012:  return "job_no"                                        ;
case  0xDFA020:  return "activate_function_keys"                        ;
case  0xDFA021:  return "in_field_len"                                  ;
case  0xDFA022:  return "crd_status_info"                               ;
case  0xDFC000:  return "text_table"                                    ;
case  0xFFA000:  return "ip_params"                                     ;
case  0xFFA001:  return "isdn_params"                                   ;
case  0xFFA002:  return "modem_params"                                  ;
default: return "????";
        }
    }

    private String getMsgText(int  msg_text_id) {
        switch (msg_text_id) {
            case 0x000001: return "SW-DL Hersteller";
            case 0x000002: return "Jahr";
            case 0x000003: return "Zahlungsart";
            case 0x000004: return "Papiervorschub";
            case 0x000005: return "Tracenummer";
            case 0x000006: return "Warengruppe";
            case 0x000007: return "Gutschrift";
            case 0x000008: return "nach Erweiterung";
            case 0x000009: return "Kontoabfrage";
            case 0x00000A: return "Zahlung";
            case 0x00000B: return "Pay@Table";
            case 0x00000C: return "Trinkgeld-Zahlung";
            case 0x00000D: return "Händler";
            case 0x00000E: return "Umsatzerfassung";
            case 0x00000F: return "Wartung";
            case 0x000010: return "Pay and Call";
            case 0x000011: return "Tel. Rückfrage";
            case 0x000012: return "Kassenbericht";
            case 0x000013: return "Pay at Table";
            case 0x000014: return "Betrag";
            case 0x000015: return "Gesamtsumme";
            case 0x000016: return "Kassierer";
            case 0x000017: return "Passwort-Eingabe";
            case 0x000018: return "Gutscheinkarte Aufladung";
            case 0x000019: return "Beleg wiederholen";
            case 0x00001A: return "Tischnummer";
            case 0x00001B: return "Aktivierung";
            case 0x00001C: return "Karten-Nr";
            case 0x00001D: return "Monat";
            case 0x00001E: return "Trinkgeld";
            case 0x00001F: return "Aktionen";
            case 0x000020: return "EMV Diagnose";
            case 0x000021: return "Diagnosen";
            case 0x000022: return "Erweiterte Diagnose";
            case 0x000023: return "Endsumme";
            case 0x000024: return "Belegnummer";
            case 0x000025: return "Storno";
            case 0x000026: return "Transaktion";
            case 0x000027: return "Kassenschnitt";
            case 0x000028: return "Gutscheinkarte";
            case 0x000029: return "Techniker";
            case 0x00002A: return "Konfig Diagnose";
            case 0x00002B: return "Reservierung";
            case 0x00002C: return "Gesamtsumme";
            case 0x00002D: return "Gutschrift Erfassung";
            case 0x00002E: return "Initialisierung";
            case 0x00002F: return "BSW";
            case 0x000030: return "ValueMaster";
            case 0x000031: return "nach Reservierung";
            case 0x000032: return "Inbetriebnahme";
            case 0x000033: return "AID";
            case 0x000034: return "Gutschrift Pay@Table";
            case 0x000035: return "Erstreservierung";
            case 0x000036: return "Lastschrift onl. (TSD)";
            case 0x000037: return "Aufladung";
            case 0x000038: return "Dummy";
            case 0x000039: return "Hauptmenü";
            case 0x00003A: return "Trinkgeld";
            case 0x00003B: return "girocard";
            case 0x00003C: return "ELV";
            case 0x00003D: return "Verbindung testen";
            case 0x00003E: return "Trinkgeld";
            case 0x00003F: return "Erweiterung";
        }

        return "???";
    }

	private boolean isTagDisplay(int iTag) {
		switch (iTag) {
		   case 0x00DF8305: return true;
		   case 0x00DF8306: return true;
		   case 0x0000DF66: return true;
		   case 0x00009F1C: return true;
		   case 0x00009F1E: return true;
		}
		
		return false;
	}

    private String formatTagName(Integer iTag) {
    	return Utils.PadRight("'" + getTagName2(iTag) + "'",35);
    }

    private String formatTag(int iTag) {
    	return Utils.PadRight(Integer.toHexString(iTag).toUpperCase(), 8);
    }

    private  String formatLength(Integer iLen) {
    	return "(" + Utils.PadLeft(iLen.toString(), 2) + ")";
    }
}
