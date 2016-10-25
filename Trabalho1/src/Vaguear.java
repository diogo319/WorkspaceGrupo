
public class Vaguear {
	
	int dMin, dMax, aMin, aMax, rMin, rMax, td, ts, dr, angulo, raio, offsetMotorDireito, offsetMotorEsquerdo;
	float vRobot;
	long agora, escolhaTS, sensorTS;
	boolean feito = false;
	boolean b1, b2;
	MyRobotLego robot;
	String nomeRobot;
	
	//teste
	
	public static void main(String args[]){
		Vaguear v = new Vaguear();
		v.run();
		System.exit(1);
	}
	
	public void run(){
		while(!feito){
			nomeRobot = "Guia4";
			ligar();
		}
	}
	
	public Vaguear(){
	}
	
	public void ajustarVM(){
		robot.AjustarVMD(offsetMotorDireito);
		robot.AjustarVME(offsetMotorEsquerdo);
	}
	
	public void ligar(){
		robot = new MyRobotLego();
		boolean open = robot.OpenNXT(nomeRobot);
		if (open){
			configurarRobot();
			accao();
		}
		else{
			fechar();
		}
	}
	
	public void fechar(){
		//TODO
		feito = true;
	}
	
	public void configurarRobot(){
		robot.SetSensorTouch(RobotLego.S_2);
		td = 0;
		ts = 500;
		escolhaTS = sensorTS = System.currentTimeMillis();
		dMin = 10;
		dMax = 75;
		aMin = 0;
		aMax = 90;
		rMin = 10;
		rMax = 50;
		vRobot = 0.02f;
		offsetMotorDireito = 1;
		offsetMotorEsquerdo = 0;
		ajustarVM();
	}
	
	public void accao(){
		agora = System.currentTimeMillis();
		//System.out.println("coiso = " + (agora-escolhaTS) + "     td = " + (td-50));
		b1 = (agora - escolhaTS >= td -50);
		b2 = (agora - sensorTS >= ts);
		if(b2){
			sensor();
		}
		else if(b1 && !b2){
			escolha();
		}
		else{
			dormir();
		}
	}
	
	public void sensor(){
		sensorTS = System.currentTimeMillis();
		b2 = false;
		int estadoSensor = robot.Sensor(RobotLego.S_2);
		if(estadoSensor == 1){
			obstaculo();
		}
		else{
			accao();
		}
	}
	
	public void escolha(){
		int i = (int)(Math.random() * 4);
		escolhaTS = System.currentTimeMillis();
		b1 = false;
		switch(i){
		case 0:
			reta();
			return;
		case 1: 
			curvarDireita();
			return;
		case 2: 
			curvarEsquerda();
			return;
		case 3: 
			parar();
			return;
		}
	}
	
	public void dormir(){
		//TODO
		long tempoDormir = Math.min((td-50)-(agora-escolhaTS), ts-(agora-sensorTS));
		try {
			Thread.sleep(Math.abs(tempoDormir));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		accao();
	}
	
	public void obstaculo(){
		robot.Parar(true);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		robot.CloseNXT();
		fechar();
	}
	
	public void reta(){
		dr = (int)(dMin + (dMax - dMin) * Math.random());
		robot.Reta(dr);
		td = (int)(dr/vRobot);
		accao();
	}
	
	public void curvarDireita(){
		angulo = (int)(aMax * Math.random());
		raio = (int)(rMin + (rMax - rMin) * Math.random());
		robot.CurvarDireita(raio, angulo);
		td = (int)((raio * ((angulo*Math.PI)/180)/(vRobot)));
		accao();
	}
	
	public void curvarEsquerda(){
		angulo = (int)(aMax * Math.random());
		raio = (int)(rMin + (rMax - rMin) * Math.random());
		robot.CurvarEsquerda(raio, angulo);
		td = (int)((raio * ((angulo*Math.PI)/180)/(vRobot)));
		accao();
	}
	
	public void parar(){
		robot.Parar(false);
		td = 0;
		accao();
	}
}

