package com.alexandre.marah.localcryptovalue.service;

import com.alexandre.marah.localcryptovalue.client.CoinGeckoClient;
import com.alexandre.marah.localcryptovalue.model.CoinData;
import com.alexandre.marah.localcryptovalue.model.CoinMarketData;
import com.alexandre.marah.localcryptovalue.model.LocalCryptoValue;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class CryptoValueServiceTest {

    @Mock
    private CoinGeckoClient coinGeckoClient;

    @InjectMocks
    private CryptoValueService cryptoValueService;

    @Test
    void should_get_crypto_list(){
        // given
        CoinMarketData[] coinMarketDataList = new CoinMarketData[2];
        coinMarketDataList[0] = CoinMarketData.builder()
                .id("bitcoin")
                .name("Bitcoin")
                .build();
        coinMarketDataList[1] = CoinMarketData.builder()
                .id("eth")
                .name("Ethereum")
                .build();
        when(coinGeckoClient.getCryptoList(anyInt()))
                .thenReturn(new ResponseEntity<CoinMarketData[]>(coinMarketDataList, HttpStatus.OK));

        // when
        Map<String, String> result = cryptoValueService.getCryptoList();

        // then
        assertThat(result)
                .hasSize(2)
                .containsKey("bitcoin")
                .containsKey("eth");
    }

    @Test
    void should_get_empty_crypto_list(){
        // given
        when(coinGeckoClient.getCryptoList(anyInt()))
                .thenReturn(new ResponseEntity<CoinMarketData[]>(HttpStatus.BAD_REQUEST));

        // when
        Map<String, String> result = cryptoValueService.getCryptoList();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void should_get_local_crypto_value() throws IOException, GeoIp2Exception {
        // given
        CoinData coinData = CoinData.builder()
                .id("bitcoin")
                .name("Bitcoin")
                .marketCapRank(1)
                .marketData(CoinData.MarketData.builder()
                        .currentPrice(Map.of("eur", BigDecimal.valueOf(14000)))
                        .build())
                .build();
        when(coinGeckoClient.getCryptoValue("bitcoin"))
                .thenReturn(new ResponseEntity<CoinData>(coinData, HttpStatus.OK));

        // when
        LocalCryptoValue result = cryptoValueService.getLocalCryptoValue("bitcoin", "176.158.248.76");

        // then
        assertThat(result.getCryptoId()).isEqualTo("bitcoin");
        assertThat(result.getCryptoName()).isEqualTo("Bitcoin");
        assertThat(result.getCryptoValue()).isEqualTo("14’000.00 €");
    }

    @Test
    void should_get_empty_local_crypto_value_for_incorrect_crypto_id() throws IOException, GeoIp2Exception {
        // given

        // when
        LocalCryptoValue result = cryptoValueService.getLocalCryptoValue("bitcoin incorrect", "176.158.248.76");

        // then
        assertThat(result).isNull();
    }

    @Test
    void should_get_empty_local_crypto_value_for_incorrect_ip() throws IOException, GeoIp2Exception {
        // given

        // when
        LocalCryptoValue result = cryptoValueService.getLocalCryptoValue("bitcoin", "176.158.248.76; select *");

        // then
        assertThat(result).isNull();
    }
}
