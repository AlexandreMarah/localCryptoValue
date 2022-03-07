package com.alexandre.marah.localcryptovalue.controller;

import com.alexandre.marah.localcryptovalue.model.LocalCryptoValue;
import com.alexandre.marah.localcryptovalue.service.CryptoValueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CryptoValueController.class)
@Import(CryptoValueController.class)
@AutoConfigureWebClient
class CryptoValueControllerTest {

    @MockBean
    private CryptoValueService cryptoValueService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_get_crypto_list() throws Exception {
        // given
        Map<String, String> cryptoMap = new HashMap<>();
        cryptoMap.put("bitcoin", "Bitcoin");
        cryptoMap.put("eth", "ethereum");
        when(cryptoValueService.getCryptoList()).thenReturn(cryptoMap);

        // when
        this.mockMvc.perform(get("/getCryptoList")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("cryptolist"))
                .andExpect(model().attribute("cryptoItemsMap", aMapWithSize(2)));
    }

    @Test
    void should_get_local_crypto_value() throws Exception {
        // given
        LocalCryptoValue localCryptoValue = LocalCryptoValue.builder()
                .cryptoId("bitcoin")
                .cryptoName("Bitcoin")
                .cryptoValue("14,000 $")
                .build();
        when(cryptoValueService.getLocalCryptoValue("bitcoin", "127.128.129.1"))
                .thenReturn(localCryptoValue);

        // when
        this.mockMvc.perform(get("/getLocalCryptoValue")
                        .param("cryptoItemSelection", "bitcoin")
                        .param("ip", "127.128.129.1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("getLocalCryptoValue"))
                .andExpect(model().attribute("cryptoLocalValue", hasProperty("cryptoId", is("bitcoin"))))
                .andExpect(model().attribute("cryptoLocalValue", hasProperty("cryptoName", is("Bitcoin"))))
                .andExpect(model().attribute("cryptoLocalValue", hasProperty("cryptoValue", is("14,000 $"))));
    }
}
