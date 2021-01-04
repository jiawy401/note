package com.actuator.actuatorjiawy;

public interface SystemInfoMBean {
    /**
     *以get开头是属性 ， 没有get开头的是操作；
     */
    int getCpuCore();
    long totalMemory();
    void shutdown();
    String getInCOunt(String count);
}
