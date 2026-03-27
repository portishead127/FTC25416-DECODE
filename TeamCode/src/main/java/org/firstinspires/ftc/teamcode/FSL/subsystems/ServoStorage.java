package org.firstinspires.ftc.teamcode.FSL.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.StateMachine;
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
    public final CRServo flickServo;
    private final Telemetry telemetry;
    private Color currentColor;
    private boolean intaking;
    private boolean allowAny;
    private int focusedSlot;
    private StateMachine.StorageStates currentState;
    private enum Slot {
        SLOT1(
                Configuration.StorageConfig.INTAKE_AT_SLOT1_SERVO_POS,
                Configuration.StorageConfig.INTAKE_AT_SLOT1_ENCODER_POS,
                Configuration.StorageConfig.SHOOTING_FROM_SLOT1_SERVO_POS,
                Configuration.StorageConfig.SHOOTING_FROM_SLOT1_ENCODER_POS
        ),
        SLOT2(
                Configuration.StorageConfig.INTAKE_AT_SLOT2_SERVO_POS,
                Configuration.StorageConfig.INTAKE_AT_SLOT2_ENCODER_POS,
                Configuration.StorageConfig.SHOOTING_FROM_SLOT2_SERVO_POS,
                Configuration.StorageConfig.SHOOTING_FROM_SLOT2_ENCODER_POS
        ),
        SLOT3(
                Configuration.StorageConfig.INTAKE_AT_SLOT3_SERVO_POS,
                Configuration.StorageConfig.INTAKE_AT_SLOT3_ENCODER_POS,
                Configuration.StorageConfig.SHOOTING_FROM_SLOT3_SERVO_POS,
                Configuration.StorageConfig.SHOOTING_FROM_SLOT3_ENCODER_POS
        );

        private final double intakeServoPos;
        private final double intakeEncoderPos;
        private final double shootingServoPos;
        private final double shootingEncoderPos;

        Slot(double intakeServo, double intakeEncoder, double shootingServo, double shootingEncoder) {
            this.intakeServoPos = intakeServo;
            this.intakeEncoderPos = intakeEncoder;
            this.shootingServoPos = shootingServo;
            this.shootingEncoderPos = shootingEncoder;
        }

        public double getServoPos(boolean intaking) {
            return intaking ? intakeServoPos : shootingServoPos;
        }

        public double getEncoderPos(boolean intaking) {
            return intaking ? intakeEncoderPos : shootingEncoderPos;
        }
    }
    public ServoStorage(HardwareMap hm, Telemetry telemetry){
        this(hm, telemetry, true);
    }
    public ServoStorage(HardwareMap hm, Telemetry telemetry, boolean empty){
        encoder = hm.get(DcMotorEx.class, "ENC");
        servo1 = hm.get(Servo.class, "S1");
        servo2 = hm.get(Servo.class, "S2");
        servo3 = hm.get(Servo.class, "S3");
        flickServo = hm.get(CRServo.class, "FLS");
        colorSensor = hm.get(ColorRangeSensor.class, "CS");

        focusedSlot = 1;
        intaking = true;
        currentState = StateMachine.StorageStates.INTAKING;

        queue = new ArrayDeque<>();
        slots = new Color[3];

        if(!empty){
            slots[0] = Color.PURPLE;
            slots[1] = Color.PURPLE;
            slots[2] = Color.GREEN;
        }
        this.telemetry = telemetry;
    }
    public void manageStorage(){
        switch(currentState){
            case INTAKING:
                checkForColour();
                if(foundColor()) {
                    slots[focusedSlot - 1] = currentColor;
                    if(allSlotsFull()){
                        intaking = false;
                    }
                    cycleSlot();
                    currentState = StateMachine.StorageStates.ROTATING;
                }
                break;
            case ROTATING:
                if(!atPosition()){
                    break;
                }
                if(intaking) currentState = StateMachine.StorageStates.INTAKING;
                else currentState = StateMachine.StorageStates.AWAITING_FLICK;
                break;
            case AWAITING_FLICK:
                intaking = false;
                if(allSlotsEmpty()){
                    resetToIntake();
                    break;
                }
                if(allowAny){
                    currentState = StateMachine.StorageStates.FLICKING_ALL;
                    break;
                }
                if(queue.isEmpty()) {
                    stopFlicker();
                    break;
                }

                Color desired = queue.peekFirst();
                if(slots[focusedSlot - 1] == desired){
                    currentState = StateMachine.StorageStates.FLICKING;
                    break;
                }
                focusedSlot = findSuitableSlot(desired);
                setServos();
                break;
            case FLICKING:
                spinFlicker();
                //possible timer?
                slots[focusedSlot - 1] = null;
                queue.pollFirst();
                currentState = StateMachine.StorageStates.AWAITING_FLICK;
                break;
            case FLICKING_ALL:
                spinFlicker();
                slots[0] = null;
                slots[1] = null;
                slots[2] = null;
                focusedSlot = 3;
                currentState = StateMachine.StorageStates.ROTATING_ALL;
                break;
            case ROTATING_ALL:
                if(!atPosition()){
                    break;
                }
                resetToIntake();
        }
    }
    public void spinFlicker(){
        flickServo.setPower(Configuration.StorageConfig.FLICK_SPEED);
    }
    public void stopFlicker(){
        flickServo.setPower(0);
    }
    private boolean foundColor(){
        return currentColor != Color.NONE;
    }
    private void cycleSlot(){
        focusedSlot += 1;
        if(focusedSlot == 4){ focusedSlot = 1; }
        setServos();
    }
    private boolean atPosition(){
        return Math.abs(encoder.getCurrentPosition() - getEncoderTarget()) <= Configuration.StorageConfig.ENCODER_TOLERANCE;
    }
    public void resetToIntake(){
        stopFlicker();
        intaking = true;
        focusedSlot = 1;
        setServos();
        currentState = StateMachine.StorageStates.ROTATING;
    }
    public void resetToShoot(){
        stopFlicker();
        intaking = false;
        focusedSlot = 1;
        setServos();
        currentState = StateMachine.StorageStates.ROTATING;
    }
    private int findSuitableSlot(Color desired){
        for(int i = 0; i < slots.length; i++){
            if(slots[i] == desired) return i+1;
        }
        for(int i = 0; i < slots.length; i++){
            if(slots[i] != null) return i+1;
        }
        return 1;
    }
    private boolean allSlotsFull(){
        return slots[0] != null && slots[1] != null && slots[2] != null;
    }
    private void setServos() {
        double pos = getCurrentSlot().getServoPos(intaking);
        servo1.setPosition(pos);
        servo2.setPosition(pos);
        servo3.setPosition(pos);
    }
    private double getEncoderTarget() {
        return getCurrentSlot().getEncoderPos(intaking);
    }
    private boolean allSlotsEmpty(){
        return slots[0] == null && slots[1] == null && slots[2] == null;
    }
    private Slot getCurrentSlot() {
        return switch (focusedSlot) {
            case 1 -> Slot.SLOT1;
            case 2 -> Slot.SLOT2;
            default -> Slot.SLOT3;
        };
    }
    private void checkForColour(){
        currentColor = ColorMethods.fromSensor(colorSensor);
    }
}
