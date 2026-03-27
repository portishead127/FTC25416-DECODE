package org.firstinspires.ftc.teamcode.FSL.subsystems;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.Color;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.ColorMethods;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.Configuration;

import java.util.ArrayDeque;

public class ServoStorage {
    private final ArrayDeque<Color> queue;
    public final Color[] slots;
    private final ColorRangeSensor colorSensor;
    public final DcMotorEx encoder;
    public final Servo servo1, servo2, servo3;
    public final Servo flickServo;
    private final Telemetry telemetry;
    private Color currentColor;
    private int focusedSlot;
    public ServoStorage(HardwareMap hm, Telemetry telemetry){
        this(hm, telemetry, true);
    }
    public ServoStorage(HardwareMap hm, Telemetry telemetry, boolean empty){
        encoder = hm.get(DcMotorEx.class, "ENC");
        servo1 = hm.get(Servo.class, "S1");
        servo2 = hm.get(Servo.class, "S2");
        servo3 = hm.get(Servo.class, "S3");
        flickServo = hm.get(Servo.class, "FLS");
        colorSensor = hm.get(ColorRangeSensor.class, "CS");

        focusedSlot = 1;

        queue = new ArrayDeque<>();
        slots = new Color[3];

        if(!empty){
            slots[0] = Color.PURPLE;
            slots[1] = Color.PURPLE;
            slots[2] = Color.GREEN;
        }
        this.telemetry = telemetry;
    }
    public void intake(){
        //possible timer check goes here?
        currentColor = ColorMethods.fromSensor(colorSensor);
    }
    public boolean foundColor(){
        return currentColor != Color.NONE;
    }
    public void cycleSlot(boolean intaking){
        focusedSlot += 1;
        if(focusedSlot == 4){ focusedSlot = 1; }
        setServos(getServoPosFromSlotNum(intaking));
    }
    public boolean atPosition(boolean intaking){
        return Math.abs(encoder.getCurrentPosition() - getEncoderPosFromSlotNum(intaking)) <= Configuration.StorageConfig.ENCODER_TOLERANCE;
    }
    private void setServos(double pos){
        servo1.setPosition(pos);
        servo2.setPosition(pos);
        servo3.setPosition(pos);
    }
    private double getServoPosFromSlotNum(boolean intaking){
        if(intaking) return getIntakingServoPosFromSlotNum();
        return getShootingServoPosFromSlotNum();
    }
    private double getEncoderPosFromSlotNum(boolean intaking){
        if(intaking) return getIntakingEncoderPosFromSlotNum();
        return getShootingEncoderPosFromSlotNum();
    }
    private double getIntakingServoPosFromSlotNum(){
        return switch (focusedSlot) {
            case 1 -> Configuration.StorageConfig.INTAKE_AT_SLOT1_SERVO_POS;
            case 2 -> Configuration.StorageConfig.INTAKE_AT_SLOT2_SERVO_POS;
            case 3 -> Configuration.StorageConfig.INTAKE_AT_SLOT3_SERVO_POS;
            default -> 0;
        };
    }
    private double getShootingServoPosFromSlotNum(){
        return switch (focusedSlot) {
            case 1 -> Configuration.StorageConfig.SHOOTING_FROM_SLOT1_SERVO_POS;
            case 2 -> Configuration.StorageConfig.SHOOTING_FROM_SLOT2_SERVO_POS;
            case 3 -> Configuration.StorageConfig.SHOOTING_FROM_SLOT3_SERVO_POS;
            default -> 0;
        };
    }
    private double getIntakingEncoderPosFromSlotNum(){
        return switch (focusedSlot) {
            case 1 -> Configuration.StorageConfig.INTAKE_AT_SLOT1_ENCODER_POS;
            case 2 -> Configuration.StorageConfig.INTAKE_AT_SLOT2_ENCODER_POS;
            case 3 -> Configuration.StorageConfig.INTAKE_AT_SLOT3_ENCODER_POS;
            default -> 0;
        };
    }
    private double getShootingEncoderPosFromSlotNum(){
        return switch (focusedSlot) {
            case 1 -> Configuration.StorageConfig.SHOOTING_FROM_SLOT1_ENCODER_POS;
            case 2 -> Configuration.StorageConfig.SHOOTING_FROM_SLOT2_ENCODER_POS;
            case 3 -> Configuration.StorageConfig.SHOOTING_FROM_SLOT3_ENCODER_POS;
            default -> 0;
        };
    }
}
