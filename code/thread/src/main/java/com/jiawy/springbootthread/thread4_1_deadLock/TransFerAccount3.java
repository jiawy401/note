package com.jiawy.springbootthread.thread4_1_deadLock;

@SuppressWarnings("all")
public class TransFerAccount3 implements Runnable {

    private Account fromAccount;   //转出账户
    private Account toAccount;      //转入账户
    private int account;

    public TransFerAccount3(Account fromAccount, Account toAccount, int account ) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.account = account;
    }

    @Override
    public void run() {

        /**
         * -循环等待，线程T1等待线程T2占有的资源，线程T2等待线程T1占有的资源，就是循环等待
         * 以下解决的，循环等待。 死锁。
         * 控制加锁的放向 ，  从业务逻辑上是有问题的
         *
         */
        Account left = null ;
        Account right = null;
        if(fromAccount.hashCode() >  toAccount.hashCode()){
            left = toAccount;
            right = fromAccount;
        }
        while (true) {
            try {

                synchronized (left) {
                    synchronized (right) {
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
        Thread a = new Thread(new TransFerAccount3(fromAccount, toAccount, 10 ));
        Thread b = new Thread(new TransFerAccount3(toAccount, fromAccount, 30 ));
        a.start();
        b.start();
    }
}
