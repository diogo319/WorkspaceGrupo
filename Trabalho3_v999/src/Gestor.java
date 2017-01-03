import java.util.concurrent.Semaphore;

public class Gestor extends Comportamento {
	
	private final static int SENSOR_DISTANCIA = Robot.S_1;
	private final static int TEMPO_SENSOR = 500;
	private final static int THRESHOLD = 2;
	private long sensorTS,agoraTS,delay;
	Estado ultimoEstado;
	private Vaguear vaguear;
	private EvitarObstaculos evitar;
	private SeguirParede seguir;
	private Distancia distancia;
	private int d;
	
	public Gestor(MyRobotLego robot, Vaguear v, EvitarObstaculos eo, SeguirParede sp, Semaphore semaforo, Semaphore semaforoRobot, Distancia distancia){
		super(robot,semaforo,semaforoRobot);
		this.robot = robot;
		this.distancia = distancia;
		vaguear = v;
		evitar = eo;
		seguir = sp;
		run = true;
		sensorTS = System.currentTimeMillis();
		agoraTS = System.currentTimeMillis();
		d = 0;
		delay = 0;
		ultimoEstado = Estado.DISTANCIA_VAGUEAR;
	}
	
	public void run() {
		do {
			//System.out.println("!!!~" + semaforoRobot.availablePermits());
			switch(estado) {
			case ESPERA:
				System.out.println("GESTOR: EM ESPERA");
				try {
					semaforo.acquire();
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				System.out.println("GESTOR: SAIU ESPERA");
				break;
			case CONFIGURACAO:
				bloquearRobot();
				robot.SetSensorLowspeed(SENSOR_DISTANCIA);
				robot.setConfigurado(true);
				libertarRobot();
				if(estado == Estado.CONFIGURACAO) estado = Estado.DISTANCIA;
				System.out.println("GESTOR: CONFIGURACAO");
				break;
			/*case ACCAO:
				agoraTS = System.currentTimeMillis();
				if (estado == Estado.ACCAO) {
					if (agoraTS - sensorTS >= TEMPO_SENSOR) {
						estado = Estado.DISTANCIA;
					} else {
						delay = TEMPO_SENSOR - (agoraTS-sensorTS);
						estado = Estado.DORMIR;
					}
				}
				System.out.println("GESTOR : ACCAO");
				break;*/
			case DISTANCIA:
				d = produzirDistancia();
				System.out.println("GESTOR : PRODUZIU DISTANCIA, d = " + d);
				sensorTS = System.currentTimeMillis();
				if(estado == Estado.DISTANCIA) {
					if(d < 100) {
						seguir.activar();
						estado = Estado.SEGUIR;
					} else {
						vaguear.activar();
						estado = Estado.VAGUEAR;
					}
				}
				System.out.println("GESTOR : DISTÂNCIA , próximo estado -> " + estado);
				break;
			case DISTANCIA_SEGUIR:
				d = produzirDistancia();
				if(d <= 100 + THRESHOLD) {
					if(estado == Estado.DISTANCIA_SEGUIR) estado = Estado.SEGUIR;
				} else {
					seguir.desactivar();
					vaguear.activar();
					if(estado == Estado.DISTANCIA_SEGUIR) estado = Estado.VAGUEAR;
				}
				System.out.println("GESTOR : SEGUIR PRODUZIU DISTANCIA , d = " + d);
				break;
			case SEGUIR:
				agoraTS = System.currentTimeMillis();
				delay = TEMPO_SENSOR - (agoraTS - sensorTS);
				System.out.println("GESTOR : SEGUIR VAI DORMIR , delay = " + delay);
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (estado == Estado.SEGUIR) estado = Estado.DISTANCIA_SEGUIR;
				System.out.println("GESTOR : SEGUIR DORMIU , delay = " + delay);
				break;
			case DISTANCIA_VAGUEAR:
				d = produzirDistancia();
				if(d > 100) {
					if(estado == Estado.DISTANCIA_VAGUEAR) estado = Estado.VAGUEAR;
				} else {
					vaguear.desactivar();
					seguir.activar();
					if(estado == Estado.DISTANCIA_VAGUEAR) estado = Estado.SEGUIR;
				}
				System.out.println("GESTOR : VAGUEAR PRODUZIU DISTANCIA , d = " + d);
				break;
			case VAGUEAR:
				agoraTS = System.currentTimeMillis();
				delay = TEMPO_SENSOR - (agoraTS - sensorTS);
				System.out.println("GESTOR : VAGUEAR VAI DORMIR , delay = " + delay);
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (estado == Estado.VAGUEAR) estado = Estado.DISTANCIA_VAGUEAR;
				System.out.println("GESTOR : VAGUEAR DORMIU , delay = " + delay);
				break;			/*case DORMIR:
				delay = TEMPO_SENSOR;
				System.out.println("GESTOR: VAI DORMIR , delay = " + delay);
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(estado == Estado.DORMIR) estado = comportamento;
				System.out.println("GESTOR : DORMIU , delay = " + delay);
				break;*/
			case DESACTIVAR:
				if(evitar.activo()) evitar.desactivar();
				if(vaguear.activo()) vaguear.desactivar();
				if(seguir.activo()) seguir.desactivar();
				if(estado == Estado.DESACTIVAR) estado = Estado.ESPERA;
				System.out.println("GESTOR: DESACTIVAR");
				break;
			case TERMINAR:	
				vaguear.terminar();
				evitar.terminar();
				seguir.terminar();
				robot.Parar(true);
				libertarRobot();
				run = false;
				System.out.println("GESTOR: TERMINAR");
			}
		} while(run);
		System.out.println("GESTOR: RUN END , run = " + run);
	}
	
	private int produzirDistancia() {
		bloquearRobot();
		int d = robot.SensorUS(Gestor.SENSOR_DISTANCIA);
		libertarRobot();
		sensorTS = System.currentTimeMillis();
		distancia.escrever(d);
		return d;
	}
		
	/*public static void main(String args[]) {
		//debug = true , modo de teste
		MyRobotLego robot = new MyRobotLego("Guia3", false);
		robot.OpenNXT();
		robot.SetSensorTouch(EvitarObstaculos.SENSOR_TOQUE);
		robot.SetSensorLowspeed(Gestor.SENSOR_DISTANCIA);
		Semaphore semaforoRobot = new Semaphore(1);
		Gestor gestor = new Gestor(robot,semaforoRobot);
		gestor.run();
	}*/
	
}
