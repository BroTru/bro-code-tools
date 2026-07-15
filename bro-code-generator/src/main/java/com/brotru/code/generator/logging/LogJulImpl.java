package com.brotru.code.generator.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bronek
 */
public class LogJulImpl implements Log {

    private final java.util.logging.Logger logger;

    public LogJulImpl(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isLoggable(Level.FINE);
    }

    @Override
    public void debug(CharSequence content) {
        logger.log(Level.FINE, content.toString());
    }

    @Override
    public void debug(CharSequence content, Throwable error) {
        logger.log(Level.FINE, error, () -> content.toString());
    }

    @Override
    public void debug(Throwable error) {
        logger.log(Level.FINE, error, () -> "");
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isLoggable(Level.INFO);
    }

    @Override
    public void info(CharSequence content) {
        logger.log(Level.INFO, content.toString());
    }

    @Override
    public void info(CharSequence content, Throwable error) {
        logger.log(Level.INFO, error, () -> content.toString());
    }

    @Override
    public void info(Throwable error) {
        logger.log(Level.INFO, error, () -> "");
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isLoggable(Level.WARNING);
    }

    @Override
    public void warn(CharSequence content) {
        logger.log(Level.WARNING, content.toString());
    }

    @Override
    public void warn(CharSequence content, Throwable error) {
        logger.log(Level.WARNING, error, () -> content.toString());
    }

    @Override
    public void warn(Throwable error) {
        logger.log(Level.WARNING, error, () -> "");
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isLoggable(Level.SEVERE);
    }

    @Override
    public void error(CharSequence content) {
        logger.log(Level.SEVERE, content.toString());
    }

    @Override
    public void error(CharSequence content, Throwable error) {
        logger.log(Level.SEVERE, error, () -> content.toString());
    }

    @Override
    public void error(Throwable error) {
        logger.log(Level.SEVERE, error, () -> "");
    }
}
