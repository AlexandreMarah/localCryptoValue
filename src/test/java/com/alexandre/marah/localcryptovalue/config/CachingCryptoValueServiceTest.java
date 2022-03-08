package com.alexandre.marah.localcryptovalue.config;

import com.alexandre.marah.localcryptovalue.model.LocalCryptoValue;
import com.alexandre.marah.localcryptovalue.service.CryptoValueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CachingCryptoValueServiceTest {

    @Autowired
    private CacheConfigManager cacheConfigManager;

    @Autowired
    private CryptoValueService cryptoValueService;

    @BeforeEach
    void init() {
        Objects.requireNonNull(cacheConfigManager.alternateCacheManager().getCache("crypto-list")).clear();
        Objects.requireNonNull(cacheConfigManager.cacheManager().getCache("crypto-value")).clear();
    }

    @Test
    void should_get_crypto_list_from_cache(){
        // given
        cacheConfigManager.alternateCacheManager().getCache("crypto-list")
                .put("getCryptoList", Map.of("cryptoID", "cryptoName"));

        // when
        Map<String, String> result = cryptoValueService.getCryptoList();

        // then
        assertThat(result).isEqualTo(Map.of("cryptoID", "cryptoName"));
    }

    @Test
    void should_get_crypto_value_from_cache(){
        // given
        LocalCryptoValue localCryptoValue = LocalCryptoValue.builder()
                .cryptoId("bitcoin")
                .cryptoName("Bitcoin")
                .cryptoValue("500 000 $")
                .build();
        cacheConfigManager.cacheManager().getCache("crypto-value")
                .put(SimpleKeyGenerator.generateKey("bitcoin", "123.124.125.1"), localCryptoValue);

        // when
        LocalCryptoValue result = cryptoValueService.getLocalCryptoValue("bitcoin", "123.124.125.1");

        // then
        assertThat(result).isEqualTo(localCryptoValue);
    }
}
