package com.tperuch.assemblyservice.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;

public class GsonConfig {

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
