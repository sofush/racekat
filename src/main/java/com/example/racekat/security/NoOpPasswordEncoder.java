package com.example.racekat.security;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Denne klasse implementerer Spring Security's PasswordEncoder interface som sørger for at tjekke
 * overensstemmelse mellem passwords. Normalt bruger man en krypteringsalgoritme til at tjekke overensstemmelse,
 * da passwords i databasen burde være krypteret. Dette er altså den simple men usikre løsning.
 */
public class NoOpPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.contentEquals(rawPassword);
    }
}
