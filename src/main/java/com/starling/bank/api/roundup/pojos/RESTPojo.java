package com.starling.bank.api.roundup.pojos;

import java.io.Serializable;
import java.util.ArrayList;

public class RESTPojo extends Pojo implements Serializable {

    class Message {
        public String message = "";
    }

    public ArrayList<Message> errors = new ArrayList<Message>();
    public Boolean success = Boolean.TRUE;
}
