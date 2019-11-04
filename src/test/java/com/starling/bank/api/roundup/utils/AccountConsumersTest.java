package com.starling.bank.api.roundup.utils;

import com.starling.bank.api.roundup.RoundUpService;
import com.starling.bank.api.roundup.pojos.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@Testable
public class AccountConsumersTest {

    Accounts accounts;

    @BeforeEach
    void atEveryCycle() {
        accounts = null;
        enusreAccounnts();
    }

    void fetchAccounts() {
        AccountsConsumer ac = new AccountsConsumer();
        accounts = ac.getCustomerAccounts().getBody();
    }

    void enusreAccounnts() {
        if( accounts == null ) {
            fetchAccounts();
        }
    }

    @Test
    void getCustomerAccounts() {
        AccountsConsumer ac = new AccountsConsumer();
        accounts = ac.getCustomerAccounts().getBody();
        assertThat(accounts).isNotNull();
        assertThat(accounts.accounts).isNotNull().isNotEmpty();
    }

    @Test
    void getAccountsIds() {
        AccountsConsumer ac = new AccountsConsumer();
        for(Account account : accounts.accounts) {
            assertThat(account.accountUid).isNotNull();
            AccountId accId = ac.getAccountIds(account.accountUid).getBody();
            assertThat(accId.accountIdentifier.length()).isNotNull().isGreaterThan(0);
        }
    }

    @Test
    void queryAvailableBalance() {
        Integer amount = 100;
        AccountsConsumer ac = new AccountsConsumer();
        for(Account account : accounts.accounts) {
            assertThat(ac.isBalanceAvailable(account.accountUid, amount)).isTrue();
        }
    }

    @Test
    void getFeed() {
        AccountsConsumer ac = new AccountsConsumer();
        for(Account account : accounts.accounts) {
            ResponseEntity<Feed> batch = ac.getExpedintureTransactions(account, RoundUpService.startOfWeekDate(LocalDate.of(2019, 10, 15)));
            assertThat(batch.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
            assertThat(batch.getBody().feedItems.isEmpty()).isFalse();
        }
    }

    @Test
    void getOrCreateSavingsGoal() {
        AccountsConsumer ac = new AccountsConsumer();
        for(Account account : accounts.accounts) {
            SavingGoal goal = ac.getOrCreateSavingsGoal(account);
            assertThat(goal.savingsGoalUid).isNotNull().isNotEmpty();
        }
    }

    @Test
    void getAllSavingGoalsOfAccount() {
        AccountsConsumer ac = new AccountsConsumer();
        for(Account account : accounts.accounts) {
            ResponseEntity<SavingGoals> response = ac.getAllSavingGoalsOfAccount(account);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().savingsGoalList.size()).isGreaterThan(0);
            assertThat(response.getBody().savingsGoalList.get(0).savingsGoalUid).isNotNull().isNotEmpty();
        }
    }

    @Test
    void createSavingGoalOnAccount() {
        AccountsConsumer ac = new AccountsConsumer();
        for(Account account : accounts.accounts) {
            if(!account.accountUid.isEmpty()) {
                ResponseEntity<SavingGoalUidResponse> response = ac.createSavingGoalOnAccount(account);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody().savingsGoalUid).isNotNull().isNotEmpty();
            }
        }
    }

    @Test
    void transferAmountFromAccountIntoGoal() {
        AccountsConsumer ac = new AccountsConsumer();
        LocalDate weekDate = LocalDate.of(2019, 10, 2);
        for(; weekDate.isBefore(LocalDate.of(2019,11,1)); weekDate = weekDate.plusDays(7)) {
            for(Account account : accounts.accounts) {
                SavingGoal goal = ac.getOrCreateSavingsGoal(account);
                Amount change = new Amount(new Balance("GBP", 100));
                if(ac.isBalanceAvailable(account.accountUid, change.amount.minorUnits)) {
                    boolean res = ac.transferAmountFromAccountIntoGoal(account, change, goal);
                    assertThat(res).isTrue();
                }
            }
        }
    }

    @Test
    void verifySavingsGoalDeposit() {
        AccountsConsumer ac = new AccountsConsumer();
        assertThat(accounts.accounts.size()).isGreaterThan(0);

        Account account = accounts.accounts.get(0);

        AccountBalance t0 = ac.getAccountBalance(account).getBody();
        transferAmountFromAccountIntoGoal();
        AccountBalance t1 = ac.getAccountBalance(account).getBody();
        transferAmountFromAccountIntoGoal();
        AccountBalance t2 = ac.getAccountBalance(account).getBody();
        Amount amount = new Amount(new Balance("GBP", 100));


        assertThat(t0.amount.minorUnits - (amount.amount.minorUnits * 5)).isEqualTo(t1.amount.minorUnits);
        assertThat(t1.amount.minorUnits - (amount.amount.minorUnits * 5)).isEqualTo(t2.amount.minorUnits);
        // * 5 because 1 transferAmountFromAccountIntoGoal() call, works through 5 weeks.
    }


}
