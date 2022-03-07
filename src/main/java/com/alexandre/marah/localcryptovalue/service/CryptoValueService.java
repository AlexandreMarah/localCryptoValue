package com.alexandre.marah.localcryptovalue.service;

import com.alexandre.marah.localcryptovalue.client.CoinGeckoClient;
import com.alexandre.marah.localcryptovalue.model.CityDatabaseSingleton;
import com.alexandre.marah.localcryptovalue.model.CoinData;
import com.alexandre.marah.localcryptovalue.model.CoinMarketData;
import com.alexandre.marah.localcryptovalue.model.LocalCryptoValue;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.alexandre.marah.localcryptovalue.utils.LocalCryptoConstants.*;

@Service
@AllArgsConstructor
@Slf4j
public class CryptoValueService {

    @Autowired
    private CoinGeckoClient coinGeckoClient;

    public Map<String, String> getCryptoList() {
        ResponseEntity<CoinMarketData[]> cryptoList = coinGeckoClient.getCryptoList(COIN_GECKO_CRYPTO_LIST_SIZE);
        return cryptoList.getStatusCode() == HttpStatus.OK ? Arrays.stream(cryptoList.getBody())
                .collect(Collectors.toMap(CoinMarketData::getId, CoinMarketData::getName)) : Map.of();
    }

    public LocalCryptoValue getLocalCryptoValue(String cryptoId, String ip)
            throws IOException, GeoIp2Exception {
        if (!Pattern.matches(CRYPTO_ID_REGULAR_EXPRESSION, cryptoId)
                || !(Pattern.matches(IPV4_ADDRESS_REGULAR_EXPRESSION, ip) || Pattern.matches(IPV6_ADDRESS_REGULAR_EXPRESSION, ip))) {
            return null;
        }
        CityResponse cityInfo = getCityResponse(ip);
        Optional<Locale> locale = getLocale(cityInfo);
        if (locale.isPresent()) {
            Currency localCurrency = Currency.getInstance(locale.get());
            return retrieveAndBuildLocalCryptoValue(cryptoId, locale.get(), localCurrency);
        }
        return null;
    }

    private LocalCryptoValue retrieveAndBuildLocalCryptoValue(String cryptoId, Locale locale, Currency localCurrency) {
        ResponseEntity<CoinData> coinData = coinGeckoClient.getCryptoValue(cryptoId);
        if (coinData.getStatusCode().is2xxSuccessful()) {
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            CoinData coinDataBody = coinData.getBody();
            if (coinDataBody == null) {
                log.info("Unable to retrieve the local price for cryptocurrency " + cryptoId);
                return null;
            }
            BigDecimal localeCoinValue = coinDataBody.getMarketData() != null && coinDataBody.getMarketData().getCurrentPrice() != null ?
                    coinDataBody.getMarketData().getCurrentPrice().get(localCurrency.getCurrencyCode().toLowerCase()) : null;
            return LocalCryptoValue
                    .builder()
                    .cryptoId(cryptoId)
                    .cryptoName(coinDataBody.getName())
                    .cryptoValue(currencyFormatter.format(localeCoinValue))
                    .build();
        } else {
            log.info("Unable to retrieve the local price for cryptocurrency " + cryptoId);
            return null;
        }
    }

    private Optional<Locale> getLocale(CityResponse cityInfo) {
        return Arrays.stream(Locale.getAvailableLocales())
                .filter(l -> l.getCountry().equalsIgnoreCase(cityInfo.getRegisteredCountry().getIsoCode()))
                .findFirst();
    }

    private CityResponse getCityResponse(String ip) throws IOException, GeoIp2Exception {
        InetAddress inetAddress = InetAddress.getByName(ip);
        DatabaseReader dbReader = CityDatabaseSingleton.getInstance().getDatabaseReader();
        return dbReader.city(inetAddress);
    }
}