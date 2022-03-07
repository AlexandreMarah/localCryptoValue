package com.alexandre.marah.localcryptovalue.controller;

import com.alexandre.marah.localcryptovalue.service.CryptoValueService;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Controller that contains endpoints related to local cryptocurrency price retrieving.
 */
@Controller
@RequiredArgsConstructor
public class CryptoValueController {

    private final CryptoValueService cryptoValueService;

    @GetMapping("/getCryptoList")
    public String getCryptoList(Model model) {
        model.addAttribute("cryptoItemsMap", cryptoValueService.getCryptoList());
        return "cryptolist";
    }

    /**
     * Get the local value of a cryptocurrency.
     * The local value is coming from the provided IP address or from the requested address otherwise.
     * @param cryptoId      the cryptocurrency ID for which the local value should be retrieved
     * @param ipAddress     optional IP address to determine the locale currency
     * @param model         the model
     * @param request       the request
     * @return              the name of the jsp used to display the results.
     * @throws IOException
     * @throws GeoIp2Exception
     */
    @GetMapping("/getLocalCryptoValue")
    public String getLocalCryptoValue(@RequestParam(value = "cryptoItemSelection") String cryptoId,
                                      @RequestParam(value = "ip", required = false) String ipAddress,
                                      Model model,
                                      HttpServletRequest request) throws IOException, GeoIp2Exception {
        String ip = ipAddress != null ? ipAddress : request.getRemoteAddr();
        model.addAttribute("cryptoLocalValue", cryptoValueService.getLocalCryptoValue(cryptoId, ip));
        return "getLocalCryptoValue";
    }
}
