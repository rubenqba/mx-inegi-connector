package com.github.rubenqba.inegi.service.impl;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;

public class HttpClientHolder {

    private static HttpClientHolder instance;

    private final OkHttpClient client;

    private HttpClientHolder() {
        ConnectionSpec requireTls12 = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .build();
        this.client = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(requireTls12))
                .followRedirects(true)
                .followSslRedirects(true)
                .readTimeout(Duration.ofSeconds(20))
                .build();
    }

    public static OkHttpClient getHttpClient() {
        if (Objects.isNull(instance)) {
            instance = new HttpClientHolder();
        }
        return instance.client;
    }
}
