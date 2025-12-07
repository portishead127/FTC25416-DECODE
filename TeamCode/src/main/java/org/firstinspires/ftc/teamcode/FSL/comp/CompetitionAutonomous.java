package org.firstinspires.ftc.teamcode.FSL.comp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FSL.helper.Robot;
@Autonomous(name = "Competition: Auto", group = "Competition")
public class CompetitionAutonomous extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);
    }
}
