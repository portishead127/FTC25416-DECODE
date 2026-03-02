package org.firstinspires.ftc.teamcode.FSL.helper.configs;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Configurable
@Config
public class StorageConfig {
   public static double KP = 0.0091;
   public static double KI = 0;
   public static double KD = 0.0005;
   public static double KS = 0;
   public static double ENCODER_RES = 282;
   public static double SPEED = 1;
   public static double TICK_TOLERANCE = 8;
   public static double FLICK_SERVO_MAX = 0;
   public static double FLICK_SERVO_MIN = 1;

   public static double FLICK_FORWARD_TIME = 200;
   public static double FLICK_RETURN_TIME = 350; //total
}
