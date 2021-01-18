package com.jiawy.springbootthread.thread2_1;

import org.openjdk.jol.info.ClassLayout;

/**
 * 打印类的布局
 */
public class ClassLayoutDemo {
    public static void main(String[] args) {

        //例1====================================================start
        ClassLayoutDemo classLayoutDemo = new ClassLayoutDemo();
        System.out.println(ClassLayout.parseInstance(classLayoutDemo).toPrintable());
        /**
         * 打印结果：
         * com.jiawy.springbootthread.thread2_1.ClassLayoutDemo object internals:
         *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
         *       0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
         *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
         *       8     4        (object header)                           05 c1 00 f8 (00000101 11000001 00000000 11111000) (-134168315)
         *      12     4        (loss due to the next object alignment)
         * Instance size: 16 bytes
         * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
         *1
         */
        //例1---=====================================================end
        //例2---=====================================================start
        ClassLayoutDemo classLayoutDemo1 = new ClassLayoutDemo();

        synchronized (classLayoutDemo1){
            System.out.println("locking");
            System.out.println(ClassLayout.parseInstance(classLayoutDemo1).toPrintable());
        }

        /**
         * com.jiawy.springbootthread.thread2_1.ClassLayoutDemo object internals:
         *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
         *       0     4        (object header)                           80 f0 37 03 (10000000 11110000 00110111 00000011) (53997696)
         *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
         *       8     4        (object header)                           05 c1 00 f8 (00000101 11000001 00000000 11111000) (-134168315)
         *      12     4        (loss due to the next object alignment)
         * Instance size: 16 bytes
         * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
         */

        //例2---=====================================================end

    }
}
