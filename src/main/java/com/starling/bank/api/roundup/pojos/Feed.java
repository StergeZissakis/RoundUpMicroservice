package com.starling.bank.api.roundup.pojos;

import java.io.Serializable;
import java.util.ArrayList;

public class Feed extends Pojo implements Serializable {
    public ArrayList<FeedItem> feedItems = new ArrayList<FeedItem>();
}
