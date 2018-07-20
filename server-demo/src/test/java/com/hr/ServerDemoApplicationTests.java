package com.hr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hr.service.HrService;

/**
 * SpringBootTest
 * @author chenlong
 * @email 1021773811@qq.com
 * @date 2018年7月19日下午2:31:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServerDemoApplicationTests {
	
	@Autowired
	private HrService hrService;

	@Test
	public void contextLoads() {
		System.out.println("SpringBoot测试！！！！");
	}
	
	@Test
	public void testGetData() {
		hrService.getData("C:\\Excel\\附件一、南京总部-原.xlsx");
	}

}
