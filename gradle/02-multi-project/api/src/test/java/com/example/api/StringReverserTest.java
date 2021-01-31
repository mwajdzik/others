package com.example.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringReverserTest {

    private final StringReverser reverser = new StringReverser();

    @Test
    void canReverse() {
        assertEquals("tinUJ", reverser.reverse("JUnit"));
    }
}
