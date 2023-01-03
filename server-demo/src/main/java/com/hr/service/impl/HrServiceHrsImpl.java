package com.hr.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
 * hr服务实现类(HRS)
 * 
 * @author chenlong
 *
 */
@Service
public class HrServiceHrsImpl implements HrService {

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

		// {姓名=0, 部门=1, 打卡时间=2}
		Map<String, Integer> titleMap = new HashMap<String, Integer>(16);
		// 第一行（标题行）
		Row titleRow = sheet.getRow(0);
		for (int i = 0; i < titleRow.getLastCellNum(); i++) {
			titleMap.put(titleRow.getCell(i).getStringCellValue(), i);
		}
		LOG.info("标题栏：{}", titleMap);
		System.out.println(titleMap);

		// 这个月的第一个星期6是几号
//		int first6Day = 6;

		// Map<String, List<String>> result = new HashMap<>();

		// <"userName",<"2022/07/16",List<"2022/07/16 14:54:52">>>
		Map<String, Map<String, List<String>>> xlsxDatas = new HashMap<>();
		// <"userName",<>>
		Map<String, Map<String, String>> allPersonResult = new HashMap<>();

		// 遍历所有行
		for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {

			// 行数据
			Row row = sheet.getRow(rowNum);

			// 表示这条数据是谁的
			String userName = "";

			// 遍历列
			for (String title : titleMap.keySet()) {
				// 列号
				Integer cellNum = titleMap.get(title);
				// 列值
				String cellValue = row.getCell(cellNum) == null ? "" : row.getCell(cellNum).getStringCellValue();

				// System.out.println(title+":"+cellValue);

				// 姓名
				if (cellNum == 0) {

					// 姓名
					userName = cellValue;

					// 第一列是姓名，这里进行一些初始化动作。
					if (xlsxDatas.get(userName) == null) {
						Map<String, List<String>> data = new HashMap<>();
						xlsxDatas.put(userName, data);
					}
					if (allPersonResult.get(userName) == null) {
						Map<String, String> personResult = new HashMap<>();
						allPersonResult.put(userName, personResult);
					}

				}

				// 打卡时间
				else if (cellNum == 2) {

					// 格式化
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd  HH:mm:ss");
					LocalDateTime date = LocalDateTime.parse(cellValue, formatter);

					// 日期:2022/7/1
					String riqi = date.getYear() + "/" + date.getMonthValue() + "/" + date.getDayOfMonth();

					// 日期前一天:2022/7/0
					String qianRiqi = date.getYear() + "/" + date.getMonthValue() + "/" + (date.getDayOfMonth() - 1);

					// userName的打卡数据
					Map<String, List<String>> data = xlsxDatas.get(userName);

					// 小时
					int hour = date.getHour();
					// 算前一天的打卡数据
					if (hour < 6) {
						if (data.get(qianRiqi) == null) {
							data.put(qianRiqi, new ArrayList<String>());
						}
						data.get(qianRiqi).add(cellValue);
					} else {
						if (data.get(riqi) == null) {
							data.put(riqi, new ArrayList<String>());
						}
						data.get(riqi).add(cellValue);
					}

				}
			}

		}

		// 分析结果 start....

		// 20/21/22以后打卡列表：<userName-20,List>
		Map<String, List<String>> jiabanMap = new HashMap<>();

