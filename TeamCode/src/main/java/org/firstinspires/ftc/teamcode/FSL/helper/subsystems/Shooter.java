package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.ShooterConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.constants.UltraplanetaryMotorConstants;

public class Shooter {
    private final DcMotorEx motor;
    private final Servo servo;
    private final Telemetry telemetry;
    private double targetScalar;

    public Shooter(HardwareMap hm, Telemetry telemetry) {
        motor = hm.get(DcMotorEx.class, "SHM");
        servo = hm.get(Servo.class, "SHS");
        servo.setPosition(0.5);
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
                targetScalar = ShooterConfig.MOTOR_VEL_SCALAR_FOR_3_POINTER;
            }
            else{
                targetScalar = ShooterConfig.MOTOR_VEL_SCALAR_FOR_LAYUP;
            }
        }
        else{
            targetScalar = 0;
        }
        motor.setVelocity(UltraplanetaryMotorConstants.MAX_VELOCITY * targetScalar);
        sendTelemetry();
    }

    public void update(boolean queueEmpty, double range){
        double servoPos = calculateServoPos(range);
        setServo(servoPos);
        if(!queueEmpty){
            targetScalar = calculateVelocity(range);
        }
        else{
            targetScalar = 0;
        }
        motor.setVelocity(UltraplanetaryMotorConstants.MAX_VELOCITY * targetScalar);
        sendTelemetry();
    }
    public boolean isWarmedUp() {
        double currentVelocity = motor.getVelocity();  // ticks per second (actual measured)
        double target = UltraplanetaryMotorConstants.MAX_VELOCITY * targetScalar;

        // True if within 90% or better, with a small epsilon to stabilize near boundary
        return currentVelocity >= (target * ShooterConfig.WARM_UP_THRESHOLD) - ShooterConfig.VELOCITY_EPSILON;
    }

    public void fire(double scalar) {
        motor.setVelocity(scalar * UltraplanetaryMotorConstants.MAX_VELOCITY);
    }
    public void stop() {
        fire(0);
    }
    public void setServo(double pos){
        servo.setPosition(pos);
    }

    public double calculateServoPos(double range){
        //GET FORMULA FOR THIS... SEE SHOOTERTEST
        //E.G Angle = 64 - 0.4 * range
        return 0;
    }
    public double calculateVelocity(double range){
        //GET FORMULA FOR THIS... SEE SHOOTERTEST
        //E.G Velocity = 2692 - 10 * range + 0.32 * range^2
        return 0;
    }
    public void sendTelemetry() {
        telemetry.addLine("SHOOTER\n");
        telemetry.addData("SERVO POS", servo.getPosition());
        telemetry.addData("MOTOR VEL", motor.getVelocity());
        telemetry.addData("TARGET SCALAR", targetScalar);
        telemetry.addData("TARGET VEL", targetScalar * UltraplanetaryMotorConstants.MAX_VELOCITY);
        telemetry.addData("WARMED UP", isWarmedUp());
        telemetry.addData("% OF TARGET", (100 * motor.getVelocity())/(targetScalar * UltraplanetaryMotorConstants.MAX_VELOCITY));
    }
}