package rocketflyerCustomThreadPool;

import java.util.concurrent.Callable;

public class Main {
	public static void main(String[] args)
	{
		CustomThreadPool pool=new CustomThreadPool(6);

		Callable r=new Callable() 
		{	
			@Override
			public Object call() throws Exception 
			{	
				Thread.sleep(1000);
				System.out.println(Thread.currentThread().getName() + " is executing task.");
				return null;
			}
		};


		try {
			pool.execute(r);
			pool.execute(r);
			pool.execute(r);
			pool.execute(r);
			pool.execute(r);
			pool.execute(r);
			pool.execute(r);
			pool.execute(r);
			pool.execute(r);
			pool.execute(r);
			pool.execute(r);
			pool.execute(r);
			pool.execute(r);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}



		pool.shutdown();
	}
}
