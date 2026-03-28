package org.firstinspires.ftc.teamcode.FSL.subsystems;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.FSL.helper.StateMachine;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.IntakeConfig;

public class NationalsIntake{
    private final DcMotorEx motor;
    private StateMachine.IntakeStates currentState;
    public NationalsIntake(HardwareMap hm){
        motor = hm.get(DcMotorEx.class, "INM");
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);

        currentState = StateMachine.IntakeStates.OFF;
    }
    public void update(){
        switch (currentState){
            case OFF -> motor.setPower(0);
            case INTAKING -> motor.setPower(IntakeConfig.INTAKE_SCALAR);
            case TRANSFERING -> motor.setPower(IntakeConfig.TRANSFER_SCALAR);
        }
    }
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