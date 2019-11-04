package com.starling.bank.api.roundup.pojos;

import java.io.Serializable;
import java.util.ArrayList;

public class SpendingPeriod  extends Pojo implements Serializable {
    public ArrayList<Expenditure> breakdown = new ArrayList<Expenditure>();
}
