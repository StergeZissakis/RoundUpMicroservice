package com.starling.bank.api.roundup.pojos;

import java.io.Serializable;

public class Amount extends Pojo implements Serializable {

    public Amount() {
        amount = new Balance("GBP", 0);
    }

    public Amount(Balance b) {
        amount = b;
    }

    public Balance amount;
}
