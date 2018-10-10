package com.hr.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
 * 
 * @author chenlong
 * @email 1021773811@qq.com
 * @date 2018年7月19日下午3:18:07
 */
@Service
public class HrServiceImpl implements HrService {

	/**
	 * 日志对象
	 */
	private static final Logger LOG = LoggerFactory.getLogger(HrServiceImpl.class);

	@Override
	public void statisticsTimeCardData(String sourceFilePath, String targetFilePath) {

		// 表格对象
		Workbook workbook = OfficeUtil.readExcel(sourceFilePath);

		LOG.info("表格sheet总数：{}", workbook.getNumberOfSheets());

		// 读取第一页（考勤信息）
		Sheet sheet = workbook.getSheetAt(0);

		LOG.info("表格总行数：{}", sheet.getLastRowNum() + 1);

		// 用于存放title，例：{迟到=10, 请假=15, 旷工=12, 加班=18, 备注=20, 上班打卡时间=8, 考勤日期=3, 考勤规则=5,
		// 请假天数=16, 请假小时=17, 部门=1, 周几=4, 用户姓名=2, 下班打卡时间=9, 用户ID=0, 上班时间=6, 未刷卡=13,
		// 下班时间=7, 早退=11, 异常=19, 出差=14}
		Map<String, Integer> titleMap = new HashMap<String, Integer>(16);
		// 第一行（标题行）
		Row titleRow = sheet.getRow(0);
		for (int i = 0; i < titleRow.getLastCellNum(); i++) {
			titleMap.put(titleRow.getCell(i).getStringCellValue(), i);
		}
		LOG.info("标题栏：{}", titleMap);
		// 从第二行开始读数据（第一行为标题）
		Map<Integer, Map<String, String>> datasMap = new HashMap<Integer, Map<String, String>>(16);
		// 结果集（分析后的数据）
		Map<Integer, Map<String, String>> resultDatasMap = new LinkedHashMap<Integer, Map<String, String>>();
		// 遍历所有行
		for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {

			// 行数据
			Row row = sheet.getRow(rowNum);
			// 保存行数据
			Map<String, String> data = new HashMap<String, String>(16);

			// 遍历列
			for (String title : titleMap.keySet()) {
				// 列号
				Integer cellNum = titleMap.get(title);
				// 列值
//				System.out.println(cellNum);
//				System.out.println(cellNum+"--"+row.getCell(cellNum));
				
				String cellValue = row.getCell(cellNum) == null ? "" : row.getCell(cellNum).getStringCellValue();
				data.put(title, cellValue);
			}

//			if (data.get("考勤日期").equals("2018-07-19")) {
//				continue;
//			}
			
//			if(data.get("考勤日期").startsWith("2018-09")) {
//				continue;
//			}

			// 备注去掉二类和三类
			if (!data.get("备注").startsWith("二类") && !data.get("备注").startsWith("三类")) {
				// 异常一栏为异常
				if ("异常".equals(data.get("异常"))) {
					// 只有迟到状态
					if (data.get("迟到").startsWith("迟到") && !data.get("旷工").equals("旷工") && !data.get("早退").startsWith("早退")) {

						// 拿该用户昨天打开数据
						Map<String, String> yesterdayData = datasMap.get(rowNum - 1);
//						System.out.println(data);
//						System.out.println(yesterdayData);
//						System.out.println(rowNum);
						// 如果是改用户的数据
						if (yesterdayData != null && data.get("用户ID").equals(yesterdayData.get("用户ID"))) {
							// 昨天下班打开时间
							String yesterdayOffdutyTimeStr = yesterdayData.get("下班打卡时间");
							// 格式化
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
							// 如果不为空
							if (!StringUtils.isEmpty(yesterdayOffdutyTimeStr)) {
								LocalDateTime date = LocalDateTime.parse(yesterdayOffdutyTimeStr, formatter);
								int hour = date.getHour();
								// 下班时间，如果加班到8点以后，今天的迟到就不算了
								if (hour < 20) {
									resultDatasMap.put(rowNum, data);
								}
								// 如果昨天没有下班打卡时间
							} else {
								resultDatasMap.put(rowNum, data);
							}
							// 如果没昨天打卡记录（比如：X月1号）
						} else {
							resultDatasMap.put(rowNum, data);
						}
					} else {
						// 其他异常直接返回
						resultDatasMap.put(rowNum, data);
					}
				} else {

					// 迟到、早退、旷工（i）
					// 未刷卡、出差、请假（j）
					// 异常次数
					int i = 0;
					if (data.get("迟到").startsWith("迟到")) {
						i += 1;
					}
					if (data.get("早退").startsWith("早退")) {
						i += 1;
					}
					if (data.get("旷工").startsWith("旷工")) {
						i += 1;
					}

					// 补单次数
					int j = 0;
					if (!StringUtils.isEmpty(data.get("未刷卡"))) {
						j += 1;
					}
					if (!StringUtils.isEmpty(data.get("出差"))) {
						j += 1;
					}
					if (!StringUtils.isEmpty(data.get("请假"))) {
						j += 1;
					}
					
					// i>j
					if (i > j) {
						resultDatasMap.put(rowNum, data);
					}

				}
			}
			datasMap.put(rowNum, data);
		}

		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (Integer key : resultDatasMap.keySet()) {
			dataList.add(resultDatasMap.get(key));
		}

		System.out.println(OfficeUtil.writeExcel(targetFilePath, sheet.getSheetName(), titleMap, dataList));

	}

	public static void main(String[] args) {
		HrService hrService = new HrServiceImpl();
		hrService.statisticsTimeCardData("C:\\Excel\\南京.xlsx","C:\\Excel\\result\\南京-result.xlsx");
	}

}
