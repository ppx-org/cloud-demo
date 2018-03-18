package com.ppx.cloud.demo.module.barcode;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;

public class BarCode {
	public static void main(String[] args) {
		System.out.println("------------begin");
		
		RestTemplate restTemplate = new RestTemplate();
		
		//String r = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxe2d27ee638a5cb7a&secret=5a3e84b9d19c79d75db597583e3a74d5", String.class); 
		
		//System.out.println("rrrrrr:" + r);
		
		String ACCESS_TOKEN = "7_VGOPO2XYVAVINzS4WZOmYCvG-q8_EQfh4N840shEGh5RoaGPvdGQL-9x3xTBsNALs7OCNnnC-fNoP-ZwbryQNfAwNjMCMmuCkbuamO4wqkMgdcG4PuOPXw1ByDsU-NW42dxMcsSGAJLPzZeKVEQcAFAPYK"; 
		
		String url = "https://api.weixin.qq.com/wxa/getwxacode?access_token=" + ACCESS_TOKEN;
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("{\"path\":\"pages/index/index\"}", headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.POST, entity, byte[].class);
        byte[] imageBytes = response.getBody();
        //System.out.println("len:" + new String(imageBytes));
		try {
			File f = new File("F:/U/img/1.png");
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(imageBytes);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
}
