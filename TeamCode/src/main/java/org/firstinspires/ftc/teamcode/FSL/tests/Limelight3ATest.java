package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name = "Test: Limelight 3A Test", group = "Test")
public class Limelight3ATest extends NextFTCOpMode {
    public Limelight3ATest(){
        addComponents(
                new SubsystemComponent(CameraSwivel.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
        CameraSwivel.INSTANCE.initLimeLight3A.schedule();
    }

    @Override
    public void onStartButtonPressed() {
        CameraSwivel.INSTANCE.focusOnAprilTag.schedule();
        updateTelemetry(telemetry);
    }
}
