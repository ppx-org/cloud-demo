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

import com.ppx.cloud.grant.common.GrantContext;

/**
 * 文件名加"_"防止命名冲突
 * @author dengxz
 * @date 2017年12月7日
 */
public class BitSetUtils {
	
	// type  招聘 1,
	
	
	// TODO 按merchantId分
	
	private static String version = "V1";
	
	
	
	
	//  file.searchPath
	private static String getSearchPath() {
		return System.getProperty("file.searchPath");
	}
	
	private static String getRealPath(String path) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		return getSearchPath() + "/" + merchantId + "/" + version + "/" + path + "/";
	}
	
	public static int initPath(String path) {
		String realPath = getRealPath(path);
		
		// 不存目录就创建
		File pathFile = new File(realPath);
		if (!pathFile.exists()) {
			if (!pathFile.mkdirs()) return -1;
		}
		
		// 删除原来的索引
		File[] f = pathFile.listFiles();
		for (File file : f) {
			file.delete();
		}
		return 1;
	}
	
	public static int writeBitSet(String path, String w, BitSet bs) {	
		String realPath = getRealPath(path);
		
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(realPath + "_" + w))) {
			out.writeObject(bs);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 1;
	}
	
	public static BitSet readBitSet(String path, String w) {
		String realPath = getRealPath(path);
	
		File f = new File(realPath + "_" + w);
		if (!f.exists()) return null;
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(realPath + "_" + w))){			
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
