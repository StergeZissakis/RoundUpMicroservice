package com.starling.bank.api.roundup.pojos;

import java.io.Serializable;
import java.util.ArrayList;

public class SavingGoals extends Pojo implements Serializable {
    public ArrayList<SavingGoal> savingsGoalList = new ArrayList<SavingGoal>();
}
