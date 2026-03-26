package org.firstinspires.ftc.teamcode.FSL.auto;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.FSL.teleop.CompetitionTeleOpBlue;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.TurretConfig;
import org.firstinspires.ftc.teamcode.FSL.Robot;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;

import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "RED", group = "COMP")
@Configurable // Panels
public class BlueSafeAuto extends OpMode {
    ElapsedTime timer;
    private Robot robot;
    private int pathState = 0; // Current autonomous path state (state machine)

    @Override
    public void init() {
        timer.reset();
        robot = new Robot(hardwareMap, telemetry, true, false);
    }

    @Override
    public void loop() {
        autonomousPathUpdate(); // Update autonomous state machine
        robot.storage.update(robot.shooter.isWarmedUp());
        robot.turret.update();
    }

    @Override
    public void stop() {
        CompetitionTeleOpBlue.motif = robot.camera.motif;
    }
    public void autonomousPathUpdate(){
        switch (pathState) {
            case 0:
                robot.camera.readMotif();
                robot.storage.setQueue(Scoring.convertToScoringPattern(robot.camera.motif));
                robot.turret.pidController.setTarget(-25 * TurretConfig.TICKS_PER_DEGREE);
                robot.shooter.fire(2100);
                robot.shooter.setServo(0.85);
                if(timer.milliseconds() > 15000) {
                    robot.turret.pidController.setTarget(0);
                    robot.driveTrain.auto();
                    setPathState(1);
                }
                break;
            case 1:
                setPathState(-1);
                break;
        }
    }

    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
    }
}
