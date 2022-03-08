package com.alexandre.marah.localcryptovalue.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Collection of coin market related objects.
 */
@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinMarket {

    @JsonUnwrapped
    private List<CoinMarketData> coinMarketDataList;

}
