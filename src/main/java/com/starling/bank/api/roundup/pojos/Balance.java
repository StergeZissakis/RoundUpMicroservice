package com.starling.bank.api.roundup.pojos;

import java.io.Serializable;

public class Balance extends Pojo implements Serializable {

    public Balance() {
        currency = "";
        minorUnits = new Integer(0);
    }

    public Balance(String currency) {
        this.currency = currency;
    }
    public Balance(Integer amount) {
        this.minorUnits = amount;
    }

    public Balance(String currency, Integer amount) {
        this(currency);
        this.minorUnits = amount;
    }

    public String currency = "";
    public Integer minorUnits = new Integer(0);
}
