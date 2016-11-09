import java.util.Random;

public class VaguearDebug {

	public static final int porto = RobotLego.S_2;
	Comunicacao pc;
	MyRobotLego robot;
	boolean run;
	Random rnd;
	//Física
	float velRobot;
	//Timestamps
	long escolhaTS, sensorTS,agoraTS,tempoSensor,td,delay;
	//Parametros escolha
	int dmin,dmax,amax,rmin,rmax;
	//Escolha
	Estado estado;
	int sensorActivado;
	boolean verificarEscolha,verificarSensor;
	int distancia,angulo,raio;

	long start;
		
	public VaguearDebug(MyRobotLego robot, int dmin, int dmax, int amax, int rmin, int rmax) {
		this.robot = robot;
		this.dmin = dmin;
		this.dmax = dmax;
		this.amax = amax;
		this.rmin = rmin;
		this.rmax = rmax;
		inicializarVariaveis();
		start = System.currentTimeMillis();
	}
	
	public VaguearDebug() {
		robot = new MyRobotLego("Guia4",RobotLego.S_2,false);
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
		verificarEscolha = false;
		verificarSensor = false;
		run = false;
		distancia = 0;
		angulo = 0;
		raio = 0;
		rnd = new Random();
		pc = new Comunicacao();
		estado = Estado.LIGAR;
	}
	
	public void run() {
		do {
			if(System.currentTimeMillis()-start > 5000 && robot.debug()) estado = Estado.TERMINAR;
			switch(estado) {
			case LIGAR:
				run = robot.OpenNXT();
				estado = Estado.CONFIGURACAO;
				System.out.println("LIGAR");
				break;
			case CONFIGURACAO:
				configuracao();
				System.out.println("CONFIGURACAO");
			case ACCAO:
				agoraTS = System.currentTimeMillis();
				if(agoraTS - sensorTS >= tempoSensor) {
					estado = Estado.SENSOR;
				} else if (agoraTS - escolhaTS >= td-50) {
					estado = Estado.ESCOLHA;
				} else {
					delay = Math.min((long)(td-50)-(agoraTS-escolhaTS),tempoSensor-(agoraTS-sensorTS));
					estado = Estado.DORMIR;
				}
				System.out.println("ACCAO");
				break;
			case ESCOLHA:
				estado = Estado.values()[rnd.nextInt(4)];
				escolhaTS = System.currentTimeMillis();
				System.out.println("ESCOLHA = " + estado.toString());
				break;
			case RETA:
				recta();
				estado = Estado.ACCAO;
				System.out.println("RETA , td = " + td);
				break;
			case ESQUERDA:
				curvarEsquerda();
				estado = Estado.ACCAO;
				System.out.println("ESQUERDA , td = " + td);
				break;
			case DIREITA:
				curvarDireita();
				estado = Estado.ACCAO;
				System.out.println("DIREITA , td = " + td);
				break;
			case PARAR:
				parar();
				estado = Estado.ACCAO;
				System.out.println("DIREITA , td = " + td);
				break;
			case SENSOR:
				sensorActivado = robot.Sensor();
				sensorTS = System.currentTimeMillis();
				if(sensorActivado == 1) {
					estado = Estado.OBSTACULO;
				} else estado = Estado.ACCAO;
				System.out.println("SENSOR");
				break;
			case DORMIR:
				System.out.println("DORMINDO , delay = " + delay);
				try {
					Thread.sleep(delay);
		        } catch (Exception e) {
		            System.out.println(e);
		        }
				estado = Estado.ACCAO;
				break;
			case OBSTACULO:
				robot.Parar(true);
				System.out.println("OBSTACULO");
			case TERMINAR:
				robot.CloseNXT();
				run = false;
				pc.enviarMensagem(Mensagem.VAGUEAR_TERMINADO);
				pc.fecharCanal();
				System.out.println("RUN END , run = " + run);
				break;
			}
		} while(run);
		System.out.println(pc.receberMensagem());
	}

	public void recta() {
		distancia = dmin + rnd.nextInt(dmax-dmin);
		robot.Reta(distancia);
		td = (long)(distancia / velRobot);
	}
	
	public void curvarDireita() {
		angulo = rnd.nextInt(amax);
		raio = rmin + rnd.nextInt(rmax-rmin);
		robot.CurvarDireita(raio,angulo);
		td = (long)((angulo*Math.PI/180*raio) / velRobot);
	}

	public void curvarEsquerda() {
		angulo = rnd.nextInt(amax);
		raio = rmin + rnd.nextInt(rmax-rmin);
		robot.CurvarEsquerda(raio,angulo);
		td = (long)((angulo*Math.PI/180*raio) / velRobot);
	}
	
	void parar() {
		System.out.println("PARAR");
		robot.Parar(true);
		td = 50;
	}
	
	void configuracao() {
		//Robot
		tempoSensor = 500;
		velRobot = 0.02f;
		robot.SetSensorTouch();
		//robot.SetSpeed((int) velRobot * 100);
	}
	
	public static void main(String[] args) {
		//debug = true , modo de teste
		MyRobotLego robot = new MyRobotLego("Guia4", RobotLego.S_2, true);
		VaguearDebug vaguear = new VaguearDebug(robot,10,75,90,10,50);
		//Vaguear vaguear = new Vaguear();
		vaguear.run();
		System.exit(1);
	}
}