package com.cyborg.learning.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

class Main
{
	public static void main(String[] args) throws InterruptedException, ExecutionException
	{
		
		//Customizable Parameters
		final int TASKS=8;
		final int TOTAL_ELEMENTS=10000000;
		final int ELEMENTS_PER_THREAD=(int)(TOTAL_ELEMENTS/TASKS);
		final int RANDOM_MAX=100000000;
		
		final int MAX_THREADS=2;
		
		
		List<Integer> mr=new ArrayList<Integer>(TOTAL_ELEMENTS);		
		
		//Populating ArrayList - 10 Lakh Data
		System.out.println("Uploading Elements to Array Starts");
		
		for(int i=0;i<TOTAL_ELEMENTS;i++)
		{
			mr.add((int)(Math.random()*RANDOM_MAX));
		}	
		
		System.out.println("Uploading Elements to Array Stops");
			
		long startTime=System.nanoTime();
		
		CountDownLatch latch=new CountDownLatch(TASKS);
		FutureTask[] future=new FutureTask[TASKS];
		
		ExecutorService pool=Executors.newFixedThreadPool(MAX_THREADS);
		//Initializing and starting thread - 10 Thread
		for(int i=0;i<TASKS;i++)
		{
			future[i]=new FutureTask<>(new ThreadParse(i*ELEMENTS_PER_THREAD,(i+1)*ELEMENTS_PER_THREAD,mr,latch,i));
			pool.execute(future[i]);
		}
		
		//System.out.println("Thread has entered waiting state");	
		
		latch.await();
		
		Variable[] var=new Variable[TASKS];	
		
		//Initializing and starting thread - 10 Thread
		for(int i=0;i<TASKS;i++)
		{
			var[i]=(Variable)future[i].get();
		}
		
		//Finding minimum of all
		int min_1=9999999,min_2=9999999;
		for(int i=0;i<TASKS;i++)
		{
			if(min_1>var[i].getMin1())
			{
				min_2=min_1;
				min_1=var[i].getMin1();
				if(min_2>var[i].getMin2())
				{
					min_2=var[i].getMin2();
				}
			}
			else if(min_2>var[i].getMin1()&&min_1!=var[i].getMin1())
			{
				min_2=var[i].getMin1();
			}
			else if(min_2>var[i].getMin2()&&min_1!=var[i].getMin2())
			{
				min_2=var[i].getMin2();
			}
		}
		
		pool.shutdown();
		System.out.println("Second smallest Number in Array : "+min_2);
		long endTime=System.nanoTime();
		System.out.println(((endTime-startTime)/(1000000000.0))+" seconds taken to find second smallest minimum");
		
	}
}