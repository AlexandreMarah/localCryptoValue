package com.alexandre.marah.localcryptovalue.model;


import com.maxmind.geoip2.DatabaseReader;
import lombok.Getter;

import java.io.File;
import java.io.IOException;

import static com.alexandre.marah.localcryptovalue.utils.LocalCryptoConstants.CITY_DATABASE_FILE_LOCATION;

/**
 * Singleton implementation to retrieve CityDatabase object from local file.
 */
@Getter
public final class CityDatabaseSingleton {

    private static CityDatabaseSingleton instance;
    private final DatabaseReader databaseReader;

    private CityDatabaseSingleton(DatabaseReader databaseReader) {
        this.databaseReader = databaseReader;
    }

    public static CityDatabaseSingleton getInstance() throws IOException {
        if (instance == null) {
            File database = new File(CITY_DATABASE_FILE_LOCATION);
            DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
            instance = new CityDatabaseSingleton(dbReader);
        }

        return instance;
    }
}
