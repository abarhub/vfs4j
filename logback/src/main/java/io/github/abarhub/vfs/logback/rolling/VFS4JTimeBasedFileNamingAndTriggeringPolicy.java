package io.github.abarhub.vfs.logback.rolling;

import ch.qos.logback.core.rolling.TriggeringPolicy;
import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.spi.ContextAware;

public interface VFS4JTimeBasedFileNamingAndTriggeringPolicy<E> extends TriggeringPolicy<E>, ContextAware {

    /**
     * Set the host/parent {@link VFS4JTimeBasedRollingPolicy}.
     *
     * @param tbrp parent TimeBasedRollingPolicy
     */
    void setTimeBasedRollingPolicy(VFS4JTimeBasedRollingPolicy<E> tbrp);

    /**
     * Return the file name for the elapsed periods file name.
     *
     * @return
     */
    String getElapsedPeriodsFileName();

    /**
     * Return the current periods file name without the compression suffix. This
     * value is equivalent to the active file name.
     *
     * @return current period's file name (without compression suffix)
     */
    String getCurrentPeriodsFileNameWithoutCompressionSuffix();

    /**
     * Return the archive remover appropriate for this instance.
     */
    ArchiveRemover getArchiveRemover();

    /**
     * Return the current time which is usually the value returned by
     * System.currentMillis(). However, for <b>testing</b> purposed this value may
     * be different than the real time.
     *
     * @return current time value
     */
    long getCurrentTime();

    /**
     * Set the current time. Only unit tests should invoke this method.
     *
     * @param now
     */
    void setCurrentTime(long now);
}
