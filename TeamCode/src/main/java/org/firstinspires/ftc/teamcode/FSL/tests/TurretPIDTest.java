package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.configs.TurretConfig;
import org.firstinspires.ftc.teamcode.FSL.subsystems.Turret;

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
    }
}
