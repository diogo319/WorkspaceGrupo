import java.util.concurrent.Semaphore;

public class Comportamento extends Thread {

	protected boolean run;
	protected Estado estado;
	protected MyRobotLego robot;
	protected Semaphore semaforo;
	protected Semaphore semaforoRobot;
	
	public Comportamento(MyRobotLego robot, Semaphore semaforo, Semaphore semaforoRobot) {
		this.robot = robot;
		this.semaforo = semaforo;
		this.semaforoRobot = semaforoRobot;
		run = true;
		estado = Estado.ESPERA;
	}
	
	protected void bloquearRobot() {
		try {
			semaforoRobot.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void libertarRobot() {
		semaforoRobot.release();
	}
	
	public void terminar() {
		semaforo.release();
		estado = Estado.TERMINAR;
	}
	
	public boolean terminado(){
		return !run;
	}
	
	public boolean activo() {
		return estado != Estado.ESPERA;
	}
	
	public void activar() {
		estado = Estado.CONFIGURACAO;
		semaforo.release();
	}
	
	public void desactivar() {
		estado = Estado.ESPERA;
		semaforo.drainPermits();
	}
}
