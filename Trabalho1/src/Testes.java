import java.io.IOException;

public class Testes {
	
	void run(){
		Process p = null;
		try{
			p = Runtime.getRuntime().exec("java -jar C:\\Users\\diogo\\Desktop\\Vaguear.jar");
		}catch(IOException e){System.out.println("erro 1");}
		
		if(p != null){
			try{
				p.waitFor();
			}catch(InterruptedException e){System.out.println("erro");}
		}
	}
	
	/*public static void main(String[] Args){
		
	}*/
}