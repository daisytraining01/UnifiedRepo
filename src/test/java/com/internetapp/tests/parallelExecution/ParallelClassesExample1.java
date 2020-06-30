package com.internetapp.tests.parallelExecution;


import org.testng.annotations.Test;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ParallelClassesExample1 {
	
	public String getTime() {
        Calendar cal = Calendar.getInstance();
	    Date date=cal.getTime();
	    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	    String formattedDate=sdf.format(date);
        return formattedDate;
	}

	@Test
	public void testCase1() {
		System.out.println("I am inside Thread 1 : test case one with Thread id :" +Thread.currentThread().getId() + " at time : " + getTime());
	}
	
	@Test
	public void testCase2() {
		System.out.println("I am inside Thread 1 : test case two with Thread id :" +Thread.currentThread().getId() + " at time : " + getTime());
	}
	
	@Test
	public void testCase3() {
		System.out.println("I am inside Thread 1 : test case three with Thread id :" +Thread.currentThread().getId() + " at time : " + getTime());
	}
	
	@Test
	public void testCase4() {
		System.out.println("I am inside Thread 1 : test case four with Thread id :" +Thread.currentThread().getId() + " at time : " + getTime());
	}
	
}
