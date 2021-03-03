package com.jiawy.springbootthread.thread4_1_deadLock;

public class TransFerAccount1 implements Runnable {

    private Account fromAccount;   //转出账户
    private Account toAccount;      //转入账户
    private int account;
    private Allocator allocator;

    public TransFerAccount1(Account fromAccount, Account toAccount, int account, Allocator allocator) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.account = account;
        this.allocator = allocator;
    }

    @Override
    public void run() {
        while (true) {

            /**
             * -占有且等待，线程T1已经取得共享资源X，在等待共享资源Y的时候，不释放共享资源X
             * 以下解决的是：占有且等待的时候 发生死锁
             */
            if (allocator.apply(fromAccount, toAccount)) {
                try {

                    synchronized (fromAccount) {
                        synchronized (toAccount) {
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
                    allocator.free(fromAccount, toAccount);
                }
            }
        }
    }

    public static void main(String[] args) {
        Account fromAccount = new Account("Mic", 10000);
        Account toAccount = new Account("tom", 300000);
        Allocator allocator = new Allocator();
        Thread a = new Thread(new TransFerAccount1(fromAccount, toAccount, 10, allocator));
        Thread b = new Thread(new TransFerAccount1(toAccount, fromAccount, 30, allocator));
        a.start();
        b.start();
    }
}
