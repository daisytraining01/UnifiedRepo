package com.internetapp.tests.parallelExecution;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.Test;


public class ParallelClassesExample2 {
	
	public String getTime() {
        Calendar cal = Calendar.getInstance();
	    Date date=cal.getTime();
	    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	    String formattedDate=sdf.format(date);
        return formattedDate;
	}	

	@Test
	public void testCase5() {
		System.out.println("I am inside Thread 2 : test case five with Thread id :" +Thread.currentThread().getId() + " at time : " + getTime());
	}
	
	@Test
	public void testCase6() {
		System.out.println("I am inside Thread 2 : test case six with Thread id :" +Thread.currentThread().getId() + " at time : " + getTime());
	}
	
	@Test
	public void testCase7() {
		System.out.println("I am inside Thread 2 : test case seven with Thread id :" +Thread.currentThread().getId() + " at time : " + getTime());
	}
	
	@Test
	public void testCase8() {
		System.out.println("I am inside Thread 2 : test case eight with Thread id :" +Thread.currentThread().getId() + " at time : " + getTime());
	}
	
}
