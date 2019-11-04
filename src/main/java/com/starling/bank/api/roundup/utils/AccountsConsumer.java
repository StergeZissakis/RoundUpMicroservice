package com.starling.bank.api.roundup.utils;

import com.starling.bank.api.roundup.pojos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class AccountsConsumer extends StarlingAPIConsumer {

    protected static final Logger log = LoggerFactory.getLogger(AccountsConsumer.class);

    private static final String baseUrl = "https://api-sandbox.starlingbank.com";
    private static final String accountsUrl = baseUrl + "/api/v2/accounts";
    private static final String accountUidUrl = baseUrl + "/api/v2/accounts/{accountUid}/identifiers";
    private static final String enoughBalanceUrl = baseUrl + "/api/v2/accounts/{accountUid}/confirmation-of-funds";
    private static final String transactionsBetweenUrl = baseUrl + "/api/v2/feed/account/{accountUid}/category/{categoryUid}/transactions-between";
    private static final String getAllSavingGoalsOfAccountUrl = baseUrl + "/api/v2/account/{accountUid}/savings-goals";
    private static final String getSavingGoalByUidUrl = baseUrl + "/api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}";
    private static final String createNewSavingGoalUrl = baseUrl + "/api/v2/account/{accountUid}/savings-goals";
    private static final String transferIntoGoalUrl = baseUrl + "/api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/{transferUid}";
    private static final String accountBalanaceUrl = baseUrl + "/api/v2/accounts/{accountUid}/balance";

    public AccountsConsumer() {
        log.debug("Default CTOR");
    }

    public ResponseEntity<Accounts> getCustomerAccounts() {
        log.debug("getCustomerAccounts");
        super.uri = accountsUrl;
        ResponseEntity accounts = super.sendGetRequest(new Accounts());
        return accounts;
    }

    public ResponseEntity<AccountId> getAccountIds(String accountUid) {
        log.debug("sendGetAccountIdsRequest");
        super.uri = accountUidUrl;
        super.addPathParameter("accountUid", accountUid);
        ResponseEntity accountId =  super.sendGetRequest(new AccountId());
        return accountId;
    }

    public boolean isBalanceAvailable(String accountUid, Integer desiredAmount) {
        log.debug("isBalanceAvailable");
        super.uri = enoughBalanceUrl;
        super.addPathParameter("accountUid", accountUid);
        super.addQueryParameter("targetAmountInMinorUnits", desiredAmount.toString());
        AmountAvailability aa = (AmountAvailability) super.sendGetRequest(new AmountAvailability()).getBody();
        // ensure no overdraft is used.
        return  aa.accountWouldBeInOverdraftIfRequestedAmountSpent == false
                && aa.requestedAmountAvailableToSpend == true;
    }

    public ResponseEntity<Feed>  getExpedintureTransactions(Account account, LocalDate weekStarting) {
        log.debug("getExpedintureTransactions");
        super.uri = transactionsBetweenUrl;
        super.addPathParameter("accountUid", account.accountUid);
        super.addPathParameter("categoryUid", account.defaultCategory);
        LocalDateTime fromDate = LocalDateTime.of(weekStarting, LocalTime.MIN);
        LocalDateTime toDate = LocalDateTime.of(weekStarting.plusDays(6), LocalTime.MAX);
        super.addQueryParameter("minTransactionTimestamp", fromDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm':00.000Z'")));
        super.addQueryParameter("maxTransactionTimestamp", toDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm':00.000Z'")));
        ResponseEntity feed =  super.sendGetRequest(new Feed());
        return feed;
    }

    public SavingGoal getOrCreateSavingsGoal(Account account) {
        log.debug("getOrCreateSavingGoal");
        ResponseEntity<SavingGoals> existingGoals = getAllSavingGoalsOfAccount(account);
        if( existingGoals.getStatusCode() == HttpStatus.OK && !existingGoals.getBody().savingsGoalList.isEmpty()) {
            // always return the first goal found, if any
            return existingGoals.getBody().savingsGoalList.get(0);
        }
        // if nothing found, create one
        ResponseEntity<SavingGoalUidResponse> newGoal = this.createSavingGoalOnAccount(account);
        return getSavingGoalByUid(account, newGoal.getBody().savingsGoalUid).getBody();
    }

    public ResponseEntity<SavingGoal> getSavingGoalByUid(Account account, String uuid) {
        log.debug("getSavingGoalBy(Account:" + account.accountUid + ", UUID:" + uuid + ")");
        super.uri = getSavingGoalByUidUrl;
        super.addPathParameter("accountUid", account.accountUid);
        super.addPathParameter("savingsGoalUid", uuid);
        ResponseEntity ret = super.sendGetRequest(new SavingGoal());
        return ret;
    }


    public ResponseEntity<SavingGoals> getAllSavingGoalsOfAccount(Account account) {
        log.debug("getAllSavingGoalsOfAccount");
        super.uri = getAllSavingGoalsOfAccountUrl;
        super.addPathParameter("accountUid", account.accountUid);
        ResponseEntity ret = super.sendGetRequest(new SavingGoals());
        return ret;
    }

    public ResponseEntity<SavingGoalUidResponse> createSavingGoalOnAccount(Account account) {
        log.debug("createSavingGoalOnAccount");
        super.uri = createNewSavingGoalUrl;
        super.addPathParameter("accountUid", account.accountUid);
        SavingGoalCreate goal = new SavingGoalCreate("WeeklyRoundUpChange", account.currency);
        ResponseEntity ret = super.sendCreatePutRequest(goal, new SavingGoalUidResponse());
        return ret;
    }

    public boolean transferAmountFromAccountIntoGoal(Account account, Amount amount, SavingGoal goal) {
        log.debug("transferAmountFromAccountIntoGoal");
        String txUid = UUID.randomUUID().toString();
        super.uri = transferIntoGoalUrl;
        super.addPathParameter("accountUid", account.accountUid);
        super.addPathParameter("savingsGoalUid", goal.savingsGoalUid);
        super.addPathParameter("transferUid", txUid);
        ResponseEntity<TransferUidResponse> res = super.sendTransferPutRequest(amount);
        boolean x = res.getStatusCode() == HttpStatus.OK;
        boolean y = res.getBody().transferUid.equalsIgnoreCase(txUid);
        return x && y;
    }

    public ResponseEntity<AccountBalance> getAccountBalance(Account account) {
        log.debug("getAccountBalance: " + account.accountUid);
        super.uri = accountBalanaceUrl;
        super.addPathParameter("accountUid", account.accountUid);
        ResponseEntity ret = super.sendGetRequest(new AccountBalance());
        return ret;
    }

}
