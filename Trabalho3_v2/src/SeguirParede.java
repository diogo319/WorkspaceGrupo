import java.util.Random;
import java.util.concurrent.Semaphore;

public class SeguirParede extends Comportamento {
	
	boolean run;
	//Física
	float velRobot;
	//Timestamps
	long escolhaTS, sensorTS,agoraTS,tempoSensor,td,delay,start;
	//Parametros escolha
	int dmin,dmax,amax,rmin,rmax;
	//Escolha
	Estado estado;
	int sensorActivado;
	boolean verificarEscolha,verificarSensor;
	int[] distancia;
	Semaphore semaforoDistancia;
	
	public SeguirParede(MyRobotLego robot, Semaphore semaforo, Semaphore semaforoRobot, Semaphore semaforoDistancia, BufferDistancias distancia) {
		super(robot, semaforo, semaforoRobot);
		this.semaforoDistancia = semaforoDistancia;
		estado = Estado.LIGAR;
	}
	
	public void run() {
		do {
			/*if(System.currentTimeMillis()-start > 5000 && robot.debug()) {
				estado = Estado.COMUNICACAO;
			}*/
			
			switch(estado) {
			case LIGAR:	
				try {
					semaforo.acquire();
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				System.out.println("SEGUIR: LIGAR");
				break;
			case ACCAO:
				agoraTS = System.currentTimeMillis();
				if (agoraTS - sensorTS >= td-50) {
					bloquearRobot();
					estado = Estado.ESCOLHA;
				} else {
					libertarRobot();
					delay = (td-50)-(agoraTS-escolhaTS);
					estado = Estado.DORMIR;
				}
				System.out.println("SEGUIR: ACCAO");
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

				System.out.println("SEGUIR: ESCOLHA = " + estado.toString());
				break;
			case DORMIR:
				System.out.println("SEGUIR: DORMINDO , delay = " + delay);
				try {
					Thread.sleep(delay);
		        } catch (Exception e) {
		            System.out.println(e);
		        }
				estado = Estado.ACCAO;
				break;
			case RETA:
				//recta();
				libertarRobot();
				estado = Estado.ACCAO;
				System.out.println("SEGUIR: RETA , td = " + td);
				break;
			case ESQUERDA:
				//curvarEsquerda();
				libertarRobot();
				estado = Estado.ACCAO;
				System.out.println("SEGUIR: ESQUERDA , td = " + td);
				break;
			case DIREITA:
				//curvarDireita();
				libertarRobot();
				estado = Estado.ACCAO;
				System.out.println("SEGUIR: DIREITA , td = " + td);
				break;
			case PARAR:
				libertarRobot();
				estado = Estado.ACCAO;
				System.out.println("SEGUIR: PARAR , td = " + td);
				break;
			case TERMINAR:
				desactivar();
				System.out.println("SEGUIR: RUN END , run = " + run);
				break;
			}	
		} while(run);
	}
	
}
