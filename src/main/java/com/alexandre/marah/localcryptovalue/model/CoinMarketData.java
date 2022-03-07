package com.alexandre.marah.localcryptovalue.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinMarketData {

    @JsonProperty("id")
    private String id;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("name")
    private String name;
    @JsonProperty("current_price")
    private BigDecimal currentPrice;
    @JsonProperty("market_cap_rank")
    private int marketCapRank;
    @JsonProperty("market_cap")
    private BigDecimal marketCap;

}
