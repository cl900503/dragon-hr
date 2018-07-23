package com.hr.service;

import java.util.Map;

/**
 * hr服务接口
 * @author chenlong
 * @email 1021773811@qq.com
 * @date 2018年7月19日下午4:42:51
 *
 */
public interface HrService {
	
	/**
	 * 获取数据
	 * @author chenlong
	 * @email 1021773811@qq.com
	 * @date 2018年7月19日下午4:42:58
	 *
	 * @param filePath 文件路径
	 * @return
	 */
	Map<Integer,Map<String, String>> getData(String filePath);
	
}
