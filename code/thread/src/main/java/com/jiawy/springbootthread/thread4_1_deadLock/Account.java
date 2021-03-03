package com.jiawy.springbootthread.thread4_1_deadLock;

public class Account {

    private String  accountName;
    private int balance;

    public Account(String accountName, int balance) {
        this.accountName = accountName;
        this.balance = balance;
    }

    public void debit (int amount){ //GENGX更新转出方的余额
        this.balance -= amount;
    }
    public void credit (int amount){ //GENGX更新转出方的余额
        this.balance += amount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
