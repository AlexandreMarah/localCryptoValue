package com.alexandre.marah.localcryptovalue.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CityDatabaseSingletonTest {

    @Test
    void test_singleton_object() throws IOException {
        CityDatabaseSingleton cityDatabaseSingleton1 = CityDatabaseSingleton.getInstance();
        CityDatabaseSingleton cityDatabaseSingleton2 = CityDatabaseSingleton.getInstance();
        assertThat(cityDatabaseSingleton1).isEqualTo(cityDatabaseSingleton2);
    }
}
