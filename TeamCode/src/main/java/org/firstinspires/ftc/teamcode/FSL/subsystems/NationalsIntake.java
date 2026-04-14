package org.firstinspires.ftc.teamcode.FSL.subsystems;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.FSL.helper.configs.Configuration;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.IntakeConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.control.StateMachine;

public class NationalsIntake{
    private final DcMotorEx intakeMotor;
    private final DcMotorEx transferMotor;
    private StateMachine.IntakeStates currentState;
    public NationalsIntake(HardwareMap hm){
        intakeMotor = hm.get(DcMotorEx.class, "INM");
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        transferMotor = hm.get(DcMotorEx.class, "TRM");
        transferMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        transferMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        transferMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        currentState = StateMachine.IntakeStates.OFF;
    }
    public void update(){
        switch (currentState){
            case OFF:
                intakeMotor.setPower(0);
                transferMotor.setPower(0);
                break;
            case INTAKING:
                intakeMotor.setPower(IntakeConfig.INTAKE_MOTOR_INTAKE_SCALAR);
                transferMotor.setPower(IntakeConfig.TRANSFER_MOTOR_INTAKE_SCALAR);
                break;
            case TRANSFERING:
                intakeMotor.setPower(IntakeConfig.INTAKE_MOTOR_TRANSFER_SCALAR);
                transferMotor.setPower(IntakeConfig.TRANSFER_MOTOR_TRANSFER_SCALAR);
                break;
        }
    }
    public void runJustIntake(){intakeMotor.setPower(IntakeConfig.INTAKE_MOTOR_INTAKE_SCALAR);}
    public void runIntake(){
        currentState = StateMachine.IntakeStates.INTAKING;
    }
    public void runTransfer(){
        currentState = StateMachine.IntakeStates.TRANSFERING;
    }
    public void stop(){
        currentState = StateMachine.IntakeStates.OFF;
    }
}