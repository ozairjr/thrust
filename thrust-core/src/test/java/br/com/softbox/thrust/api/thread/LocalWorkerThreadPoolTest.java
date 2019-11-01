package br.com.softbox.thrust.api.thread;

import org.junit.Assert;
import org.junit.Test;

import br.com.softbox.thrust.test.thread.LocalPoolNullBuilder;

public class LocalWorkerThreadPoolTest {

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidMinimum() throws Exception {
		try {
			new LocalPoolNullBuilder(-1, 2);
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e.getMessage().contains("Invalid minimum pool size"));
			throw e;
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidMinimumGreaterMaximum() throws Exception {
		try {
			new LocalPoolNullBuilder(3, 2);
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e.getMessage().contains("Minimum is greater maximum: "));
			throw e;
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidMaximum() throws Exception {
		try {
			new LocalPoolNullBuilder(1, -2);
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e.getMessage().contains("Invalid maximum pool size: "));
			throw e;
		}
	}

	@Test
	public void noBuilder() {
		try {
			new LocalPoolNullBuilder(1, 1);
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains("Builder didn't create a new thrust worker builder"));
		}
	}
	
	@Test
	public void getWorker() throws Exception {
		
		LocalPool2ndNullBuilder pool = new LocalPool2ndNullBuilder();
		
		Assert.assertEquals(1, pool.getCurrentThreads());
		
		ThrustWorkerThread wt = pool.getThrustWorkerThread();
		Assert.assertNotNull(wt);
		wt.start();
		
		ThrustWorkerThread wt2 = pool.getThrustWorkerThread();
		Assert.assertNull(wt2);
		
		wt.join();
		Assert.assertFalse(wt.isAlive());
		
		wt = pool.getThrustWorkerThread();
		Assert.assertNotNull(wt);
		
		wt2 = pool.getThrustWorkerThread();
		Assert.assertNull(wt2);
	}
	
	@Test
	public void getWorkerRemoveIt() throws Exception {
		
		LocalPool2ndNullBuilder pool = new LocalPool2ndNullBuilder();
		
		Assert.assertEquals(1, pool.getCurrentThreads());
		
		ThrustWorkerThread wt = pool.getThrustWorkerThread();
		Assert.assertNotNull(wt);
		wt.start();
		
		pool.removeWorkerThread(null);
		pool.removeWorkerThread(wt);
		
	}

}
