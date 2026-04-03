package org.firstinspires.ftc.teamcode.FSL.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.Configuration;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.TurretConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.control.PIDController;

public class Turret {
    Telemetry telemetry;
    private final DcMotorEx motor;
    private final PIDController pidController;
    public Turret(HardwareMap hm, Telemetry telemetry){
        motor = hm.get(DcMotorEx.class, "TM");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setDirection(DcMotorSimple.Direction.REVERSE); //SPINNING ACW NEEDS TO INCREASE TICKS
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        pidController = new PIDController(Configuration.TurretConfig.KP,0,0);
        pidController.setOutputLimits(-1,1);
        this.telemetry = telemetry;
    }
    public void update(){
        //Only one mode icl
        motor.setPower(pidController.calculate(motor.getCurrentPosition()));
    }
    public void setTargetAsRad(double target){
        pidController.setTarget(Configuration.TurretConfig.TICKS_PER_RADIAN * target);
    }
    public void sendTelemetry(){
        telemetry.addLine("TURRET - HARDWARE\n");
        telemetry.addData("MOTOR POS", motor.getCurrentPosition());
        telemetry.addData("MOTOR TARGET", pidController.getTarget());
        telemetry.addData("MOTOR VEL", motor.getVelocity());
    }
}
