package com.starling.bank.api.roundup.pojos;

import java.io.Serializable;

public class SavingGoal extends SavingGoalCreate implements Serializable {

     public SavingGoal() {
          super();
     }

     public SavingGoal(String name, String currency) {
          super.name = name;
          super.currency = currency;
          this.totalSaved = new Balance(currency, 0);
          this.savedPercentage = new Integer(0);
     }

     public String savingsGoalUid = "";
     public Balance totalSaved;
     public Integer savedPercentage;
}
