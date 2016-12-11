import java.util.concurrent.Semaphore;

public class Gestor  {
	
	final int TEMPO_SENSOR = 500;
	public boolean run;
	public Semaphore semaforoVaguear, semaforoEvitar, semaforoRobot, semaforoDistancia, semaforoSeguir;
	MyRobotLego robot;
	Estado estado;
	long sensorTS,agoraTS, escolhaTS, delay,td;
	int alfa;
	BufferDistancias distancias;
	
	Vaguear vaguear;
	EvitarObstaculos evitar;
	SeguirParede seguir;
	
	public Gestor(MyRobotLego robot, Semaphore semaforoRobot){
		this.robot = robot;
		this.semaforoRobot = semaforoRobot;
		semaforoVaguear = new Semaphore(0); 
		semaforoEvitar = new Semaphore(0);
		semaforoSeguir = new Semaphore(0);
		semaforoDistancia = new Semaphore(1);
		distancias = new BufferDistancias();
		estado = Estado.ACCAO;
		alfa = 0;
		run = true;
		sensorTS = System.currentTimeMillis();
		
		vaguear = new Vaguear(robot,semaforoVaguear,semaforoRobot);
		evitar = new EvitarObstaculos(robot,semaforoEvitar,semaforoRobot);
		seguir = new SeguirParede(robot,semaforoSeguir,semaforoRobot,semaforoDistancia,distancias);
		
		vaguear.start();
		evitar.start();
		seguir.start();
		
		evitar.activar();
		vaguear.activar();
		System.out.println(estado);
	}
	
	public void run() {
		do {
			switch(estado) {
			case ACCAO:
				agoraTS = System.currentTimeMillis();
				System.out.println(agoraTS - sensorTS);
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
				System.out.println("distancia = " +d);
				sensorTS = System.currentTimeMillis();
				if(d >= 100) {
					vaguear.desactivar();
					seguir.activar();
				}
				
				estado = Estado.ACCAO;
				System.out.println("GESTOR : DISTANCIA , d = " + d);
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
		} while(run);
	}
	
	private int produzirDistancia() {
		try {
			semaforoDistancia.acquire();
			semaforoRobot.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int d = robot.SensorUS(RobotLego.S_1);
		distancias.inserir(d);
		semaforoDistancia.release();
		semaforoRobot.release();
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
		MyRobotLego robot = new MyRobotLego("Guia4", RobotLego.S_2, false);
		robot.OpenNXT();
		robot.SetSensorTouch();
		robot.SetSensorLowspeed(RobotLego.S_1);
		Semaphore semaforoRobot = new Semaphore(0);
		Gestor gestor = new Gestor(robot,semaforoRobot);
		gestor.run();
	}
	
}
