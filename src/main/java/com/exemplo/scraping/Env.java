package com.exemplo.scraping;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {
    private static final Dotenv dotenv = Dotenv.load();

    public static String getEmail() {
        return dotenv.get("EMAIL");
    }

    public static String getSenha() {
        return dotenv.get("SENHA");
    }
}
