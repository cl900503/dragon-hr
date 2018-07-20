package com.hr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hr.service.HrService;
import com.hr.utils.OfficeUtil;

/**
 * hr服务实现类
 * @author chenlong
 * @email 1021773811@qq.com
 * @date 2018年7月19日下午3:18:07
 */
@Service
public class HrServiceImpl implements HrService{

	private static final Logger LOG = LoggerFactory.getLogger(HrServiceImpl.class);
	
	@Override
	public Map<String, String> getData(String filePath) {
		
		//表格对象
		Workbook workbook = OfficeUtil.readExcel(filePath);
		
		LOG.info("表格sheet总数：{}",workbook.getNumberOfSheets());
		
		//读取第一页（考勤信息）
		Sheet sheet = workbook.getSheetAt(0);
		
		LOG.info("表格总行数：" + (sheet.getLastRowNum()+1));
		
		//第一行（标题行）
		Map<String, Integer> titleMap = new HashMap<String, Integer>();
		Row titleRow = sheet.getRow(0);
		for(int i = 0; i < 21;i++) {
			titleMap.put(titleRow.getCell(i).getStringCellValue(),i);
		}
		System.out.println(titleMap);
		//从第二行开始读数据（第一行为标题）
		Map<Integer,Map<String, String>> dataList = new HashMap<Integer,Map<String, String>>();
		//结果集
		Map<Integer,Map<String, String>> resultDataList = new HashMap<Integer,Map<String, String>>();
		for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
			
			//行数据
			Row row = sheet.getRow(rowNum);
			//保存行数据
			Map<String, String> data = new HashMap<String, String>();
			//遍历列
			for(String title:titleMap.keySet()) {
				//列号
				Integer cellNum = titleMap.get(title);
				//列值
				String cellValue = row.getCell(cellNum).getStringCellValue();
				data.put(title, cellValue);
			}
			
			if("异常".equals(data.get("异常"))) {
				resultDataList.put(rowNum, data);
				System.out.println(data);
			}
			dataList.put(rowNum, data);
		}
		
		return null;
	
	}
	
	
	public static void main(String[] args) {
		HrService hrService = new HrServiceImpl();
		hrService.getData("C:\\Excel\\附件一、南京总部-原.xlsx");
	}

}
