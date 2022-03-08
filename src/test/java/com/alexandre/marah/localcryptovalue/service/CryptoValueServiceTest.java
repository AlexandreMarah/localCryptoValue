package com.alexandre.marah.localcryptovalue.service;

import com.alexandre.marah.localcryptovalue.client.CoinGeckoClient;
import com.alexandre.marah.localcryptovalue.model.CityDatabaseSingleton;
import com.alexandre.marah.localcryptovalue.model.CoinData;
import com.alexandre.marah.localcryptovalue.model.CoinMarketData;
import com.alexandre.marah.localcryptovalue.model.LocalCryptoValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class CryptoValueServiceTest {

    @Mock
    private CoinGeckoClient coinGeckoClient;

    @InjectMocks
    private CryptoValueService cryptoValueService;

    public static final int COIN_GECKO_CRYPTO_LIST_SIZE = 100;

    @Test
    @DisplayName("Should get crypto list")
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
        when(coinGeckoClient.getCryptoList(COIN_GECKO_CRYPTO_LIST_SIZE))
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
    @DisplayName("Should get empty crypto list when Coin Gecko client response is not OK")
    void should_get_empty_crypto_list_for_coin_gecko_not_ok(){
        // given
        when(coinGeckoClient.getCryptoList(COIN_GECKO_CRYPTO_LIST_SIZE))
                .thenReturn(new ResponseEntity<CoinMarketData[]>(HttpStatus.BAD_REQUEST));

        // when
        Map<String, String> result = cryptoValueService.getCryptoList();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should get empty crypto list when Coin Gecko client throws an exception")
    void should_get_empty_crypto_list_for_coin_gecko_exception(){
        // given
        when(coinGeckoClient.getCryptoList(COIN_GECKO_CRYPTO_LIST_SIZE))
                .thenThrow(new RestClientException("Rest client exception"));

        // when
        Map<String, String> result = cryptoValueService.getCryptoList();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should get local cryptocurrency value")
    void should_get_local_crypto_value() {
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
    @DisplayName("Should get empty crypto list when Coin Gecko client response is not OK")
    void should_get_null_local_crypto_value_for_coin_gecko_not_ok(){
        // given
        when(coinGeckoClient.getCryptoValue("bitcoin"))
                .thenReturn(new ResponseEntity<CoinData>(HttpStatus.BAD_REQUEST));

        // when
        LocalCryptoValue result = cryptoValueService.getLocalCryptoValue("bitcoin", "176.158.248.76");

        // then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should get empty crypto list when Coin Gecko client returns empty coin data")
    void should_get_null_local_crypto_value_for_empty_coin_gecko_coin_data(){
        // given
        when(coinGeckoClient.getCryptoValue("bitcoin"))
                .thenReturn(new ResponseEntity<CoinData>(HttpStatus.OK));

        // when
        LocalCryptoValue result = cryptoValueService.getLocalCryptoValue("bitcoin", "176.158.248.76");

        // then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should get null crypto for incorrect input cryptocurrency ID")
    void should_get_null_local_crypto_value_for_incorrect_crypto_id() {
        // given

        // when
        LocalCryptoValue result = cryptoValueService.getLocalCryptoValue("bitcoin incorrect", "176.158.248.76");

        // then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should get null crypto for incorrect input IP address")
    void should_get_null_local_crypto_value_for_incorrect_ip() {
        // given

        // when
        LocalCryptoValue result = cryptoValueService.getLocalCryptoValue("bitcoin", "176.158.248.76; select *");

        // then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should get null local cryptocurrency value when provided IP address could not be converted to InetAddress")
    void should_get_null_local_crypto_value_when_ip_could_not_be_converted_to_inet_address() {
        // given
        try (MockedStatic<InetAddress> inetAddressMockedStatic = Mockito.mockStatic(InetAddress.class)){
            inetAddressMockedStatic.when(() -> InetAddress.getByName("123.124.125.1")).thenThrow(new UnknownHostException());

            // when
            LocalCryptoValue result = cryptoValueService.getLocalCryptoValue("bitcoin", "176.158.248.76");

            // then
            assertThat(result).isNull();
        }
    }

    @Test
    @DisplayName("Should get null local cryptocurrency value when the city local database could not be loaded")
    void should_get_null_local_crypto_value_when_city_database_could_not_be_loaded() {
        // given
        try(MockedStatic<CityDatabaseSingleton> cityDatabaseSingletonMockedStatic = Mockito.mockStatic(CityDatabaseSingleton.class)){
            cityDatabaseSingletonMockedStatic.when(CityDatabaseSingleton::getInstance).thenThrow(new IOException());

            // when
            LocalCryptoValue result = cryptoValueService.getLocalCryptoValue("bitcoin", "176.158.248.76");

            // then
            assertThat(result).isNull();
        }
    }

    @Test
    @DisplayName("Should get null local cryptocurrency value when the " +
            "provided IP address could not be found in the local city database")
    void should_get_null_local_crypto_value_when_ip_address_not_found() {
        // given

        // when
        LocalCryptoValue result = cryptoValueService.getLocalCryptoValue("bitcoin", "123.124.125.1");

        // then
        assertThat(result).isNull();
    }
}
