package com.github.avpyanov.tools.testit.client;

import com.github.avpyanov.tools.testit.client.api.*;
import feign.Feign;
import feign.Retryer;
import feign.form.FormEncoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import okhttp3.Interceptor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class TestItApiClient {

    private final Feign.Builder feinBuilder;
    private final String endpoint;

    public TestItApiClient(String endpoint, String token) {
        final OkHttpClient client = getHttpClient(new TestItRequestsInterceptor(token));
        this.endpoint = endpoint;
        feinBuilder = Feign.builder()
                .client(client)
                .encoder(new FormEncoder(new GsonEncoder()))
                .decoder(new GsonDecoder())
                .retryer(Retryer.NEVER_RETRY);
    }

    public Attachments attachmentsApi() {
        return feinBuilder.target(Attachments.class, endpoint);
    }

    public Autotests autotestsApi() {
        return feinBuilder.target(Autotests.class, endpoint);
    }

    public Projects projectsApi() {
        return feinBuilder.target(Projects.class, endpoint);
    }

    public Sections sectionsApi() {
        return feinBuilder.target(Sections.class, endpoint);
    }

    public TestRuns testRunsApi() {
        return feinBuilder.target(TestRuns.class, endpoint);
    }

    public WorkItems workItemsApi() {
        return feinBuilder.target(WorkItems.class, endpoint);
    }

    private OkHttpClient getHttpClient(Interceptor interceptor) {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            return new OkHttpClient(
                    new okhttp3.OkHttpClient.Builder()
                            .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                            .addInterceptor(interceptor).build());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new IllegalStateException("Unable to create http client " + e);
        }
    }
}