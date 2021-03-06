package com.ppx.cloud.search.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.util.StringUtils;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class WordUtils {
	
	private static List<String> extWordList = new ArrayList<String>();
	
	public static void setExtWord(String extWords) {
		if (StringUtils.isEmpty(extWords)) {
			return;
		}
		
		extWordList = new ArrayList<String>();
		String[] extWord = extWords.split(",");
		for (String w : extWord) {
			extWordList.add(w);
		}
	}
	
	public static String splitWord(String w) {
		
		Configuration cfg = DefaultConfig.getInstance();
		Dictionary dict = Dictionary.initial(cfg);
		dict.addWords(extWordList);
		
		
		IKAnalyzer analyzer = new IKAnalyzer();
		StringReader reader = new StringReader(w);
		
		List<String> listWord = new ArrayList<String>();
		try {
			TokenStream tokenStream = analyzer.tokenStream("", reader);
			tokenStream.reset();			
			while (tokenStream.incrementToken()) {
				CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
				listWord.add(termAttribute.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			analyzer.close();
			reader.close();
		}
		return StringUtils.collectionToCommaDelimitedString(listWord);
	}
	
	public static void main(String[] args) {
		String out = splitWord("https代理*服务器搭建,猎聘网,代理服务器搭建");
		System.out.println("xxxxxxxxxxxxxxxxout:" + out);
		
	}
	
}
