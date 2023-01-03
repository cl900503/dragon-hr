package com.hr.easyexcel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.fastjson2.JSON;

/**
 * List<LocalDateTime> <-> String
 * 
 * @author chenlong
 * @date 2022-12-30 15:11:39
 */
public class HrListLocalDateTimeStringConverter implements Converter<List<LocalDateTime>> {

	/**
	 * 日期格式
	 */
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd  HH:mm:ss");

	@Override
	public Class<?> supportJavaTypeKey() {
		return List.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	public WriteCellData<?> convertToExcelData(List<LocalDateTime> value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws Exception {
		List<String> list = new ArrayList<>();
		if (value != null && value.size() > 0) {
			for (LocalDateTime localDateTime : value) {
				list.add(localDateTime.format(DATE_TIME_FORMATTER));
			}
		}
		return new WriteCellData<LocalDateTime>(JSON.toJSONString(list));
	}

}
