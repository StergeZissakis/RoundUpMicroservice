package com.starling.bank.api.roundup.pojos;

import java.io.Serializable;

public class AccountBalance extends Pojo implements Serializable {
    public Balance clearedBalance;
    public Balance effectiveBalance;
    public Balance pendingTransactions;
    public Balance availableToSpend;
    public Balance acceptedOverdraft;
    public Balance amount;
}