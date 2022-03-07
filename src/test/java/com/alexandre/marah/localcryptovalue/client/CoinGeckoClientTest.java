package com.alexandre.marah.localcryptovalue.client;

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
        when(restTemplate.getForEntity(anyString(), eq(CoinMarketData[].class)))
                .thenReturn(new ResponseEntity<CoinMarketData[]>(HttpStatus.OK));

        // when
        ResponseEntity<CoinMarketData[]> result = coinGeckoClient.getCryptoList(100);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    }


}
