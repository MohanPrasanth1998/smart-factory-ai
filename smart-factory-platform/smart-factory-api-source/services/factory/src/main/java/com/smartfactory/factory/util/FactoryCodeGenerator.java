package com.smartfactory.factory.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class FactoryCodeGenerator {

    private static final String CHARACTERS ="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final int CODE_LENGTH = 6;

    private final SecureRandom random = new SecureRandom();

    public String generate(){
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return "FAC-" + code;
    }
}
