package org.vfs4j.example.simple;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainTest.class);

    @BeforeEach
    void setUp() throws IOException {
        Path p = Paths.get("./target/test");
        if (Files.exists(p)) {
            FileUtils.deleteDirectory(p.toFile());
        }
        assertFalse(Files.exists(p));
    }

    @Test
    void main() throws IOException {
        LOGGER.info("main");
        Main.main(null);

        // Vérifications
        Path p = Paths.get("./target/test");
        assertTrue(Files.exists(p));
    }
}