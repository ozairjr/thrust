package br.com.softbox.thrust.api.thread;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

class ThrustThreadControl extends Thread {

	/**
	 * Tempo em minutos em que a thread será removida do pool caso não seja
	 * utilizada
	 */
	private static final int MAX_IDLE_TIMEOUT = 10;
	/**
	 * Tempo em milisegundos para avaliação e remoção das threads idle
	 */
	private static final int MAX_WAIT_MS = 30000;

	private final LocalWorkerThreadPool pool;

	public ThrustThreadControl(LocalWorkerThreadPool threadPool) {
		this.pool = threadPool;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(MAX_WAIT_MS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException(e);
			}
			if (pool.getCurrentThreads() > pool.getMinPoolSize()) {
				searchWorkForRemove();
			}
		}
	}

	private void searchWorkForRemove() {
		List<ThrustWorkerThread> workersToRemove = new LinkedList<>();
		pool.idle.iterator().forEachRemaining(worker -> searchToRemove(worker, LocalDateTime.now(), workersToRemove));
		workersToRemove.forEach(this::removeWorker);
	}

	private void searchToRemove(ThrustWorkerThread worker, LocalDateTime now,
			List<ThrustWorkerThread> workersToRemove) {
		if (worker.getLastTimeUsed().until(now, ChronoUnit.MINUTES) > MAX_IDLE_TIMEOUT) {
			workersToRemove.add(worker);
		}
	}

	private void removeWorker(ThrustWorkerThread worker) {
		if (pool.getCurrentThreads() > pool.getMinPoolSize() && pool.idle.remove(worker)) {
			pool.removeWorkerThread(worker);
		}
	}

}
