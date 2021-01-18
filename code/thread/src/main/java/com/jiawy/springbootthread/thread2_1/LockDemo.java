package com.jiawy.springbootthread.thread2_1;

import org.openjdk.jol.info.ClassLayout;

public class LockDemo {
//    public static void main(String[] args) {
//        LockDemo lockDemo = new LockDemo();
//        Thread t1 = new Thread(()->{
//           synchronized (lockDemo){
//               System.out.println("t1抢占到锁");
//               System.out.println(ClassLayout.parseInstance(lockDemo).toPrintable());
//           }
//        });
//        t1.start();
//
//        synchronized (lockDemo){
//            System.out.println("Main抢占锁");
//            System.out.println(ClassLayout.parseInstance(lockDemo).toPrintable());
//        }
//
//        /**
//         * Main抢占锁
//         * com.jiawy.springbootthread.thread2_1.LockDemo object internals:
//         *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
//         *       0     4        (object header)                           2a d5 57 1f (00101010 11010101 01010111 00011111) (525849898)
//         *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
//         *       8     4        (object header)                           05 c1 00 f8 (00000101 11000001 00000000 11111000) (-134168315)
//         *      12     4        (loss due to the next object alignment)
//         * Instance size: 16 bytes
//         * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
//         *
//         * t1抢占到锁
//         * com.jiawy.springbootthread.thread2_1.LockDemo object internals:
//         *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
//         *       0     4        (object header)                           2a d5 57 1f (00101010 11010101 01010111 00011111) (525849898)
//         *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
//         *       8     4        (object header)                           05 c1 00 f8 (00000101 11000001 00000000 11111000) (-134168315)
//         *      12     4        (loss due to the next object alignment)
//         * Instance size: 16 bytes
//         * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
//         */
//    }
    public static void main(String[] args) throws InterruptedException {
        LockDemo lockDemo = new LockDemo();
        Thread t1 = new Thread(()->{
           synchronized (lockDemo){
               System.out.println("t1抢占到锁");
               System.out.println(ClassLayout.parseInstance(lockDemo).toPrintable());
           }
        });
        t1.start();
        Thread.sleep(10000);  //每一个锁之间是独立的，这样锁就是轻量级锁
        synchronized (lockDemo){
            System.out.println("Main抢占锁");
            System.out.println(ClassLayout.parseInstance(lockDemo).toPrintable());
        }

        /**
         * Main抢占锁
         * com.jiawy.springbootthread.thread2_1.LockDemo object internals:
         *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
         *       0     4        (object header)                           2a d5 57 1f (00101010 11010101 01010111 00011111) (525849898)
         *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
         *       8     4        (object header)                           05 c1 00 f8 (00000101 11000001 00000000 11111000) (-134168315)
         *      12     4        (loss due to the next object alignment)
         * Instance size: 16 bytes
         * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
         *
         * t1抢占到锁
         * com.jiawy.springbootthread.thread2_1.LockDemo object internals:
         *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
         *       0     4        (object header)                           2a d5 57 1f (00101010 11010101 01010111 00011111) (525849898)
         *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
         *       8     4        (object header)                           05 c1 00 f8 (00000101 11000001 00000000 11111000) (-134168315)
         *      12     4        (loss due to the next object alignment)
         * Instance size: 16 bytes
         * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
         */
    }
}
