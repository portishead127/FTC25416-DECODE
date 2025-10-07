package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name = "Test: Shooter Test", group = "Test")
public class ShooterTest extends NextFTCOpMode {
    public ShooterTest(){
        addComponents(
                new SubsystemComponent(
                        Shooter.INSTANCE
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onStartButtonPressed() {
        Shooter.INSTANCE.telemetryServo.schedule();
        Gamepads.gamepad1().dpadUp().whenBecomesTrue(Shooter.INSTANCE.addToServo);
        Gamepads.gamepad1().dpadDown().whenBecomesTrue(Shooter.INSTANCE.subtractFromServo);
        Gamepads.gamepad1().square().whenBecomesTrue(Shooter.INSTANCE.fire);
        Gamepads.gamepad1().triangle().whenBecomesTrue(Shooter.INSTANCE.fireHalf);
        Gamepads.gamepad1().cross().whenBecomesTrue(Shooter.INSTANCE.stop);
    }
}
