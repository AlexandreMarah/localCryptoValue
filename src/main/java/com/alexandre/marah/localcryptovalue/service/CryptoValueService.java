package com.alexandre.marah.localcryptovalue.service;

import com.alexandre.marah.localcryptovalue.client.CoinGeckoClient;
import com.alexandre.marah.localcryptovalue.model.CoinMarketData;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CryptoValueService {

    @Autowired
    private CoinGeckoClient coinGeckoClient;

    public Map<String, String> getCryptoList() {
        ResponseEntity<CoinMarketData[]> cryptoList = coinGeckoClient.getCryptoList(100);
        return cryptoList.getStatusCode() == HttpStatus.OK ? Arrays.stream(cryptoList.getBody())
                .collect(Collectors.toMap(CoinMarketData::getId, CoinMarketData::getName)) : Map.of();
    }
}