		// 对打卡数据进行排序
		xlsxDatas.forEach((userName, data) -> {

			if (jiabanMap.get(userName + "-20") == null) {
				jiabanMap.put(userName + "-20", new ArrayList<String>());
			}

//			if (jiabanMap.get(userName + "-21") == null) {
//				jiabanMap.put(userName + "-21", new ArrayList<String>());
//			}

			if (jiabanMap.get(userName + "-22") == null) {
				jiabanMap.put(userName + "-22", new ArrayList<String>());
			}
			
//			if (jiabanMap.get(userName + "-weekend-records") == null) {
//				jiabanMap.put(userName + "-weekend-records", new ArrayList<String>());
//			}
//
//			if (jiabanMap.get(userName + "-weekend-counts") == null) {
//				jiabanMap.put(userName + "-weekend-counts", new ArrayList<String>());
//			}

			data.forEach((riqi, list) -> {
				// 排序打卡时间
				Collections.sort(list);

				list.forEach(dateStr -> {

					// 格式化
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd  HH:mm:ss");
					LocalDateTime date = LocalDateTime.parse(dateStr, formatter);

					// 小时
					int hour = date.getHour();

					// start...20/21/22以后打卡次数
					if (hour >= 22) {
						jiabanMap.get(userName + "-22").add(riqi);
//					} else if (hour >= 21) {
//						jiabanMap.get(userName + "-21").add(riqi);
					} else if (hour >= 20) {
						jiabanMap.get(userName + "-20").add(riqi);
					} else if (hour < 6) {
						System.out.println(userName + ":" + dateStr);
						String qianRiqi = date.getYear() + "/" + date.getMonthValue() + "/"
								+ (date.getDayOfMonth() - 1);
						jiabanMap.get(userName + "-22").add(qianRiqi);
					}
					// end.20/21/22以后打卡次数

				});

//				// start...周末加班>8小时次数
//				// 格式化
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");
//				LocalDate date = LocalDate.parse(riqi, formatter);
//				// 周六周日：计算工作时长
//				if ((date.getDayOfMonth() - first6Day) % 7 == 0 || (date.getDayOfMonth() - first6Day) % 7 == 1) {
//
//					// 根据list算时间:xxx-xxx-xxx
//
//					if (list.size() <= 1) {
//						jiabanMap.get(userName + "-weekend-records").add("[" + list.get(0) + "-未知]->0");
//						//jiabanMap.get(userName + "-weekend-counts").add("0");
//					} else {
//
//						DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd  HH:mm:ss");
//						LocalDateTime fromDate = LocalDateTime.parse(list.get(0), f);
//						LocalDateTime toDate = LocalDateTime.parse(list.get(list.size() - 1), f);
//						long minutes = ChronoUnit.MINUTES.between(fromDate, toDate);
//
//						String record = "[" + list.get(0) + "-" + list.get(list.size() - 1) + "]->" + minutes + "->"
//								+ minutes / 60 + "小时" + minutes % 60 + "分钟";
//						System.out.println(list);
//						jiabanMap.get(userName + "-weekend-records").add(record);
//
//						// 7小时50分钟当成8小时
//						if (minutes >= (7 * 60 + 50)) {
//							jiabanMap.get(userName + "-weekend-counts").add(riqi);
//						}
//
//					}
//
//				}
//				// end.周末加班>8小时次数

			});

		});

		Map<String, Integer> resultTitleMap = new HashMap<>();
		resultTitleMap.put("姓名", 0);
		resultTitleMap.put("20点", 1);
//		resultTitleMap.put("21点", 2);
		resultTitleMap.put("22点", 2);
//		resultTitleMap.put("周六周日加班情况（>8小时次数）", 4);
		resultTitleMap.put("20点打卡列表", 3);
//		resultTitleMap.put("21点打卡列表", 6);
		resultTitleMap.put("22点打卡列表", 4);
//		resultTitleMap.put("周六周日加班情况", 8);

		// 分析结果
		Map<String, Map<String, String>> userMap = new HashMap<>();

		Object[] key_arr = jiabanMap.keySet().toArray();
		Arrays.sort(key_arr);
		for (Object key : key_arr) {
			// System.out.println("key:" + key);
			String userName = key.toString().split("-")[0];

			List<String> list = jiabanMap.get(key);

			if (userMap.get(userName) == null) {
				Map<String, String> dataMap = new HashMap<String, String>();
				userMap.put(userName, dataMap);
			}

			if (key.toString().endsWith("-weekend-records")) {

				userMap.get(userName).put("周六周日加班情况", StringUtils.join(list, "\n"));

			} else if (key.toString().endsWith("-weekend-counts")) {

				userMap.get(userName).put("周六周日加班情况（>8小时次数）", list.size() + "");

			} else {

				List<String> distinct = list.stream().distinct().collect(Collectors.toList());
				// System.out.println(key + ":" + distinct.size() + ":" + distinct);

				userMap.get(userName).put(key.toString().split("-")[1] + "点", distinct.size() + "");
				userMap.get(userName).put(key.toString().split("-")[1] + "点打卡列表", distinct.toString());
			}

		}

		// 整合导出数据
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		userMap.forEach((k, v) -> {
			v.put("姓名", k);
			dataList.add(v);
			System.out.println(v);
		});

		System.out.println(OfficeUtil.writeExcel(targetFilePath, sheet.getSheetName(), resultTitleMap, dataList));

	}

	public static void main(String[] args) {
		HrService hrService = new HrServiceHrsImpl();
		hrService.statisticsTimeCardData("C:\\Users\\10217\\Desktop\\hr\\11月加班考勤.xlsx",
				"C:\\Users\\10217\\Desktop\\hr\\11月加班考勤-result.xlsx");
	}

}
