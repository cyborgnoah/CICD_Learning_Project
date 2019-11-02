package com.cyborg.learning.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;


public class CustomThreadPool 
{	
	private BlockingQueue<Runnable> blockingQueue;
	private List<PoolThread> poolThread;
	private AtomicBoolean shutDownFlag;
	
	CustomThreadPool(final int noOfThreads)
	{		
		this.blockingQueue=new LinkedBlockingQueue<>();
		this.poolThread=new ArrayList<>(noOfThreads);
		this.shutDownFlag = new AtomicBoolean(false);
		for(int i=0;i<noOfThreads;i++)
		{
			PoolThread thread=new PoolThread(blockingQueue,this);
			thread.setName("Thread No "+i+" has started.");
			thread.start();
			poolThread.add(thread);
		}
	}
	
	public void execute(Runnable r) throws InterruptedException 
	{
		if(!shutDownFlag.get())
		{
			blockingQueue.put(r);
		}
		else
		{
			throw new RuntimeException("Pool has been terminated");
		}
	}
	
	public void shutdown() 
	{
		shutDownFlag.set(true);
	}
	
	public class PoolThread extends Thread
	{

		private BlockingQueue<Runnable> taskQueue;
		private CustomThreadPool customThreadPool;
		
		public PoolThread(BlockingQueue<Runnable> taskQueue, CustomThreadPool customThreadPool) 
		{
			this.taskQueue=taskQueue;
			this.customThreadPool=customThreadPool;
		}
		
		@Override
		public void run()
		{
			try
			{
				while(!customThreadPool.shutDownFlag.get() || !taskQueue.isEmpty())
				{
					Runnable c;
					while((c=taskQueue.poll())!=null)
					{
						c.run();
					}					
				}
			}
			catch (Exception e) 
			{
				throw new CustomThreadPoolException(e);
			}
		}
	}
	
	private class CustomThreadPoolException extends RuntimeException 
	{
		private static final long serialVersionUID = 1L;
		public CustomThreadPoolException(Throwable t) {
			super(t);
		}
	}
}