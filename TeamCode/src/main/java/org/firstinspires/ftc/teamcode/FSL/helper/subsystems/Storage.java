package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.FSL.helper.control.PIDController;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.ColorMethods;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.Color;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.StorageConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;

import java.util.LinkedList;

public class Storage {
    private final LinkedList<Color> queue = new LinkedList<Color>();
    public final Color[] slots;
    private final ColorRangeSensor colorSensor;
    public final DcMotorEx motor;
    private final Servo servo;
    private final Telemetry telemetry;
    public final PIDController pidController;
    private final ElapsedTime flickTimer;
    private Color currentColor;
    private boolean intakeMode;
    private boolean isFlicking;
    private boolean wasIntakeMode;
    private int focusedIndex;
    private final double basePosition;
    private final double ticksPerThird;
    private final double ticksPerHalf;

    public Storage(HardwareMap hm, Telemetry telemetry, boolean emptyStorage) {
        motor = hm.get(DcMotorEx.class, "STM");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);//prob wrong needs to be acw

        servo = hm.get(Servo.class, "FLS");
        colorSensor = hm.get(ColorRangeSensor.class, "CS");
        colorSensor.enableLed(true);
        this.telemetry = telemetry;

        pidController = new PIDController(StorageConfig.KP, StorageConfig.KI, StorageConfig.KD, StorageConfig.KF);

        if(emptyStorage){
            slots = new Color[]{null, null, null};
        }else{
            slots = new Color[]{Color.PURPLE, Color.PURPLE, Color.GREEN};
        }

        wasIntakeMode = !emptyStorage;
        intakeMode = emptyStorage;
        flickTimer = new ElapsedTime();

        focusedIndex = 0;

        basePosition = motor.getCurrentPosition();

        ticksPerThird = StorageConfig.ENCODER_RES / 3.0;
        ticksPerHalf  = StorageConfig.ENCODER_RES / 2.0;

        pidController.setOutputLimits(-1, 1);
        pidController.setTolerance(StorageConfig.TICK_TOLERANCE);
    }
    private void updateFlick() {
        if (!isFlicking) return;

        if (flickTimer.milliseconds() < StorageConfig.FLICK_FORWARD_TIME) {
            servo.setPosition(StorageConfig.FLICK_SERVO_MAX);
        }
        else if (flickTimer.milliseconds() < StorageConfig.FLICK_RETURN_TIME) {
            servo.setPosition(StorageConfig.FLICK_SERVO_MIN);
        }
        else {
            isFlicking = false;
        }
    }
    private void startFlick() {
        if (isFlicking) return; // prevent double flicks
        isFlicking = true;
        flickTimer.reset();
    }
    public boolean isEmpty(){
        return intakeMode;
    }
    public void setQueue(LinkedList<Color> colors){
        queue.clear();
        queue.addAll(colors);
    }
    public boolean queueIsEmpty(){ return queue.isEmpty(); }
    public void rotate1Slot(boolean anticlockwise){
        if(!pidController.atTarget()) return;

        if(anticlockwise){
            focusedIndex = (focusedIndex + 1) % 3;
        } else {
            focusedIndex = (focusedIndex - 1 + 3) % 3;
        }

        double target;
        // Absolute target instead of cumulative
        if(intakeMode){
            target = basePosition + focusedIndex * ticksPerThird;
        }
        else{
            target = basePosition + ticksPerHalf + focusedIndex * ticksPerThird;
        }
        pidController.setTarget(target);
    }


    public void goToSlot0AlignedWithShooter(){
        focusedIndex = 0;
        pidController.setTarget(basePosition + ticksPerHalf);
    }
    public void goToSlot0AlignedWithIntake(){
        focusedIndex = 0;
        pidController.setTarget(basePosition);
    }
    public void update(boolean shootable) {
        intakeMode = ((slots[0] == null && slots[1] == null && slots[2] == null) || queueIsEmpty());

        // Detect transitions
        boolean justBecameFull  = wasIntakeMode && !intakeMode;
        boolean justBecameEmpty = !wasIntakeMode && intakeMode;

        updateFlick();

        if (intakeMode) {
            setQueue(Scoring.NONE);
            if(justBecameEmpty){
                goToSlot0AlignedWithIntake();
            }
            intakeUpdate();
        } else {
            if (justBecameFull) {
                goToSlot0AlignedWithShooter();  // only once when becoming full
            }
            transferUpdate(shootable);
        }

        wasIntakeMode = intakeMode;
        motor.setPower(pidController.calculate(motor.getCurrentPosition()));
        sendTelemetry();
    }
    public void intakeUpdate(){
        if(!pidController.atTarget() || isFlicking){
            return;
        }

        currentColor = ColorMethods.fromSensor(colorSensor);
        if(currentColor != Color.NONE){
            slots[focusedIndex] = currentColor;
            rotate1Slot(true);
        }
    }
    public void transferUpdate(boolean shootable) {
        if (!pidController.atTarget() || isFlicking) {
            return;
        }

        Color desired = queue.peekFirst();

        if (desired == slots[focusedIndex]) {
            if(shootable){
                startFlick();
                slots[focusedIndex] = null;
                queue.removeFirst();
            }
        }
        else if (desired == slots[(focusedIndex + 1) % 3]) {
            rotate1Slot(false);
        }
        else if (desired == slots[(focusedIndex -1 + 3) % 3]) {
            rotate1Slot(true);
        }
        else {
            if (slots[focusedIndex] != null) {
                if(shootable){
                    startFlick();
                    slots[focusedIndex] = null;
                    queue.removeFirst();
                }
            }
            else if (slots[(focusedIndex + 1) % 3] != null) {
                rotate1Slot(false);
            }
            else if (slots[(focusedIndex -1 + 3) % 3] != null) {
                rotate1Slot(true);
            }
        }
    }
    public void sendTelemetry() {
        telemetry.addLine("STORAGE - HARDWARE\n");
        telemetry.addData("SERVO POS", servo.getPosition());
        telemetry.addData("MOTOR POS", motor.getCurrentPosition());
        telemetry.addData("MOTOR TARGET POS", pidController.getTarget());
        telemetry.addData("MOTOR VEL", motor.getVelocity());
        telemetry.addData("COLOR SENSOR", currentColor.name());
        telemetry.addData("DETECTION DISTANCE (mm)", colorSensor.getDistance(DistanceUnit.MM));

        telemetry.addLine("STORAGE - SLOTS\n");
        telemetry.addData("INTAKE MODE", intakeMode);
        telemetry.addData("FOCUSED INDEX", focusedIndex);
        telemetry.addData("SLOT 0", slots[0]);
        telemetry.addData("SLOT 1", slots[1]);
        telemetry.addData("SLOT 2", slots[2]);

        telemetry.addLine("STORAGE - QUEUE\n");
        telemetry.addData("EMPTY", queueIsEmpty());
        telemetry.addData("QUEUE", queue);

        telemetry.addLine("STORAGE - FLICKER\n");
        telemetry.addData("FLICKING", isFlicking);
        telemetry.addData("FLICK TIMER (ms)", flickTimer.milliseconds());
    }
}
