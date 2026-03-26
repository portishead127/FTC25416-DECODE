package org.firstinspires.ftc.teamcode.FSL.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.ShooterConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.control.PIDController;

public class Shooter {
    public final DcMotorEx motor;
    private final Servo servo;
    private final Telemetry telemetry;
    private double target;
    public final PIDController pidController;
    //servo - 0 - steep
    //1 - shallow



    public Shooter(HardwareMap hm, Telemetry telemetry) {
        motor = hm.get(DcMotorEx.class, "SHM");
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);

        motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,new PIDFCoefficients(ShooterConfig.KP, 0 , 0, ShooterConfig.KF));

        servo = hm.get(Servo.class, "SHS");
        servo.setPosition(0.5);

        pidController = new PIDController(ShooterConfig.KP, ShooterConfig.KI, ShooterConfig.KD, ShooterConfig.KF);
        pidController.setOutputLimits(-1,1);
        this.telemetry = telemetry;
    }

    public void simpleUpdate(boolean queueEmpty, double range){
        boolean in3Pointer = range > ShooterConfig.LIMIT_FOR_3_POINTER_RANGE;
        if(in3Pointer){
            servo.setPosition(ShooterConfig.SERVO_POS_FOR_3_POINTER);
        }
        else{
            servo.setPosition(ShooterConfig.SERVO_POS_FOR_LAYUP);
        }

        if(!queueEmpty){
            if(in3Pointer){
                target = ShooterConfig.MOTOR_VEL_SCALAR_FOR_3_POINTER;
            }
            else{
                target = ShooterConfig.MOTOR_VEL_SCALAR_FOR_LAYUP;
            }
        }
        else{
            target = 0;
        }
        pidController.setTarget(target);
        motor.setVelocity(pidController.calculate(motor.getVelocity()));
    }

    public void staticUpdate(boolean queueEmpty, double range){
        double servoPos = calculateServoPos(range);
        setServo(servoPos);
        if(!queueEmpty){
            target = calculateVelocity(range);
        }
        else{
            target = 0;
        }
        motor.setVelocity(target);
//        sendTelemetry();
    }
    public void dynamicUpdate(boolean queueEmpty, double range, double robotVelAlongShot){
        double dynamicRange = range + robotVelAlongShot * calculateFlightTime(range);
        double servoPos = calculateServoPos(dynamicRange);


        setServo(servoPos);
        if(!queueEmpty){
            target = calculateVelocity(dynamicRange);
        }
        else{
            target = 0;
        }
//        pidController.setTarget(target);
//        motor.setVelocity(pidController.calculate(motor.getVelocity()));
        sendTelemetry();
    }
    public boolean isWarmedUp() {
        double currentVelocity = motor.getVelocity();  // ticks per second (actual measured)

        // True if within 90% or better, with a small epsilon to stabilize near boundary
        return currentVelocity >= (target * ShooterConfig.WARM_UP_THRESHOLD) - ShooterConfig.VELOCITY_EPSILON;
    }

    public void fire(double target) {
        this.target = target;
        motor.setVelocity(target);
    }
    public void setServo(double pos){
        servo.setPosition(pos);
    }
    public double calculateFlightTime(double range){
        //GET FORMULA
        return 0;
    }
    public double calculateServoPos(double range){
        //GET FORMULA FOR THIS... SEE SHOOTERTEST
        //E.G Angle = 64 - 0.4 * range
        return 0;
    }
    public double calculateVelocity(double range){
        //GET FORMULA FOR THIS... SEE SHOOTERTEST
        //E.G Velocity = 2692 - 10 * range + 0.32 * range^2
        return 1500;
    }
    public void sendTelemetry() {
        telemetry.addLine("SHOOTER\n");
        telemetry.addData("SERVO POS", servo.getPosition());
        telemetry.addData("MOTOR VEL", motor.getVelocity());
        telemetry.addData("TARGET VEL", target);
        telemetry.addData("WARMED UP", isWarmedUp());
        telemetry.addData("% OF TARGET", (100 * motor.getVelocity())/(target));
    }
}