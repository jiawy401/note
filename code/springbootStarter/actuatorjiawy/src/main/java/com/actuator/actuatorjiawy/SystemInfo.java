package com.actuator.actuatorjiawy;

public class SystemInfo implements SystemInfoMBean {
    @Override
    public int getCpuCore() {
        return Runtime.getRuntime().availableProcessors();
    }

    @Override
    public long totalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    @Override
    public void shutdown() {
        System.exit(0);
    }

    @Override
    public String getInCOunt(String count) {
        return count;
    }
}
