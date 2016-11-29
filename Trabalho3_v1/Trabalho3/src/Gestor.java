import java.util.concurrent.Semaphore;

public class Gestor  {
	public Semaphore semaforoVaguear, semaforoEvitar, semaforoRobot;
	Estado estado;
	long sensorTS, agoraTS, escolhaTS, delay,td;
	Vaguear vaguear;
	EvitarObstaculos evitar;
	
	public Gestor(MyRobotLego robot, Semaphore semaforoRobot){
		this.semaforoRobot = semaforoRobot;
		semaforoVaguear = new Semaphore(0); 
		semaforoEvitar = new Semaphore(0);
		estado = Estado.SENSOR;
		vaguear = new Vaguear(robot,semaforoVaguear,semaforoRobot);
		evitar = new EvitarObstaculos(robot,semaforoEvitar,semaforoRobot);
		vaguear.start();
		evitar.start();
		
		evitar.activar();
		vaguear.activar();
	}
	
	public void run() {
		switch(estado) {
		case ACCAO:

			break;
		case SENSOR:
			evitar.activar();
			try {
				semaforoRobot.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally { semaforoRobot.release(); }
			
			evitar.desactivar();
			
			vaguear.activar();
			estado = Estado.ACCAO;
			break;
		case DORMIR:
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			estado = Estado.ACCAO;
			break;
		}
	}
	
	public static void main(String args[]) {
		//debug = true , modo de teste
		MyRobotLego robot = new MyRobotLego("FSO2", RobotLego.S_2, false);
		robot.OpenNXT();
		robot.SetSensorTouch();
		System.out.println(robot.Sensor());
		Semaphore semaforoRobot = new Semaphore(0);
		Gestor gestor = new Gestor(robot,semaforoRobot);
	}
	
}
