package org.firstinspires.ftc.teamcode.FSL.helper;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "khabylamemechanism", group = "stupid")
public class KhabyLameMechanism extends LinearOpMode {
    public DcMotorEx barbecuechickenalert;
    @Override
    public void runOpMode() throws InterruptedException {barbecuechickenalert = hardwareMap.get(DcMotorEx.class, "bbq"); waitForStart(); if(rowrowrowyourboatgentlydownthestream()){ivebeenrunningintoyouinmyhead();} if(gamepad1.cross){tomatomatomatoma();}}private void idrinksodaieatpizza(){} private void ivebeenrunningintoyouinmyhead(){barbecuechickenalert.setPower(0);}

    private void tomatomatomatoma(){
        barbecuechickenalert.setPower(1);
    }

    private boolean rowrowrowyourboatgentlydownthestream(){return gamepad1.circle;}
}
