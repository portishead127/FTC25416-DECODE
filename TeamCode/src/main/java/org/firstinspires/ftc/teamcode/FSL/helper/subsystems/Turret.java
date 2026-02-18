package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.TurretConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.control.PIDController;

public class Turret {
    Telemetry telemetry;
    private DcMotorEx motor;
    private final PIDController pidController;
    public Turret(HardwareMap hm, Telemetry telemetry){
        motor = hm.get(DcMotorEx.class, "CSM");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setDirection(DcMotorSimple.Direction.REVERSE); //SPINNING ACW NEEDS TO INCREASE TICKS
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        pidController = new PIDController(TurretConfig.KP, TurretConfig.KI, TurretConfig.KD);
        this.telemetry = telemetry;
    }
    public void update(){
        motor.setVelocity(TurretConfig.MAX_VEL * pidController.calculateScalar(motor.getCurrentPosition()));
        sendTelemetry();
    }
    public void setPIDTarget(double bearingInTicks, boolean append){
        double valToAssign = Math.max(-TurretConfig.MAX_OFFSET,Math.min(bearingInTicks, TurretConfig.MAX_OFFSET));
        pidController.setTarget(valToAssign, append);
    }
    public void sendTelemetry(){
        telemetry.addLine("TURRET - HARDWARE\n");
        telemetry.addData("MOTOR POS", motor.getCurrentPosition());
        telemetry.addData("MOTOR TARGET", pidController.target);
        telemetry.addData("TARGET TOLERANCE", pidController.tolerance);
        telemetry.addData("MOTOR VEL", motor.getVelocity());
    }
}
