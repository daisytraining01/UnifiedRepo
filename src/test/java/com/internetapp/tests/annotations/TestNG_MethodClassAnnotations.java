package com.internetapp.tests.annotations;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

public class TestNG_MethodClassAnnotations {
	
	@BeforeClass
	public void setUp() {
		System.out.println("\nThis runs once before all the test methods in the class are executed");
	}
	
	@AfterClass
	public void cleanUp() {
		System.out.println("\nThis runs once after all the test methods in the class are executed");
	}
	
	@BeforeMethod
	public void beforeMethod() {
		System.out.println("\nThis runs before every test method");
	}

	@AfterMethod
	public void afterMethod() {
		System.out.println("\nThis runs after every test method");
	}
	
	@Test
	public void testMethod1() {
		System.out.println("\nRunning Test -> testMethod1");
	}
	
	@Test
	public void testMethod2() {
		System.out.println("\nRunning Test -> testMethod2");
	}
}