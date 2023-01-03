package com.hr.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.hr.domain.CardData;
import com.hr.domain.ResultData;
import com.hr.service.HrService;
import com.hr.service.impl.helper.DataHelper;

/**
 * hr服务实现类(HRS)
 * 
 * @author chenlong
 *
 */
@Service
public class HrServiceHrsImpl implements HrService {

	@Override
	public void statisticsTimeCardData(String sourceFilePath, String targetFilePath) {
		
		// 1. 读表格：获取原始数据
		// 存放表格读取到的
		List<CardData> cardDataList = new ArrayList<>();
		// 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
		// 这里每次会读取100条数据 然后返回过来 直接调用使用数据就行
		EasyExcel.read(sourceFilePath, CardData.class, new PageReadListener<CardData>(dataList -> {
			cardDataList.addAll(dataList);
		})).sheet().doRead();

		// 2. 分析表格数据
		List<ResultData> resultDataList = DataHelper.analyse(cardDataList);

		// 3. 写表格：输出分析结果
		// 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
		// 如果这里想使用03 则 传入excelType参数即可
		EasyExcel.write(targetFilePath, ResultData.class).sheet("统计结果").doWrite(resultDataList);

	}

	public static void main(String[] args) {
		HrService hrService = new HrServiceHrsImpl();
		hrService.statisticsTimeCardData("C:\\Users\\10217\\Desktop\\hr\\11月加班考勤-bak.xlsx",
				"C:\\Users\\10217\\Desktop\\hr\\11月加班考勤-result.xlsx");
	}

}
