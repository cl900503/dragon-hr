package com.hr.service;

/**
 * hr服务接口
 * @author chenlong
 * @email 1021773811@qq.com
 * @date 2018年7月19日下午4:42:51
 *
 */
public interface HrService {
	
	/**
	 * 从考勤Excle中分析出考勤异常的数据
	 * @author chenlong
	 * @email 1021773811@qq.com
	 * @date 2018年7月25日上午11:02:03
	 *
	 * @param sourceFilePath 源文件路径（源Excle）
	 * @param targetFilePath 生成文件路径（分析后的Excle）
	 */
	void statisticsTimeCardData(String sourceFilePath, String targetFilePath);
	
}
