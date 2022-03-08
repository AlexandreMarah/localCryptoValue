package com.alexandre.marah.localcryptovalue.client;

import com.alexandre.marah.localcryptovalue.model.CoinData;
import com.alexandre.marah.localcryptovalue.model.CoinMarketData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoinGeckoClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CoinGeckoClient coinGeckoClient;

    @Test
    void should_get_crypto_list_from_coingecko() {
        // given
        CoinMarketData coinMarketData = CoinMarketData.builder()
                .id("bitcoin")
                .build();
        CoinMarketData coinMarketData2 = CoinMarketData.builder()
                .id("ethereum")
                .build();
        when(restTemplate.getForEntity(anyString(), eq(CoinMarketData[].class)))
                .thenReturn(new ResponseEntity<CoinMarketData[]>(new CoinMarketData[]{coinMarketData, coinMarketData2}, HttpStatus.OK));

        // when
        ResponseEntity<CoinMarketData[]> result = coinGeckoClient.getCryptoList(100);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).hasSize(2);
    }

    @Test
    void should_get_crypto_value_from_coingecko(){
        // given
        CoinData coinData = CoinData.builder().id("bitcoin").build();
        when(restTemplate.getForEntity("https://api.coingecko.com/api/v3/coins/bitcoin?localization=false" +
                "&tickers=false&market_data=true&community_data=false&developer_data=false&sparkline=false", CoinData.class))
                .thenReturn(new ResponseEntity<CoinData>(coinData, HttpStatus.OK));

        // when
        ResponseEntity<CoinData> result = coinGeckoClient.getCryptoValue("bitcoin");

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody().getId()).isEqualTo("bitcoin");
    }


}
