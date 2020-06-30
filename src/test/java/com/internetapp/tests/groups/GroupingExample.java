package com.internetapp.tests.groups;

import org.testng.annotations.Test;

public class GroupingExample {

	@Test(groups = {"Breakfast", "Food"})
	public void myTestA() {
		
		System.out.println("I am eating Bread");
		
	}
	
	@Test(groups = "Breakfast")
	public void myTestB() {
		
		System.out.println("I am eating Butter");
		
	}
	
	@Test(groups = "Lunch")
	public void myTestC() {
		
		System.out.println("I am eating Pizza");
		
	}
	
	@Test(groups = "Lunch")
	public void myTestD() {
		
		System.out.println("I am eating chicken");
		
	}
	
	@Test(groups = "Dinner")
	public void myTestE() {
		
		System.out.println("I am eating snacks ");
		
	}
	
	@Test(groups = "Dinner")
	public void myTestF() {
		
		System.out.println("I am drinking tea");
		
	}
	
}
