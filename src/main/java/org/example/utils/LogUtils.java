package org.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.example.enums.SeleniumProcessEnum;

public class LogUtils {

    private static final Logger logger = LogManager.getLogger(LogUtils.class);
    public static long formId;

    public static void logInfo(String elementDescription, SeleniumProcessEnum process, String status) {
        ThreadContext.put("formId", String.valueOf(formId));
        ThreadContext.put("elementDescription", elementDescription);
        ThreadContext.put("seleniumProcess", process.name());
        ThreadContext.put("seleniumStatus", status);
        logger.info(String.format("Element: %s, Process:%s, Status:%s", elementDescription, process, status));
        ThreadContext.clearAll();
    }

    public static void logInfo(String elementDescription, SeleniumProcessEnum process, String status, String msg) {
        ThreadContext.put("formId", String.valueOf(formId));
        ThreadContext.put("elementDescription", elementDescription);
        ThreadContext.put("seleniumProcess", process.name());
        ThreadContext.put("seleniumStatus", status);
        logger.info(String.format("Element: %s, Process:%s, Status:%s, Msg:%s", elementDescription, process, status, msg));
        ThreadContext.clearAll();
    }

    public static void logWarn(String elementDescription, String process, String status, String msg) {
        ThreadContext.put("formId", String.valueOf(formId));
        ThreadContext.put("elementDescription", elementDescription);
        ThreadContext.put("seleniumProcess", process);
        ThreadContext.put("seleniumStatus", status);
        logger.warn(String.format("Element: %s, Process:%s, Status:%s Msg:%s", elementDescription, process, status, msg));
        ThreadContext.clearAll();
    }
}
