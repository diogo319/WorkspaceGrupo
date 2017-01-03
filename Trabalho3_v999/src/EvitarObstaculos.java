import java.util.Random;
import java.util.concurrent.Semaphore;

public class EvitarObstaculos extends Comportamento {
	
	final static int SENSOR_TOQUE = RobotLego.S_2;
	final static int TEMPO_SENSOR = 500;
	//Física
	private float velRobot;
	//Timestamps
	private long sensorTS,agoraTS,td,delay;
	private int sensorActivado;
	private int distancia,angulo,raio;
	private boolean debug;
	
	public EvitarObstaculos(MyRobotLego robot, Semaphore semaphore, Semaphore semaforoRobot) {
		super(robot,semaphore,semaforoRobot);
		//Parametros movimento
		inicializarVariaveis();
	}
	
	public void inicializarVariaveis() {
		sensorActivado = 0;
		sensorTS = System.currentTimeMillis();
		velRobot = 0.02f;
		distancia = -15;
		td = (long)(-distancia/velRobot + 750) ;
		angulo = 90;
		raio = 0;
		debug = false;
	}
	
	public void run() {
		do {
			switch(estado) {
			case ESPERA:
				if(debug) if(debug) System.out.println("EVITAR: EM ESPERA");
				try {
					semaforo.acquire();
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				if(debug) System.out.println("EVITAR: SAIU ESPERA");
				break;
			case CONFIGURACAO:
				if(!robot.configurado()) {
					bloquearRobot();
					robot.SetSensorTouch(SENSOR_TOQUE);
					robot.setConfigurado(true);
					libertarRobot();
				}
				if(debug) System.out.println("EVITAR: CONFIGURACAO");
				if(estado == Estado.CONFIGURACAO) estado = Estado.ACCAO;
				break;
			case ACCAO:
				agoraTS = System.currentTimeMillis();
				if(agoraTS - sensorTS >= TEMPO_SENSOR) {
					bloquearRobot();
					if(estado == Estado.ACCAO) estado = Estado.EVITAR;
					else libertarRobot();
				} else {
					 delay = TEMPO_SENSOR - (agoraTS - sensorTS);
					 if(estado == Estado.ACCAO) estado = Estado.DORMIR;
				 }
				if(debug) System.out.println("EVITAR: ACCAO -> " + estado);
				break;
			case EVITAR:
				sensor();
				if(sensorActivado == 1) {
					obstaculo();
					if(debug) System.out.println("EVITAR: SENSOR = " + sensorActivado);
					try {
						Thread.sleep(td);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(debug) System.out.println("EVITAR: DORMIU = " + td);
				} else {
					libertarRobot(); 
					if(estado == Estado.EVITAR) estado = Estado.ACCAO;
				}
				break;
			case DORMIR:
				try {
					Thread.sleep(delay);	
		        } catch (Exception e) {
		            if(debug) System.out.println(e);
		        }
				if(estado == Estado.DORMIR) estado = Estado.ACCAO;
				if(debug) System.out.println("EVITAR: DORMIU , delay = " + delay);
				break;
			case TERMINAR:
				run = false;
				if(debug) System.out.println("EVITAR: TERMINAR");
				break;
			}
		} while(run);
		if(debug) System.out.println("EVITAR: RUN END , run = " + run);
	}

	private void sensor() {
		sensorActivado = robot.Sensor(SENSOR_TOQUE);
		sensorTS = System.currentTimeMillis();
	}

	private void obstaculo() {
		robot.Parar(true);
		robot.Reta(distancia);
		robot.CurvarEsquerda(raio,angulo);
		robot.Parar(false);
	}
	
	/*private void configuracao() {
		velRobot = 0.02f;
		//Tempo da curva + tempo da recta
		td = (long)(750 +  -distancia / velRobot);
		robot.SetSensorTouch(SENSOR_TOQUE);
		//robot.SetSpeed((int) velRobot * 100);
		if(debug) System.out.println("CONFIGURACAO");
	}*/
	
	/*public static void main(String[] args) {
		//debug = true , modo de teste
		MyRobotLego robot = new MyRobotLego("Guia3", false);
		robot.OpenNXT();
		robot.SetSensorTouch(EvitarObstaculos.SENSOR_TOQUE);
		Semaphore semaforo = new Semaphore(1);
		Semaphore semaforoRobot = new Semaphore(1);
		EvitarObstaculos evitar = new EvitarObstaculos(robot, semaforo, semaforoRobot);
		evitar.activar();
		evitar.run();
		System.exit(1);
	}*/

}
