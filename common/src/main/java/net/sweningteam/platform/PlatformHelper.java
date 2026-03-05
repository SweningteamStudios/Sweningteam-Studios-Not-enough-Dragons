package net.sweningteam.platform;

public interface PlatformHelper {
    RegisterHelper getRegisterHelper();

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
