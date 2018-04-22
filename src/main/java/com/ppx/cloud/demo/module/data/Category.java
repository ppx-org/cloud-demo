package com.ppx.cloud.demo.module.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Coordinate;

public class Category {
	

	public static void main(String[] args) throws Exception {
		System.out.println("----------begin");
		
		String path = "F:/git_repo/ppx/cloud-demo/test/category/";
		
		
		String toImgFile = "blank500_out.png";
		
		Map<String, String> map = new HashMap<String, String>();
		
		// 公式:=CONCATENATE("map.put(""",B2,""",""",C2,D2,""");")
		map.put("装饰品","00");
		map.put("工艺品","10");
		map.put("相框","20");
		map.put("钥匙扣","30");
		map.put("杯子","40");
		map.put("置物架","01");
		map.put("暖宝宝","11");
		map.put("饭盒","21");
		map.put("眼镜","31");
		map.put("浴巾","41");

		map.put("手机数据线","02");
		map.put("耳机","12");
		map.put("电风扇","22");
		map.put("剃须刀","32");

		map.put("早教机","42");
		map.put("婴幼儿玩具","03");
		map.put("气球","13");
		map.put("戏水玩具","23");

		map.put("笔记本","33");
		map.put("台历","43");
		map.put("笔","04");

		map.put("面膜","14");
		map.put("洗发水","24");


		
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
