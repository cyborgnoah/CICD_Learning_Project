package com.cyborg.learning.Main;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class ThreadParse implements Callable<Variable>{

	List<Integer> mr;	
	int start,end;
	Variable ob;
	CountDownLatch latch;
	int threadNum;
	ThreadParse(int start, int end, List<Integer> mr,CountDownLatch latch,int threadNum)
	{
		this.mr=mr;
		this.start=start;
		this.end=end;
		this.latch=latch;
		this.threadNum=threadNum;
		ob=new Variable();
		ob.setMin1(9999999);
		ob.setMin2(9999999);
	}
	
	public Variable getOb() {
		return ob;
	}

	public void setOb(Variable ob) {
		this.ob = ob;
	}

	@Override
	public Variable call() throws Exception {
		System.out.println("Task "+threadNum+" Started");
		for(int i=start;i<end;i++)
		{
			if(ob.getMin1()>mr.get(i))
			{					
				ob.setMin2(ob.getMin1());
				ob.setMin1(mr.get(i));
			}
			else if(ob.getMin2()>mr.get(i)&&ob.getMin1()!=mr.get(i))
			{
				ob.setMin2(mr.get(i));
			}	
		}
			
		System.out.println("Task "+threadNum+" : Min1 : "+ob.getMin1()+" , Min2 :"+ob.getMin2());
		synchronized (latch) {
			latch.countDown();	
		}
		
		System.out.println("Task "+threadNum+" Stopped");
		return ob;
	}

}
