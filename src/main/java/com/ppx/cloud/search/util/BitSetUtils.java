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

	public static final String PATH_STORE = "store";
	
	public static final String PATH_TITLE = "title";
	
	public static final String PATH_CAT = "cat";
	
	public static final String PATH_BRAND = "brand";

	public static final String PATH_THEME = "theme";
	
	public static final String PATH_PROMO = "promo";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// TODO 按merchantId分
	
	// private static String version = "V1";
	
	
	
	
	//  file.searchPath
	private static String getSearchPath() {
		return System.getProperty("file.searchPath");
	}
	
	public static String getRealPath(String versionName, String path) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		return getSearchPath() + "/" + merchantId + "/" + versionName + "/" + path + "/";
	}
	
	
	public static Map<String, Integer> removeVersionPath(String versionName) {
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		countMap.put("file", 0);
		countMap.put("folder", 0);
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String path = getSearchPath() + "/" + merchantId + "/" + versionName;
		File pathFile = new File(path);
		if (pathFile.exists()) {
			deleteAllFilesOfDir(pathFile, countMap);
		}
		return countMap;
	}
	
	private static void deleteAllFilesOfDir(File path, Map<String, Integer> countMap) {  
	    if (!path.exists())  
	        return;  
	    if (path.isFile()) {  
	        path.delete();
	        countMap.put("file", countMap.get("file") + 1);
	        return;  
	    }  
	    File[] files = path.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	    	deleteAllFilesOfDir(files[i], countMap);  
	    }  
	    
	    path.delete();
	    countMap.put("folder", countMap.get("folder") + 1);
	} 
	
	public static int initPath(String version, String path) {
		String realPath = getRealPath(version, path);
		
		// 不存目录就创建
		File pathFile = new File(realPath);
		if (!pathFile.exists()) {
			if (!pathFile.mkdirs()) return -1;
		}
		return 1;
	}
	
	public static int writeBitSet(String versionName, String path, String w, BitSet bs) {	
		String realPath = getRealPath(versionName, path);
		
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(realPath + "_" + w))) {
			out.writeObject(bs);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 1;
	}
	
	public static BitSet readBitSet(String versionName, String path, String w) {
		String realPath = getRealPath(versionName, path);
	
		File f = new File(realPath + "_" + w);
		if (!f.exists()) return new BitSet();
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(realPath + "_" + w))){			
			BitSet rBs = (BitSet)in.readObject();
			return rBs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BitSet();
	}
	
	
	// 从BitSet toString改造
	public static List<Integer> bsToPage(BitSet bs, int begin, int len) {
		List<Integer> returnList = new ArrayList<Integer>();
		int maxOffset = begin + len;
		int c = 0;
        StringBuilder b = new StringBuilder();
        int i = bs.nextSetBit(0);
        if (i != -1) {
            if (begin == 0) {
            	returnList.add(i);
            }
            while (true) {
                if (++i < 0) break;
                if ((i = bs.nextSetBit(i)) < 0) break;
                int endOfRun = bs.nextClearBit(i);
                
                boolean isEnd = false;
                do { 
                	c++;
                	if (c >= maxOffset) {
                		isEnd = true;
                		break;
                	}
                	if (c >= begin) {
                		returnList.add(i);
                	}
                }
                while (++i != endOfRun);
                if (isEnd) break;
            }
        }
        return returnList;
    }
	
	


	
	
	
	
}
