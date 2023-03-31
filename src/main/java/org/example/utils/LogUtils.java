package org.example.utils;


import org.example.enums.SeleniumProcessEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LogUtils {

    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);
    public static long formId;

    public static void logInfo(String elementDescription, SeleniumProcessEnum process, String status) {
        MDC.put("elementDescription", elementDescription);
        MDC.put("seleniumProcess", process.name());
        MDC.put("seleniumStatus", status);
        logger.info(String.format("Element: %s, Process:%s, Status:%s", elementDescription, process, status));
    }

    public static void logInfo(String elementDescription, SeleniumProcessEnum process, String status, String msg) {
        MDC.put("elementDescription", elementDescription);
        MDC.put("seleniumProcess", process.name());
        MDC.put("seleniumStatus", status);
        logger.info(String.format("Element: %s, Process:%s, Status:%s, Msg:%s", elementDescription, process, status, msg));
    }

    public static void logWarn(String elementDescription, String process, String status, String msg) {
        MDC.put("elementDescription", elementDescription);
        MDC.put("seleniumProcess", process);
        MDC.put("seleniumStatus", status);
        logger.warn(String.format("Element: %s, Process:%s, Status:%s Msg:%s", elementDescription, process, status, msg));
    }
}
