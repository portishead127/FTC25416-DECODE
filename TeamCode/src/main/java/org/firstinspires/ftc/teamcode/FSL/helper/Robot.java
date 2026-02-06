package org.firstinspires.ftc.teamcode.FSL.helper;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.MecanumSet;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.PIDStorage;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Storage;

public class Robot{
    public CameraSwivel cameraSwivel;
    public Shooter shooter;
    public PIDStorage storage;
    public MecanumSet mecanumSet;
    public Intake intake;
    public Robot(HardwareMap hm, Telemetry telemetry, boolean isBlue){
        cameraSwivel = new CameraSwivel(hm, telemetry, isBlue);
        shooter = new Shooter(hm, telemetry);
        storage = new PIDStorage(hm, telemetry);
        mecanumSet = new MecanumSet(hm, telemetry);
        intake = new Intake(hm, telemetry);
    };

    public void update() {
        cameraSwivel.update(true);
        shooter.update(storage.queueIsEmpty(), cameraSwivel.range);
        storage.update(cameraSwivel.locked && shooter.isWarmedUp());
        intake.run(!storage.isFull());
    }
}
