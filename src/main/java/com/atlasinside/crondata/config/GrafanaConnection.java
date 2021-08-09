package com.atlasinside.crondata.config;

import com.appnexus.grafana.client.GrafanaClient;
import com.appnexus.grafana.configuration.GrafanaConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrafanaConnection {
    private static GrafanaClient instance;

    public GrafanaConnection() {
    }

    public static GrafanaClient getInstance() {
        if (instance == null) {
            GrafanaConfiguration grafanaConfiguration =
                new GrafanaConfiguration().host("http://localhost:3000");
            instance = new GrafanaClient(grafanaConfiguration);
        }
        return instance;
    }
}
