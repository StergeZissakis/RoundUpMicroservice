package com.starling.bank.api.roundup.pojos;

import java.io.Serializable;

public class AmountAvailability extends Pojo implements Serializable {
    public boolean requestedAmountAvailableToSpend = false;
    public boolean accountWouldBeInOverdraftIfRequestedAmountSpent = true;
}
