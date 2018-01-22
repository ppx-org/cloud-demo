package com.ppx.cloud.demo.module.test.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


/**
 * 概述： 所有已 imp 为开头的方法都是数据导入方法，所有以 exp为开头的都是数据导出方法。
 * 即：imp 是从excel中解析数据，并导入到内存中，  exp 是从内存中读取数据，写入到excel 文件
 * 所有可用方法都是静态方法,可以直接调用。 
 * 提供的接口，主要有简单的直接通过API 解析Excel 和复杂的根据模板来解析Excel 2类
 * 方法命名中 带有 templet 字眼的都是需要 Excel模板文件， 不带templet 字眼，或带Notemplet的都是无需模板的
 * 所有公共静态方法都可以使用， 其他方法基本上为内部使用
 * @author huangdos
 *
 */
@SuppressWarnings("rawtypes")
public class ExcelUitl {
	//单表，主数据的前后缀包括起来的数据
	public static final String mainDatePrefix="#";
	//从表： 数据字段的前后缀
	public static final String subDatePrefix="%";
	//文件结束标示 , 主要用在 导入模板中
	public static final String END="_END_";
	//行序号 -- 标明从表数据的开始行， 用在导入导出模板中
	public static final String ROW_NUM="%STRNUM%";// 表示输出数据的行数
	

	
	/**
	 * 根据标题列keylist参数， 和传入的列表数据，直接输出excel 文件流到outio
	 * 无需模板，主要用于简单的下载导出的excel数据
	 * @param listdate 需要输出的数据
	 * @param outio 输出流
	 * @param keylist  标题头，以及对于映射关系, 分别是 字段编码 code ,字段名称 name , 列长度 length ,数据字典转换对象(List数组) changer
	 * @throws Exception
	 * @throws IOException
	 */
	public static void expListDateSample(List<Map> listdate,
			OutputStream outio ,List<ExcelTitle> keylist,String titleName, String title2Name) throws Exception, IOException {
		
		
		
//		WorkbookSettings wss = new WorkbookSettings();
//		wss.setGCDisabled(true);//关闭垃圾收集器
//		WritableWorkbook wwb = Workbook.createWorkbook(outio,wss);// 复制一个模板，并进行编辑模板
//		WritableSheet sheet = wwb.createSheet(titleName==null?"sheet1":titleName, 0);
//		
//        // 指定单元格位置是第一列第一行(0, 0)以及单元格内容为张三
//        Label label = new Label(0, 0, "张三");
//        // 将定义好的单元格添加到工作表中
//        sheet.addCell(label);
//        // 保存数字的单元格必须使用Number的完整包路径
//        jxl.write.Number number = new jxl.write.Number(1, 0, 30);
//        sheet.addCell(number);
//        
//        
//		wwb.write();
//		wwb.close();
		
		
		
		
//		if(listdate.isEmpty()||keylist.isEmpty())
//			return ;
//		File f = new File(inFile); // 输出的文件
//		Workbook workbook = Workbook.getWorkbook(f);// 获取操作Workbook
		WorkbookSettings wss = new WorkbookSettings();
		wss.setGCDisabled(true);//关闭垃圾收集器
		WritableWorkbook wwb = Workbook.createWorkbook(outio,wss);// 复制一个模板，并进行编辑模板
//		ExcelCommonMethod Excel = new ExcelCommonMethod();
		WritableSheet sheet=wwb.createSheet(titleName==null?"sheet1":titleName, 0);
//		List titles= new ArrayList(keylist.size());
//		System.out.println("产生标题"+keylist);
		int columnBestWidth[]=new  int[keylist.size()];    //保存最佳列宽数据的数组
		
		 WritableFont wFont = new WritableFont(   
		            WritableFont.TIMES, 32, WritableFont.BOLD, true);  
		// new WritableCellFormat(new NumberFormat("￥0.00"))
		jxl.write.WritableCellFormat titleFmt = new jxl.write.WritableCellFormat(wFont);
		
		
		
      // 指定单元格位置是第一列第一行(0, 0)以及单元格内容为张三
      Label label = new Label(0, 0, "张三", titleFmt);
      // 将定义好的单元格添加到工作表中
      sheet.addCell(label);
      // 保存数字的单元格必须使用Number的完整包路径
      jxl.write.Number number = new jxl.write.Number(1, 0, 30);
      sheet.addCell(number);
      
      
		
//		WritableFont font = new WritableFont(WritableFont.createFont("雅黑"),  
//                20,   
//                WritableFont.BOLD,   CellFormat
//                false,  
//                UnderlineStyle.NO_UNDERLINE);  
//		jxl.write.WritableCellFormat titleFmt = new jxl.write.WritableCellFormat();
		//titleFmt.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THICK,jxl.format.Colour.BLACK);
//		titleFmt.setAlignment(Alignment.CENTRE);
//		titleFmt.setFont(font);
		
		jxl.write.WritableCellFormat title2Fmt = new jxl.write.WritableCellFormat();
		//title2Fmt.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THICK,jxl.format.Colour.BLACK);
	///	title2Fmt.setAlignment(Alignment.LEFT);
//		font.setPointSize(16);
//		title2Fmt.setFont(font);
		//title
//			Label lb1 = new Label(0,  0, titleName,titleFmt);
//			sheet.addCell(lb1);
//			sheet.setColumnView(0, 50); //设置宽度
		
//			//单位名称
//			Label lb2 = new Label(0,  1, title2Name,title2Fmt);
//			sheet.addCell(lb2);
//			sheet.setColumnView(0, 50); //设置宽度
		
		sheet.mergeCells(0, 0,10,0);
		sheet.mergeCells(0, 1,10,0);
		
//		WritableFont tfont = new WritableFont(WritableFont.createFont("雅黑"),  
//                13,   
//                WritableFont.NO_BOLD,   
//                false,  
//                UnderlineStyle.NO_UNDERLINE);  
//		jxl.write.WritableCellFormat wcsB = new jxl.write.WritableCellFormat();
		//wcsB.setBackground(jxl.format.Colour.IVORY);
		//wcsB.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THICK,jxl.format.Colour.GRAY_25);
		//wcsB.setFont(tfont);
//		for(int i=0;i<keylist.size();i++){
//			ExcelTitle key=keylist.get(i);
//			Label lb = new Label(i, 2, (String)key.getName(),wcsB);
//			sheet.addCell(lb);
//			String len= "abc"; //宽度
////			if("".equals(l1en))
////				sheet.setColumnView(i, 200); //设置宽度
////			else
////					sheet.setColumnView(i, Integer.parseInt(len)); //设置宽度
//			
//			int width=key.getName().length()+10+2;    ///汉字占2个单位长度
//            if(columnBestWidth[i]<width)    ///求取到目前为止的最佳列宽
//                columnBestWidth[i]=width;
//		}
//		System.out.println("产生标题code:"+titles);
//		
//		WritableFont cfont = new WritableFont(WritableFont.createFont("雅黑"),  
//                11,   
//                WritableFont.NO_BOLD,   
//                false,  
//                UnderlineStyle.NO_UNDERLINE);  
//		
//		jxl.write.WritableCellFormat wccol = new jxl.write.WritableCellFormat();
//		//wccol.setWrap(true); //自动换行
//		//wccol.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.GRAY_25);
//		//wccol.setFont(cfont);
//		
//		jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00");    //设置数字格式
//		jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf); //设置表单格式   
//		//wcfN.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.GRAY_25);
//		//wcfN.setFont(cfont);
//		
//		for(int i=0;i<listdate.size();i++){
//			Map m= listdate.get(i);
////			System.out.println("i="+i+",m="+m);
//			for(int j=0;j<keylist.size();j++){
//				ExcelTitle keyindex=keylist.get(j);
//				String key=keyindex.getCode();
//				List changer=(List)keyindex.getChangerList();
//				String data= "abc";
//				if(changer!=null){
//					data= "abc";
//				}
//				
//				//是否数字
//				double num_data = Double.MAX_VALUE;
//				if (data.length()<15 && data.indexOf(".")!=-1){
//					try {
//						num_data = Double.valueOf(data);
//					//}catch (NullPointerException ne){
//						
//						jxl.write.Number labelNF = new jxl.write.Number(j,  i+3, num_data, wcfN); //格式化数值
//						sheet.addCell(labelNF);   //在表单中添加格式化的数字
//					}catch (NumberFormatException nfe){
//						Label lb = new Label(j,  i+3, data,wccol);
//						sheet.addCell(lb);
//					}
//				}else {
//					Label lb = new Label(j,  i+3, data,wccol);
//					sheet.addCell(lb);
//				}
//				
//				 int width=data.length()+10+4;    ///汉字占2个单位长度
//                 if(columnBestWidth[j]<width)    ///求取到目前为止的最佳列宽
//                     columnBestWidth[j]=width;
//			}
//			
//		}
//		 for(int i=0;i<columnBestWidth.length;i++){    ///设置每列宽
//	            sheet.setColumnView(i, columnBestWidth[i]);
//	        }
		wwb.write();
		wwb.close();
//		System.out.println("输出结束!");
//		return null;
	}
	

	

	


}





