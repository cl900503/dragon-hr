package com.hr.utils;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
				workbook = new XSSFWorkbook(is);
			} else if (filePath.endsWith(OfficeConstant.EXCEL_FORMAT_XLS)) {
				workbook = new HSSFWorkbook(is);
			} else {
				LOG.info("请检查文件格式（只能处理.xlsx和.xls格式）");
			}

		} catch (Exception e) {
			LOG.error(e.toString());
		}
		return workbook;
	}

}
