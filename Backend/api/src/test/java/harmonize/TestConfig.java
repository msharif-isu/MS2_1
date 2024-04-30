package harmonize;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContexts;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.client.TestRestTemplate.HttpClientOption;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import harmonize.Services.AdminTestService;
import harmonize.Services.ModeratorTestService;
import harmonize.Services.MusicTestService;
import harmonize.Services.UserTestService;

@Configuration
public class TestConfig {

    @Bean
    public AdminTestService adminTestService() {
        return new AdminTestService("admin", "adminpw");
    }

    @Bean
    public ModeratorTestService modTestService() {
        return new ModeratorTestService("mod", "modpw");
    }

    @Bean
    public UserTestService todTestService() {
        return new UserTestService("twilson", "todpw");
    }

    @Bean
    public UserTestService bobTestService() {
        return new UserTestService("broberts", "bobpw");
    }

    @Bean
    public UserTestService samTestService() {
        return new UserTestService("sjones", "sampw");
    }

    @Bean
    public MusicTestService musicTestService() {
        return new MusicTestService();
    }

    @Bean
    public TestRestTemplate testRestTemplate() throws Exception {
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(null, new TrustAllStrategy())
                .build();
        SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslcontext)
                .build();
        HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslSocketFactory)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .evictExpiredConnections()
                .build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplateBuilder rtb = new RestTemplateBuilder()
            .requestFactory(() -> factory)
            .messageConverters(new MappingJackson2HttpMessageConverter());

        TestRestTemplate testRestTemplate = new TestRestTemplate(rtb, null, null, HttpClientOption.SSL);

        return testRestTemplate;
    }
}
