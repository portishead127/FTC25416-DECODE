package org.firstinspires.ftc.teamcode.FSL.comp;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.Motif;
import org.firstinspires.ftc.teamcode.FSL.helper.Robot;
import org.firstinspires.ftc.teamcode.FSL.helper.Scoring;

@TeleOp(name = "BLUE Competition: TeleOp", group = "BLUE")
@Configurable
public class CompetitionTeleOpBlue extends LinearOpMode {
    Robot robot;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap, telemetry, true);

        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
        waitForStart();
        while(opModeIsActive()){
            robot.update(gamepad1, gamepad2);
        }
    }
}
