package com.ppx.cloud.demo.module.test;

import net.coobird.thumbnailator.Thumbnails;

public class Test {

	public static void main(String[] args) throws Exception {
		
		System.out.println("xxxxxxxxx--1");
		
		String path = "C:/Users/LENOVO/Desktop/tmp/1.png";
		Thumbnails.of(path).size(150, 150).toFile("C:/Users/LENOVO/Desktop/tmp/1_150.png");
		
		
		/*
		Thumbnails.of(targetImg)  
        .watermark(position, ImageIO.read(new File(pressImg)), alpha)  
        .outputQuality(1)//生成质量100%  
        .scale(1)//缩放比例  
        .toFile(targetImg); */
	}

}
