package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Storage;

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
        Gamepads.gamepad2().square().whenTrue(Storage.INSTANCE.spin);
        Gamepads.gamepad2().triangle().whenBecomesTrue(() -> {
            Storage.INSTANCE.setColorGreen.schedule();
            Storage.INSTANCE.searchForBallColor.schedule();
        });
        Gamepads.gamepad2().circle().whenBecomesTrue(() -> {
            Storage.INSTANCE.setColorPurple.schedule();
            Storage.INSTANCE.searchForBallColor.schedule();
        });
        Gamepads.gamepad2().cross().whenBecomesTrue(() -> {
            Storage.INSTANCE.setColorNone.schedule();
            Storage.INSTANCE.searchForBallColor.schedule();
        });
    }
}
