package com.internetapp.tests;

import java.lang.Math; 

/**
 * The Calculator class provides static methods for
 *  arithmetic operations on two integers.
 */
public class Calculator {
   public static int add(int number1, int number2) {
      return number1 + number2;
   }
 
   public static int sub(int number1, int number2) {
      return number1 - number2;
   }
 
   public static int mul(int number1, int number2) {
      return number1 * number2;
   }
 
   // Integer divide. Return a truncated int.
   public static int divInt(int number1, int number2) {
      if (number2 == 0) {
         throw new IllegalArgumentException("Cannot divide by 0!");
      }
      return number1 / number2;
   }
 
   // Real number divide. Return a double.
   public static double divReal(int number1, int number2) {
      if (number2 == 0) {
         throw new IllegalArgumentException("Cannot divide by 0!");
      }
      return (double) number1 / number2;
   }
   
   
   // Exponent
   public static double exp(double number1, double number2) {
      if (number2 == 0) {
         throw new IllegalArgumentException("Cannot divide by 0!");
      }
      return Math.pow(number1, number2);
   }
   
   
   // Modulus
   public static int mod(int number1, int number2) {
      if (number2 == 0) {
         throw new IllegalArgumentException("Cannot divide by 0!");
      }
      return number1%number2;
   }
       
     
   // Inverse
   public static double inverse(int number1) {
      if (number1 == 0) {
         throw new IllegalArgumentException("Cannot divide by 0!");
      }
      return (double) 1 / number1;
   }
         
   
   // Negate
   public static int negate(int number1) {
      if (number1 == 0) {
         throw new IllegalArgumentException("Cannot divide by 0!");
      }
      return (-1) * number1;
   }   
   
   public static void main(String args[]) {
	   System.out.println(add(1,2));
	   System.out.println(sub(1,2));
	   System.out.println(mul(1,2));
	   System.out.println(divInt(1,2));
	   System.out.println(divReal(1,2));
	   System.out.println(exp(1,2));
	   System.out.println(mod(1,2));
	   System.out.println(inverse(2)); 
	   System.out.println(negate(2));   
   }
}