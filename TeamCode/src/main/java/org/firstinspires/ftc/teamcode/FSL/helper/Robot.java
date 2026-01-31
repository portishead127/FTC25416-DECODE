package org.firstinspires.ftc.teamcode.FSL.helper;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.MecanumSet;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Storage;

public class Robot{
    public CameraSwivel cameraSwivel;
    public Shooter shooter;
    public Storage storage;
    public MecanumSet mecanumSet;
    public Intake intake;
    public Robot(HardwareMap hm, Telemetry telemetry){
        shooter = new Shooter(hm, telemetry);
        storage = new Storage(hm, telemetry);
        mecanumSet = new MecanumSet(hm, telemetry);
        intake = new Intake(hm, telemetry);
    };

    public void update() {
        cameraSwivel.focusOnAprilTag(false);

        if (storage.queueIsEmpty()) {
            shooter.stop();
        } else {
            shooter.fire();
        }

        if (intake.isBusy()) {
            storage.spin();
        } else if (cameraSwivel.locked && !storage.queueIsEmpty() && shooter.isWarmedUp()) {
            storage.loadIntoShooter();
        } else {
            storage.stop();
        }
    }
}
