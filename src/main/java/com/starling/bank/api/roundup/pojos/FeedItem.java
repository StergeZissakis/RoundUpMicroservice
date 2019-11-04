package com.starling.bank.api.roundup.pojos;

import java.io.Serializable;

public class FeedItem extends Pojo implements Serializable {
    public String feedItemUid = "";
    public String categoryUid = "";
    public Balance amount = new Balance();
    public Balance sourceAmount = new Balance();
    public String direction = "";
    public String updatedAt = "";
    public String transactionTime = "";
    public String settlementTime = "";
    public String source = "";
    public String status = "";
    public String counterPartyType = "";
    public String counterPartyUid = "";
    public String counterPartyName = "";
    public String counterPartySubEntityUid = "";
    public String counterPartySubEntityName = "";
    public String counterPartySubEntityIdentifier = "";
    public String counterPartySubEntitySubIdentifier = "";
    public String reference = "";
    public String country = "";
    public String spendingCategory = "";
}
