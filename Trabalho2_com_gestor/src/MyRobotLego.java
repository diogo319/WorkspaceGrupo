
public class MyRobotLego {

	int portoSensorToque;
	String nomeRobot;
	RobotLego robot;
	boolean debug;
	
	public MyRobotLego(String nomeRobot, int portoSensorToque,  boolean debug) {
		if(!debug) robot = new RobotLego();
		this.portoSensorToque = portoSensorToque;
		this.nomeRobot = nomeRobot;
		this.debug = debug;
	}
	
	public void Reta(int distancia) {
		if(debug) System.out.println("MRL : RETA , distancia = " + distancia);
		else robot.Reta(distancia);
	}
	
	public void Parar(boolean b) {
		if(debug) System.out.println("MRL : PARAR , " + b);
		else robot.Parar(b);
	}
	
	public void CurvarEsquerda(int angulo, int raio) {
		if(debug) System.out.println("MRL : ESQUERDA , angulo = " + angulo + " raio = " + raio);
		else robot.CurvarEsquerda(angulo, raio);
	}
	
	public void CurvarDireita(int raio, int angulo) {
		if(debug) System.out.println("MRL : DIREITA , angulo = " + angulo + " raio = " + raio);
		else robot.CurvarDireita(raio, angulo);
	}
	
	public int Sensor() {
		if(debug) {
			System.out.println("MRL : SENSOR");
			return 0;
		}
		return robot.Sensor(portoSensorToque);
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
	
	public void SetSensorTouch() {
		if(!debug) robot.SetSensorTouch(portoSensorToque);
		System.out.println("MRL : SENSOR SET");
	}
	
	public void SetSpeed(int v) {
		if(!debug) robot.SetSpeed(v);
		System.out.println("MRL : vRobot = " + v);
	}
	
	public void CloseNXT() {
		if(debug) {
			System.out.println("DEBUG CONNECTION CLOSED");
		}
		else robot.CloseNXT();
	}
	
	public void AjustarVMD(int offset) {
		if(!debug) robot.AjustarVMD(offset);
	}
	
	public void AjustarVME(int offset) {
		if(!debug) robot.AjustarVME(offset);
	}
	
}
