package org.firstinspires.ftc.teamcode.FSL.comp;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.Motif;
import org.firstinspires.ftc.teamcode.FSL.helper.Robot;
import org.firstinspires.ftc.teamcode.FSL.helper.Scoring;

@TeleOp(name = "RED Competition: TeleOp", group = "RED")
@Configurable
public class CompetitionTeleOpRed extends LinearOpMode {
    Robot robot;
    Motif motif;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap, telemetry, false);
        motif = robot.cameraSwivel.readMotif();

        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
        waitForStart();
        while(opModeIsActive()){
            robot.update(gamepad1, gamepad2);
        }
    }
}
