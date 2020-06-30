package com.internetapp.tests;


import org.testng.annotations.Test;

public class TestNG_DisableTest {
	@Test(enabled = false)
	public void myTest1() {
		System.out.println("Running my test 1");
	}

	@Test
	public void myTest2() {
		System.out.println("Running my test 2");
	}

}
