package com.ppx.cloud.demo.module.test;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class Test {

	public static void main(String[] args) throws Exception {
		System.out.println("9999999999999999999");
		
		//RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory() { protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException { super.prepareConnection(connection, httpMethod); connection.setInstanceFollowRedirects(false); } });

		RestTemplate restTemplate = RestTemplateUtils.getRestTemplate();

		HttpHeaders headers = new HttpHeaders();
        headers.add("Connection", "keep-alive");
        headers.add("Cache-Control", "max-age=0");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        headers.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.add("Accept-Encoding", "gzip, deflate");
        headers.add("Accept-Language", "zh-CN,zh;q=0.8");
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Cookie", "");
	       
        //HttpEntity<String> formEntity = new HttpEntity<String>("os_username=hukezhou&os_password=hukezhou&os_destination=&user_role=&atl_token=&login=登录", headers); 
        HttpEntity<String> formEntity = new HttpEntity<String>("os_username=dengxiangzhongs&os_password=dengxiangzhong&os_destination=%2Fbrowse%2FXY-62&user_role=&atl_token=&login=%E7%99%BB%E5%BD%95", headers);        	
    	ResponseEntity<String> r = restTemplate.exchange("http://t.51hrc.cn/login.jsp", HttpMethod.POST, formEntity, String.class, "");
    	
    	System.out.println("r:" + r.getStatusCode());
    	System.out.println("r:" + r.getHeaders());
    	System.out.println("r:" + r.getBody());
	}

}
