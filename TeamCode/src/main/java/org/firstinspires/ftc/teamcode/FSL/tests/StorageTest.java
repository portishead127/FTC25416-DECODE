package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.colors.Colors;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Storage;

import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name= "Test: Storage Test", group = "Test")
public class StorageTest extends NextFTCOpMode {
    public StorageTest(){
        addComponents(
                new SubsystemComponent(
                        Storage.INSTANCE
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    @Override
    public void onStartButtonPressed() {
        Gamepads.gamepad1().cross().whenBecomesTrue(Storage.INSTANCE.releaseSlot(Storage.INSTANCE.findSlotWithColor(Colors.PURPLE)));
        Gamepads.gamepad1().square().whenBecomesTrue(Storage.INSTANCE.releaseSlot(Storage.INSTANCE.findSlotWithColor(Colors.GREEN)));
        Gamepads.gamepad1().rightBumper().whenTrue(Storage.INSTANCE.reload);
    }

    @Override
    public void onUpdate() {
        Storage.INSTANCE.telemetryStorage.schedule();
        updateTelemetry(telemetry);
    }
}
