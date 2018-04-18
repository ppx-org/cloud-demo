package com.ppx.cloud.demo.module.test;

import java.io.File;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Coordinate;
import net.coobird.thumbnailator.geometry.Positions;

public class Test {

	public static void main(String[] args) throws Exception {
		
		System.out.println("xxxxxxxxx--1");
		
		
		//String path = "C:/Users/LENOVO/Desktop/tmp/1.png";
		//Thumbnails.of(path).size(150, 150).toFile("C:/Users/LENOVO/Desktop/tmp/1_150.png");
		
		
		String targetImg = "E:/U/img/blank500.png";
		
		String pressImg = "E:/U/img/1_100.png";
		
	
		String toImg = "E:/U/img/blank500_new001.png";
			
		
		
		
		
		
		
		
		
		Coordinate p = new Coordinate(0, 0);
		
		Thumbnails.of(targetImg)  
        .watermark(p, ImageIO.read(new File(pressImg)), 1)  
        .outputQuality(1) // 生成质量100%  
        .scale(1) // 缩放比例 
        .toFile(toImg);
		
		
	}

}
