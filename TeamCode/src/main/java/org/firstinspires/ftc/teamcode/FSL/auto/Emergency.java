package org.firstinspires.ftc.teamcode.FSL.auto;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.FSL.NationalsRobot;


@Autonomous(name = "EMERGENCY", group = "COMP")
@Configurable // Panels
public class Emergency extends OpMode {
    private NationalsRobot robot;
    private int pathState = 0; // Current autonomous path state (state machine)

    @Override
    public void init() {
        robot = new NationalsRobot(hardwareMap, telemetry);
    }
    @Override
    public void loop() {
        autonomousPathUpdate(); // Update autonomous state machine
    }
    public void autonomousPathUpdate(){
        if (pathState == 0) {
            robot.emergencyAuto();
            setPathState(-1);
        }
    }

    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
    }
}
