package com.ppx.cloud.demo.module.test.excel;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ExcelController {
	
	
	
	@GetMapping @ResponseBody
	public void test(HttpServletRequest request, HttpServletResponse resp) {
//		System.out.println("xxxxxxxxxxxxx--001");
		

		
		try {
	
			resp.setHeader("Content-Disposition", "attachment;filename=" + new String("TEST".getBytes("GB2312"),"ISO-8859-1")+".xls");
			resp.setContentType("application/vnd.ms-excel;charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			//输出流
			OutputStream os = resp.getOutputStream();
			List<Map> datas = new ArrayList<Map>();
			List dataList = new ArrayList();
			boolean isSheets = false ;  //是否需要多个sheet显示
		
			//表头
			List<ExcelTitle> titleList = new ArrayList<ExcelTitle>();
			ExcelTitle person_status = new ExcelTitle("person_status", "状态", "hr_staff_status");
			titleList.add(person_status);
			ExcelTitle title = new ExcelTitle("staff_name", "姓名");
			titleList.add(title);
			
			
			
			ExcelUitl.expListDateSample(datas, os, titleList,  "花名册","");
			os.flush();
			os.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}

