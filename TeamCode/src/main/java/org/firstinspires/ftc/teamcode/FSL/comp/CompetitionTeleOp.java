package org.firstinspires.ftc.teamcode.FSL.comp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FSL.helper.Robot;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.MecanumSet;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name = "Competition: TeleOp", group = "Competition")
public class CompetitionTeleOp extends NextFTCOpMode {
    public CompetitionTeleOp(){
        addComponents(
                new SubsystemComponent(Robot.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onStartButtonPressed() {
        MecanumSet.INSTANCE.driverController.schedule();
        Gamepads.gamepad1().rightBumper().whenFalse(MecanumSet.INSTANCE.setMedium);
        Gamepads.gamepad1().leftBumper().whenTrue(MecanumSet.INSTANCE.setSlow);
        Gamepads.gamepad1().rightBumper().whenTrue(MecanumSet.INSTANCE.setFast);

        Gamepads.gamepad2().touchpad().whenBecomesTrue(Robot.INSTANCE.manualMotifControl);
        Robot.INSTANCE.telemetryMotif.schedule();
//
//        Gamepads.gamepad2().square().whenBecomesTrue(Robot.INSTANCE.scoreMotif);
//        Gamepads.gamepad2().triangle().whenBecomesTrue(Robot.INSTANCE.scoreGreen);
//        Gamepads.gamepad2().circle().whenBecomesTrue(Robot.INSTANCE.scorePurple);
//        //Gamepads.gamepad2().rightBumper().whenTrue(intake)

        CameraSwivel.INSTANCE.focusOnAprilTag.schedule();
        updateTelemetry(telemetry);
    }
}
