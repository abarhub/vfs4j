package io.github.abarhub.vfs.logback.rolling;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import ch.qos.logback.core.rolling.helper.RollingCalendar;
import ch.qos.logback.core.spi.ContextAwareBase;
import io.github.abarhub.vfs.core.api.VFS4JFiles;
import io.github.abarhub.vfs.core.api.path.VFS4JPathName;
import io.github.abarhub.vfs.core.api.path.VFS4JPaths;

import java.io.IOException;
import java.util.Date;

import static ch.qos.logback.core.CoreConstants.CODES_URL;

public abstract class VFS4JTimeBasedFileNamingAndTriggeringPolicyBase<E> extends ContextAwareBase
        implements VFS4JTimeBasedFileNamingAndTriggeringPolicy<E> {

    static private String COLLIDING_DATE_FORMAT_URL = CODES_URL + "#rfa_collision_in_dateFormat";

    protected VFS4JTimeBasedRollingPolicy<E> tbrp;

    protected ArchiveRemover archiveRemover = null;
    protected String elapsedPeriodsFileName;
    protected RollingCalendar rc;

    protected long artificialCurrentTime = -1;
    protected Date dateInCurrentPeriod = null;

    protected long nextCheck;
    protected boolean started = false;
    protected boolean errorFree = true;

    public boolean isStarted() {
        return started;
    }

    public void start() {
        DateTokenConverter<Object> dtc = tbrp.fileNamePattern.getPrimaryDateTokenConverter();
        if (dtc == null) {
            throw new IllegalStateException(
                    "FileNamePattern [" + tbrp.fileNamePattern.getPattern() + "] does not contain a valid DateToken");
        }

        // TODO : to fix
//        if (dtc.getZoneId() != null) {
//            TimeZone tz = TimeZone.getTimeZone(dtc.getZoneId());
//            rc = new RollingCalendar(dtc.getDatePattern(), tz, Locale.getDefault());
//        } else {
        rc = new RollingCalendar(dtc.getDatePattern());
//        }
        addInfo("The date pattern is '" + dtc.getDatePattern() + "' from file name pattern '"
                + tbrp.fileNamePattern.getPattern() + "'.");
        rc.printPeriodicity(this);

        if (!rc.isCollisionFree()) {
            addError(
                    "The date format in FileNamePattern will result in collisions in the names of archived log files.");
            addError(CoreConstants.MORE_INFO_PREFIX + COLLIDING_DATE_FORMAT_URL);
            withErrors();
            return;
        }

        setDateInCurrentPeriod(new Date(getCurrentTime()));
        if (tbrp.getParentsRawFileProperty() != null) {
            VFS4JPathName currentFile = VFS4JPaths.parsePath(tbrp.getParentsRawFileProperty());
            try {
                if (VFS4JFiles.exists(currentFile) && VFS4JFiles.isReadable(currentFile)) {
                    setDateInCurrentPeriod(new Date(VFS4JFiles.getLastModifiedTime(currentFile).toMillis()));
                }
            } catch (IOException e) {
                throw new RuntimeException("Error", e);
            }
        }
        addInfo("Setting initial period to " + dateInCurrentPeriod);
        computeNextCheck();
    }

    public void stop() {
        started = false;
    }

    protected void computeNextCheck() {
        nextCheck = rc.getNextTriggeringDate(dateInCurrentPeriod).getTime();
    }

    protected void setDateInCurrentPeriod(long now) {
        dateInCurrentPeriod.setTime(now);
    }

    // allow Test classes to act on the dateInCurrentPeriod field to simulate old
    // log files needing rollover
    public void setDateInCurrentPeriod(Date _dateInCurrentPeriod) {
        this.dateInCurrentPeriod = _dateInCurrentPeriod;
    }

    public String getElapsedPeriodsFileName() {
        return elapsedPeriodsFileName;
    }

    public String getCurrentPeriodsFileNameWithoutCompressionSuffix() {
        return tbrp.fileNamePatternWithoutCompSuffix.convert(dateInCurrentPeriod);
    }

    public void setCurrentTime(long timeInMillis) {
        artificialCurrentTime = timeInMillis;
    }

    public long getCurrentTime() {
        // if time is forced return the time set by user
        if (artificialCurrentTime >= 0) {
            return artificialCurrentTime;
        } else {
            return System.currentTimeMillis();
        }
    }

    public void setTimeBasedRollingPolicy(VFS4JTimeBasedRollingPolicy<E> _tbrp) {
        this.tbrp = _tbrp;

    }

    public ArchiveRemover getArchiveRemover() {
        return archiveRemover;
    }

    protected void withErrors() {
        errorFree = false;
    }

    protected boolean isErrorFree() {
        return errorFree;
    }
}
