package org.firstinspires.ftc.teamcode.FSL.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.control.StateMachine;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.Color;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.ColorMethods;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.Configuration;
import org.firstinspires.ftc.teamcode.FSL.helper.control.ShooterReadyProvider;
import org.jetbrains.annotations.TestOnly;

import java.util.ArrayDeque;
import java.util.Collection;

public class ServoStorage {
    private final ArrayDeque<Color> queue;
    private final Color[] slots;
    private final ColorRangeSensor colorSensor;
    private final DcMotorEx encoder;
    private final Servo servo1, servo2, servo3;
    private final CRServo flickServo;
    private final Telemetry telemetry;
    private final ShooterReadyProvider shooterReadyProvider;
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
        this(hm, telemetry, true, () -> true);
    }
    public ServoStorage(HardwareMap hm, Telemetry telemetry, boolean empty, ShooterReadyProvider shooterReadyProvider){
        this.shooterReadyProvider = shooterReadyProvider;
        encoder = hm.get(DcMotorEx.class, "ENC");
        encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        servo1 = hm.get(Servo.class, "S1");
        servo2 = hm.get(Servo.class, "S2");
        servo3 = hm.get(Servo.class, "S3");
        flickServo = hm.get(CRServo.class, "FLS");
        colorSensor = hm.get(ColorRangeSensor.class, "CS");

        focusedSlot = 1;
        intaking = true;

        queue = new ArrayDeque<>();
        slots = new Color[3];

        if(!empty){
            slots[0] = Color.PURPLE;
            slots[1] = Color.PURPLE;
            slots[2] = Color.GREEN;
            currentState = StateMachine.StorageStates.AWAITING_FLICK;
        }
        else{
            currentState = StateMachine.StorageStates.INTAKING;
        }
        moveToCurrentSlot();
        this.telemetry = telemetry;
    }
    public void update(){
        switch(currentState){
            case INTAKING:
                if (slots[focusedSlot - 1] != Color.NONE) {
                    cycleSlot();
                    currentState = StateMachine.StorageStates.ROTATING;
                    break;
                }

                checkForColour();
                if (foundColor()) {
                    slots[focusedSlot - 1] = currentColor;

                    if (allSlotsFull()) {
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
                if(allSlotsEmpty()){
                    resetToIntake();
                    break;
                }
                if(allowAny){
                    if(focusedSlot == 1){
                        currentState = StateMachine.StorageStates.FLICKING_ALL;
                    }
                    else{
                        resetToShoot();
                        allowAny = true;
                    }
                    break;
                }
                if(queue.isEmpty()) {
                    stopFlicker();
                    break;
                }

                Color desired = queue.peekFirst();
                if(slots[focusedSlot - 1] == desired && shooterReadyProvider.isShooterReady()){
                    currentState = StateMachine.StorageStates.FLICKING;
                    break;
                }
                focusedSlot = findSuitableSlot(desired);
                moveToCurrentSlot();
                currentState = StateMachine.StorageStates.ROTATING;
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
    public void forceIntake(){
        if(currentState == StateMachine.StorageStates.AWAITING_FLICK && !allSlotsFull()){
            resetToIntake();
        }
    }
    public void forceShoot(){
        if(currentState == StateMachine.StorageStates.FLICKING && !allSlotsEmpty()){
            resetToShoot();
        }
    }
    public void allowAny(){
        allowAny = true;
    }
    public boolean isBusy() {
        return currentState == StateMachine.StorageStates.ROTATING
                || currentState == StateMachine.StorageStates.FLICKING
                || currentState == StateMachine.StorageStates.ROTATING_ALL;
    }
    public void clearQueue(){
        if(isBusy()) return;
        queue.clear();
    }
    public void setQueue(Color color){
        if(isBusy()) return;
        queue.clear();
        queue.addFirst(color);
    }
    public void setQueue(Collection<Color> colors){
        if(isBusy()) return;
        queue.clear();
        for (Color color: colors) {
            queue.addLast(color);
        }
    }
    public void spinFlicker(){
        flickServo.setPower(Configuration.StorageConfig.FLICK_SPEED);
    }
    public void stopFlicker(){
        flickServo.setPower(0);
    }
    public boolean isIntaking(){
        return intaking;
    }
    private boolean foundColor(){
        return currentColor != Color.NONE;
    }
    private void cycleSlot(){
        focusedSlot += 1;
        if(focusedSlot == 4){ focusedSlot = 1; }
        moveToCurrentSlot();
    }
    private boolean atPosition(){
        return Math.abs(encoder.getCurrentPosition() - getEncoderTarget()) <= Configuration.StorageConfig.ENCODER_TOLERANCE;
    }
    private void resetToIntake(){
        stopFlicker();
        intaking = true;
        allowAny = false;
        focusedSlot = 1;
        clearQueue();
        moveToCurrentSlot();
        currentState = StateMachine.StorageStates.ROTATING;
    }
    private void resetToShoot(){
        stopFlicker();
        intaking = false;
        allowAny = false;
        focusedSlot = 1;
        clearQueue();
        moveToCurrentSlot();
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
    private void moveToCurrentSlot() {
        double pos = getCurrentSlot().getServoPos(intaking);
        servo1.setPosition(pos);
        servo2.setPosition(pos);
        servo3.setPosition(pos);
    }
    @TestOnly
    public void setServos(double pos){
        servo1.setPosition(pos);
        servo2.setPosition(pos);
        servo3.setPosition(pos);
    }
    @TestOnly
    public double getServoPos(){
        return servo1.getPosition();
    }
    @TestOnly
    public double getEncoderPos(){
        return encoder.getCurrentPosition();
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
    public void sendTelemetry(){
        if (telemetry == null) return;
        telemetry.addData("Storage State", currentState);
        telemetry.addData("Focused Slot", focusedSlot);
        telemetry.addData("Intaking", intaking);
        telemetry.addData("Detected Color", currentColor);
        telemetry.addData("Slot1", slots[0]);
        telemetry.addData("Slot2", slots[1]);
        telemetry.addData("Slot3", slots[2]);
        telemetry.addData("Queue", queue.size());
        telemetry.addData("Encoder", encoder.getCurrentPosition());
        telemetry.addData("Target Enc", getEncoderTarget());
    }
}
