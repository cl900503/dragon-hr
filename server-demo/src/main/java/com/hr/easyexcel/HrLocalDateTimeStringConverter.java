package com.hr.easyexcel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * LocalDateTime <-> String
 * 
 * @author chenlong
 * @date 2022-12-30 10:30:35
 */
public class HrLocalDateTimeStringConverter implements Converter<LocalDateTime> {

	/**
	 * 日期格式
	 */
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd  HH:mm:ss");

	@Override
	public Class<?> supportJavaTypeKey() {
//		System.out.println("supportJavaTypeKey");
		return LocalDateTime.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
//		System.out.println("supportExcelTypeKey");
		return CellDataTypeEnum.STRING;
	}

	@Override
	public LocalDateTime convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws Exception {
//		System.out.println("convertToJavaData");
		return LocalDateTime.parse(cellData.getStringValue(), DATE_TIME_FORMATTER);
	}

	@Override
	public WriteCellData<?> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws Exception {
//		System.out.println("convertToExcelData");
		return new WriteCellData<LocalDateTime>(value.format(DATE_TIME_FORMATTER));
	}

}
