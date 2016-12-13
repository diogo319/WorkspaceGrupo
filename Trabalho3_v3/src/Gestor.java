import java.util.concurrent.Semaphore;

public class Gestor  {
	
	final static int SENSOR_DISTANCIA = Robot.S_1;
	final static int TEMPO_SENSOR = 500;
	public boolean run;
	public Semaphore semaforoVaguear, semaforoEvitar, semaforoRobot, semaforoDistancia, semaforoSeguir;
	MyRobotLego robot;
	Estado estado;
	long sensorTS,agoraTS, escolhaTS, delay,td;
	Distancia distancia;
	
	Vaguear vaguear;
	EvitarObstaculos evitar;
	SeguirParede seguir;
	
	public Gestor(MyRobotLego robot, Semaphore semaforoRobot){
		this.robot = robot;
		this.semaforoRobot = semaforoRobot;
		semaforoVaguear = new Semaphore(0); 
		semaforoEvitar = new Semaphore(0);
		semaforoSeguir = new Semaphore(0);
		semaforoDistancia = new Semaphore(0);
		distancia = new Distancia();
		distancia.escrever(150);
		estado = Estado.ACCAO;
		run = true;
		sensorTS = System.currentTimeMillis();
		
		vaguear = new Vaguear(robot,semaforoVaguear,semaforoRobot);
		evitar = new EvitarObstaculos(robot,semaforoEvitar,semaforoRobot);
		seguir = new SeguirParede(robot,semaforoSeguir,semaforoRobot,distancia);
		
		vaguear.start();
		evitar.start();
		seguir.start();
		
		//evitar.activar();
		//vaguear.activar();
	}
	
	public void run() {
		do {
			switch(estado) {
			case ACCAO:
				agoraTS = System.currentTimeMillis();
				if (agoraTS - sensorTS >= TEMPO_SENSOR) {
					estado = Estado.DISTANCIA;
				} else {
					delay = TEMPO_SENSOR - (agoraTS-sensorTS);
					estado = Estado.DORMIR;
				}
				System.out.println("GESTOR : ACCAO");
				break;
			case DISTANCIA:
				int d = produzirDistancia();
				sensorTS = System.currentTimeMillis();
				if(d <= 100) {
					//vaguear.desactivar();
					seguir.activar();
				} else {
					seguir.desactivar();
					//vaguear.activar();
				}
				System.out.println("GESTOR : DISTANCIA , d = " + d);
				estado = Estado.ACCAO;
				break;
			/*case ESPERA:
				bloquearRobot();
				robot.Parar(true);
				robot.Reta(1);
				
				break;*/
			case DORMIR:
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("GESTOR : DORMIR , delay = " + delay);
				estado = Estado.ACCAO;
				break;
			}
			System.out.println("Proximo estado (GESTOR) = " + estado);
		} while(run);
	}
	
	private int produzirDistancia() {
		try {
			semaforoRobot.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int d = robot.SensorUS(Gestor.SENSOR_DISTANCIA);
		
		distancia.escrever(d);
		semaforoRobot.release();
		semaforoDistancia.release();
		return d;
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
	
	public static void main(String args[]) {
		//debug = true , modo de teste
		MyRobotLego robot = new MyRobotLego("FSO2", false);
		robot.OpenNXT();
		robot.SetSensorTouch(EvitarObstaculos.SENSOR_TOQUE);
		robot.SetSensorLowspeed(Gestor.SENSOR_DISTANCIA);
		Semaphore semaforoRobot = new Semaphore(1);
		Gestor gestor = new Gestor(robot,semaforoRobot);
		gestor.run();
	}
	
}
