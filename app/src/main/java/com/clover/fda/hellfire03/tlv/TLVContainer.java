package com.clover.fda.hellfire03.tlv;

import java.util.ArrayList;

public class TLVContainer {
        protected int tag;
        protected byte[] baValue;
        protected ArrayList<TLVContainer> subcontainer;

        public TLVContainer () {
            tag = -1;
            baValue = new byte[0];
            subcontainer = new ArrayList<TLVContainer>();
        }
}
