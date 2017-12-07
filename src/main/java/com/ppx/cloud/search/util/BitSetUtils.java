package com.ppx.cloud.search.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件名加"_"防止命名冲突
 * @author dengxz
 * @date 2017年12月7日
 */
public class BitSetUtils {
	
	// type  招聘 1,
	
	
	//  file.searchPath
	private String getSearchPath() {
		return System.getProperty("file.searchPath");
	}
	
	public static void writeBitSet(String path, String w, BitSet bs) {	
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path + "_" + w))) {
			out.writeObject(bs);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static BitSet readBitSet(String path, String w) {
		File f = new File(path + "_" + w);
		if (!f.exists()) return null;
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path + "_" + w))){			
			BitSet rBs = (BitSet)in.readObject();
			return rBs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void main(String[] args) {
//		BitSet bs = new BitSet();
//		bs.set(10000);
//		writeBitSet("E:/ppx-file/search/", "test", bs);
		
		
		
		// 1.3K * 1000 条
		// 招聘 : blob
		/*
		  招聘:xxx
		 51job:www
		 1.3K * 3000条 = 3M
		 
		 */
		
		
		System.out.println("-------1");
	}
}
