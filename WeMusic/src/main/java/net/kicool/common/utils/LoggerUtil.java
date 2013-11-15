package net.kicool.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * LoggerUtil presents both the old static logging API and new instance methods that mirror the
 * slf4j.logger interface. Both API's forward logging requests to slf4j.
 *
 * Note that with the static methods there is only one logger, with the name com.ef.common.LoggerUtil.
 */
public class LoggerUtil {

    private static HashMap<String, Logger> tag2logger = new HashMap<String, Logger>();

    // Gets the logger to use for a static logging call.
    private static Logger getDefaultLogger(String tag) {
        Logger logger = tag2logger.get(tag);
        if (null == logger) {
            logger = LoggerFactory.getLogger(tag);
            tag2logger.put(tag, logger);
        }
        return logger;
    }

    private Logger logger_imp;

    public static void init() {

    }

    //
    // Old com.ef.common.LoggerUtil static API
    //

    // ERROR
    public static void e(String tag, String message) {
        getDefaultLogger(tag).error(message);
    }

    // ERROR with exception
    public static void e(String tag, String message, Throwable ex) {
        getDefaultLogger(tag).error(message, ex);
    }

    // WARN
    public static void w(String tag, String message) {
        getDefaultLogger(tag).warn(message);
    }

    // WARN with exception
    public static void w(String tag, String message, Throwable ex) {
        getDefaultLogger(tag).error(message, ex);
    }

    // INFO
    public static void i(String tag, String message) {
        getDefaultLogger(tag).info(message);
    }

    // DEBUG
    public static void d(String tag, String message) {
        getDefaultLogger(tag).debug(message);
    }

    // DEBUG with exception
    public static void d(String tag, String message, Throwable ex) {
       getDefaultLogger(tag).debug(message, ex);
    }

    // DEBUG with exception but no message
    public static void d(String tag, Throwable ex) {
        getDefaultLogger(tag).debug(tag, ex);
    }

    // VERBOSE (TRACE)
    public static void v(String tag, String message) {
        getDefaultLogger(tag).trace(message);
    }

    //
    // New API for com.ef.common.LoggerUtil object instances.
    // The interface is a facade around the slf4j facade.
    //

    public LoggerUtil(Class clazz) {
        this.logger_imp = LoggerFactory.getLogger(clazz);
    }

    public LoggerUtil(String name) {
        this.logger_imp = LoggerFactory.getLogger(name);
    }

    public void trace(String s) {
        logger_imp.trace(s);
    }

    public void trace(String s, Object o) {
        logger_imp.trace(s, o);
    }

    public void trace(String s, Object o, Object o2) {
        logger_imp.trace(s, o, o);
    }

    public void trace(String s, Object... objects) {
        logger_imp.trace(s, objects);
    }

    public void trace(String s, Throwable throwable) {
        logger_imp.trace(s, throwable);
    }

    public void debug(String s) {
        logger_imp.debug(s);
    }

    public void debug(String s, Object o) {
        logger_imp.debug(s, o);
    }

    public void debug(String s, Object o, Object o2) {
        logger_imp.debug(s, o, o);
    }

    public void debug(String s, Object... objects) {
        logger_imp.debug(s, objects);
    }

    public void debug(String s, Throwable throwable) {
        logger_imp.debug(s, throwable);
    }

    public void info(String s) {
        logger_imp.info(s);
    }

    public void info(String s, Object o) {
        logger_imp.info(s, o);
    }

    public void info(String s, Object o, Object o2) {
        logger_imp.info(s, o, o);
    }

    public void info(String s, Object... objects) {
        logger_imp.info(s, objects);
    }

    public void info(String s, Throwable throwable) {
        logger_imp.info(s, throwable);
    }

    public void warn(String s) {
        logger_imp.warn(s);
    }

    public void warn(String s, Object o) {
        logger_imp.warn(s, o);
    }

    public void warn(String s, Object... objects) {
        logger_imp.warn(s, objects);
    }

    public void warn(String s, Object o, Object o2) {
        logger_imp.warn(s, o, o2);
    }

    public void warn(String s, Throwable throwable) {
        logger_imp.warn(s, throwable);
    }

    public void error(String s) {
        logger_imp.error(s);
    }

    public void error(String s, Object o) {
        logger_imp.error(s, o);
    }

    public void error(String s, Object o, Object o2) {
        logger_imp.error(s, o, o2);
    }

    public void error(String s, Object... objects) {
        logger_imp.error(s, objects);
    }

    public void error(String s, Throwable throwable) {
        logger_imp.error(s, throwable);
    }

    public String getName() {
        return logger_imp.getName();
    }

}
