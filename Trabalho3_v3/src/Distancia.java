import java.util.concurrent.Semaphore;

public class Distancia {
	int distancia;
	Semaphore semaforo;
	
	public Distancia() {
		distancia = 0;
		semaforo = new Semaphore(0);
	}
	
	public void escrever(int d) {
		distancia = d;
		semaforo.release();
	}
	
	public int ler() {
		try {
			semaforo.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return distancia;
	}
}
