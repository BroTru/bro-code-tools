package com.brotru.code.generator.logging;

/**
 *
 * @author bronek
 */
public class LogMojoImpl implements Log {
    private final org.apache.maven.plugin.logging.Log mojoLog;

    private String prefix;

    public LogMojoImpl(org.apache.maven.plugin.logging.Log mojoLog) {
        this.mojoLog = mojoLog;
    }

    public String getPrefix() {
        return prefix;
    }

    public LogMojoImpl setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public boolean isDebugEnabled() {
        return mojoLog.isDebugEnabled();
    }

    @Override
    public void debug(CharSequence cs) {
        mojoLog.debug(prefix == null ? cs : prefix + cs);
    }

    @Override
    public void debug(CharSequence cs, Throwable thrwbl) {
        mojoLog.debug(prefix == null ? cs : prefix + cs, thrwbl);
    }

    @Override
    public void debug(Throwable thrwbl) {
        mojoLog.debug(thrwbl);
    }

    @Override
    public boolean isInfoEnabled() {
        return mojoLog.isInfoEnabled();
    }

    @Override
    public void info(CharSequence cs) {
        mojoLog.info(prefix == null ? cs : prefix + cs);
    }

    @Override
    public void info(CharSequence cs, Throwable thrwbl) {
        mojoLog.info(prefix == null ? cs : prefix + cs, thrwbl);
    }

    @Override
    public void info(Throwable thrwbl) {
        mojoLog.info(thrwbl);
    }

    @Override
    public boolean isWarnEnabled() {
        return mojoLog.isWarnEnabled();
    }

    @Override
    public void warn(CharSequence cs) {
        mojoLog.warn(prefix == null ? cs : prefix + cs);
    }

    @Override
    public void warn(CharSequence cs, Throwable thrwbl) {
        mojoLog.warn(prefix == null ? cs : prefix + cs, thrwbl);
    }

    @Override
    public void warn(Throwable thrwbl) {
        mojoLog.warn(thrwbl);
    }

    @Override
    public boolean isErrorEnabled() {
        return mojoLog.isErrorEnabled();
    }

    @Override
    public void error(CharSequence cs) {
        mojoLog.error(prefix == null ? cs : prefix + cs);
    }

    @Override
    public void error(CharSequence cs, Throwable thrwbl) {
        mojoLog.error(prefix == null ? cs : prefix + cs, thrwbl);
    }

    @Override
    public void error(Throwable thrwbl) {
        mojoLog.error(thrwbl);
    }

}
