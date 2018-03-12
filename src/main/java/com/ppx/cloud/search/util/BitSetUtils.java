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

import com.ppx.cloud.search.common.SGrantContext;

/**
 * 文件名加"_"防止命名冲突
 * @author dengxz
 * @date 2017年12月7日
 */
public class BitSetUtils {
	
	public static final String ORDER_NORMAL = "search_normal";
	
	public static final String ORDER_NEW = "search_new";
	
	public static final String PATH_STORE = "store";
	
	public static final String PATH_TITLE = "title";
	
	public static final String PATH_CAT = "cat";
	
	public static final String PATH_BRAND = "brand";

	public static final String PATH_THEME = "theme";
	
	public static final String PATH_PROMO = "promo";
	
	
	private static Map<Integer, String> versionMap = new HashMap<Integer, String>();
	
	public static void setVersionMap(int mId, String versionName) {
		versionMap.put(mId, versionName);
	}	
	
	public static String getCurrentVersionName() {
		return versionMap.get(getmId());
	}
	
	private static int getmId() {
		return SGrantContext.getSession().getmId();
	}
	
	// file.searchPath
	private static String getSearchPath() {
		return System.getProperty("file.searchPath");
	}
	
	public static String getRealPath(String versionName, String path) {
		return getSearchPath() + getmId() + "/" + versionName + "/" + path + "/";
	}
	
	public static Map<String, Integer> removeVersionPath(String versionName) {
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		countMap.put("removeFile", 0);
		countMap.put("removeFolder", 0);
		
		String path = getSearchPath() + getmId() + "/" + versionName;
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
	        countMap.put("removeFile", countMap.get("removeFile") + 1);
	        return;  
	    }  
	    File[] files = path.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	    	deleteAllFilesOfDir(files[i], countMap);  
	    }  
	    
	    path.delete();
	    countMap.put("removeFolder", countMap.get("removeFolder") + 1);
	} 
	
	public static int initPath(String versionName, String path) {
		String realPath = getRealPath(versionName, path);
		
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
	
	public static BitSet readBitSet(String versionName, String path, Integer w) {
		return readBitSet(versionName, path, w);
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

