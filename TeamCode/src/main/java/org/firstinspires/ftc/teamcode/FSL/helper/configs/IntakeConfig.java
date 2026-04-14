package org.firstinspires.ftc.teamcode.FSL.helper.configs;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Config
@Configurable
public class IntakeConfig {
    public static double INTAKE_MOTOR_INTAKE_SCALAR = 0.7;
    public static double TRANSFER_MOTOR_INTAKE_SCALAR = 0.3;
    public static double INTAKE_MOTOR_TRANSFER_SCALAR = 0.4;
    public static double TRANSFER_MOTOR_TRANSFER_SCALAR = 0.6;
}
