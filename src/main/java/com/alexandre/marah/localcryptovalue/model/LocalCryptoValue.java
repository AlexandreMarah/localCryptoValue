package com.alexandre.marah.localcryptovalue.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Local cryptocurrency price related data.
 */
@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalCryptoValue {

    private String cryptoId;
    private String cryptoName;
    private String cryptoValue;

}
