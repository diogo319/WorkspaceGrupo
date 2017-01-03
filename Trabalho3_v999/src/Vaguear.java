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
	int distancia,angulo,raio;
	boolean verificarEscolha,verificarSensor;
	private boolean debug;
	
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
		debug = false;
	}
	
	public void run() {
		do {
			switch(estado) {
			case ESPERA:
				if(debug) System.out.println("VAGUEAR: EM ESPERA ");
				try {
					semaforo.acquire();
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				if(debug) System.out.println("VAGUEAR: SAIU ESPERA");
				break;
			case CONFIGURACAO:
				if(estado == Estado.CONFIGURACAO) estado = Estado.ACCAO;
				break;
			case ACCAO:
				if(estado == Estado.ACCAO) {
					agoraTS = System.currentTimeMillis();
					if (agoraTS - escolhaTS >= td-50) estado = Estado.ESCOLHA;
					else {
						delay = (td-50)-(agoraTS-escolhaTS);
						estado = Estado.DORMIR;
					}
				}
				if(debug) System.out.println("VAGUEAR: ACCAO");
				break;
			case ESCOLHA:
				if(estado == Estado.ESCOLHA) estado = Estado.values()[rnd.nextInt(4)];
				escolhaTS = System.currentTimeMillis();
				if(debug) System.out.println("VAGUEAR: ESCOLHA = " + estado.toString());
				break;
			case DORMIR:
				if(debug) System.out.println("VAGUEAR: VAI DORMIR , delay = " + delay);
				try {
					Thread.sleep(delay);
		        } catch (Exception e) {
		            if(debug) System.out.println(e);
		        }
				if(estado == Estado.DORMIR) estado = Estado.ACCAO;
				if(debug) System.out.println("VAGUEAR: DORMIU , delay = " + delay);
				break;
			case RETA:
				bloquearRobot();
				recta();
				libertarRobot();
				if(estado == Estado.RETA) estado = Estado.ACCAO;
				if(debug) System.out.println("VAGUEAR: RETA , td = " + td);
				break;
			case ESQUERDA:
				bloquearRobot();
				curvarEsquerda();
				libertarRobot();
				if(estado == Estado.ESQUERDA) estado = Estado.ACCAO;
				if(debug) System.out.println("VAGUEAR: ESQUERDA , td = " + td);
				break;
			case DIREITA:
				bloquearRobot();
				curvarDireita();
				libertarRobot();
				if(estado == Estado.DIREITA) estado = Estado.ACCAO;
				if(debug) System.out.println("VAGUEAR: DIREITA , td = " + td);
				break;
			case PARAR:
				bloquearRobot();
				parar();
				libertarRobot();
				if(estado == Estado.PARAR) estado = Estado.ACCAO;
				if(debug) System.out.println("VAGUEAR: PARAR , td = " + td);
				break;
			case TERMINAR:
				bloquearRobot();
				robot.Parar(true);
				libertarRobot();
				run = false;
				if(debug) System.out.println("VAGUEAR: TERMINAR");
				break;
			case DESACTIVAR:
				bloquearRobot();
				robot.Parar(true);
				robot.setConfigurado(false);
				libertarRobot();
				if(estado == Estado.DESACTIVAR) estado = Estado.ESPERA;
				if(debug) System.out.println("VAGUEAR: DESACTIVAR");
				break;
			}	
		} while(run);
		if(debug) System.out.println("VAGUEAR: RUN END , run = " + run);
	}

	public long getTD() {
		return this.td;
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
		robot.Parar(true);
		td = 50;
	}
	
	/*public static void main(String[] args) {
		//debug = true , modo de teste
		MyRobotLego robot = new MyRobotLego("Guia3", false);
		Semaphore semaforo = new Semaphore(1);
		Semaphore semaforoRobot = new Semaphore(1);
		Vaguear vaguear = new Vaguear(robot, semaforo, semaforoRobot);
		vaguear.run();
		System.exit(1);
	}*/
}
