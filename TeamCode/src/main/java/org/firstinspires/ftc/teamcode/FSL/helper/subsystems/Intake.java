package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Intake{
    private static double MOTOR1_MAX_TICKS_PER_SECOND;
    private final DcMotorEx motor1;
    private final Telemetry telemetry;
    public Intake(HardwareMap hm, Telemetry telemetry){
        motor1 = hm.get(DcMotorEx.class, "INM");
        this.telemetry = telemetry;
        MOTOR1_MAX_TICKS_PER_SECOND = motor1.getMotorType().getAchieveableMaxTicksPerSecond();
    }
    public void run(boolean forward){
        if(forward){
            motor1.setVelocity(MOTOR1_MAX_TICKS_PER_SECOND);
        }
        else{
            motor1.setVelocity(0.2 * -MOTOR1_MAX_TICKS_PER_SECOND);
        }
    }
    public void stop(){
        motor1.setVelocity(0);
    }
    public boolean isBusy(){
        return motor1.isBusy();
    }
    public void sendTelemetry(){
        telemetry.addLine("INTAKE\n--------------`----------------------");
        telemetry.addData("INTAKE MOTOR VEL", motor1.getVelocity());
    }
}