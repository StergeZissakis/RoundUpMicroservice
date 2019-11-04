package com.starling.bank.api.roundup.pojos;

import java.io.Serializable;

public class AccountId extends Pojo implements Serializable {
    public String accountIdentifier = "";
    public String bankIdentifier = "";
    public String iban = "";
    public String bic = "";
}
