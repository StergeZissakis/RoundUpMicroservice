package com.starling.bank.api.roundup.pojos;

import java.io.Serializable;

public class Account extends RESTPojo implements Serializable {
    public String accountUid = "";
    public String defaultCategory = "";
    public String currency = "";
    public String createdAt = "";
}
