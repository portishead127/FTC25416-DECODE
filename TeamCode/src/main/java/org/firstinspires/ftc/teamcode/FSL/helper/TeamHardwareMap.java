package org.firstinspires.ftc.teamcode.FSL.helper;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.FSL.helper.systems.MecanumSet;

public class TeamHardwareMap {

    //BIG SYSTEM CLASSES GO HERE
    public MecanumSet mecanumSet;


    public TeamHardwareMap(HardwareMap hardwareMap){
        //GETTING INDIVIDUAL MOTORS FROM HARDWARE MAP
        DcMotor frontLeftMotor = hardwareMap.get(DcMotor.class, "FLM");
        DcMotor frontRightMotor = hardwareMap.get(DcMotor.class, "FLM");
        DcMotor backLeftMotor = hardwareMap.get(DcMotor.class, "FLM");
        DcMotor backRightMotor = hardwareMap.get(DcMotor.class, "FLM");

        //Not certain
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        //INITIALISING THE BIG SYSTEM CLASSES
        mecanumSet = new MecanumSet(frontLeftMotor, frontRightMotor, backRightMotor, backLeftMotor);
    }

}
