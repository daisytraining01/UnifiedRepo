package com.internetapp.tests.listeners;


import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;


@Listeners(com.internetapp.tests.listeners.CustomListener.class)
public class TestNG_Test {
	
	@BeforeMethod
	@BeforeClass
	public void setUp() {
		System.out.println("TestNG_ListenersTest1 -> Code in before class");
	}
	
	@AfterClass
	public void cleanUp() {
		System.out.println("TestNG_ListenersTest1 -> Code in after class");
	}
	
	@Test
	public void testMethod1() {
		System.out.println("TestNG_ListenersTest1 -> Code in testMethod1");
		AssertJUnit.assertTrue(true);
	}
	
	@Test
	public void testMethod2() {
		System.out.println("TestNG_ListenersTest1 -> Code in testMethod2");
		AssertJUnit.assertTrue(false);
	}
}
