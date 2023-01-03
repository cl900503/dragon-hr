package com.hr.domain;

import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import com.hr.easyexcel.HrLocalDateTimeStringConverter;

import lombok.Data;

/**
 * 打卡数据
 * 
 * @author chenlong
 * @date 2022-12-29 14:39:15
 */
@Data
public class CardData {

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
	 * 打卡时间
	 */
	@ExcelProperty(value = "打卡时间", converter = HrLocalDateTimeStringConverter.class)
	private LocalDateTime cardTime;

}
