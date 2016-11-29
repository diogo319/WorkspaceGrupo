import java.util.concurrent.Semaphore;

public class Comportamento extends Thread {

	MyRobotLego robot;
	Semaphore semaforo;
	Semaphore semaforoRobot;
	boolean terminar;
	
	public Comportamento(MyRobotLego robot, Semaphore semaforo, Semaphore semaforoRobot) {
		this.robot = robot;
		this.semaforo = semaforo;
		this.semaforoRobot = semaforoRobot;
	}
	
	public void bloquearRobot() {
		try {
			semaforoRobot.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void libertarRobot() {
		semaforoRobot.release();
	}
	
	public void activar() {
		semaforo.release();
	}
	
	public void desactivar() {
		try {
			//nº de unidades no semaforo 0
			semaforo.drainPermits();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
