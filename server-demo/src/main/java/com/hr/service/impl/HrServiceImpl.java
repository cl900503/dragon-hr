package com.hr.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
	public Map<Integer,Map<String, String>> getData(String filePath) {
		
		//表格对象
		Workbook workbook = OfficeUtil.readExcel(filePath);
		
		LOG.info("表格sheet总数：{}",workbook.getNumberOfSheets());
		
		//读取第一页（考勤信息）
		Sheet sheet = workbook.getSheetAt(0);
		
		LOG.info("表格总行数：{}",sheet.getLastRowNum()+1);
		
		//第一行（标题行）
		Map<String, Integer> titleMap = new HashMap<String, Integer>();
		Row titleRow = sheet.getRow(0);
		for(int i = 0; i < 21;i++) {
			titleMap.put(titleRow.getCell(i).getStringCellValue(),i);
		}
		System.out.println(titleMap);
		//从第二行开始读数据（第一行为标题）
		Map<Integer,Map<String, String>> datasMap = new HashMap<Integer,Map<String, String>>();
		//结果集
		Map<Integer,Map<String, String>> resultDatasMap = new HashMap<Integer,Map<String, String>>();
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
			
			if(data.get("考勤日期").equals("2018-07-19")) {
				continue;
			}
			
			//异常一栏为异常，备注去掉二类和三类
			if("异常".equals(data.get("异常")) && !data.get("备注").startsWith("二类") && !data.get("备注").startsWith("三类")) {
				//只有迟到状态
				if(data.get("迟到").startsWith("迟到") && !data.get("旷工").equals("旷工")) {
					
					//拿该用户昨天打开数据
					Map<String, String> yesterdayData = datasMap.get(rowNum-1);
					//如果是改用户的数据
					if(data.get("用户ID").equals(yesterdayData.get("用户ID"))) {
						//昨天下班打开时间
						String yesterdayOffdutyTimeStr = yesterdayData.get("下班打卡时间");
						//格式化
						DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
						//如果不为空
				    	if(!StringUtils.isEmpty(yesterdayOffdutyTimeStr)) {
				    		LocalDateTime date= LocalDateTime.parse(yesterdayOffdutyTimeStr,formatter);
				    		int hour = date.getHour();
				    		//下班时间，如果加班到8点以后，今天的迟到就不算了
				    		if(hour<20) {
				    			resultDatasMap.put(rowNum, data);
				    		}
				    	//如果昨天没有下班打卡时间
				    	}else {
				    		resultDatasMap.put(rowNum, data);
				    	}
				    //如果没昨天打卡记录（比如：X月1号）
					}else {
						resultDatasMap.put(rowNum, data);
					}
				}else {
					resultDatasMap.put(rowNum, data);
				}
			}
			datasMap.put(rowNum, data);
		}
		
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for(Integer key : resultDatasMap.keySet()) {
			//LOG.info(key+"----"+resultDatasMap.get(key));
			dataList.add(resultDatasMap.get(key));
		}
		//LOG.info("统计完数据数量：{}",resultDatasMap.size());
		
		OfficeUtil.writeExcel("C:\\Excel\\result\\a.xlsx","测试",titleMap,dataList);
		
		return resultDatasMap;
	
	}
	
	
	public static void main(String[] args) {
		HrService hrService = new HrServiceImpl();
		hrService.getData("C:\\Excel\\附件一、南京总部-原.xlsx");
	}

}
