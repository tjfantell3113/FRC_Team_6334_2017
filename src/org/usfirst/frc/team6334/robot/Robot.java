
package org.usfirst.frc.team6334.robot;


import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team6334.robot.commands.ExampleCommand;
import org.usfirst.frc.team6334.robot.subsystems.ExampleSubsystem;

import com.analog.adis16448.frc.ADIS16448_IMU;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	Joystick mainStick, auxStick;
	Spark motorpwm0, motorpwm1, motorpwm2, motorpwm3, motorpwm4;
	Victor climber, shooter, feeder;
	Timer timer;
	DigitalInput din0, din1, din2, din3, din4, din5, din6, photoElectric, photoElectric2;
	int DigitalSwitch;
	int Switch0, Switch1, Switch2, Switch3, Switch4;
	double driveRight, driveLeft;
	
	Servo servo0, servoBallRelease;
	
	//ADIS16448_IMU imu;
	UsbCamera cam0, cam1;
	VideoSink server;
	
	boolean ropeCamera = false, runAuto = true;

	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		chooser.addDefault("Default Auto", new ExampleCommand());
		//chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		cam0 = CameraServer.getInstance().startAutomaticCapture(0);
		cam1 = CameraServer.getInstance().startAutomaticCapture(1);
		server = CameraServer.getInstance().getServer();
		/*VideoSource[] knownSources = VideoSource.enumerateSources();
		for(int i = 0; i < knownSources.length; i++){
			if(knownSources[0].getKind() == VideoSource.Kind.kUsb){
				//USB Camera
				String name = knownSources[0].getName();
				System.out.println("Camera " + i + ": " + name);
			}
		}*/
		
		servoBallRelease = new Servo(0);
		motorpwm1 = new Spark(1);
		motorpwm2 = new Spark(2);
		motorpwm3 = new Spark(3);
		motorpwm4 = new Spark(4);
		climber = new Victor(5);
	//	shooter = new Victor(6);
	//	feeder = new Victor(7);
		servo0 = new Servo(8);
		
		mainStick = new Joystick(0);
		auxStick = new Joystick(1);
		
		timer = new Timer();
		
		din0 = new DigitalInput(0);
		din1 = new DigitalInput(1);
		din2 = new DigitalInput(2);
		din3 = new DigitalInput(3); 
		din4 = new DigitalInput(4); 
		din5 = new DigitalInput(5);
		din6 = new DigitalInput(6);
		photoElectric = new DigitalInput(7);	 //left photo electric sensor
		photoElectric2 = new DigitalInput(8);    //right photo electric sensor
		
		//imu = new ADIS16448_IMU();
		
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**d
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();

		if(runAuto){
				// red (1), left
			if(!din0.get()) {     
				System.out.print("din0");
				servoBallRelease.setAngle(10);
				motorpwm2.set(-0.2);
				motorpwm4.set(-0.2);
				motorpwm1.set(0.2);
				motorpwm3.set(0.2);
				Timer.delay(.1);
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(.1);
				motorpwm2.set(-1);
				motorpwm4.set(-1);
				motorpwm1.set(1);
				motorpwm3.set(1);
				Timer.delay(.52);    //.58 forward
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(.4);     //.98 ease
				motorpwm2.set(-0.1);
				motorpwm4.set(-0.1);
				motorpwm1.set(0.1);
				motorpwm3.set(0.1);
				Timer.delay(.33);    //1.31 ease
				motorpwm2.set(.4);
				motorpwm4.set(.4);
				motorpwm1.set(.4);
				motorpwm3.set(.4);
				Timer.delay(.4);    //1.78 rotate right
				motorpwm2.set(-.7);
				motorpwm4.set(-.7);
				motorpwm1.set(.7);
				motorpwm3.set(.7);
				Timer.delay(.52);    //2.23 forward
				motorpwm2.set(-.15);
				motorpwm4.set(-.15);
				motorpwm1.set(.15);
				motorpwm3.set(.15);
				Timer.delay(4);      //10.28 stop
				motorpwm2.set(.5);
				motorpwm4.set(.5);
				motorpwm1.set(-.5);
				motorpwm3.set(-.5);
				Timer.delay(0.4);
				motorpwm2.set(.5);
				motorpwm4.set(.5);
				motorpwm1.set(-.3);
				motorpwm3.set(-.3);
				Timer.delay(0.1);
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(0.6);
				motorpwm2.set(-.15);
				motorpwm4.set(-.15);
				motorpwm1.set(.15);
				motorpwm3.set(.15);
				Timer.delay(4);      //8.9 stop
			}
			
				// red (2), center
			else if(!din1.get())  { 
				System.out.print("din1");
				servoBallRelease.setAngle(10);
				motorpwm2.set(-0.2);
				motorpwm4.set(-0.2);
				motorpwm1.set(0.2);
				motorpwm3.set(0.2);
				Timer.delay(.2);  	 //.15		
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(.3);
				motorpwm2.set(-1);
				motorpwm4.set(-1);
				motorpwm1.set(1);
				motorpwm3.set(1);
				Timer.delay(.5);     //.65 forward 
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(.3);     //.95 ease
				motorpwm2.set(-0.2);
				motorpwm4.set(-0.2);
				motorpwm1.set(0.2);
				motorpwm3.set(0.2);
				Timer.delay(.36);    //1.3 ease
				motorpwm2.set(-.15);
				motorpwm4.set(-.15);
				motorpwm1.set(.15);
				motorpwm3.set(.15);
				Timer.delay(3);      //5.3 stop
				motorpwm2.set(.5);
				motorpwm4.set(.5);
				motorpwm1.set(-.5);
				motorpwm3.set(-.5);
				Timer.delay(0.3);    //backup & retry
				motorpwm2.set(.3);
				motorpwm4.set(.3);
				motorpwm1.set(-.5);
				motorpwm3.set(-.5);
				Timer.delay(0.15);    // backward, slight turn to the left
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(0.6);    // forward
				motorpwm2.set(-.15);
				motorpwm4.set(-.15);
				motorpwm1.set(.15);
				motorpwm3.set(.15);
				Timer.delay(3);
				motorpwm2.set(0.8);
				motorpwm4.set(0.8);
				motorpwm1.set(-0.8);
				motorpwm3.set(-0.8);
				Timer.delay(.5);     //5.8 backward
				if(din6.get()){
					motorpwm2.set(.5);
					motorpwm4.set(.5);
					motorpwm1.set(-.5);
					motorpwm3.set(-.5);
					Timer.delay(.4);     //6.2 ease
					motorpwm2.set(0.2);
					motorpwm4.set(0.2);
					motorpwm1.set(-0.2);
					motorpwm3.set(-0.2);
					Timer.delay(.35);    //6.55 ease
					motorpwm2.set(0);
					motorpwm4.set(0);
					motorpwm1.set(0);
					motorpwm3.set(0);
					Timer.delay(0.1);    //6.65 stop
					motorpwm2.set(-.3);
					motorpwm4.set(-.3);
					motorpwm1.set(.9);
					motorpwm3.set(.9);
					Timer.delay(0.8);    //7.45
					motorpwm2.set(-.8);
					motorpwm4.set(-.8);
					motorpwm1.set(.8);
					motorpwm3.set(.8);
					Timer.delay(0.83);    //8.25
					motorpwm2.set(-.3);
					motorpwm4.set(-.3);
					motorpwm1.set(.9);
					motorpwm3.set(.9);
					Timer.delay(0.25);    //8.5
					motorpwm2.set(-.8);
					motorpwm4.set(-.8);
					motorpwm1.set(.8);
					motorpwm3.set(.8);
					Timer.delay(0.1);    //8.6
					motorpwm2.set(.3);
					motorpwm4.set(.3);
					motorpwm1.set(-.3);
					motorpwm3.set(-.3);
					Timer.delay(.2);	 //8.9
					motorpwm2.set(-.5);
					motorpwm4.set(-.5);
					motorpwm1.set(-.5);
					motorpwm3.set(-.5);
					Timer.delay(0.9);
					motorpwm2.set(0.3);
					motorpwm2.set(0.3);
					motorpwm2.set(-0.3);
					motorpwm2.set(-0.3);
					Timer.delay(.5);
					servoBallRelease.setAngle(120);
					for(int i = 0; i < 40; i++){
						if(i % 2 == 0){
							motorpwm2.set(-.3);
							motorpwm4.set(-.3);
							motorpwm1.set(-.3);
							motorpwm3.set(-.3);
						}
						if(i % 2 == 1){
							motorpwm2.set(.3);
							motorpwm4.set(.3);
							motorpwm1.set(.3);
							motorpwm3.set(.3);
						}
						Timer.delay(0.1);
					}      //13.0 stop for 4
					motorpwm2.set(.6);
					motorpwm4.set(.6);
					motorpwm1.set(.6);
					motorpwm3.set(.6);
					Timer.delay(.16);    //13.16 turn in place left
					servoBallRelease.setAngle(10);
					motorpwm2.set(-1);
					motorpwm4.set(-1);
					motorpwm1.set(1);
					motorpwm3.set(1);
					Timer.delay(.7);    //13.86 forward
					motorpwm2.set(-0.2);
					motorpwm4.set(-0.2);
					motorpwm1.set(0.2);
					motorpwm3.set(0.2);
					Timer.delay(0.3);   //14.21 ease
					motorpwm2.set(0);
					motorpwm4.set(0);
					motorpwm1.set(0);
					motorpwm3.set(0);
				}
			}
			
				// red (3), right
			else if(!din2.get()) {    
				System.out.print("din2");
				servoBallRelease.setAngle(10);
				motorpwm2.set(-0.2);
				motorpwm4.set(-0.2);
				motorpwm1.set(0.2);
				motorpwm3.set(0.2);
				Timer.delay(.1);     // . . . ease
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(.1);     // . . . ease
				motorpwm2.set(-1);
				motorpwm4.set(-1);
				motorpwm1.set(1);
				motorpwm3.set(1);
				Timer.delay(.52);    //.58 forward
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(.4);     //.98 ease . . .
				motorpwm2.set(-0.1);
				motorpwm4.set(-0.1);
				motorpwm1.set(0.1);
				motorpwm3.set(0.1);
				Timer.delay(.33);    //1.31 ease . . .
				motorpwm2.set(-.4);
				motorpwm4.set(-.4);
				motorpwm1.set(-.4);
				motorpwm3.set(-.4);
				Timer.delay(.4);    //1.78 rotate left
				motorpwm2.set(-.7);
				motorpwm4.set(-.7);
				motorpwm1.set(.7);
				motorpwm3.set(.7);
				Timer.delay(.52);    //2.23 forward
				motorpwm2.set(-.15);
				motorpwm4.set(-.15);
				motorpwm1.set(.15);
				motorpwm3.set(.15);
				Timer.delay(3);      //6.23 stop at gear peg
				motorpwm2.set(.5);
				motorpwm4.set(.5);
				motorpwm1.set(-.5);
				motorpwm3.set(-.5);
				Timer.delay(0.3);    //backup & retry
				motorpwm2.set(.3);
				motorpwm4.set(.3);
				motorpwm1.set(-.5);
				motorpwm3.set(-.5);
				Timer.delay(0.1);    // backward, slight turn to the left
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(0.6);    // forward
				motorpwm2.set(-.15);
				motorpwm4.set(-.15);
				motorpwm1.set(.15);
				motorpwm3.set(.15);
				Timer.delay(3);      // stop at gear peg
				motorpwm2.set(0.7);
				motorpwm4.set(0.7);
				motorpwm1.set(-0.7);
				motorpwm3.set(-0.7);
				Timer.delay(0.5);     //6.83 backward
				if(din6.get()){
					motorpwm2.set(.4);
					motorpwm4.set(.4);
					motorpwm1.set(.4);
					motorpwm3.set(.4);
					Timer.delay(.4);     //rotate right
					motorpwm2.set(0);
					motorpwm4.set(0);
					motorpwm1.set(0);
					motorpwm3.set(0);
					Timer.delay(.1);     //pause
					motorpwm2.set(0.75);
					motorpwm4.set(0.75);
					motorpwm1.set(-0.75);
					motorpwm3.set(-0.75);
					Timer.delay(.8);   //backup
					motorpwm2.set(0);
					motorpwm4.set(0);
					motorpwm1.set(0);
					motorpwm3.set(0);
					Timer.delay(.1);     //pause
					motorpwm2.set(-.4);
					motorpwm4.set(-.4);
					motorpwm1.set(-.4);
					motorpwm3.set(-.4);
					Timer.delay(.45);   //rotate left
					motorpwm2.set(0);
					motorpwm4.set(0);
					motorpwm1.set(0);
					motorpwm3.set(0);
					Timer.delay(.1);    //pause
					motorpwm2.set(0.5);
					motorpwm4.set(0.5);
					motorpwm1.set(-0.5);
					motorpwm3.set(-0.5);
					Timer.delay(.65);   //backward
					motorpwm2.set(0.2);
					motorpwm4.set(0.2);
					motorpwm1.set(-0.4);
					motorpwm3.set(-0.4);
					Timer.delay(.7);   //backward, slight turn to the left
					servoBallRelease.setAngle(120);
					motorpwm2.set(.1);
					motorpwm4.set(.1);
					motorpwm1.set(-.1);
					motorpwm3.set(-.1);
					Timer.delay(2);
					for(int i = 0; i < 20; i++){
						if(i % 2 == 0){
							motorpwm2.set(-.3);
							motorpwm4.set(-.3);
							motorpwm1.set(-.3);
							motorpwm3.set(-.3);
						}
						if(i % 2 == 1){
							motorpwm2.set(.3);
							motorpwm4.set(.3);
							motorpwm1.set(.3);
							motorpwm3.set(.3);
						}
						Timer.delay(0.1); 
					}    
					servoBallRelease.setAngle(10);
				}
			}
			
				// blue (4), left
			else if(!din3.get()) {  
				System.out.print("din3");
				servoBallRelease.setAngle(10);
				motorpwm2.set(-0.2);
				motorpwm4.set(-0.2);
				motorpwm1.set(0.2);
				motorpwm3.set(0.2);
				Timer.delay(.1);     // . . . ease
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(.1);    // . . . ease
				motorpwm2.set(-1);
				motorpwm4.set(-1);
				motorpwm1.set(1);
				motorpwm3.set(1);
				Timer.delay(.52);    //.58 forward
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(.4);     //.98 ease . . .
				motorpwm2.set(-0.1);
				motorpwm4.set(-0.1);
				motorpwm1.set(0.1);
				motorpwm3.set(0.1);
				Timer.delay(.33);    //1.31 ease . . .
				motorpwm2.set(.4);
				motorpwm4.set(.4);
				motorpwm1.set(.4);
				motorpwm3.set(.4);
				Timer.delay(.4);    //1.78 rotate right
				motorpwm2.set(-.7);
				motorpwm4.set(-.7);
				motorpwm1.set(.7);
				motorpwm3.set(.7);
				Timer.delay(.52);    //2.23
				motorpwm2.set(-.15);
				motorpwm4.set(-.15);
				motorpwm1.set(.15);
				motorpwm3.set(.15);
				Timer.delay(3);      //6.23 stop at gear peg
				motorpwm2.set(.5);
				motorpwm4.set(.5);
				motorpwm1.set(-.5);
				motorpwm3.set(-.5);
				Timer.delay(0.4);    //backup & retry
				motorpwm2.set(.5);
				motorpwm4.set(.5);
				motorpwm1.set(-.3);
				motorpwm3.set(-.3);
				Timer.delay(0.1);    // backward, slight turn to the left
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(0.6);    // forward
				motorpwm2.set(-.15);
				motorpwm4.set(-.15);
				motorpwm1.set(.15);
				motorpwm3.set(.15);
				Timer.delay(3);      //6.23 stop
				motorpwm2.set(0.7);
				motorpwm4.set(0.7);
				motorpwm1.set(-0.7);
				motorpwm3.set(-0.7);
				Timer.delay(.5);
				if(din6.get()){
					motorpwm2.set(-.4);
					motorpwm4.set(-.4);
					motorpwm1.set(-.4);
					motorpwm3.set(-.4);
					Timer.delay(.4);
					motorpwm2.set(0);
					motorpwm4.set(0);
					motorpwm1.set(0);
					motorpwm3.set(0);
					Timer.delay(.1);
					motorpwm2.set(0.75);
					motorpwm4.set(0.75);
					motorpwm1.set(-0.75);
					motorpwm3.set(-0.75);
					Timer.delay(.8);
					motorpwm2.set(0);
					motorpwm4.set(0);
					motorpwm1.set(0);
					motorpwm3.set(0);
					Timer.delay(.1);
					motorpwm2.set(.4);
					motorpwm4.set(.4);
					motorpwm1.set(.4);
					motorpwm3.set(.4);
					Timer.delay(.45);
					motorpwm2.set(0);
					motorpwm4.set(0);
					motorpwm1.set(0);
					motorpwm3.set(0);
					Timer.delay(.1);
					motorpwm2.set(0.5);
					motorpwm4.set(0.5);
					motorpwm1.set(-0.5);
					motorpwm3.set(-0.5);
					Timer.delay(.65);
					motorpwm2.set(0.4);
					motorpwm4.set(0.4);
					motorpwm1.set(-0.2);
					motorpwm3.set(-0.2);
					Timer.delay(.7);
					servoBallRelease.setAngle(120);
					motorpwm2.set(.15);
					motorpwm4.set(.15);
					motorpwm1.set(-.15);
					motorpwm3.set(-.15);
					Timer.delay(2);
					for(int i = 0; i < 20; i++){
						if(i % 2 == 0){
							motorpwm2.set(-.3);
							motorpwm4.set(-.3);
							motorpwm1.set(-.3);
							motorpwm3.set(-.3);
						}
						if(i % 2 == 1){
							motorpwm2.set(.3);
							motorpwm4.set(.3);
							motorpwm1.set(.3);
							motorpwm3.set(.3);
						}
						Timer.delay(0.1);
					}      //13.2 stop for 4
					motorpwm2.set(-.6);
					motorpwm4.set(-.6);
					motorpwm1.set(-.6);
					motorpwm3.set(-.6);
					Timer.delay(.2);    //13.4 turn in place left
					servoBallRelease.setAngle(10);
					motorpwm2.set(-1);
					motorpwm4.set(-1);
					motorpwm1.set(1);
					motorpwm3.set(1);
					Timer.delay(.7);    //14.1
					motorpwm2.set(-.2);
					motorpwm4.set(-.2);
					motorpwm1.set(.2);
					motorpwm3.set(.2);
					Timer.delay(.3);    //14.74
					motorpwm2.set(0);
					motorpwm4.set(0);
					motorpwm1.set(0);
					motorpwm3.set(0); 
				}
			}
			
				// blue (5), center
			else if(!din4.get()) {
				System.out.print("din4");
				servoBallRelease.setAngle(10);
				motorpwm2.set(-0.2);
				motorpwm4.set(-0.2);
				motorpwm1.set(0.2);
				motorpwm3.set(0.2);
				Timer.delay(.2);    //.15	
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(.3);
				motorpwm2.set(-0.5);
				motorpwm4.set(-0.5);
				motorpwm1.set(0.5);
				motorpwm3.set(0.5);
				Timer.delay(.9);     //.65 forward 
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(.3);     //.95 ease
				motorpwm2.set(-0.2);
				motorpwm4.set(-0.2);
				motorpwm1.set(0.2);
				motorpwm3.set(0.2);
				Timer.delay(.36);    //1.3 ease
				motorpwm2.set(-.15);
				motorpwm4.set(-.15);
				motorpwm1.set(.15);
				motorpwm3.set(.15);
				Timer.delay(3);      //5.3 stop
				motorpwm2.set(.5);
				motorpwm4.set(.5);
				motorpwm1.set(-.5);
				motorpwm3.set(-.5);
				Timer.delay(0.3);    //backup & retry
				motorpwm2.set(.3);
				motorpwm4.set(.3);
				motorpwm1.set(-.5);
				motorpwm3.set(-.5);
				Timer.delay(0.15);    // backward, slight turn to the left
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(0.6);    // forward
				motorpwm2.set(-.15);
				motorpwm4.set(-.15);
				motorpwm1.set(.15);
				motorpwm3.set(.15);
				Timer.delay(3);
				motorpwm2.set(0.8);
				motorpwm4.set(0.8);
				motorpwm1.set(-0.8);
				motorpwm3.set(-0.8);
				Timer.delay(.5);     //5.8 backward
				motorpwm2.set(.5);
				motorpwm4.set(.5);
				motorpwm1.set(-.5);
				motorpwm3.set(-.5);
				Timer.delay(.4);     //6.2
				if(din6.get()){
					motorpwm2.set(0.2);
					motorpwm4.set(0.2);
					motorpwm1.set(-0.2);
					motorpwm3.set(-0.2);
					Timer.delay(.35);    //6.55
					motorpwm2.set(0);
					motorpwm4.set(0);
					motorpwm1.set(0);
					motorpwm3.set(0);
					Timer.delay(0.1);    //6.65
					motorpwm2.set(-.9);
					motorpwm4.set(-.9);
					motorpwm1.set(.3);
					motorpwm3.set(.3);
					Timer.delay(0.8);    //7.45
					motorpwm2.set(-.8);
					motorpwm4.set(-.8);
					motorpwm1.set(.8);
					motorpwm3.set(.8);
					Timer.delay(0.83);    //8.25
					motorpwm2.set(-.9);
					motorpwm4.set(-.9);
					motorpwm1.set(.3);
					motorpwm3.set(.3);
					Timer.delay(0.25);    //8.5
					motorpwm2.set(-.8);
					motorpwm4.set(-.8);
					motorpwm1.set(.8);
					motorpwm3.set(.8);
					Timer.delay(0.1);    //8.6
					motorpwm2.set(.3);
					motorpwm4.set(.3);
					motorpwm1.set(-.3);
					motorpwm3.set(-.3);
					Timer.delay(.2);	 //8.9
					motorpwm2.set(.5);
					motorpwm4.set(.5);
					motorpwm1.set(.5);
					motorpwm3.set(.5);
					Timer.delay(.9);
					motorpwm2.set(0.3);
					motorpwm2.set(0.3);
					motorpwm2.set(-0.3);
					motorpwm2.set(-0.3);
					Timer.delay(.5);
					servoBallRelease.setAngle(120);
					for(int i = 0; i < 40; i++){
						if(i % 2 == 0){
							motorpwm2.set(-.3);
							motorpwm4.set(-.3);
							motorpwm1.set(-.3);
							motorpwm3.set(-.3);
						}
						if(i % 2 == 1){
							motorpwm2.set(.3);
							motorpwm4.set(.3);
							motorpwm1.set(.3);
							motorpwm3.set(.3);
						}
						Timer.delay(0.1);
					}
					motorpwm2.set(-.6);
					motorpwm4.set(-.6);
					motorpwm1.set(-.6);
					motorpwm3.set(-.6);
					Timer.delay(.16);    //13.06
					servoBallRelease.setAngle(10);
					motorpwm2.set(-1);
					motorpwm4.set(-1);
					motorpwm1.set(1);
					motorpwm3.set(1);
					Timer.delay(.7);    //13.76
					motorpwm2.set(-0.2);
					motorpwm4.set(-0.2);
					motorpwm1.set(0.2);
					motorpwm3.set(0.2);
					Timer.delay(0.3);    //14.26
					motorpwm2.set(0);
					motorpwm4.set(0);
					motorpwm1.set(0);
					motorpwm3.set(0);	
				}
			}
				
				// blue (6), right
			else if(!din5.get()) {  
				System.out.print("din5");
				servoBallRelease.setAngle(10);
				motorpwm2.set(-0.2);
				motorpwm4.set(-0.2);
				motorpwm1.set(0.2);
				motorpwm3.set(0.2);
				Timer.delay(.1);
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(.1);
				motorpwm2.set(-1);
				motorpwm4.set(-1);
				motorpwm1.set(1);
				motorpwm3.set(1);
				Timer.delay(.52);    //.58 forward
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(.4);     //0.98 ease
				motorpwm2.set(-0.1);
				motorpwm4.set(-0.1);
				motorpwm1.set(0.1);
				motorpwm3.set(0.1);
				Timer.delay(.33);    //1.31 ease
				motorpwm2.set(-.4);
				motorpwm4.set(-.4);
				motorpwm1.set(-.4);
				motorpwm3.set(-.4);
				Timer.delay(.4);    //1.78 rotate
				motorpwm2.set(-.7);
				motorpwm4.set(-.7);
				motorpwm1.set(.7);
				motorpwm3.set(.7);
				Timer.delay(.52);    //6.28
				motorpwm2.set(-.15);
				motorpwm4.set(-.15);
				motorpwm1.set(.15);
				motorpwm3.set(.15);
				Timer.delay(4);      //10.28 stop
				motorpwm2.set(.5);
				motorpwm4.set(.5);
				motorpwm1.set(-.5);
				motorpwm3.set(-.5);
				Timer.delay(0.4);
				motorpwm2.set(.3);
				motorpwm4.set(.3);
				motorpwm1.set(-.5);
				motorpwm3.set(-.5);
				Timer.delay(0.1);
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(0.6);
				motorpwm2.set(-.15);
				motorpwm4.set(-.15);
				motorpwm1.set(.15);
				motorpwm3.set(.15);
				Timer.delay(4);
			}
		
			else {
				System.out.print("default");
				servoBallRelease.setAngle(10);
				motorpwm2.set(-1);
				motorpwm4.set(-1);
				motorpwm1.set(1);
				motorpwm3.set(1);
				Timer.delay(.5);     //.5 forward 
				motorpwm2.set(-.5);
				motorpwm4.set(-.5);
				motorpwm1.set(.5);
				motorpwm3.set(.5);
				Timer.delay(.4);     //.9 ease
				motorpwm2.set(-0.2);
				motorpwm4.set(-0.2);
				motorpwm1.set(0.2);
				motorpwm3.set(0.2);
				Timer.delay(.35);    //1.25 ease
				motorpwm2.set(0);
				motorpwm4.set(0);
				motorpwm1.set(0);
				motorpwm3.set(0);
				Timer.delay(13.65);  // 15 stop
				
			}		
			
			
			//red, left with Sensors
//				motorpwm2.set(-0.2);
//				motorpwm4.set(-0.2);
//				motorpwm1.set(0.2);
//				motorpwm3.set(0.2);
//				Timer.delay(.1);
//				motorpwm2.set(-.5);
//				motorpwm4.set(-.5);
//				motorpwm1.set(.5);
//				motorpwm3.set(.5);
//				Timer.delay(.1);
//				motorpwm2.set(-1);
//				motorpwm4.set(-1);
//				motorpwm1.set(1);
//				motorpwm3.set(1);
//				Timer.delay(.52);    //.58 forward
//				motorpwm2.set(-.5);
//				motorpwm4.set(-.5);
//				motorpwm1.set(.5);
//				motorpwm3.set(.5);
//				Timer.delay(.4);     //.98 ease
//				motorpwm2.set(-0.1);
//				motorpwm4.set(-0.1);
//				motorpwm1.set(0.1);
//				motorpwm3.set(0.1);
//				Timer.delay(.33);    //1.31 ease
//				motorpwm2.set(.4);
//				motorpwm4.set(.4);
//				motorpwm1.set(.4);
//				motorpwm3.set(.4);
//				Timer.delay(.44);    //1.78 rotate right
//				motorpwm2.set(-.7);
//				motorpwm4.set(-.7);
//				motorpwm1.set(.7);
//				motorpwm3.set(.7);
//				Timer.delay(.45);    //2.23
//				motorpwm2.set(0);
//				motorpwm4.set(0);
//				motorpwm1.set(0);
//				motorpwm3.set(0);
//				Timer.delay(1);
//				if(photoElectric.get()){    //left Sensor is true, backup and go left
//					motorpwm2.set(0.8);
//					motorpwm4.set(0.8);
//					motorpwm1.set(-0.73);
//					motorpwm3.set(-0.73);
//					Timer.delay(.5);
//					motorpwm2.set(-0.8);
//					motorpwm4.set(-0.8);
//					motorpwm1.set(0.9);
//					motorpwm3.set(0.9);
//					Timer.delay(.5);
//					motorpwm2.set(0);
//					motorpwm4.set(0);
//					motorpwm1.set(0);
//					motorpwm3.set(0);
//					Timer.delay(8.33);
//				}		
//				else if(photoElectric2.get()){      //right Sensor is true, backup and go right
//					motorpwm2.set(0.73);
//					motorpwm4.set(0.73);
//					motorpwm1.set(-0.8);
//					motorpwm3.set(-0.8);
//					Timer.delay(.5);
//					motorpwm2.set(-0.9);
//					motorpwm4.set(-0.9);
//					motorpwm1.set(0.8);
//					motorpwm3.set(0.8);
//					Timer.delay(.5);
//					motorpwm2.set(0);
//					motorpwm4.set(0);
//					motorpwm1.set(0);
//					motorpwm3.set(0);
//					Timer.delay(8.33);
//				}
//				else{								//neither sensor see the peg
//					motorpwm2.set(0);
//					motorpwm4.set(0);
//					motorpwm1.set(0);
//					motorpwm3.set(0);
//					Timer.delay(9.33);
//				}
//				motorpwm2.set(0);
//				motorpwm4.set(0);
//				motorpwm1.set(0);
//				motorpwm3.set(0);
//				Timer.delay(6);      //8.9 stop
//				motorpwm2.set(0.76);
//				motorpwm4.set(0.76);
//				motorpwm1.set(-0.8);
//				motorpwm3.set(-0.8);
//				Timer.delay(1.2);     //10.1 forward
//				motorpwm2.set(-.73);
//				motorpwm4.set(-.73);
//				motorpwm1.set(.77);
//				motorpwm3.set(.77);
//				Timer.delay(.4);	//10.5 backward
//				motorpwm2.set(0);
//				motorpwm4.set(0);
//				motorpwm1.set(0);
//				motorpwm3.set(0);
//				Timer.delay(0.5);      //11.0
//				motorpwm2.set(-.6);
//				motorpwm4.set(-.6);
//				motorpwm1.set(-.6);
//				motorpwm3.set(-.6);
//				Timer.delay(.2);    //11.2
//				motorpwm2.set(-1);
//				motorpwm4.set(-1);
//				motorpwm1.set(1);
//				motorpwm3.set(1);
//				Timer.delay(.7);    //11.9
//				motorpwm2.set(-.2);
//				motorpwm4.set(-.2);
//				motorpwm1.set(.2);
//				motorpwm3.set(.2);
//				Timer.delay(.3);    //13.2
//				motorpwm2.set(0);
//				motorpwm4.set(0);
//				motorpwm1.set(0);
//				motorpwm3.set(0);
		}
		runAuto = false;
		motorpwm2.set(0);
		motorpwm4.set(0);
		motorpwm1.set(0);
		motorpwm3.set(0);
	}

	
	
	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		runAuto = true;
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		//SmartDashboard.putData("IMU", imu);
		runAuto = true;
		/**
		 * X-Box controller drive
		 * 
		 * motorpwm2.set(rStick.getRawAxis(5));
		 * motorpwm4.set(rStick.getRawAxis(5));
		 * motorpwm1.set(lStick.getRawAxis(2));
		 * motorpwm3.set(lStick.getRawAxis(2));
		 */
		
		    //Single Joystick Drive, TURBO BOOST MODE
		if(!mainStick.getRawButton(5)){
			motorpwm2.set((-mainStick.getX()*2+1)*mainStick.getY());
			motorpwm4.set((-mainStick.getX()*2+1)*mainStick.getY());
			motorpwm1.set((-mainStick.getX()*2-1)*mainStick.getY());
			motorpwm3.set((-mainStick.getX()*2-1)*mainStick.getY());
		}
		
		else{
			
			if((-mainStick.getX()*2+1)*mainStick.getY() > 1)
			{
				driveRight = 0.5;
			}
		
			else if((-mainStick.getX()*2+1)*mainStick.getY() < -1)
			{
				driveRight = -0.5;
			}
				
			else {
				driveRight = ((-mainStick.getX()*2+1)*mainStick.getY())/2;
			}
			
			
			if((-mainStick.getX()*2-1)*mainStick.getY() > 1)
			{
				driveLeft = 0.5;
			}
		
			else if((-mainStick.getX()*2-1)*mainStick.getY() < -1)
			{
				driveLeft = -0.5;
			}
				
			else {
				driveLeft = ((-mainStick.getX()*2-1)*mainStick.getY())/2;
			}
			
			
			motorpwm2.set(driveRight);
			motorpwm4.set(driveRight);
			motorpwm1.set(driveLeft);
			motorpwm3.set(driveLeft);
		}
		
		
		//Lifting mechanism
		if(auxStick.getRawButton(6)){
			climber.set(Math.abs(auxStick.getY()));
		}
		else{
			climber.set(0);
		}
		
		
		//ball release mechanism
		if(auxStick.getRawButton(3)){
			servoBallRelease.setAngle(120);
		}
		else{
			servoBallRelease.setAngle(10);
		}
		
		/*
		//speed 75%
	if(mainStick.getRawButton(10)){
			motorpwm2.set(.50);
			motorpwm4.set(.50);
			motorpwm1.set(-.50);
			motorpwm3.set(-.50);
	}   */
	
	
	// camera1 position, up & down
	if(auxStick.getRawButton(11))
	 	{
		 servo0.setAngle(30);
	 	}
	 else if(auxStick.getRawButton(10))
		 {
		  servo0.setAngle(70);
		 }
	 else
		 {
		  servo0.setAngle(40);
		 } 	 	
	
	// switch between camera1 and camera2
	if(auxStick.getTrigger() && !ropeCamera){
		//NetworkTable.getTable("").putString("Camera Choice", "USB Camera 1");
		server.setSource(cam1);
		ropeCamera = true;
	}
	else if(auxStick.getTrigger() && ropeCamera){
		//NetworkTable.getTable("").putString("Camera Choice", "USB Camera 0");
		server.setSource(cam0);
		ropeCamera = false;
	}
	 
		/*	 //Double Joystick Drive
		
		 motorpwm2.set(lStick.getY());
		 motorpwm4.set(lStick.getY());
		 motorpwm1.set(-rStick.getY());
		 motorpwm3.set(-rStick.getY());
		 
		 if(lStick.getRawButton(3)){
			 motorpwm2.set(-.25);
			 motorpwm4.set(-.25);
			 motorpwm1.set(.25);
			 motorpwm3.set(.25);
			 Timer.delay(5);
			 motorpwm2.set(0);
			 motorpwm4.set(0);
			 motorpwm1.set(0);
			 motorpwm3.set(0);
		 }
		 if(lStick.getRawButton(4)){
			 motorpwm2.set(-.50);
			 motorpwm4.set(-.50);
			 motorpwm1.set(.50);
			 motorpwm3.set(.50);
			 Timer.delay(5);
			 motorpwm2.set(0);
			 motorpwm4.set(0);
			 motorpwm1.set(0);
			 motorpwm3.set(0);
		 }
		 if(lStick.getRawButton(5)){
			 motorpwm2.set(-.75);
			 motorpwm4.set(-.75);
			 motorpwm1.set(.75);
			 motorpwm3.set(.75);
			 Timer.delay(3);
			 motorpwm2.set(0);
			 motorpwm4.set(0);
			 motorpwm1.set(0);
			 motorpwm3.set(0);
		 }
		 if(lStick.getRawButton(6)){
			 motorpwm2.set(-1);
			 motorpwm4.set(-1);
			 motorpwm1.set(1);
			 motorpwm3.set(1);
			 Timer.delay(2);
			 motorpwm2.set(0);
			 motorpwm4.set(0);
			 motorpwm1.set(0);
			 motorpwm3.set(0);
		 }
		 
		 
		 */

		 // Single Joystick (This was not worth my time, I should have been doing something better with my life)
			
		
		
		
		/* if(rStick.getZ() > 0){
			 motorpwm2.set(rStick.getZ());
			 motorpwm4.set(rStick.getZ());
			 motorpwm1.set(-rStick.getZ());
			 motorpwm3.set(-rStick.getZ());
		 }
		 if(rStick.getZ() < 0){
			 motorpwm2.set(-rStick.getZ());
			 motorpwm4.set(-rStick.getZ());
			 motorpwm1.set(rStick.getZ());
			 motorpwm3.set(rStick.getZ());
		 }
		 if(rStick.getY() > 0){
			 if (rStick.getX() > 0){
				 motorpwm2.set(rStick.getY() - Math.abs(rStick.getY()*rStick.getX()));
				 motorpwm4.set(rStick.getY() - Math.abs(rStick.getY()*rStick.getX()));
				 motorpwm1.set(rStick.getY());
				 motorpwm3.set(rStick.getY());
			 }
			 else if (rStick.getX() < 0){
				 motorpwm2.set(rStick.getY());
				 motorpwm4.set(rStick.getY());
				 motorpwm1.set(rStick.getY() - Math.abs(rStick.getY()*rStick.getX()));
				 motorpwm3.set(rStick.getY() - Math.abs(rStick.getY()*rStick.getX()));
			 }
			 else{
				 motorpwm2.set(rStick.getY());
				 motorpwm4.set(rStick.getY());
				 motorpwm1.set(rStick.getY());
				 motorpwm3.set(rStick.getY());
			 }
		 }
		 if(rStick.getY() > 0){
			 if (rStick.getX() > 0){
				 motorpwm2.set(rStick.getY() - Math.abs(rStick.getY()*rStick.getX()));
				 motorpwm4.set(rStick.getY() - Math.abs(rStick.getY()*rStick.getX()));
				 motorpwm1.set(rStick.getY());
				 motorpwm3.set(rStick.getY());
			 }
			 else if (rStick.getX() < 0){
				 motorpwm2.set(rStick.getY());
				 motorpwm4.set(rStick.getY());
				 motorpwm1.set(-(Math.abs(rStick.getY()) - Math.abs(rStick.getY()*rStick.getX())));
				 motorpwm3.set(-(Math.abs(rStick.getY()) - Math.abs(rStick.getY()*rStick.getX())));
			 }
			 else{
				 motorpwm2.set(rStick.getY());
				 motorpwm4.set(rStick.getY());
				 motorpwm1.set(rStick.getY());
				 motorpwm3.set(rStick.getY());
			 }
		 } */
		 
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
		runAuto = true;
	}
}
