package com.alexandre.marah.localcryptovalue.service;

import com.alexandre.marah.localcryptovalue.client.CoinGeckoClient;
import com.alexandre.marah.localcryptovalue.model.CoinMarketData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
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
}
