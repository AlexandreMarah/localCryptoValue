package com.alexandre.marah.localcryptovalue.controller;

import com.alexandre.marah.localcryptovalue.service.CryptoValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CryptoValueController {

    private final CryptoValueService cryptoValueService;

    @GetMapping("/getCryptoList")
    public String getCryptoList(Model model) {
        model.addAttribute("cryptoItemsMap", cryptoValueService.getCryptoList());
        return "cryptolist";
    }
}
