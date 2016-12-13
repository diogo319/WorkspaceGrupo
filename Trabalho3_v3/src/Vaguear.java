import java.util.Random;
import java.util.concurrent.Semaphore;

public class Vaguear extends Comportamento {

	Random rnd;
	//Física
	float velRobot;
	//Timestamps
	long escolhaTS, sensorTS,agoraTS,tempoSensor,td,delay,start;
	//Parametros escolha
	int dmin,dmax,amax,rmin,rmax;
	int sensorActivado;
	boolean verificarEscolha,verificarSensor;
	int distancia,angulo,raio;
	
	public Vaguear(MyRobotLego robot, Semaphore semaforo, Semaphore semaforoRobot) {
		super(robot, semaforo,semaforoRobot);		
		//Parametros movimento
		dmin = 10;
		dmax = 75;
		amax = 90;
		rmin = 10;
		rmax = 50;
		inicializarVariaveis();
	}
	
	void inicializarVariaveis() {
		escolhaTS = System.currentTimeMillis();
		sensorTS = System.currentTimeMillis();
		td = 0;
		velRobot = 0.02f;
		verificarEscolha = false;
		verificarSensor = false;
		distancia = 0;
		angulo = 0;
		raio = 0;
		rnd = new Random();
		start = System.currentTimeMillis();
	}
	
	public void run() {
		do {
			/*if(System.currentTimeMillis()-start > 5000 && robot.debug()) {
				estado = Estado.COMUNICACAO;
			}*/
			if (!activo) estado = Estado.LIGAR;
			switch(estado) {
			case LIGAR:	
				try {
					semaforo.acquire();
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				System.out.println("VAGUEAR: LIGAR");
			case ACCAO:
				agoraTS = System.currentTimeMillis();
				if (agoraTS - escolhaTS >= td-50) {
					bloquearRobot();
					estado = Estado.ESCOLHA;
				} else {
					libertarRobot();
					delay = (td-50)-(agoraTS-escolhaTS);
					estado = Estado.DORMIR;
				}
				System.out.println("VAGUEAR: ACCAO");
				/*agoraTS = System.currentTimeMillis();
				if(agoraTS - sensorTS >= tempoSensor) {
					estado = Estado.SENSOR;
				} else if (agoraTS - escolhaTS >= td-50) {
					estado = Estado.ESCOLHA;
				} else {
					delay = Math.min((long)(td-50)-(agoraTS-escolhaTS),tempoSensor-(agoraTS-sensorTS));
					estado = Estado.DORMIR;
				}
				System.out.println("ACCAO");*/
				break;
			case ESCOLHA:
				estado = Estado.values()[rnd.nextInt(4)];
				escolhaTS = System.currentTimeMillis();
				System.out.println("VAGUEAR: ESCOLHA = " + estado.toString());
				break;
			case DORMIR:
				System.out.println("VAGUEAR: DORMINDO , delay = " + delay);
				try {
					Thread.sleep(delay);
		        } catch (Exception e) {
		            System.out.println(e);
		        }
				estado = Estado.ACCAO;
				break;
			case RETA:
				recta();
				libertarRobot();
				estado = Estado.ACCAO;
				System.out.println("VAGUEAR: RETA , td = " + td);
				break;
			case ESQUERDA:
				curvarEsquerda();
				libertarRobot();
				estado = Estado.ACCAO;
				System.out.println("VAGUEAR: ESQUERDA , td = " + td);
				break;
			case DIREITA:
				curvarDireita();
				libertarRobot();
				estado = Estado.ACCAO;
				System.out.println("VAGUEAR: DIREITA , td = " + td);
				break;
			case PARAR:
				parar();
				libertarRobot();
				estado = Estado.ACCAO;
				System.out.println("VAGUEAR: PARAR , td = " + td);
				break;
			case TERMINAR:
				terminar();
				System.out.println("VAGUEAR: RUN END , run = " + run);
				break;
			}	
		} while(run);
	}

	public long getTD() {
		return this.td;
	}

	private void obstaculo() {
		robot.Parar(true);
		robot.CloseNXT();
	}
	
	private void terminar() {
		run = false;
	}

	private void recta() {
		distancia = dmin + rnd.nextInt(dmax-dmin);
		robot.Reta(distancia);
		td = (long)(distancia / velRobot);
	}
	
	private void curvarDireita() {
		angulo = rnd.nextInt(amax);
		raio = rmin + rnd.nextInt(rmax-rmin);
		robot.CurvarDireita(raio,angulo);
		td = (long)((angulo*Math.PI/180*raio) / velRobot);
	}

	private void curvarEsquerda() {
		angulo = rnd.nextInt(amax);
		raio = rmin + rnd.nextInt(rmax-rmin);
		robot.CurvarEsquerda(raio,angulo);
		td = (long)((angulo*Math.PI/180*raio) / velRobot);
	}
	
	private void parar() {
		System.out.println("PARAR");
		robot.Parar(true);
		td = 50;
	}
	
	public static void main(String[] args) {
		//debug = true , modo de teste
		MyRobotLego robot = new MyRobotLego("Guia4", false);
		Semaphore semaforo = new Semaphore(1);
		Semaphore semaforoRobot = new Semaphore(1);
		Vaguear vaguear = new Vaguear(robot, semaforo, semaforoRobot);
		vaguear.run();
		System.exit(1);
	}
}
