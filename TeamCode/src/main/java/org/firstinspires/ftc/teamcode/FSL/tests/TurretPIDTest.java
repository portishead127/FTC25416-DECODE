package org.firstinspires.ftc.teamcode.FSL.tests;

import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.Robot;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.TurretConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Camera;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.PedroFollowerDriveTrain;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Storage;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Turret;

@TeleOp(name = "TEST: Turret PID Test", group = "TEST")
public class TurretPIDTest extends OpMode {
    Turret turret;

    @Override
    public void init() {
        turret = new Turret(hardwareMap, telemetry);
        telemetry.addData("STATUS", "INITIALISED");
        telemetry.update();
    }

    @Override
    public void loop(){
        if(gamepad1.squareWasPressed()){turret.pidController.setTarget(TurretConfig.TICKS_PER_DEGREE * 90);} //should go acw
        if(gamepad1.circleWasPressed()){turret.pidController.setTarget(TurretConfig.TICKS_PER_DEGREE * -90);} //should go cw
        if(gamepad1.crossWasPressed()){turret.pidController.setTarget(0);} //should go forward
        if(gamepad1.crossWasPressed()){turret.pidController.setTarget(TurretConfig.TICKS_PER_DEGREE * 178);} //should go almost fully pointing back acw
        if(gamepad1.crossWasPressed()){turret.pidController.setTarget(TurretConfig.TICKS_PER_DEGREE * -178);} //should go almost fully point back cw
        turret.update();
        telemetry.update();
    }
}
