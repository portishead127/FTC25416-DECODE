package org.firstinspires.ftc.teamcode.FSL.comp;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.Robot;

//FTC DASH - 192.168.49.1:8080/dash
//PANELS - 192.168.49.1:8001/
@TeleOp(name = "BLUE: Competition", group = "BLUE")
@Configurable
public class CompetitionTeleOpBlue extends LinearOpMode {
    Robot robot;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap, telemetry, true, false, false);

        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
        waitForStart();
        while(opModeIsActive()){
            robot.update(gamepad1, gamepad2);
        }
    }
}
