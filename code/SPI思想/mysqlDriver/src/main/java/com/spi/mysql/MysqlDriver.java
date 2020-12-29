package com.spi.mysql;

import com.spi.DataBaseDriver;

public class MysqlDriver  implements DataBaseDriver {

    @Override
    public String buildConnect(String s) {
        return "Mysql的驱动实现：" +s;
    }
}
