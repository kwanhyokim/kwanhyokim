package com.sktechx.godmusic.personal.common.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 설명 :
 *
 * @author 김형진/SKTECHX (twinkle@sk.com)
 * @date 2018. 7. 19.
 */
@Configuration
public class RestTemplateConfig {

    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private FormHttpMessageConverter formHttpMessageConverter;

    @Autowired
    private StringHttpMessageConverter stringHttpMessageConverter;

    @Value("30")
    private int maxConnectionsPerRoute;

    @Value("30")
    private int maxTotalConnections;

    @Value("5000")
    private int connectionTimeout;

    @Value("5000")
    private int readTimeout;

    @Bean
    public RestTemplate restTemplate() {
        return createRestTemplate(clientHttpRequestFactory());
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        return createClientHttpRequestFactory(maxConnectionsPerRoute, maxTotalConnections);
    }

    private RestTemplate createRestTemplate(ClientHttpRequestFactory factory){
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setMessageConverters(getMessageConverters());
        return restTemplate;
    }

    private ClientHttpRequestFactory createClientHttpRequestFactory(int outboundMaxperRoute, int outboundMaxTotal) {
        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(new PoolingHttpClientConnectionManager() {
                    {
                        setDefaultMaxPerRoute(outboundMaxperRoute);
                        setMaxTotal(outboundMaxTotal);
                    }
                }).build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(connectionTimeout);
        factory.setReadTimeout(readTimeout);
        return factory;
    }

    private List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(stringHttpMessageConverter);
        messageConverters.add(formHttpMessageConverter);
        messageConverters.add(mappingJackson2HttpMessageConverter);
        return messageConverters;
    }
}
