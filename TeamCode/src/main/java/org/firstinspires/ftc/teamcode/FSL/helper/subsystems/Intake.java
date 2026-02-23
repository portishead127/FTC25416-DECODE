package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.IntakeConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.constants.UltraplanetaryMotorConstants;

public class Intake{
    private final DcMotorEx motor;
    private final Telemetry telemetry;
    public Intake(HardwareMap hm, Telemetry telemetry){
        motor = hm.get(DcMotorEx.class, "INM");
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.telemetry = telemetry;
    }
    public void update(boolean forward){
        if(forward){
            motor.setPower(IntakeConfig.FORWARD_SCALAR);
        }
        else{
            motor.setPower(-IntakeConfig.BACKWARD_SCALAR);
        }
        sendTelemetry();
    }
    public void stop(){
        motor.setVelocity(0);
    }
    public boolean isBusy(){
        return motor.isBusy();
    }
    private void sendTelemetry(){
        telemetry.addLine("INTAKE\n");
        telemetry.addData("INTAKE MOTOR VEL", motor.getVelocity());
    }
}