package com.starling.bank.api.roundup;

import com.starling.bank.api.roundup.pojos.*;
import com.starling.bank.api.roundup.utils.AccountsConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;

@Service
public class RoundUpService {


    protected static final Logger log = LoggerFactory.getLogger(RoundUpService.class);

    protected Accounts obtainCustomerAccounts() {
        log.debug("obtainCustomerAccounts");
        AccountsConsumer ac = new AccountsConsumer();
        return ac.getCustomerAccounts().getBody();

    }

    public void processRoundUp(LocalDate dateWithinWeek) {
        log.debug("processRoundUp");

        AccountsConsumer ac = new AccountsConsumer();

        Accounts customerAccounts = obtainCustomerAccounts();
        if( customerAccounts.accounts.isEmpty()) {
            log.debug("No custommer acounts found");
            return;
        }

        LocalDate mondayOfWeek = startOfWeekDate(dateWithinWeek);

        for(Account a : customerAccounts.accounts) {
            Integer changeTotal = processOutgoingTransactions(a, mondayOfWeek);
            // if there are any change whose amount can be deducted from the account without overdrawing or get rejected
            if( changeTotal > 0 && ac.isBalanceAvailable(a.accountUid, changeTotal)) {
                if( !peformMoneyTransferIntoGoalAccount(a, changeTotal) ) {
                    log.error("TX Failed: from " + a.defaultCategory + " Amount: " + changeTotal );
                }
            }
            else {
                if( changeTotal > 0 ) {
                    log.info("Not enough balance to deduct " + changeTotal + " from account " + a.accountUid);
                } else {
                    log.info("No change for " + a.accountUid);
                }
            }
        }
    }


    protected boolean peformMoneyTransferIntoGoalAccount(Account account, Integer amount) {
        UUID transferUid = UUID.randomUUID();   // user generated transfer ID
        AccountsConsumer ac = new AccountsConsumer();
        SavingGoal goal = ac.getOrCreateSavingsGoal(account);
        Amount txAmount = new Amount();
        txAmount.amount.currency = account.currency;
        txAmount.amount.minorUnits = amount;
        return ac.transferAmountFromAccountIntoGoal(account, txAmount, goal);
    }


    protected Integer processOutgoingTransactions(Account account, LocalDate monday) {
        AccountsConsumer ac = new AccountsConsumer();
        Integer ret = new Integer(0);
        ResponseEntity<Feed> f = ac.getExpedintureTransactions(account, monday);
        if( f.getStatusCode() == HttpStatus.OK ) {
            for (FeedItem fItem : f.getBody().feedItems) {
                if (fItem.status.equals("SETTLED") && fItem.direction.equals("OUT")) { // only for cleared outgoings
                    ret += roundUpAmount(fItem.amount.minorUnits);
                }
            }
        }

        return ret;
    }

    public static LocalDate startOfWeekDate(LocalDate dateOfWeek) {
        return dateOfWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public static Integer roundUpAmount(Integer amount) {
        if( +amount % 100 == 0) {
            return 0;
        }

        Integer hundreds = amount / 100;
        return 100 - (amount - (hundreds * 100));
    }
}
