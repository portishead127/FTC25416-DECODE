package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.configs.CameraSwivelConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;

@TeleOp(name = "TEST - Camera Test", group = "TEST")
public class CameraTest extends OpMode {
    CameraSwivel cameraSwivel;
    @Override
    public void init() {
        cameraSwivel = new CameraSwivel(hardwareMap, telemetry, true, false);
        telemetry.addData("STATUS:", "INITIALISED");
        telemetry.addData("MOTIF:", cameraSwivel.motif.name());
        cameraSwivel.resumeStreaming();
        telemetry.update();
    }

    @Override
    public void loop() {
//        cameraSwivel.update(gamepad1.left_stick_x);
        cameraSwivel.focusOnAprilTag();
//        if(gamepad1.squareWasPressed()){cameraSwivel.setPIDTarget(CameraSwivelConfig.MAX_OFFSET, false);}
//        if(gamepad1.crossWasPressed()){cameraSwivel.setPIDTarget(-CameraSwivelConfig.MAX_OFFSET, false);}
//        if(gamepad1.circleWasPressed()){cameraSwivel.setPIDTarget(0, false);}
        cameraSwivel.sendTelemetry();
        telemetry.addData("GAMEPAD 1 LX:", gamepad1.left_stick_x);
        telemetry.update();
    }
}
