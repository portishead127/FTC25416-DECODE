package org.firstinspires.ftc.teamcode.FSL.auto;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.FSL.teleop.CompetitionTeleOpBlue;
import org.firstinspires.ftc.teamcode.FSL.Robot;


@Autonomous(name = "EMERGEN", group = "COMP")
@Configurable // Panels
public class emergency extends OpMode {
    private Robot robot;
    private int pathState = 0; // Current autonomous path state (state machine)

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, true, false);
    }

    @Override
    public void loop() {
        autonomousPathUpdate(); // Update autonomous state machine
    }

    @Override
    public void stop() {
        CompetitionTeleOpBlue.motif = robot.camera.motif;
    }
    public void autonomousPathUpdate(){
        switch (pathState) {
            case 0:
                    robot.driveTrain.auto();
                    setPathState(-1);
        }
    }

    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
    }
}
