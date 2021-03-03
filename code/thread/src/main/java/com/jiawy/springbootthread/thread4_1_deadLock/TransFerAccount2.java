package com.jiawy.springbootthread.thread4_1_deadLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("all")
public class TransFerAccount2 implements Runnable {

    private Account fromAccount;   //转出账户
    private Account toAccount;      //转入账户
    private int account;

    Lock fromLock = new ReentrantLock();
    Lock toLock = new ReentrantLock();

    public TransFerAccount2(Account fromAccount, Account toAccount, int account ) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.account = account;
    }

    @Override
    public void run() {
        while (true) {
                try {
                    /**
                     * 不可抢占， 其他线程不能强行抢占线程T1占有的资源
                     * 以下解决的，不会存在不可抢占的因素。 死锁。
                     */
                    if (fromLock.tryLock()) {  //返回true或false
                        if(toLock.tryLock()) { //返回true或false
                            if (fromAccount.getBalance() >= account) {
                                fromAccount.debit(account);
                                toAccount.credit(account);
                            }
                        }
                    }

                    //转出账户的余额
                    System.out.println(fromAccount.getAccountName() + "->" + fromAccount.getBalance());

                    //转入账户的余额
                    System.out.println(toAccount.getAccountName() + "->" + toAccount.getBalance());

                } finally {
                }
            }
        }

    public static void main(String[] args) {
        Account fromAccount = new Account("Mic", 10000);
        Account toAccount = new Account("tom", 300000);
        Allocator allocator = new Allocator();
        Thread a = new Thread(new TransFerAccount2(fromAccount, toAccount, 10 ));
        Thread b = new Thread(new TransFerAccount2(toAccount, fromAccount, 30 ));
        a.start();
        b.start();
    }
}
