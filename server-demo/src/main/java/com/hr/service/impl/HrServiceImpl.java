package com.hr.service.impl;

import java.util.Map;

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
		
		//读取第三页
		Sheet sheet = workbook.getSheetAt(0);
		
		LOG.info("表格总行数：" + (sheet.getLastRowNum()+1));
		
		for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
			LOG.info(sheet.getRow(rowNum).getCell(5).toString());
		}
		
		return null;
	
	}

}
