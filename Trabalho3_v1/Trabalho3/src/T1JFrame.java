import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class T1JFrame extends JFrame {

	private JPanel contentPane;
	
	private JTextField nomeRobotTextField;
	private JTextField consolaJTextField;
	private JTextField motorEsquerdoTextField;
	private JTextField motorDireitoTextField;
	private JTextField distanciaJTextfield;
	private JTextField raioTextField;
	private JTextField anguloTextField;
	
	private JLabel nomeRobotJLabel;
	private JLabel consolaJLabel;
	private JLabel motorEsquerdoJLabel;
	private JLabel motorDireitoJLabel;
	private JLabel distanciaJLabel;
	private JLabel raioJLabel;
	private JLabel anguloJLabel;
	
	private JCheckBox gestorCheckBox;
	private JCheckBox vaguearCheckBox;
	private JCheckBox evitarCheckBox;
	
	private JRadioButton onOffRadioButton;
	private JCheckBox debugCheckBox;
	
	private JButton frenteJButton;
	private JButton pararJButton;
	private JButton direitaJButton;
	private JButton retaguardaJButton;
	private JButton esquerdaJButton;
	
	//Minhas Variaveis
	String nomeRobot, consola;
	int offsetMotorEsquerdo, offsetMotorDireito, distancia, angulo, raio;
	boolean onOff,debug,run;
	MyRobotLego robot;
	
	void inicializarVariaveis(){
		nomeRobot = new String("Guia4");
		consola = new String("");
		offsetMotorDireito = 1;
		offsetMotorEsquerdo = 0;
		onOff = false;
		debug = true;
		run = true;
		
		distancia = 10;
		angulo = 90;
		raio = 15;
		
		robot = new MyRobotLego(nomeRobot,RobotLego.S_2,true);
	}
	
	void actualizarBotoes() {
		frenteJButton.setEnabled(onOff);
		pararJButton.setEnabled(onOff);
		direitaJButton.setEnabled(onOff);
		retaguardaJButton.setEnabled(onOff);
		esquerdaJButton.setEnabled(onOff);
	}
	
	void actualizarCheckBoxes(boolean b) {
		vaguearCheckBox.setEnabled(b);
		evitarCheckBox.setEnabled(b);
		gestorCheckBox.setEnabled(b);
	}
	
	void ajustarVM() {
		robot.AjustarVMD(offsetMotorDireito);
		robot.AjustarVME(offsetMotorEsquerdo);
	}
	
	public void run() {
		
	}

	/**
	 * Launch the application
	 */
	public void sleep() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		T1JFrame frame = new T1JFrame();
		frame.run();
	}

	/**
	 * Create the frame.
	 */
	public T1JFrame() {
		
		//incializar variaveis
		inicializarVariaveis();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 505, 412);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		nomeRobotJLabel = new JLabel("Nome Robot:");
		nomeRobotJLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nomeRobotJLabel.setBounds(91, 11, 83, 24);
		contentPane.add(nomeRobotJLabel);
		
		nomeRobotTextField = new JTextField();
		nomeRobotTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nomeRobot = nomeRobotTextField.getText();
				myPrint("Nome do Robot: " + nomeRobot);
			}
		});
		nomeRobotTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nomeRobotTextField.setBounds(184, 15, 86, 20);
		contentPane.add(nomeRobotTextField);
		nomeRobotTextField.setColumns(10);
		//inicializar Nome do Robot
		nomeRobotTextField.setText(nomeRobot);
		
		
		onOffRadioButton = new JRadioButton("ON/OFF");
		onOffRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(onOff){
					robot.CloseNXT();
					onOff = !onOff;
					myPrint("Robot: OFF");
				}else{
					onOff = robot.OpenNXT();
					if(onOff) {
						myPrint("Robot: ON");
						ajustarVM();
					}
					else myPrint("Robot: Liga��o sem sucesso");
				}
				
				actualizarBotoes();
				actualizarCheckBoxes(!onOff);
				onOffRadioButton.setSelected(onOff);

			}
		});
		onOffRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		onOffRadioButton.setBounds(293, 14, 109, 23);
		contentPane.add(onOffRadioButton);
		//inicializar On/Off
		onOffRadioButton.setSelected(onOff);
		
		frenteJButton = new JButton("FRENTE");
		frenteJButton.setBackground(Color.GREEN);
		frenteJButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frenteJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myPrint("Bot�o Frente Pressionado: Dist " + distancia + "cm");
				robot.Reta(distancia);
				robot.Parar(false);
			}
		});
		frenteJButton.setBounds(181, 110, 123, 50);
		contentPane.add(frenteJButton);
		
		pararJButton = new JButton("PARAR");
		pararJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myPrint("Bot�o Parar Pressionado");
				robot.Parar(true);
			}
		});
		pararJButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pararJButton.setBackground(Color.RED);
		pararJButton.setBounds(181, 160, 123, 50);
		contentPane.add(pararJButton);
		
		direitaJButton = new JButton("DIREITA");
		direitaJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myPrint("Bot�o Direita Pressionado: Ang " + angulo + " Raio " + raio);
				robot.CurvarDireita(raio, angulo);
				robot.Parar(false);
			}
		});
		direitaJButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		direitaJButton.setBackground(Color.ORANGE);
		direitaJButton.setBounds(303, 160, 123, 50);
		contentPane.add(direitaJButton);
		
		retaguardaJButton = new JButton("RETAGUARDA");
		retaguardaJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myPrint("Bot�o Retaguarda Pressionado: Dist " + distancia + "cm");
				robot.Reta(-distancia);
				robot.Parar(false);
			}
		});
		retaguardaJButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		retaguardaJButton.setBackground(Color.BLUE);
		retaguardaJButton.setBounds(181, 210, 123, 50);
		contentPane.add(retaguardaJButton);
		
		esquerdaJButton = new JButton("ESQUERDA");
		esquerdaJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myPrint("Bot�o Esquerda Pressionado: Ang " + angulo + " Raio " + raio);
				robot.CurvarEsquerda(raio, angulo);
				robot.Parar(false);
			}
		});
		esquerdaJButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		esquerdaJButton.setBackground(Color.YELLOW);
		esquerdaJButton.setBounds(58, 160, 123, 50);
		contentPane.add(esquerdaJButton);
		
		debugCheckBox = new JCheckBox("Debug");
		debugCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				debug = !debug;
				debugCheckBox.setSelected(debug);
				
				if(debug){
					consolaJTextField.setText("Debug: Activado");
				}else{
					consolaJTextField.setText("Debug: Desactivado");
				}
			}
		});
		debugCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		debugCheckBox.setBounds(6, 312, 67, 23);
		contentPane.add(debugCheckBox);
		//inicializar Debug
		debugCheckBox.setSelected(debug);
		
		consolaJTextField = new JTextField();
		consolaJTextField.setEditable(false);
		consolaJTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		consolaJTextField.setBounds(69, 342, 410, 20);
		contentPane.add(consolaJTextField);
		consolaJTextField.setColumns(10);
		
		consolaJLabel = new JLabel("Consola:");
		consolaJLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		consolaJLabel.setBounds(6, 345, 473, 14);
		contentPane.add(consolaJLabel);
		
		motorEsquerdoTextField = new JTextField();
		motorEsquerdoTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		motorEsquerdoTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				offsetMotorEsquerdo = Integer.parseInt(motorEsquerdoTextField.getText());
				myPrint("Motor Esquerdo: " + offsetMotorEsquerdo);
				robot.AjustarVME(offsetMotorEsquerdo);
			}
		});
		motorEsquerdoTextField.setBounds(6, 44, 26, 20);
		contentPane.add(motorEsquerdoTextField);
		motorEsquerdoTextField.setColumns(10);
		//Inicializar offsetMotorEsquerdo no textfield
		motorEsquerdoTextField.setText("" + offsetMotorEsquerdo);
		
		motorDireitoTextField = new JTextField();
		motorDireitoTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		motorDireitoTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				offsetMotorDireito = Integer.parseInt(motorDireitoTextField.getText());
				myPrint("Motor Direito: " + offsetMotorDireito);
				robot.AjustarVMD(offsetMotorDireito);
			}
		});
		motorDireitoTextField.setColumns(10);
		motorDireitoTextField.setBounds(398, 44, 26, 20);
		contentPane.add(motorDireitoTextField);
		//Inicializar offsetMotorDireito no textfield
		motorDireitoTextField.setText("" + offsetMotorDireito);
		
		motorEsquerdoJLabel = new JLabel("Motor Esquerdo");
		motorEsquerdoJLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		motorEsquerdoJLabel.setBounds(42, 47, 98, 14);
		contentPane.add(motorEsquerdoJLabel);
		
		motorDireitoJLabel = new JLabel("Motor Direito");
		motorDireitoJLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		motorDireitoJLabel.setBounds(303, 45, 83, 14);
		contentPane.add(motorDireitoJLabel);
		
		distanciaJLabel = new JLabel("Distancia");
		distanciaJLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		distanciaJLabel.setBounds(6, 74, 54, 14);
		contentPane.add(distanciaJLabel);
		
		distanciaJTextfield = new JTextField();
		distanciaJTextfield.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				distancia = Integer.parseInt(distanciaJTextfield.getText());
				myPrint("Distancia Alterada Para: " + distancia);
			}
		});
		distanciaJTextfield.setFont(new Font("Tahoma", Font.PLAIN, 14));
		distanciaJTextfield.setBounds(69, 74, 44, 20);
		contentPane.add(distanciaJTextfield);
		distanciaJTextfield.setColumns(10);
		//Inicializar Distancia no textfield
		distanciaJTextfield.setText("" + distancia);
		
		raioJLabel = new JLabel("Raio");
		raioJLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		raioJLabel.setBounds(208, 74, 31, 14);
		contentPane.add(raioJLabel);
		
		raioTextField = new JTextField();
		raioTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				raio = Integer.parseInt(raioTextField.getText());
				myPrint("Raio Alterado Para: " + raio);
			}
		});
		raioTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		raioTextField.setColumns(10);
		raioTextField.setBounds(241, 74, 44, 20);
		contentPane.add(raioTextField);
		//Inicializar Raio no textfield
		raioTextField.setText("" + raio);
		
		anguloJLabel = new JLabel("Angulo");
		anguloJLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		anguloJLabel.setBounds(379, 74, 46, 17);
		contentPane.add(anguloJLabel);
		
		anguloTextField = new JTextField();
		anguloTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				angulo = Integer.parseInt(anguloTextField.getText());
				myPrint("Angulo Alterado Para: " + angulo);
			}
		});
		anguloTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		anguloTextField.setColumns(10);
		anguloTextField.setBounds(435, 74, 44, 20);
		contentPane.add(anguloTextField);
		//Inicializar Angulo no textfield
		anguloTextField.setText("" + angulo);
		anguloTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				angulo = Integer.parseInt(anguloTextField.getText());
				myPrint("Angulo Alterado Para: " + angulo);
			}
		});
		
		//Fechar robot ao fechar a GUI
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {

			}
		});
		
		vaguearCheckBox = new JCheckBox("Vaguear");
		vaguearCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		vaguearCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		vaguearCheckBox.setBounds(340, 237, 86, 23);
		contentPane.add(vaguearCheckBox);
		
		evitarCheckBox = new JCheckBox("EvitarObstaculos");
		evitarCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		evitarCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		evitarCheckBox.setBounds(340, 265, 127, 23);
		contentPane.add(evitarCheckBox);
		
		gestorCheckBox = new JCheckBox("Gestor");
		gestorCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		gestorCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		gestorCheckBox.setBounds(340, 293, 127, 23);
		contentPane.add(gestorCheckBox);
		
		JCheckBox seguirParedeCheckbox = new JCheckBox("SeguirParede");
		seguirParedeCheckbox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		seguirParedeCheckbox.setEnabled(true);
		seguirParedeCheckbox.setBounds(340, 319, 127, 23);
		contentPane.add(seguirParedeCheckbox);
		
		actualizarBotoes();
		actualizarCheckBoxes(!onOff);
		setVisible(true);
	}
	
	void myPrint (String s){
		if (debug) consolaJTextField.setText(s);
	}
}
