package org.vfs4j.example.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        LOGGER.info("main");

        Exemple1 exemple1 = new Exemple1();
        exemple1.exemple1();
        exemple1.exemple2();

        LOGGER.info("OK");
    }

}
