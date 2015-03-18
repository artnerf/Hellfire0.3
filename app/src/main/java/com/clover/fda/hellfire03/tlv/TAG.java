package com.clover.fda.hellfire03.tlv;

/**
 * Created by firstdata on 13.03.15.
 * Defintions of Tags
 */
public class TAG {
    public static final int NO_TAG     = 0x00;




    public static final int E0_ROOT = 0xE0;
    public static final int E1_ROOT = 0xE1;
    public static final int E2_ROOT = 0xE2;
    public static final int E3_ROOT = 0xE3;
            /*
#define TAG_E4_ROOT                0xE4
#define TAG_E5_ROOT                0xE5
#define TAG_E6_ROOT                0xE6
#define TAG_E7_ROOT                0xE7
#define TAG_EF_ROOT                0xEF

 */
    public static final int MENU_ITEM_ID  = 0xDF01;
    public static final int REQUEST_ID    = 0xDF02;
    public static final int COMPANY_NO    = 0xDF03;
    public static final int STORE_NO      = 0xDF04;
    public static final int POS_NO        = 0xDF05;
    public static final int PRINTER_READY = 0xDF06;
            /*
#define TAG_OFFLINE              0xDF08

#define TAG_SPLIT_HEADER         0xDF10
#define TAG_SPLIT_BODY           0xDF11

#define TAG_ACCOUNT              0xDF20
#define TAG_ADD_DATA             0xDF21
#define TAG_AID                  0xDF22
#define TAG_AMOUNT               0xDF23
#define TAG_BLZ                  0xDF24
#define TAG_BONUS_POINTS         0xDF25
#define TAG_CARD_NUMBER          0xDF26
*/
    public static final int CURRENCY     = 0xDF27;
    public static final int CURRENCY_EXP = 0xDF28;
            /*
#define TAG_EXP_MONTH            0xDF29
#define TAG_EXP_YEAR             0xDF2A
#define TAG_NOTE                 0xDF2B
#define TAG_REF_NUMBER           0xDF2C
#define TAG_REF_NUMBER_RECEIPT   0xDF2D
#define TAG_TA_DATE              0xDF2F
#define TAG_TA_TIME              0xDF30
*/
    public static final int TERMINAL_ID = 0xDF31;
            /*
#define TAG_TRACK1               0xDF32
#define TAG_TRACK2               0xDF33
#define TAG_TRACK3               0xDF34
#define TAG_TRACE_NUM            0xDF35
#define TAG_RECEIPT_NUM          0xDF36
#define TAG_AMOUNT_TOTAL         0xDF37
#define TAG_LANGUAGE_CARDHOLDER  0xDF38
*/
    public static final int LANGUAGE_MERCHANT = 0xDF39;
            /*
#define TAG_CASHBACK_AMOUNT      0xDF40
#define TAG_TIP_AMOUNT           0xDF41
#define TAG_GOODS_AMOUNT         0xDF42

    // Special Tags
    #define TAG_IP_ENTRY             0xDFAA
            #define TAG_YES                  0xDFAB
            #define TAG_PASSWORD             0xDFAE
*/
    public static final int PP_VENDOR     = 0xDF8008;
    public static final int PP_VERSION    = 0xDF8006;
    public static final int TIMER_VERSION = 0xDF8009;
    public static final int PRINTER_WIDTH = 0xDF8010;
    public static final int T_SNR         = 0xDF8007;
    public static final int SETUP_VERSION = 0xDF8001;
    /*
    #define TAG_LAST_ERROR         0xDF8002
    #define TAG_LAST_ERROR_TAG     0xDF8003
    #define TAG_LAST_ERROR_TEXT    0xDF8004
    */
    public static final int ECR_VERSION   = 0xDF8005;
    public static final int CAPABILITIES  = 0xDF8011;
    public static final int LOCAL_VERSION = 0xDF8012;
            /*
            #define TAG_SC_SETTINGS        0xDF8014
            #define TAG_TASK_DATA          0xDF8015
            #define TAG_IDLE_P_ON          0xDF8016
            #define TAG_IDLE_P_OFF         0xDF8017
            #define TAG_ITEM_PROPERTY      0xDF8046


            #define TAG_PP_PERIPHERAL      0xDF8203
            #define TAG_PP_REQUEST         0xDF8201
            #define TAG_PP_TIMEOUT         0xDF8020
            #define TAG_CODE_TABLE         0xDF8204
            #define TAG_PP_RESPONSE        0xDF8202
            #define TAG_REQUEST_TYPE       0xDF820E
            #define TAG_TRX_STATUS         0xDF8205

            #define TAG_MSG_R_DATA         0xDF8220
            #define TAG_MSG_R_TARGET       0xDF8221
            #define TAG_MSG_R_ADDRESS      0xDF8222
            #define TAG_MSG_R_PROTOCOL     0xDF8223

            #define TAG_IDLE_PROMPT        0xFF802B

 */
}
