package com.ppx.cloud.demo.module.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Coordinate;

public class Brand {
	

	public static void main(String[] args) throws Exception {
		System.out.println("----------begin");
		
		String path = "F:/git_repo/ppx/cloud-demo/test/brand/";
		
		
		String toImgFile = "blank500_out.png";
		
		Map<String, String> map = new HashMap<String, String>();
		
		// 公式:=CONCATENATE("map.put(""",B2,""",""",C2,D2,""");")
		map.put("装饰品","00");
		


		
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
			
			System.out.println("pressImg:" + pressImg);
			Coordinate c = new Coordinate(x, y);
			Thumbnails.of(targetImg)  
	        	.watermark(c, ImageIO.read(new File(pressImg)), 1)  
	        	.outputQuality(1).scale(1).toFile(toImg);
		}
			
		System.out.println("----------end");	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
