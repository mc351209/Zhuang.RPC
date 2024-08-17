package com.Hao.user;

import java.util.*;

public class Test11 {
	
	    public static void main(String[] args) {
	        Scanner reader = new Scanner(System.in);
	        int m = reader.nextInt();
	        int n = reader.nextInt() ;
	        int [][] array = new int[m][n] ;
	        for (int i=0 ; i<m ; i++)
	            for(int j=0 ;j<n ;j++)
	            {
	                array[i][j]=reader.nextInt();
	            }
	        reader.close() ;
	        /**
	         * 对矩阵按行打出
	         */
	        for (int i=0 ; i<m ; i++)
	        {
	            for(int j=0 ;j<n ;j++)
	            {
	                System.out.print(array[i][j]+" ");
	            }
	            System.out.println( );
	        }
	            
	    }
}