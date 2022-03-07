package com.alexandre.marah.localcryptovalue.utils;

/**
 * Local crypto value calculator constants class.
 */
public class LocalCryptoConstants {

    public static final int COIN_GECKO_CRYPTO_LIST_SIZE = 100;
    public static final String CRYPTO_ID_REGULAR_EXPRESSION = "[a-zA-Z-]+";
    public static final String IPV4_ADDRESS_REGULAR_EXPRESSION = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$";
    public static final String IPV6_ADDRESS_REGULAR_EXPRESSION = "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";
    public static final String CITY_DATABASE_FILE_LOCATION = "src/main/resources/GeoLite2-City.mmdb";

    /**
     * Constants class private constructor.
     */
    private LocalCryptoConstants() {

    }
}
