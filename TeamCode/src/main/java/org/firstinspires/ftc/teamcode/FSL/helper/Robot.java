package org.firstinspires.ftc.teamcode.FSL.helper;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Intake;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.PIDStorage;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;

public class Robot{
    public CameraSwivel cameraSwivel;
    public Shooter shooter;
    public PIDStorage storage;
    public DriveTrain driveTrain;
    public Intake intake;
    public Robot(HardwareMap hm, Telemetry telemetry, boolean isBlue, boolean isGreedyAuto, boolean emptyStorage){
        cameraSwivel = new CameraSwivel(hm, telemetry, isBlue, isGreedyAuto);
        shooter = new Shooter(hm, telemetry);
        storage = new PIDStorage(hm, telemetry, emptyStorage);
        driveTrain = new DriveTrain(hm, telemetry);
        intake = new Intake(hm, telemetry);
    };

    public void update(Gamepad gamepad1, Gamepad gamepad2) {
        cameraSwivel.update(gamepad2.left_stick_x);
        shooter.update(storage.queueIsEmpty(), cameraSwivel.range);
        storage.update(cameraSwivel.locked && shooter.isWarmedUp());
        intake.update(!storage.isFull());
        driveTrain.update(gamepad1, gamepad1.right_bumper);

        if(gamepad2.squareWasPressed()){ storage.setQueue(Scoring.convertToScoringPattern(cameraSwivel.motif)); }
        if(gamepad2.triangleWasPressed()){ storage.setQueue(Scoring.G); }
        if(gamepad2.crossWasPressed()){ storage.setQueue(Scoring.P); }
        if(gamepad2.circleWasPressed()){ storage.setQueue(Scoring.NONE); }
    }

    public void update() {
        cameraSwivel.update();
        shooter.update(storage.queueIsEmpty(), cameraSwivel.range);
        storage.update(cameraSwivel.locked && shooter.isWarmedUp());
        intake.update(!storage.isFull());
    }
}
