package com.hr.utils;

import java.io.File;

import org.apache.commons.lang.StringUtils;

/**
 * 文件工具类
 * @author chenlong
 * @email 1021773811@qq.com
 * @date 2018年7月24日下午4:53:40
 *
 */
public class FileUtil {

	/**
	 * 判断文件是否存在
	 * @author chenlong
	 * @email 1021773811@qq.com
	 * @date 2018年7月24日下午4:54:02
	 *
	 * @param filePath 文件全路径
	 * @return false：不存在 true：存在
	 */
	public static boolean fileExist(String filePath) {
		boolean flag = false;
		//如果路径不为空
		if(!StringUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			flag = file.exists();
		}
		return flag;
	}
	
	/**
	 * 删除文件
	 * @author chenlong
	 * @email 1021773811@qq.com
	 * @date 2018年7月24日下午5:02:11
	 *
	 * @param filePath 文件全路径
	 * @return false：删除失败 true：删除成功
	 */
	public static boolean deleteFile(String filePath) {
		boolean flag = false;
		//如果路径为空
		if (StringUtils.isEmpty(filePath)) {
			flag = false;
		} else {
			File file = new File(filePath);
			// 判断目录或文件是否存在
			if (!file.exists()) {
				// 不存在返回 false
				flag = false;
			} else {
				// 判断是否为文件
				if (file.isFile()) {
					// 为文件时调用删除文件方法
					file.delete();
					flag = true;
				}
			}
		}
		return flag;
	} 

}
