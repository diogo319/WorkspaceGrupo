import java.util.Random;

public class Gestor {

	Comunicacao pc;
	boolean run;
	Estado estado = Estado.LIGAR;
	int sensorActivado;
	Mensagem ultimaRecebida;	
	
	public Gestor() {
		inicializarVariaveis();
	}
	
	void run (){
		/*pc.enviarMensagem(Mensagem.EVITAR);
		System.out.println(evitarObstaculos());*/
		
		pc.enviarMensagem(Mensagem.VAGUEAR);
		System.out.println(vaguear());
		
		while(run) {
			Mensagem msg = pc.receberMensagem();
			System.out.println(msg);
			switch(msg) {
				default:
				case VAGUEAR:
				case EVITAR:
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case VAGUEAR_TERMINADO:
					pc.enviarMensagem(Mensagem.EVITAR);
					System.out.println(evitarObstaculos());
					break;
				case EVITAR_TERMINADO:
					pc.enviarMensagem(Mensagem.VAGUEAR);
					System.out.println(vaguear());
					break;
				case TERMINAR:
					run = false;
					//Esperar que outros processos leiam a mensagem para terminar
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pc.enviarMensagem(Mensagem.GESTOR_TERMINADO);
					pc.fecharCanal();
					break;
			}
			ultimaRecebida = msg;
		}
	}
	
	boolean vaguear(){
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("java -jar D:\\ISEL\\2016-2017\\FSO\\VaguearDebug.jar");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p != null;
	}
	
	boolean evitarObstaculos(){
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("java -jar D:\\ISEL\\2016-2017\\FSO\\EvitarObstaculosDebug.jar");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return p != null;
	}
	
	public void inicializarVariaveis() {
		run = true;
		pc = new Comunicacao();
	}
	
	public static void main(String[] args) {
		//debug = true , modo de teste
		Gestor gestor = new Gestor();
		gestor.run();
		System.exit(1);
	}
}