package com.voicebill.voice;

import java.math.BigDecimal;
import java.util.List;

public record VoiceDraft(Long customerId, String customerName, List<Item> items, String message) {
    public record Item(Long productId, String productName, BigDecimal quantity, String unit, BigDecimal unitPrice) {}
}
