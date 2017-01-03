public class MyRobotLego {

	private String nomeRobot;
	private RobotLego robot;
	private boolean debug, configurado;
	private int[] distancias = {29,29,30,31,32,33,255,33,34,32,32,32,31};
	private int i = 0;
	
	public MyRobotLego(String nomeRobot,  boolean debug) {
		if(!debug) robot = new RobotLego();
		this.nomeRobot = nomeRobot;
		this.debug = debug;
	}
	
	public boolean configurado() { return configurado; }
	
	public void setConfigurado(boolean b) { configurado = b; }
	
	public boolean debug() { return debug; }
	
	public void Reta(int distancia) {
		if(debug) System.out.println("MRL : RETA , distancia = " + distancia);
		else robot.Reta(distancia);
	}
	
	public void Parar(boolean b) {
		if(debug) System.out.println("MRL : PARAR , " + b);
		else robot.Parar(b);
	}
	
	public void CurvarEsquerda(int raio, int angulo) {
		if(debug) System.out.println("MRL : ESQUERDA , angulo = " + angulo + " raio = " + raio);
		else robot.CurvarEsquerda(raio, angulo);
	}
	
	public void CurvarDireita(int raio, int angulo) {
		if(debug) System.out.println("MRL : DIREITA , angulo = " + angulo + " raio = " + raio);
		else robot.CurvarDireita(raio, angulo);
	}
	
	public int Sensor(int porto) {
		if(debug) {
			return Math.random() < 0.1 ? 1 : 0;
		}
		return robot.Sensor(porto);
	}
	
	public int SensorUS(int porto) {
		if(debug) {
			//return 90 + (int)(Math.random()*10);
			i = i % distancias.length;
			return distancias[i++];
		}
		return robot.SensorUS(porto);
	}
	
	public boolean OpenNXT() {
		if(!debug && robot.openNXT(nomeRobot)) {
			System.out.println("MRL : LIVE CONNECTION");
			return true;
		} else if(debug) {
			System.out.println("MRL : DEBUG CONNECTION");
			return true;
		}
		return false;
	}
	
	public void SetSensorTouch(int portoSensorToque) {
		if(!debug) robot.SetSensorTouch(portoSensorToque);
		System.out.println("MRL : SENSOR TOUCH SET");
	}
	
	public void SetSensorLowspeed(int portoSensorDist) {
		if(!debug) robot.SetSensorLowspeed(portoSensorDist);
		System.out.println("MRL : SENSOR LIGHT SET");
	}
	
	public void SetSpeed(int v) {
		if(!debug) robot.SetSpeed(v);
		System.out.println("MRL : vRobot = " + v);
	}
	
	public void CloseNXT() {
		if(debug) {
			System.out.println("DEBUG CONNECTION CLOSED");
			return;
		}
		else robot.CloseNXT();
		try {
            Thread.sleep(100);
        } catch (Exception e) {
            System.out.println(e);
        }
	}
	
	public void AjustarVMD(int offset) {
		if(!debug) robot.AjustarVMD(offset);
	}
	
	public void AjustarVME(int offset) {
		if(!debug) robot.AjustarVME(offset);
	}

	public void mudarNome(String nomeRobot) {
		this.nomeRobot = nomeRobot;
	}
	
}