package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;

public class IntakeSystem implements Component{

    private final Talon motorTop = new Talon(3);
    private final Joystick auxStick;
    private final DoubleSolenoid intake = new DoubleSolenoid(1, 2);
    private static final DigitalInput oDown = new DigitalInput(14);
    private static final Servo oServo = new Servo(4);
    private static boolean ringIntent = false;
    public IntakeSystem(Joystick aux){
        this.auxStick = aux;
    }

    public void tickTeleop() {
        System.out.println("oDown Triggered: "+oDown.get());
        servoTick();
        if(auxStick.getRawButton(3))
            intake.set(DoubleSolenoid.Value.kForward);
        if(auxStick.getRawButton(4))
            intakeBall();
        else if (auxStick.getRawButton(6))
            motorTop.set(1);
        else if (auxStick.getRawButton(5))
            motorTop.set(-1);
        else
            motorTop.set(0);
    }

    public void intakeBall() {
        intake.set(DoubleSolenoid.Value.kReverse);
        motorTop.set(0.5);
        IntakeSystem.setRingIntent(true);
    }
    
    public void tickAuto() {
        servoTick();
        final double get = RobotTemplate.self.getAutonomousTimer().get();
        if (get < 2) {
            intake.set(DoubleSolenoid.Value.kForward);
            motorTop.set(1);
        } else if (get < 5) {
            if(Ultrasonic.getDistanceFromWall() < 2){
                intakeBall();
            }
        } else {
            intake.set(DoubleSolenoid.Value.kForward);
            motorTop.set(0);
            RobotTemplate.self.setSafeToFire();
        }       
    }
    private static void servoTick() {
        if(ringIntent && isODown() )
            oServo.set(1);
        else if (!ringIntent)
            oServo.set(0);
    }
    public static boolean isRingIntent() {
        return ringIntent;
    }
    public static void setRingIntent(boolean aRingIntent) {
        ringIntent = aRingIntent;
        IntakeSystem.servoTick();
    }
    public static boolean isODown() {
        return oDown.get();
    }
    public static double getOServoValue(){
        return oServo.get();
    }
}