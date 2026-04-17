package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
@Disabled
@TeleOp(name = "TEST: Encoder Test", group = "TEST")
public class EncoderTest extends OpMode {
    DcMotorEx encoder;
    @Override
    public void init() {
        encoder = hardwareMap.get(DcMotorEx.class, "ENC");
    }
    @Override
    public void loop() {
        telemetry.addData("Ticks", encoder.getCurrentPosition());
        telemetry.update();
    }
}
