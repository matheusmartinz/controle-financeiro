package com.mfinancas.api.utils;

import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.exceptions.IsNull;

import java.util.regex.Pattern;

public final class ValidateEmail {

    private ValidateEmail() {
    }

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static void validaEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IsNull("Obrigatório informar email.");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new FailedConditional("E-mail inválido.");
        }
    }
}
