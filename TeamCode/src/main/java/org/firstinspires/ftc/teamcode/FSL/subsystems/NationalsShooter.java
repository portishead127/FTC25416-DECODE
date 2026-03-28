package org.firstinspires.ftc.teamcode.FSL.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.StateMachine;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.ShooterConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.control.PIDController;
import org.firstinspires.ftc.teamcode.FSL.helper.control.ShooterReadyProvider;

public class NationalsShooter implements ShooterReadyProvider {
    public final DcMotorEx motor;
    private final Servo servo;
    private final Telemetry telemetry;
    private StateMachine.ShooterStates currentState;
    private double targetVel;

    public NationalsShooter(HardwareMap hm, Telemetry telemetry) {
        motor = hm.get(DcMotorEx.class, "SHM");
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
        motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,new PIDFCoefficients(ShooterConfig.KP, 0 , 0, ShooterConfig.KF));
        servo = hm.get(Servo.class, "SHS");
        servo.setPosition(0.5);

        targetVel = 0;
        currentState = StateMachine.ShooterStates.OFF;

        this.telemetry = telemetry;
    }
    public void update(){
        switch(currentState){
            case OFF:
                motor.setVelocity(0);
                break;
            case WARMING_UP:
                motor.setVelocity(targetVel);
                if(isWarmedUp()) currentState = StateMachine.ShooterStates.ON;
                break;
            case ON:
                motor.setVelocity(targetVel);
                break;
        }
    }
    public void prepareForShot(double range){
        double vel = calculateVelocity(range);
        double pos = calculateServoPos(range);

        fire(targetVel);
        setServo(pos);
    }
    public void fire(double target) {
        targetVel = target;
        if(currentState == StateMachine.ShooterStates.OFF){
            currentState = StateMachine.ShooterStates.WARMING_UP;
        }
    }
    public void stop(){
        targetVel = 0;
        currentState = StateMachine.ShooterStates.OFF;
    }
    public void setServo(double pos){
        double servoPos = Math.max(0.0, Math.min(1.0, pos));
        servo.setPosition(servoPos);
    }
    public double calculateServoPos(double range){
        return 0.9;
    }
    public double calculateVelocity(double range){
        return 1900;
    }
    public boolean isOff(){
        return targetVel == 0 && currentState == StateMachine.ShooterStates.OFF;
    }
    @Override
    public boolean isShooterReady() {
        return currentState == StateMachine.ShooterStates.ON && isWarmedUp();
    }
    private boolean isWarmedUp(){
        if (targetVel <= 0) return false;
        double currentVelocity = motor.getVelocity();
        return currentVelocity >= (targetVel * ShooterConfig.WARM_UP_THRESHOLD);
    }
    public void sendTelemetry() {
        telemetry.addLine("SHOOTER\n");
        telemetry.addData("SERVO POS", servo.getPosition());
        telemetry.addData("MOTOR VEL", motor.getVelocity());
        telemetry.addData("TARGET VEL", targetVel);
        telemetry.addData("WARMED UP", isWarmedUp());
        telemetry.addData("% OF TARGET", (100 * motor.getVelocity())/(targetVel));
    }
}