import java.util.Random;

public class Gestor {

	Comunicacao pc;
	boolean run;
	Estado estado = Estado.LIGAR;
	int sensorActivado;
	Mensagem ultimaMensagemEnviada;
		
	public Gestor() {
		inicializarVariaveis();
	}
	
	void run (){
		/*pc.enviarMensagem(Mensagem.EVITAR);
		ultimaMensagemEnviada = Mensagem.EVITAR;
		System.out.println(evitarObstaculos());*/
		
		pc.enviarMensagem(Mensagem.VAGUEAR);
		ultimaMensagemEnviada = Mensagem.VAGUEAR;
		System.out.println(vaguear());
		
		while(run) {
			Mensagem msg = pc.receberMensagem();
			System.out.println(msg);
			switch(msg) {
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
					ultimaMensagemEnviada =  Mensagem.EVITAR;
					System.out.println(evitarObstaculos());
					break;
				case EVITAR_TERMINADO:
					pc.enviarMensagem(Mensagem.VAGUEAR);
					ultimaMensagemEnviada =  Mensagem.VAGUEAR;
					System.out.println(vaguear());
					break;
			}
		}
	}
	
	boolean vaguear(){
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("java -jar E:\\ISEL\\2016-2017\\FSO\\VaguearDebug.jar");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p != null;
	}
	
	boolean evitarObstaculos(){
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("java -jar E:\\ISEL\\2016-2017\\FSO\\EvitarObstaculosDebug.jar");
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