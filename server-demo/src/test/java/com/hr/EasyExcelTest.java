package com.hr;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.hr.domain.CardData;
import com.hr.domain.ResultData;
import com.hr.service.impl.helper.DataHelper;

public class EasyExcelTest {

	public static void main(String[] args) {

		List<CardData> list = new ArrayList<>();

		String fileName = "C:\\Users\\10217\\Desktop\\hr\\11月加班考勤.xlsx";
		// 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
		// 这里每次会读取100条数据 然后返回过来 直接调用使用数据就行
		EasyExcel.read(fileName, CardData.class, new PageReadListener<CardData>(dataList -> {
//			System.out.println("****" + dataList.size());
			list.addAll(dataList);
		})).sheet().doRead();

		List<ResultData> resultDataList = DataHelper.analyse(list);
		System.out.println(resultDataList);
		
		
//		EasyExcel.read(fileName, CardData.class, new ReadListener<CardData>() {
//			@Override
//			public void invoke(CardData data, AnalysisContext context) {
//				System.out.println(data);
//			}
//
//			@Override
//			public void doAfterAllAnalysed(AnalysisContext context) {
//				// TODO Auto-generated method stub
//			}
//		}).sheet().doRead();

//		List<ResultData> list = new ArrayList<>();
//		ResultData resultData = new ResultData();
//		resultData.setName("陈龙");
//		resultData.setDepartment("安全架构部");
//		resultData.setEightPmCardTimeSize(1);
//		resultData.setTenPmCardTimeSize(2);
//		List<LocalDateTime> eightPmCardTimeList = new ArrayList<>();
//		eightPmCardTimeList.add(LocalDateTime.now());
//		resultData.setEightPmCardTimeList(eightPmCardTimeList);
//		List<LocalDateTime> tenPmCardTimeList = new ArrayList<>();
//		tenPmCardTimeList.add(LocalDateTime.now());
//		tenPmCardTimeList.add(LocalDateTime.now());
//		resultData.setTenPmCardTimeList(tenPmCardTimeList);
//		list.add(resultData);
//
		String resultFileName = "C:\\Users\\10217\\Desktop\\hr\\11月加班考勤-1229.xlsx";
		// 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
		// 如果这里想使用03 则 传入excelType参数即可
		EasyExcel.write(resultFileName, ResultData.class).sheet("统计").doWrite(resultDataList);
	}

}
