package com.voicebill.voice;

import com.voicebill.product.Product;
import com.voicebill.product.ProductRepository;
import com.voicebill.customer.Customer;
import com.voicebill.customer.CustomerRepository;
import com.voicebill.security.CurrentUserResolver;
import com.voicebill.user.User;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/voice")
public class VoiceController {
    private final VoiceService service;
    public VoiceController(VoiceService service) { this.service = service; }

    @PostMapping("/interpret")
    public VoiceDraft interpret(@RequestBody Map<String, String> body) {
        String transcript = body.getOrDefault("transcript", "");
        return service.interpret(transcript);
    }
}
