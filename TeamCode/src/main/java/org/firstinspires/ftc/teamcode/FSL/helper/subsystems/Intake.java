package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.IntakeConfig;

public class Intake{
    private static double MOTOR_MAX_TICKS_PER_SECOND;
    private final DcMotorEx motor;
    private final Telemetry telemetry;
    public Intake(HardwareMap hm, Telemetry telemetry){
        motor = hm.get(DcMotorEx.class, "INM");
        this.telemetry = telemetry;
        MOTOR_MAX_TICKS_PER_SECOND = motor.getMotorType().getAchieveableMaxTicksPerSecond();
    }
    public void run(boolean forward){
        if(forward){
            motor.setVelocity(IntakeConfig.FORWARD_SCALAR * MOTOR_MAX_TICKS_PER_SECOND);
        }
        else{
            motor.setVelocity(IntakeConfig.BACKWARD_SCALAR * -MOTOR_MAX_TICKS_PER_SECOND);
        }
    }
    public void stop(){
        motor.setVelocity(0);
    }
    public boolean isBusy(){
        return motor.isBusy();
    }
    public void sendTelemetry(){
        telemetry.addLine("INTAKE\n");
        telemetry.addData("INTAKE MOTOR VEL", motor.getVelocity());
    }
}