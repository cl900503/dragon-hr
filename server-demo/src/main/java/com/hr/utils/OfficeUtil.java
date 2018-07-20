package com.hr.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.apache.commons.lang.StringUtils;

import com.hr.constants.OfficeConstant;

/**
 * Office工具类
 * 
 * @author chenlong
 * @email 1021773811@qq.com
 * @date 2018年7月19日上午10:30:35
 */
@Component
public class OfficeUtil {

	/**
	 * 日志对象
	 */
	private static final Logger LOG = LoggerFactory.getLogger(OfficeUtil.class);

	/**
	 * 解析Excel文件
	 * 
	 * @author chenlong
	 * @email 1021773811@qq.com
	 * @date 2018年7月19日下午2:08:30
	 */
	public static Workbook readExcel(String filePath) {
		// 表格对象
		Workbook workbook = null;
		try {
			if (StringUtils.isEmpty(filePath)) {
				LOG.info("未设置文件路径！");
				return null;
			}
			// 读取文件
			InputStream is = new FileInputStream(filePath);
			if (filePath.endsWith(OfficeConstant.EXCEL_FORMAT_XLSX)) {
				//XLSX
				workbook = new XSSFWorkbook(is);
			} else if (filePath.endsWith(OfficeConstant.EXCEL_FORMAT_XLS)) {
				//XLS
				workbook = new HSSFWorkbook(is);
			} else {
				LOG.info("请检查文件格式（只能处理.xlsx和.xls格式）");
			}

		} catch (Exception e) {
			LOG.error(e.toString());
		}
		return workbook;
	}
	
	/**
	 * 根据数据写Excle文件
	 * @author chenlong
	 * @email 1021773811@qq.com
	 * @date 2018年7月20日上午9:29:13
	 *
	 * @param filePath 指定写文件路径（例如：C:\\Excel\\result\\test.xlsx）
	 * @param sheetName sheet名称
	 * @param titles 列名
	 * @param datas 数据
	 * @return
	 */
	public static boolean writeExcel(String filePath, String sheetName, Map<String, String> titleMap, List<Map<String, Object>> dataList) {
		if(StringUtils.isEmpty(filePath)){
			LOG.info("文件路径不能为空！");
			return false;
		}
		if(StringUtils.isEmpty(sheetName)) {
			LOG.info("sheet名称不能为空！");
			return false;
		}
		
		
		
		
		return false;
	}
	

}
