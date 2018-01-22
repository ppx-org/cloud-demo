package com.ppx.cloud.demo.module.test.excel;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Controller
public class ExcelBakController {
	
	
	
	@GetMapping @ResponseBody
	public void test(HttpServletRequest request, HttpServletResponse resp) {
		System.out.println("xxxxxxxxxxxxx--001");
		
	
		
		try {
			
			WritableWorkbook book = null;
	        try {
	            // 打开文件
	            book = Workbook.createWorkbook(new File("C:/Users/LENOVO/Desktop/excel/测试.xls"));
	            // 生成名为"学生"的工作表，参数0表示这是第一页
	            WritableSheet sheet = book.createSheet("学生", 0);
	            // 指定单元格位置是第一列第一行(0, 0)以及单元格内容为张三
	            Label label = new Label(0, 0, "张三");
	            // 将定义好的单元格添加到工作表中
	            sheet.addCell(label);
	            // 保存数字的单元格必须使用Number的完整包路径
	            jxl.write.Number number = new jxl.write.Number(1, 0, 30);
	            sheet.addCell(number);
	            // 写入数据并关闭文件
	            book.write();
	        } catch (Exception e) {
	            System.out.println(e);
	        }finally{
	            if(book!=null){
	                try {
	                    book.close();
	                } catch (Exception e) {
	                    e.printStackTrace();
	                } 
	            }
	        }
			
	
//			resp.setHeader("Content-Disposition", "attachment;filename=" + new String("TEST".getBytes("GB2312"),"ISO-8859-1")+".xls");
//			resp.setContentType("application/vnd.ms-excel;charset=UTF-8");
//			resp.setCharacterEncoding("UTF-8");
//			//输出流
//			OutputStream os = resp.getOutputStream();
//			
//
//		
//		
//			os.flush();
//			os.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}

