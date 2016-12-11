import java.util.Random;
import java.util.concurrent.Semaphore;

public class EvitarObstaculos extends Comportamento {
	
	final int TEMPO_SENSOR = 500;
	boolean run;
	Random rnd = new Random();
	//Física
	float velRobot;
	//Timestamps
	long sensorTS,agoraTS,tempoSensor,td,delay,start;
	//Escolha
	Estado estado;
	int sensorActivado;
	boolean verificarEscolha,verificarSensor;
	int distancia,angulo,raio;
	
	public EvitarObstaculos(MyRobotLego robot, Semaphore semaphore, Semaphore semaforoRobot) {
		super(robot,semaphore,semaforoRobot);
		//Parametros movimento
		distancia = -15;
		angulo = 90;
		raio = 0;
		inicializarVariaveis();
		// TODO Auto-generated constructor stub
	}
	
	public void inicializarVariaveis() {
		sensorActivado = 0;
		sensorTS = System.currentTimeMillis();
		velRobot = 0.02f;
		td = (long)(-distancia/velRobot + 750) ;
		verificarEscolha = false;
		verificarSensor = false;
		run = true;
		estado = Estado.LIGAR;
		start = System.currentTimeMillis();
	}
	
	public void run() {
		do {
			//
			/*if(System.currentTimeMillis()-start > 5000 && robot.debug()) {
				estado = Estado.TERMINAR;
			}*/
			switch(estado) {
			case LIGAR:
				try {
					semaforo.acquire();
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				System.out.println("EVITAR: LIGAR");
				estado = Estado.SENSOR;
				break;
			case ACCAO:
				agoraTS = System.currentTimeMillis();
				if(agoraTS - sensorTS >= TEMPO_SENSOR) {
					bloquearRobot();
					estado = Estado.SENSOR;
				} else {
					if(sensorActivado == 1) delay = td;
					else delay = TEMPO_SENSOR - (agoraTS-sensorTS);
					estado = Estado.DORMIR;
				}
				System.out.println("EVITAR: ACCAO");
				break;
			case SENSOR:
				sensor();
				if(sensorActivado == 1) estado = Estado.OBSTACULO;
				else {
					libertarRobot();
					estado = Estado.ACCAO;
				}
				System.out.println("EVITAR: SENSOR ("+sensorActivado+")");
				break;
			case DORMIR:
				System.out.println("EVITAR: DORMINDO , delay = " + delay);
				libertarRobot();
				try {
					Thread.sleep(delay);	
		        } catch (Exception e) {
		            System.out.println(e);
		        }
				estado = Estado.ACCAO;
				break;
			case OBSTACULO:
				obstaculo();
				System.out.println("EVITAR: OBSTACULO");
				estado = Estado.ACCAO;
				break;
			case TERMINAR:
				run = false;
				System.out.println("EVITAR: TERMINAR");
				break;
			}
		} while(run);
	}

	private void sensor() {
		sensorActivado = robot.Sensor();
		sensorTS = System.currentTimeMillis();
	}

	private void obstaculo() {
		robot.Parar(true);
		robot.Reta(distancia);
		robot.CurvarEsquerda(angulo, raio);
		robot.Parar(false);
	}

	private void configuracao() {
		tempoSensor = 500;
		velRobot = 0.02f;
		//Tempo da curva + tempo da recta
		td = (long)(750 +  -distancia / velRobot);
		robot.SetSensorTouch();
		//robot.SetSpeed((int) velRobot * 100);
		System.out.println("CONFIGURACAO");
	}
	
	public static void main(String[] args) {
		//debug = true , modo de teste
		MyRobotLego robot = new MyRobotLego("Guia4", RobotLego.S_2, false);
		Semaphore semaforo = new Semaphore(1);
		Semaphore semaforoRobot = new Semaphore(1);
		EvitarObstaculos evitar = new EvitarObstaculos(robot, semaforo, semaforoRobot);
		System.out.println(robot == null);
		evitar.run();
		System.exit(1);
	}

}
