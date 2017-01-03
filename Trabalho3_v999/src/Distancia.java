import java.util.concurrent.Semaphore;

public class Distancia {
	private int distancia;
	private Semaphore semaforo;
	private Semaphore semaforoConsumo;
	
	public Distancia() {
		distancia = -1;
		semaforo = new Semaphore(1);
		semaforoConsumo = new Semaphore(0);
	}
	
	public void escrever(int d) {
		try {
			semaforo.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		distancia = d;
		semaforo.release();
		if(semaforoConsumo.availablePermits() == 0) semaforoConsumo.release();
	}
	
	public int ler() {
		int d = 0;
		try {
			semaforo.acquire();
			d = distancia;
			semaforo.release();
			semaforoConsumo.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return d;
	}
	
	
	
}
