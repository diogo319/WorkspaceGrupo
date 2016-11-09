import java.util.Random;

public class Gestor {

	ProcessoM pc;
	boolean run;
	Estado estado = Estado.LIGAR;
	int sensorActivado;
		
	public Gestor() {
		inicializarVariaveis();
		pc.enviarMensagem(Mensagem.VAGUEAR);
	}
	
	void run (){
		//vaguear();
		/*evitarObstaculos();
		pc.enviarMensagem(Mensagem.ESPERAR);
		Mensagem msg = pc.receberMensagem();
		while(msg != Mensagem.VAGUEAR)
			msg = pc.receberMensagem();
			System.out.println(msg);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		System.out.println("fim");*/
		
		while(run) {
			Mensagem msg = pc.receberMensagem();
			System.out.println(msg);
			switch(msg) {
				case VAGUEAR:
					vaguear();
					pc.enviarMensagem(Mensagem.ESPERAR);
					break;
				case EVITAR:
					evitarObstaculos();
					pc.enviarMensagem(Mensagem.ESPERAR);
					break;
				case ESPERAR:
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
			}
		}
	}
	
	boolean vaguear(){
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("java -jar D:\\ISEL\\2016-2017\\FSO\\Vaguear.jar");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(p == null) {
			return false;
		}
		return true;
	}
	
	boolean evitarObstaculos(){
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("java -jar D:\\ISEL\\2016-2017\\FSO\\Vaguear.jar");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(p != null) {
			return true;
		}
		return false;

	}
	
	public void inicializarVariaveis() {
		run = true;
		pc = new ProcessoM();
	}
	
	public static void main(String[] args) {
		//debug = true , modo de teste
		Gestor gestor = new Gestor();
		gestor.run();
		System.exit(1);
	}
}
