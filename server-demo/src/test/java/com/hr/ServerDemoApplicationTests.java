package com.hr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hr.utils.OfficeUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServerDemoApplicationTests {
	
	@Autowired
	private OfficeUtil OfficeUtil;
	
	@Test
	public void contextLoads() {
		System.out.println("SpringBoot测试！！！！");
	}
	
	@Test
	public void testFf() {
		OfficeUtil.ff("hello");
	}

}
