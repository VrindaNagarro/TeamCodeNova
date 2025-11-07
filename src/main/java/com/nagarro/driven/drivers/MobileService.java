package com.nagarro.driven.drivers;
import static com.google.common.base.Preconditions.checkState;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.net.URL;

public class MobileService {

    private static ThreadLocal<AppiumDriverLocalService> appiumService = new ThreadLocal<>();

    private MobileService() {
        // no instance allowed
    }

    public static void startService(String ipAddress, int port) {

        validateServerState();
        System.out.println(" : Starting Appium Mobile Service at {}:{}");
        AppiumDriverLocalService service = getServiceBuilderWithBaseParams(ipAddress, port).build();
        appiumService.set(service);
        startAppiumServer(service);
    }
    public static URL getAppiumService() {
        AppiumDriverLocalService service = appiumService.get();
        if (service == null) {
            throw new IllegalStateException("Appium service is not started yet!");
        }
        return service.getUrl();
    }

    private static void validateServerState() {
        checkState(!isServerRunning(), "Can't re-initialize Appium local service while it's running");
    }

    private static AppiumServiceBuilder getServiceBuilderWithBaseParams(String ipAddress, int port) {
        return new AppiumServiceBuilder()
                .withIPAddress(ipAddress)
                .usingPort(port)
                .withArgument(() -> "--base-path", "/wd/");
    }

    private static void startAppiumServer(AppiumDriverLocalService service) {
        if (!service.isRunning()) {
            service.start();
        }
    }


    public static boolean isServerRunning() {
        AppiumDriverLocalService service = appiumService.get();
        return service != null && service.isRunning();
    }

}

