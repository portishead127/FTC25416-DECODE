package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name = "Test: Camera Test", group = "Test")
public class CameraTest extends NextFTCOpMode {
    public CameraTest(){
        addComponents(
                new SubsystemComponent(CameraSwivel.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    @Override
    public void onUpdate() {
        CameraSwivel.INSTANCE.focusOnAprilTag.schedule();
        updateTelemetry(telemetry);
    }
}
