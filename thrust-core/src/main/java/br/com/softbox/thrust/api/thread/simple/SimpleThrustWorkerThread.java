package br.com.softbox.thrust.api.thread.simple;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import br.com.softbox.thrust.api.thread.LocalWorkerThreadPool;
import br.com.softbox.thrust.api.thread.ThrustWorkerThread;

class SimpleThrustWorkerThread extends ThrustWorkerThread {

	private String currentScript;

	SimpleThrustWorkerThread(LocalWorkerThreadPool pool) throws IOException, URISyntaxException {
		super(pool, null, new ArrayList<>());
	}

	public synchronized void runScript(String script) {
		this.currentScript = script;
		startCurrentThread();
	}

	@Override
	public void run() {
		while (this.active.get()) {
			if (this.currentScript != null) {
				addScript(currentScript);
			}
			pool.returnThrustWorkerThread(this);
			inactivate();
		}
	}

	private synchronized void addScript(String currentScript) {
		try {
			this.thrustContextAPI.require(currentScript);
		} catch (Exception e) {
			this.active.set(false);
			throw new RuntimeException(e);
		}
	}
}
