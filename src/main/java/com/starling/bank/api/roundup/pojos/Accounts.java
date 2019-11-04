package com.starling.bank.api.roundup.pojos;

import java.io.Serializable;
import java.util.ArrayList;

public class Accounts extends Pojo implements Serializable {
    public ArrayList<Account> accounts = new ArrayList<Account>();
}
