package com.hr.domain;

import java.time.LocalDate;
import java.util.List;

import com.alibaba.excel.annotation.ExcelProperty;
import com.hr.easyexcel.HrListLocalDateStringConverter;

import lombok.Data;

/**
 * 统计结果
 * 
 * @author chenlong
 * @date 2022-12-30 11:05:58
 */
@Data
public class ResultData {

	/**
	 * 姓名
	 */
	@ExcelProperty(value = "姓名")
	private String name;
	/**
	 * 部门
	 */
	@ExcelProperty(value = "部门")
	private String department;
	/**
	 * 20点打卡次数
	 */
	@ExcelProperty(value = "20点")
	private long eightPmCardSize;
	/**
	 * 22点打卡次数
	 */
	@ExcelProperty(value = "22点")
	private long tenPmCardSize;
	/**
	 * 20点打卡列表
	 */
	@ExcelProperty(value = "20点打卡列表", converter = HrListLocalDateStringConverter.class)
	private List<LocalDate> eightPmCardList;
	/**
	 * 22点打卡列表
	 */
	@ExcelProperty(value = "22点打卡列表", converter = HrListLocalDateStringConverter.class)
	private List<LocalDate> tenPmCardList;

}
