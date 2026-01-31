package org.firstinspires.ftc.teamcode.FSL.comp;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.Motif;
import org.firstinspires.ftc.teamcode.FSL.helper.Robot;
import org.firstinspires.ftc.teamcode.FSL.helper.Scoring;

@TeleOp(name = "Competition: TeleOp", group = "Competition")
@Configurable
public class CompetitionTeleOp extends LinearOpMode {
    Robot robot;
    Motif motif;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap, telemetry);
        motif = robot.cameraSwivel.readMotif();

        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
        waitForStart();
        while(opModeIsActive()){
            if(gamepad2.squareWasPressed()){ robot.storage.setQueue(Scoring.convertToScoringPattern(motif)); }
            if(gamepad2.triangleWasPressed()){ robot.storage.setQueue(Scoring.G); }
            if(gamepad2.crossWasPressed()){ robot.storage.setQueue(Scoring.P); }
            if(gamepad2.circleWasPressed()){ robot.storage.setQueue(Scoring.NONE); }

            robot.mecanumSet.drive(gamepad1, 0.67);
            robot.update();
        }
    }
}
