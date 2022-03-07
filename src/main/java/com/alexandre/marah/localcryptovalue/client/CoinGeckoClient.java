package com.alexandre.marah.localcryptovalue.client;

import com.alexandre.marah.localcryptovalue.model.CoinData;
import com.alexandre.marah.localcryptovalue.model.CoinMarketData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * CoinGecko rest client.
 */
@Component
@Slf4j
public class CoinGeckoClient {

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<CoinMarketData[]> getCryptoList(int size) {
        log.info("Retrieve the first " + size + " cryptocurrencies names from CoinGecko.");
        return restTemplate.getForEntity("https://api.coingecko.com/api/v3/coins/markets?vs_currency=eur&order=market_cap_desc&per_page="
                + size + "&page=1&sparkline=false", CoinMarketData[].class);
    }

    public ResponseEntity<CoinData> getCryptoValue(String cryptoId) {
        log.info("Retrieve information about " + cryptoId + " cryptocurrency from CoinGecko.");
        return restTemplate.getForEntity("https://api.coingecko.com/api/v3/coins/" + cryptoId +
                "?localization=false&tickers=false&market_data=true&community_data=false&developer_data=false&sparkline=false", CoinData.class);
    }
}
