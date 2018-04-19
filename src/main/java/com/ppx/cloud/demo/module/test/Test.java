package com.ppx.cloud.demo.module.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Coordinate;

public class Test {
	
	
	public static void main(String[] args) {
		
		// aspose words for java 16.8.0
		// https://artifact.aspose.com/webapp/#/artifacts/browse/tree/General/repo/com/aspose/aspose-words/18.4/aspose-words-18.4-jdk16.jar
		
		String s = "";
		System.out.println("xxxxxxxxxx:" + s.split(";")[0]);
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void mainx(String[] args) throws Exception {
		System.out.println("----------begin");
		
		String path = "E:/U/img/";
		
		
		String toImgFile = "blank500_out.png";
		
		Map<String, String> map = new HashMap<String, String>();
		
		// 公式:=CONCATENATE("map.put(""",B2,""",""",C2,D2,""");")
		map.put("存过0","00");
		map.put("存过1","10");
		map.put("存过2","20");
		map.put("存过3","30");
		map.put("存过4","40");
		
		for (String key : map.keySet()) {
			String pressImg = path + key + ".png";
			int x = (map.get(key).charAt(0) - 48) * 100;
			int y = (map.get(key).charAt(1) - 48) * 100;
			
			String toImg = path + toImgFile;
			String targetImg = toImg;
			
			File f = new File(toImg);
			if (!f.exists()) {
				targetImg = path + "blank500.png";
			}
			
			System.out.println("out:" + targetImg);
			Coordinate c = new Coordinate(x, y);
			Thumbnails.of(targetImg)  
	        	.watermark(c, ImageIO.read(new File(pressImg)), 1)  
	        	.outputQuality(1).scale(1).toFile(toImg);
		}
			
		System.out.println("----------end");	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
