package org.firstinspires.ftc.teamcode.FSL.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.control.StateMachine;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.ShooterConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.control.ShooterReadyProvider;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.ShotPos;

public class NationalsShooter implements ShooterReadyProvider {
    public final DcMotorEx motor;
    private final Servo servo;
    private final Servo blocker;
    private final Telemetry telemetry;
    private StateMachine.ShooterStates currentState;
    private final ElapsedTime timer;
    private double targetVel;

    public NationalsShooter(HardwareMap hm, Telemetry telemetry) {
        motor = hm.get(DcMotorEx.class, "SHM");
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
        motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,new PIDFCoefficients(ShooterConfig.KP, 0 , 0, ShooterConfig.KF));
        servo = hm.get(Servo.class, "SHS");
        servo.setPosition(0.5);
        blocker = hm.get(Servo.class, "BLK");
        blocker.setPosition(0.5);

        targetVel = 0;
        currentState = StateMachine.ShooterStates.OFF;

        timer = new ElapsedTime();

        this.telemetry = telemetry;
    }
    public void update(){
        switch(currentState){
            case OFF:
                motor.setVelocity(0);
                setBlocker(ShooterConfig.BLOCKER_CLOSED);
                break;
            case WARMING_UP:
                motor.setVelocity(targetVel);
                setBlocker(ShooterConfig.BLOCKER_CLOSED);
                if(isWarmedUp()) currentState = StateMachine.ShooterStates.ON;
                break;
            case ON:
                motor.setVelocity(targetVel);
                break;
        }
    }
    public void autoUpdate(){
        switch(currentState){
            case OFF:
                motor.setVelocity(0);
                setBlocker(ShooterConfig.BLOCKER_CLOSED);
                break;
            case WARMING_UP:
                motor.setVelocity(targetVel);
                setBlocker(ShooterConfig.BLOCKER_CLOSED);
                if(isWarmedUp()) currentState = StateMachine.ShooterStates.ON;
                break;
            case ON:
                motor.setVelocity(targetVel);
                setBlockerOpen();
                break;
        }
    }
    public void setBlockerOpen(){
        if(isWarmedUp()){
            setBlocker(ShooterConfig.BLOCKER_OPEN);
            timer.reset();
        }
    }
    public void setBlocker(double pos){
        blocker.setPosition(pos);
    }
    public void prepareForShot(double range){
        double vel = calculateVelocity(range);
        double pos = calculateServoPos(range);

        fire(vel);
        setServo(pos);
    }
    public void prepareForShot(ShotPos shotPos){
        switch (shotPos){
            case LAYUP:
                fire(ShooterConfig.MOTOR_VEL_SCALAR_FOR_LAYUP);
                setServo(ShooterConfig.SERVO_POS_FOR_LAYUP);
                break;
            case TIP_OF_TRIANGLE:
                fire(ShooterConfig.MOTOR_VEL_SCALAR_FOR_TIP);
                setServo(ShooterConfig.SERVO_POS_FOR_TIP);
                break;
            case THREE_POINTER:
                fire(ShooterConfig.MOTOR_VEL_SCALAR_FOR_3_POINTER);
                setServo(ShooterConfig.SERVO_POS_FOR_3_POINTER);
                break;
        }
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
        return 0.01 * range;
    }
    public double calculateVelocity(double range){
        return 10 * range;
    }
    @Override
    public boolean isShooterReady() {
        return currentState == StateMachine.ShooterStates.ON && isWarmedUp() && blocker.getPosition() == ShooterConfig.BLOCKER_OPEN;
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
        telemetry.addData("WARMED UP", isShooterReady());
        telemetry.addData("% OF TARGET", (100 * motor.getVelocity())/(targetVel));
        telemetry.update();
    }
}