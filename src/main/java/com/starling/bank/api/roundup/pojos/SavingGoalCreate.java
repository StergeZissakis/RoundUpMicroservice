package com.starling.bank.api.roundup.pojos;

import java.io.Serializable;

public class SavingGoalCreate extends Pojo implements Serializable {
    public SavingGoalCreate() {
        this("CreatedSavingGoal", "GBP");
    }

    public SavingGoalCreate(String name, String currency) {
        this.name = name;
        this.currency = currency;
        this.target = new Balance(currency, 10000);
    }

    public String name = null;
    public String currency = null;
    public Balance target = null;
    public String base64EncodedPhoto = "";

}
