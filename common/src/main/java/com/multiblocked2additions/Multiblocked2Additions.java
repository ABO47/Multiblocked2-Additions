package com.multiblocked2additions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Multiblocked2Additions {
    public static final String MOD_ID = "multiblocked2additions";
    public static final String MOD_NAME = "Multiblocked2 Additions";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private Multiblocked2Additions() {
    }

    public static void bootstrap() {
        LOGGER.info("{} bootstrapped", MOD_NAME);
    }
}
