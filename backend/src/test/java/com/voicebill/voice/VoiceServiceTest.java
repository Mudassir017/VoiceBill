package com.voicebill.voice;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VoiceServiceTest {
    private final VoiceService service = new VoiceService(null, null, null);

    @Test
    void emptyTranscriptThrows() {
        try {
            service.interpret("");
            throw new AssertionError("expected exception");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    @Test
    void nullTranscriptThrows() {
        try {
            service.interpret(null);
            throw new AssertionError("expected exception");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    @Test
    void tooLongTranscriptThrows() {
        String longText = "a".repeat(501);
        try {
            service.interpret(longText);
            throw new AssertionError("expected exception");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
}
