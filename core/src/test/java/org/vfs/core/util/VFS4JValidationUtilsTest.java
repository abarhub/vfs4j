package org.vfs.core.util;

import org.junit.jupiter.api.Test;
import org.vfs.core.exception.VFS4JInvalideParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class VFS4JValidationUtilsTest {

    @Test
    void checkNotNullOK() {
        VFS4JValidationUtils.checkNotNull(new Object(), "Erreur");
    }

    @Test
    void checkNotNullKO() {
        final String erreur = "Erreur Null";
        try {
            VFS4JValidationUtils.checkNotNull(null, erreur);
            fail("Erreur");
        } catch (VFS4JInvalideParameterException e) {
            assertEquals(erreur, e.getMessage());
        }
    }

    @Test
    void checkNotEmptyOK() {
        VFS4JValidationUtils.checkNotEmpty("abc", "Erreur");
    }

    @Test
    void checkNotEmptyNullKO() {
        final String erreur = "Erreur Null";
        try {
            VFS4JValidationUtils.checkNotEmpty(null, erreur);
            fail("Erreur");
        } catch (VFS4JInvalideParameterException e) {
            assertEquals(erreur, e.getMessage());
        }
    }

    @Test
    void checkNotEmptyEmptyKO() {
        final String erreur = "Erreur Empty";
        try {
            VFS4JValidationUtils.checkNotEmpty("", erreur);
            fail("Erreur");
        } catch (VFS4JInvalideParameterException e) {
            assertEquals(erreur, e.getMessage());
        }
    }

    @Test
    void checkParameterOK() {
        VFS4JValidationUtils.checkParameter(true, "Erreur");
    }

    @Test
    void checkParameterKO() {
        final String erreur = "Erreur test";
        try {
            VFS4JValidationUtils.checkParameter(false, erreur);
            fail("Erreur");
        } catch (VFS4JInvalideParameterException e) {
            assertEquals(erreur, e.getMessage());
        }
    }
}