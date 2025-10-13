package org.firstinspires.ftc.teamcode.FSL.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
        Storage.INSTANCE.tele.schedule();
        Gamepads.gamepad2().square().whenTrue(Storage.INSTANCE.reload);
        Gamepads.gamepad2().square().whenFalse(Storage.INSTANCE.stop);
        Gamepads.gamepad2().circle().whenBecomesTrue(Storage.INSTANCE.flickBall);
        Gamepads.gamepad2().triangle().whenBecomesTrue(
                new SequentialGroup(
                        Storage.INSTANCE.setColorNone,
                        Storage.INSTANCE.searchForBallColor
                )
        );
//        Gamepads.gamepad2().cross().whenTrue(new SequentialGroup(
//                Storage.INSTANCE.setColorGreen,
//                Storage.INSTANCE.searchForBallColor
//        ));
//        Gamepads.gamepad2().cross().whenFalse(new SequentialGroup(
//                Storage.INSTANCE.setColorNone,
//                Storage.INSTANCE.searchForBallColor
//        ));
    }
}
