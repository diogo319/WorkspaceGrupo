
public class MyRobotLego {
	
	public boolean OpenNXT(String s){
		double f = Math.random();
		if(f < 0.9){
			return true;
		}
		else{
			System.out.println("Conexão ao robot sem sucesso");
			return false;
		}
	}
	
	public void AjustarVMD(int d){
		
	}
	
	public void AjustarVME(int d){
		
	}
	
	public void CloseNXT(){
		
	}
	
	public void Reta(int d){
		System.out.println("Reta " + d + " cm");
	}
	
	public void CurvarDireita(int raio, int angulo){
		System.out.println("Curva Direita " + raio + " cm " + angulo + "º");
	}
	
	public void CurvarEsquerda(int raio, int angulo){
		System.out.println("Curva Esquerda " + raio + " cm " + angulo + "º");
	}
	
	public void Parar(boolean f){
		if (f){
			System.out.println("Obstáculo - Parar (True)");
		}
		else{
			System.out.println("Parar (False)");
		}
	}
	
	public void SetSensorTouch(int porto){
		
	}
	
	public int Sensor(int porto){
		double f = Math.random();
		if(f < 0.9){
			return 0;
		}
		return 1;
	}
}
