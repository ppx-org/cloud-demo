package com.ppx.cloud.file.img;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.grant.common.GrantContext;

@Controller
public class ImgUploadController {

	/**
	 * 路经=merchantId/yyyyMMdd/UUID.ext
	 * 
	 * @param file
	 * @return
	 */
	@PostMapping @ResponseBody
	public Map<String, Object> multipleSave(@RequestParam("file") MultipartFile[] file) {
		List<String> returnList = new ArrayList<String>();
		
		if (file == null || file.length == 0) {
			return ControllerReturn.ok(returnList);
		}
		
		for (int i = 0; i < file.length; i++) {				
			BufferedOutputStream buffStream = null;
			try {
				String fileName = file[i].getOriginalFilename();
				if (StringUtils.isEmpty(fileName)) {
					continue;
				}
				byte[] bytes = file[i].getBytes();
				String path = getImgPath(fileName);
				buffStream = new BufferedOutputStream(new FileOutputStream(new File(System.getProperty("file.imgFilePath") + path)));
				buffStream.write(bytes);
				returnList.add(path);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (buffStream != null) {
					try {
						buffStream.close();
					} catch (Exception e) {}
				}
			}
		}
		return ControllerReturn.ok(returnList);
	}
	
	
	
	private String getImgPath(String fileName) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String path = merchantId + "/" + new SimpleDateFormat("yyyyMMdd").format(new Date());
		File pathFile = new File(System.getProperty("file.imgFilePath") + path);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		
		String ext = fileName.substring(fileName.lastIndexOf("."));
		String imgFileName = UUID.randomUUID().toString().replaceAll("-", "") + ext;
		return path + "/" + imgFileName;
	}

	
	
	
	
	
	
	// 其它图片上传
	/**
	 * 路经=merchantId/show/name.ext
	 * 
	 * @param file
	 * @return
	 */
	@PostMapping @ResponseBody
	public Map<String, Object> showSave(@RequestParam("file") MultipartFile[] file, @RequestParam("type") String[] type) {
		List<String> returnList = new ArrayList<String>();
		
		if (file == null || file.length == 0 || file.length != type.length) {
			return ControllerReturn.ok(returnList);
		}
		
		for (int i = 0; i < file.length; i++) {				
			BufferedOutputStream buffStream = null;
			try {
				String fileName = file[i].getOriginalFilename();
				if (StringUtils.isEmpty(fileName)) {
					continue;
				}
				byte[] bytes = file[i].getBytes();
				String path = getShowPath(fileName, type[i]);
				buffStream = new BufferedOutputStream(new FileOutputStream(new File(System.getProperty("file.imgFilePath") + path)));
				buffStream.write(bytes);
				returnList.add(path);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (buffStream != null) {
					try {
						buffStream.close();
					} catch (Exception e) {}
				}
			}
		}
		return ControllerReturn.ok(returnList);
	}
	
	private String getShowPath(String fileName, String type) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String ext = fileName.substring(fileName.lastIndexOf("."));
		
		if (type.equals("swiper") || type.equals("store")) {
			String path = merchantId + "/show/" + type;
			File pathFile = new File(System.getProperty("file.imgFilePath") + path);
			if (!pathFile.exists()) {
				pathFile.mkdirs();
			}
			String imgFileName = UUID.randomUUID().toString().replaceAll("-", "") + ext;
			return path + "/" + imgFileName;
		}
		else {
			String path = merchantId + "/show";
			File pathFile = new File(System.getProperty("file.imgFilePath") + path);
			if (!pathFile.exists()) {
				pathFile.mkdirs();
			}
			return path + "/" + type + ext;
		}
		
	
	}
}