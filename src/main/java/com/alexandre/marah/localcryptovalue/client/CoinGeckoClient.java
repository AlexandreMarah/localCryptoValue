package com.alexandre.marah.localcryptovalue.client;

import com.alexandre.marah.localcryptovalue.model.CoinMarketData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class CoinGeckoClient {

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<CoinMarketData[]> getCryptoList(int size) {
        log.info("Retrieve the first " + size + " cryptocurrencies names from CoinGecko API.");
        return restTemplate.getForEntity("https://api.coingecko.com/api/v3/coins/markets?vs_currency=eur&order=market_cap_desc&per_page="
                + size + "&page=1&sparkline=false", CoinMarketData[].class);
    }
}
