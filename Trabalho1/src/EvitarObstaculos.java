
public class EvitarObstaculos {
	
	//minhas vars
	static String nomeRobot;
	static Boolean openNXT, b;
	static MyRobotLego robot;
	boolean feito;
	int offsetMotorEsquerdo, offsetMotorDireito, raioCurva, anguloCurva, sensor, dRetaguarda, tCurva;
	long agora, sensorTS;
	float vRobot;
	
	
	//teste
	int teste = 0;
	
	
	public void ligar(){
		robot = new MyRobotLego();
		openNXT = robot.OpenNXT(nomeRobot);
		if(openNXT){
			configuracao();
		}
		else{
			fechar();
		}
	}
	
	public void fechar(){
		feito = true;
	}
	
	/*
	 * Inicializa todas as variaveis necessarias
	 */
	public void configuracao(){
		
		robot.SetSensorTouch(RobotLego.S_2);
		feito = false;
		openNXT = false;
		offsetMotorDireito = 1;
		offsetMotorEsquerdo = 0;
		
		sensorTS = System.currentTimeMillis();
		
		robot.SetSensorTouch(RobotLego.S_2);
		vRobot = 0.02f;
		tCurva = 750;
		raioCurva = 0;
		anguloCurva = 90;
		dRetaguarda = -15;
		sensor();
	}
	
	public void sensor(){
		sensor = robot.Sensor(RobotLego.S_2);
		if(sensor == 1){
			obstaculo();
		}
		else{
			parar();
		}
	}
	
	public void obstaculo(){
		sensorTS = System.currentTimeMillis();
		robot.Reta(dRetaguarda);
		robot.CurvarEsquerda(raioCurva, anguloCurva);
		robot.Parar(false);
		accao();
	}
	
	public void accao(){
		agora = System.currentTimeMillis();
		b = (agora-sensorTS >= (-dRetaguarda/vRobot) + tCurva);
		if(b){
			sensor();
		}
		else{
			dormir();
		}
	}
	
	public void dormir(){
		float tempoDormir = ((-dRetaguarda/vRobot) + tCurva) - (agora-sensorTS);
		try {
			Thread.sleep((long)tempoDormir);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		accao();
	}
	
	public void parar(){
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		robot.CloseNXT();
		fechar();
	}
	
	public void ajustarVM(){
		robot.AjustarVMD(offsetMotorDireito);
		robot.AjustarVME(offsetMotorEsquerdo);
	}
	
	
	
	public void run(){
		while(!feito){
			nomeRobot = "Guia4";
			ligar();
		}
	}
	
	
	public EvitarObstaculos(){
	}
	
	
	public static void main(String args[]){
		EvitarObstaculos eo = new EvitarObstaculos();
		eo.run();
		System.exit(1);
	}
}
