import java.util.Random;
import java.util.concurrent.Semaphore;

public class SeguirParede extends Comportamento {
	
	//Física
	private float velRobot,w;
	//Timestamps
	private long delay;
	//Parametros escolha
	private int d0, d1, d;
	private int alfa;
	private Distancia distancia;
	private boolean debug;
	
	public SeguirParede(MyRobotLego robot, Semaphore semaforo, Semaphore semaforoRobot, Distancia distancia) {
		super(robot, semaforo, semaforoRobot);
		this.distancia = distancia;
		alfa = 0;
		d0 = 0;
		d1 = 0;	
		velRobot = 0.02f;
		w = 0.13f;
		d = 15;
		estado = Estado.ESPERA;
		debug = false;
	}
	
	public void run() {
		do {
			switch(estado) {
			case ESPERA:
				if(debug) System.out.println("SEGUIR: EM ESPERA");
				try {
					semaforo.acquire();
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				if(debug) System.out.println("SEGUIR: SAIU ESPERA ");
				break;
			case CONFIGURACAO:
				if(estado == Estado.CONFIGURACAO) estado = Estado.ACCAO;
				break;
			case MEDIR_D0:
				d0 = distancia.ler();
				bloquearRobot();
				robot.Reta(d);
				robot.Parar(false);
				libertarRobot();
				if(estado == Estado.ACCAO) estado = Estado.DORMIR_RETA;
				if(debug) System.out.println("SEGUIR: ACCAO RECTA");
				break;
			case MEDIR_D1:
				d1 = distancia.ler();
				alfa = (int)(180 * (Math.atan(Math.abs(d0-d1)/(float)d)) / Math.PI);
				if (estado == Estado.ESCOLHA) {
					if(d0-d1 == 0) estado = Estado.ACCAO;
					else if(d0-d1 < 0) estado = Estado.DIREITA;
					else if(d0-d1 > 0) estado = Estado.ESQUERDA;
				}
				if(debug) System.out.println("SEGUIR: ESCOLHA, d0 = " + d0 + " , d1 = " + d1 + " , d = " + d);
				break;
			case DORMIR_RETA:
				delay = (long)(d/velRobot);
				if(debug) System.out.println("SEGUIR: VAI DORMIR(RETA) , delay = " + delay);
				try {
					Thread.sleep(delay);
		        } catch (Exception e) {
		            if(debug) System.out.println(e);
		        }
				if(estado == Estado.DORMIR_RETA) estado = Estado.MEDIR_D1;
				if(debug) System.out.println("SEGUIR: DORMIU , delay = " + delay);
				break;
			case DORMIR_AJUSTE:
				delay = (long)(alfa / w);
				if(debug) System.out.println("SEGUIR: VAI DORMIR(AJUSTE) , delay = " + delay);
				try {
					Thread.sleep(delay);
		        } catch (Exception e) {
		            if(debug) System.out.println(e);
		        }
				if(estado == Estado.DORMIR_AJUSTE) estado = Estado.MEDIR_D0;
				if(debug) System.out.println("SEGUIR: DORMIU , delay = " + delay);
			case ESQUERDA:
				bloquearRobot();
				robot.CurvarEsquerda(0,alfa);
				robot.Parar(false);
				libertarRobot();
				if(estado == Estado.ESQUERDA) estado = Estado.DORMIR_AJUSTE;
				if(debug) System.out.println("SEGUIR: ESQUERDA , alfa = " + alfa);
				break;
			case DIREITA:
				bloquearRobot();
				robot.CurvarDireita(0,alfa);
				robot.Parar(false);
				libertarRobot();
				if(estado == Estado.ESQUERDA) estado = Estado.DORMIR_AJUSTE;
				if(debug) System.out.println("SEGUIR: DIREITA , alfa = " + alfa);
				break;
			case TERMINAR:
				bloquearRobot();
				robot.Parar(true);
				libertarRobot();
				run = false;
				if(debug) System.out.println("SEGUIR: TERMINAR");
				break;
			}
		} while(run);
		if(debug) System.out.println("SEGUIR: RUN END , run = " + run);
	}
}
