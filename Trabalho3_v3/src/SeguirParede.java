import java.util.Random;
import java.util.concurrent.Semaphore;

public class SeguirParede extends Comportamento {
	
	//Física
	float velRobot,w;
	//Timestamps
	long escolhaTS, sensorTS,agoraTS,delay,td;
	//Parametros escolha
	int d0, d1, d;
	int alfa;
	int sensorActivado;
	Distancia distancia;
	
	public SeguirParede(MyRobotLego robot, Semaphore semaforo, Semaphore semaforoRobot, Distancia distancia) {
		super(robot, semaforo, semaforoRobot);
		this.distancia = distancia;
		alfa = 0;
		d0 = 0;
		d1 = 0;	
		sensorActivado = 0;
		velRobot = 0.02f;
		w = 0.12f;
		d = 15;
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
				System.out.println("SEGUIR: LIGAR");
				robot.Parar(true);
			case ACCAO:
				d0 = lerDistancia();
				bloquearRobot();
				robot.Reta(15);
				robot.Parar(false);
				libertarRobot();
				delay = (long)(d/velRobot);
				estado = Estado.DORMIR;
				break;
			case ESCOLHA:
				d1 = lerDistancia();
				//Matematica
				alfa = (int)(180 * (Math.atan(Math.abs(d0-d1)/(float)d)) / Math.PI);
				delay = (long)(alfa / w);
				if(d0-d1 == 0) {
					estado = Estado.ACCAO;
				}
				else if(d0-d1 < 0) estado = Estado.DIREITA;
				else if(d0-d1 > 0) estado = Estado.ESQUERDA;
				System.out.println("SEGUIR: ESCOLHA, d0 = " + d0 + " , d1 = " + d1);
				break;
			case DORMIR:
				System.out.println("SEGUIR: DORMINDO , delay = " + delay);
				try {
					Thread.sleep(delay);
		        } catch (Exception e) {
		            System.out.println(e);
		        }
				if(delay != 750){
					estado = Estado.ACCAO;
				} else {
					estado = Estado.ESCOLHA;
				}
				break;
			case ESQUERDA:
				bloquearRobot();
				robot.CurvarEsquerda(0,alfa);
				robot.Parar(false);
				libertarRobot();
				estado = Estado.DORMIR;
				System.out.println("SEGUIR: ESQUERDA , alfa = " + alfa);
				break;
			case DIREITA:
				bloquearRobot();
				robot.CurvarDireita(0,alfa);
				robot.Parar(false);
				libertarRobot();
				estado = Estado.DORMIR;
				System.out.println("SEGUIR: DIREITA , alfa = " + alfa);
				break;
			case TERMINAR:
				desactivar();
				System.out.println("SEGUIR: RUN END , run = " + run);
				break;
			}	
			System.out.println("Proximo estado (SEGUIR) = " + estado);
		} while(run);
	}
	
	private int lerDistancia() {
		return distancia.ler();
	}
	
}
