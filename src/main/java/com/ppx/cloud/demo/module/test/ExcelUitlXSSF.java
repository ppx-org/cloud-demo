package com.ppx.cloud.demo.module.test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * excel分页导出
 * 用法:
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
 * 
 * 
 * @author dengxz
 * @date 2018年3月14日
 */
public class ExcelUitlXSSF {
	
	public static SXSSFWorkbook getWorkbook(HttpServletResponse response, String title, List<ExcelTitle> titleList, int columnBestWidth[]) throws IOException {
		response.setContentType("application/force-download"); // 设置下载类型
		String filename = title + ".xlsx";
		filename = new String(filename.getBytes(), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename); // 设置文件的名称
		SXSSFWorkbook wb = new SXSSFWorkbook(100); // 内存中保留100
		// 获得该工作区的第一个sheet
		SXSSFSheet sheet = (SXSSFSheet)wb.createSheet(title);
		
		CellStyle titleStyle = wb.createCellStyle();
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		// 创建标题
		Row firstRow = sheet.createRow((int)0);
		Cell fistCell = firstRow.createCell(0);
		fistCell.setCellValue(title);
		fistCell.setCellStyle(titleStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, titleList.size() - 1));
		
		// 创建标题
		int excelRow = 1;
		// 标题行
		Row titleRow = (Row) sheet.createRow(excelRow++);
		  
		for (int i = 0; i < titleList.size(); i++) {
			Cell cell = titleRow.createCell(i);
			String name = titleList.get(i).getName();
			cell.setCellValue(name);
			// 汉字占2个单位长度
			int width = name.length() + getChineseNum(name) + 2;
			// 求取到目前为止的最佳列宽 
			if (columnBestWidth[i] < width)   {
				columnBestWidth[i] = width;
			}
		}
		return wb;
	}
	
	public static int createDataRow(SXSSFWorkbook wb, List<Map<String, Object>> dataList, List<ExcelTitle> titleList, Integer excelRow, int columnBestWidth[]) {
		// 数据内容
		if (dataList != null && dataList.size() > 0) {
			for (int i = 0; i < dataList.size(); i++) {
				// 明细行
				Row contentRow = (Row) wb.getSheetAt(0).createRow(excelRow++);
				Map<String, Object> reParam = (Map<String, Object>) dataList.get(i);
				for (int j = 0; j < titleList.size(); j++) {
					Cell cell = contentRow.createCell(j);
					Object obj = reParam.get(titleList.get(j).getCode());
					String value = (obj == null ? "" : obj.toString());
					cell.setCellValue(value);
					
					// 汉字占2个单位长度
					int width = value.length() + getChineseNum(value) + 2;
					// 求取到目前为止的最佳列宽 
					if (columnBestWidth[j] < width)   {
						columnBestWidth[j] = width;
					}
				}
			}
		}
		
		// 宽度
		for (int i = 0; i < columnBestWidth.length; i++) {  
			// 设置每列宽
			int w = columnBestWidth[i] * 256;
			w = w > 255*256 ? 255*256 : w;
			wb.getSheetAt(0).setColumnWidth(i, w);
        }
		return excelRow;
	}
	
	private static int getChineseNum(String context){    ///统计context中是汉字的个数
        int lenOfChinese=0;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");    //汉字的Unicode编码范围
        Matcher m = p.matcher(context);
        while(m.find()){
            lenOfChinese++;
        }
        return lenOfChinese;
    }
}





