package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.MecanumSet;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name = "Test: Mecanum Test", group = "Test")
public class MecanumTest extends NextFTCOpMode {
    public MecanumTest(){
        addComponents(
                new SubsystemComponent(MecanumSet.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    @Override
    public void onStartButtonPressed() {
        Gamepads.gamepad1().rightBumper().whenFalse(MecanumSet.INSTANCE::setMedium);
        Gamepads.gamepad1().leftBumper().whenTrue(MecanumSet.INSTANCE::setSlow);
        Gamepads.gamepad1().rightBumper().whenTrue(MecanumSet.INSTANCE::setFast);
        MecanumSet.INSTANCE.driverController.schedule();
    }
}
