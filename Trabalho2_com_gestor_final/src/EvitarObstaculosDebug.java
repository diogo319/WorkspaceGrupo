import java.util.Random;

public class EvitarObstaculosDebug {

	public static final int porto = RobotLego.S_2;
	Comunicacao pc;
	MyRobotLego robot;
	boolean run;
	Random rnd = new Random();
	//Física
	float velRobot;
	//Timestamps
	long sensorTS,agoraTS,tempoSensor,td,delay;
	//Escolha
	Estado estado = Estado.LIGAR;
	int sensorActivado;
	boolean verificarEscolha,verificarSensor;
	int distancia,angulo,raio;
	long start;
		
	public EvitarObstaculosDebug(MyRobotLego robot, int distancia, int angulo, int raio) {
		this.robot = robot;
		this.distancia = distancia;
		this.angulo = angulo;
		this.raio = raio;
		inicializarVariaveis();
		start = System.currentTimeMillis();
	}
	
	public void inicializarVariaveis() {
		sensorTS = System.currentTimeMillis();
		td = 0;
		verificarEscolha = false;
		verificarSensor = false;
		run = false;
		pc = new Comunicacao();
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
				tempoSensor = 500;
				velRobot = 0.02f;
				//Tempo da curva + tempo da recta
				td = (long)(750 +  -distancia / velRobot);
				robot.SetSensorTouch();
				//robot.SetSpeed((int) velRobot * 100);
				System.out.println("CONFIGURACAO");
				estado = Estado.SENSOR;
				break;
			case ACCAO:
				agoraTS = System.currentTimeMillis();
				if(agoraTS - sensorTS >= tempoSensor) {
					estado = Estado.SENSOR;
				} else {
					delay = Math.min(td-50 - (agoraTS - sensorTS), tempoSensor-(agoraTS-sensorTS));
					estado = Estado.DORMIR;
				}
				System.out.println("ACCAO");
				break;
			case SENSOR:
				//sensorActivado = robot.Sensor();
				sensorActivado = 1;
				sensorTS = System.currentTimeMillis();
				if(sensorActivado == 1) {
					estado = Estado.OBSTACULO;
				} else estado = Estado.TERMINAR;
				System.out.println("SENSOR");
				break;
			case DORMIR:
				System.out.println("DORMINDO , delay = " + delay);
				if(pc.receberMensagem() == Mensagem.TERMINAR) {
					estado = Estado.TERMINAR;
					break;
				} else estado = Estado.ACCAO;
				try {
					Thread.sleep(delay);	
		        } catch (Exception e) {
		            System.out.println(e);
		        }
				break;
			case OBSTACULO:
				System.out.println("OBSTACULO");
				robot.Reta(distancia);
				robot.CurvarEsquerda(angulo, raio);
				estado = Estado.ACCAO;
				break;
			case TERMINAR:
				robot.Parar(true);
				robot.CloseNXT();
				run = false;
				pc.enviarMensagem(Mensagem.EVITAR_TERMINADO);
				pc.fecharCanal();
				System.out.println("TERMINAR");
				break;
			default:
				estado = Estado.ACCAO;
				break;
			}
		} while(run);
	}
	
	public static void main(String[] args) {
		//debug = true , modo de teste
		MyRobotLego robot = new MyRobotLego("Guia4",RobotLego.S_2,true);
		EvitarObstaculosDebug evitarObstaculos = new EvitarObstaculosDebug(robot,-15,90,0);
		evitarObstaculos.run();
		System.exit(1);
	}
}
