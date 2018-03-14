package com.ppx.cloud.demo.module.test;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ExcelController {

	@Autowired
	private ExcelService serv;
	
	
	@GetMapping @ResponseBody
	public void test(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("test........export............excel");
		
		List<ExcelTitle> titleList = new ArrayList<ExcelTitle>();
		ExcelTitle t1 = new ExcelTitle("TEST_ID", "ID");
		ExcelTitle t2 = new ExcelTitle("TEST_NAME", "名称");
		titleList.add(t1);
		titleList.add(t2);
		
		try (OutputStream os = response.getOutputStream()) {
			int columnBestWidth[] = new int[titleList.size()];
			
			SXSSFWorkbook wb = ExcelUitlXSSF.getWorkbook(response, "测试导出", titleList, columnBestWidth);
			
			int excelRow = 2;
			int pageSize = 2;
			for (int i = 0; i < pageSize; i++) {
				List<Map<String, Object>> dataList = serv.listData(i);
				excelRow = ExcelUitlXSSF.createDataRow(wb, dataList, titleList, excelRow, columnBestWidth);
			}
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}	

	}

}
