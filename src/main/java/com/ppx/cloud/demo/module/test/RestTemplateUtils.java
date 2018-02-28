package com.ppx.cloud.demo.module.test;


import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("deprecation")
public class RestTemplateUtils {

	public static RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, Exception {
		CloseableHttpClient httpClient = acceptsUntrustedCertsHttpClient(false);
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        return restTemplate;
    }
	
	private static CloseableHttpClient acceptsUntrustedCertsHttpClient(boolean isRedirect) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpClientBuilder b = HttpClientBuilder.create();
        if (isRedirect) {
        	b.disableAutomaticRetries(); // 关闭自动处理重定向
            b.setRedirectStrategy(new LaxRedirectStrategy()); // 利用LaxRedirectStrategy处理POST重定向问题
        }
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) {
                return true;
            }
        }).build();       
        b.setSslcontext(sslContext);        

        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();
        
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
        connMgr.setMaxTotal(200);
        connMgr.setDefaultMaxPerRoute(100);
        b.setConnectionManager( connMgr);

        CloseableHttpClient client = b.build();
        return client;
    }
	
	// --------------Redirect-------------
	public static RestTemplate getRestTemplateRedirect() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, Exception {
		CloseableHttpClient httpClient = acceptsUntrustedCertsHttpClient(true);
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        return restTemplate;
    }
	
	
	
	
}
