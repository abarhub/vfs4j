package io.github.abarhub.vfs.logback.rolling;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import ch.qos.logback.core.rolling.helper.TimeBasedArchiveRemover;

import java.io.File;
import java.util.Date;

@NoAutoStart
public class VFS4JDefaultTimeBasedFileNamingAndTriggeringPolicy<E> extends VFS4JTimeBasedFileNamingAndTriggeringPolicyBase<E> {

    @Override
    public void start() {
        super.start();
        if (!super.isErrorFree())
            return;
        if (tbrp.fileNamePattern.hasIntegerTokenCOnverter()) {
            addError("Filename pattern [" + tbrp.fileNamePattern
                    + "] contains an integer token converter, i.e. %i, INCOMPATIBLE with this configuration. Remove it.");
            return;
        }

        archiveRemover = new TimeBasedArchiveRemover(tbrp.fileNamePattern, rc);
        archiveRemover.setContext(context);
        started = true;
    }

    public boolean isTriggeringEvent(File activeFile, final E event) {
        long time = getCurrentTime();
        if (time >= nextCheck) {
            Date dateOfElapsedPeriod = dateInCurrentPeriod;
            addInfo("Elapsed period: " + dateOfElapsedPeriod);
            elapsedPeriodsFileName = tbrp.fileNamePatternWithoutCompSuffix.convert(dateOfElapsedPeriod);
            setDateInCurrentPeriod(time);
            computeNextCheck();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "io.github.abarhub.vfs.logback.rolling.VFS4JDefaultTimeBasedFileNamingAndTriggeringPolicy";
    }
}
