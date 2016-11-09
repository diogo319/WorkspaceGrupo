import java.util.Random;

public class EvitarObstaculos {

	public static final int porto = RobotLego.S_2;
	
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
		
	public EvitarObstaculos(MyRobotLego robot, int distancia, int angulo, int raio) {
		this.robot = robot;
		this.distancia = distancia;
		this.angulo = angulo;
		this.raio = raio;
		//Inicializar variáveis
		sensorTS = System.currentTimeMillis();
		td = 0;
		verificarEscolha = false;
		verificarSensor = false;
		run = false;
	}
	
	public void run() {
		do {
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
				td = (long)(750 +  distancia / velRobot);
				robot.SetSensorTouch();
				//robot.SetSpeed((int) velRobot * 100);
				System.out.println("CONFIGURACAO");
				estado = Estado.SENSOR;
				break;
			case ACCAO:
				agoraTS = System.currentTimeMillis();
				if(agoraTS - sensorTS >= td) {
					estado = Estado.SENSOR;
				} else {
					delay = td - (agoraTS - sensorTS);
					estado = Estado.DORMIR;
				}
				System.out.println("ACCAO");
				break;
			case SENSOR:
				sensorActivado = robot.Sensor();
				sensorTS = System.currentTimeMillis();
				if(sensorActivado == 1) {
					estado = Estado.OBSTACULO;
				} else estado = Estado.TERMINAR;
				System.out.println("SENSOR");
				break;
			case DORMIR:
				agoraTS = System.currentTimeMillis();
				System.out.println("DORMINDO , delay = " + tempoSensor);
				try {
					Thread.sleep(delay);	
		        } catch (Exception e) {
		            System.out.println(e);
		        }
				estado = Estado.ACCAO;
				break;
			case OBSTACULO:
				System.out.println("OBSTACULO");
				robot.Reta(distancia);
				robot.CurvarEsquerda(angulo, raio);
				estado = Estado.ACCAO;
				break;
			case TERMINAR:
				robot.Parar(true);
				run = false;
				robot.CloseNXT();
				try {
		            Thread.sleep(100);
		        } catch (Exception e) {
		            System.out.println(e);
		        } 
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
		MyRobotLego robot = new MyRobotLego("Guia4",RobotLego.S_2,false);
		EvitarObstaculos evitarObstaculos = new EvitarObstaculos(robot,-15,90,0);
		evitarObstaculos.run();
		System.exit(1);
	}
}
