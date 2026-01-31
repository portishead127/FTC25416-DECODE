package org.firstinspires.ftc.teamcode.FSL.comp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.Robot;
import org.firstinspires.ftc.teamcode.FSL.helper.Scoring;

@TeleOp(name = "Competition: TeleOp", group = "Competition")
public class CompetitionTeleOp extends LinearOpMode {
    Robot robot = new Robot(hardwareMap, telemetry);
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
        waitForStart();
        while(opModeIsActive()){
            if(gamepad1.right_bumper){ robot.storage.spin(); }
            else{ robot.storage.spinThroughQueue(); }

            if(gamepad1.squareWasPressed()){ robot.storage.setQueue(Scoring.PPG); }
            if(gamepad1.triangleWasPressed()){ robot.storage.setQueue(Scoring.G); }
            if(gamepad1.crossWasPressed()){ robot.storage.setQueue(Scoring.P); }
            if(gamepad1.circleWasPressed()){ robot.storage.setQueue(Scoring.NONE); }
            robot.mecanumSet.drive(gamepad1, 0.67);
            robot.update();
        }
    }
}
