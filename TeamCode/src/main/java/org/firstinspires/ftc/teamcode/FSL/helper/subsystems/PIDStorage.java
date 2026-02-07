package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.PIDController;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.ColorMethods;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.Color;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.StorageConfig;

import java.util.LinkedList;

public class PIDStorage {
    private final LinkedList<Color> queue = new LinkedList<Color>();
    public final Color[] slots;
    private final ColorSensor colorSensor;
    private final DcMotorEx motor;
    private final Servo servo;
    private final Telemetry telemetry;
    private final PIDController pidController;
    private Color currentColor;
    private boolean intakeMode;
    private boolean isFlicking;
    private boolean wasIntakeMode;
    private ElapsedTime flickTimer;
    public PIDStorage(HardwareMap hm, Telemetry telemetry, boolean emptyStorage) {
        motor = hm.get(DcMotorEx.class, "SPM");
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);//prob wrong

        servo = hm.get(Servo.class, "FLS");
        colorSensor = hm.get(ColorRangeSensor.class, "CS");
        colorSensor.enableLed(true);
        this.telemetry = telemetry;

        pidController = new PIDController(StorageConfig.KP, StorageConfig.KI, StorageConfig.KD, StorageConfig.TICKTOLERANCE);
        slots = new Color[]{null, null, null};
        wasIntakeMode = !emptyStorage;
        intakeMode = emptyStorage;
        flickTimer = new ElapsedTime();
    }
    private void updateFlick() {
        if (!isFlicking) return;

        if (flickTimer.milliseconds() < StorageConfig.FLICKFORWARDTIME) {
            servo.setPosition(1); // flick forward
        }
        else if (flickTimer.milliseconds() < StorageConfig.FLICKRETURNTIME) {
            servo.setPosition(0); // return
        }
        else {
            isFlicking = false;   // flick finished
        }
    }
    private void startFlick() {
        if (isFlicking) return; // prevent double flicks
        isFlicking = true;
        flickTimer.reset();
    }
    public boolean isFull(){
        return !intakeMode;
    }
    public void setQueue(LinkedList<Color> colors){
        queue.clear();
        queue.addAll(colors);
    }
    public boolean queueIsEmpty(){ return queue.isEmpty(); }
    public void rotate1Slot(boolean anticlockwise){
        if(anticlockwise){
            pidController.setTarget(StorageConfig.ENCODERRESOLUTION /3, true); //append
            Color temp = slots[0];
            slots[0] = slots[2];
            slots[2] = slots[1];
            slots[1] = temp;
        }
        else{
            pidController.setTarget(-StorageConfig.ENCODERRESOLUTION /3, true); //append
            Color temp = slots[0];
            slots[0] = slots[1];
            slots[1] = slots[2];
            slots[2] = temp;
        }
    }

    public void goToSlot1AlignedWithShooter(){
        pidController.setTarget(StorageConfig.ENCODERRESOLUTION /2, false); //append
    }
    public void goToSlot1AlignedWithIntake(){
        pidController.setTarget(0, false); //append
    }
    public void update(boolean shootable) {
        intakeMode = (slots[0] == null || slots[1] == null || slots[2] == null);

        // Detect transitions
        boolean justBecameFull  = wasIntakeMode && !intakeMode;
        boolean justBecameEmpty = !wasIntakeMode && intakeMode;

        updateFlick();

        if (intakeMode) {
            if(justBecameEmpty){
                goToSlot1AlignedWithIntake();
            }
            intakeUpdate();
        } else {
            if (justBecameFull) {
                goToSlot1AlignedWithShooter();  // only once when becoming full
            }
            transferUpdate(shootable);
        }

        wasIntakeMode = intakeMode;
        motor.setVelocity(motor.getMotorType().getAchieveableMaxTicksPerSecond() * pidController.calculateScalar(motor.getCurrentPosition()));
    }
    public void intakeUpdate(){
        if(motor.isBusy() || isFlicking){
            return;
        }

        currentColor = ColorMethods.fromSensor(colorSensor);
        if(currentColor != Color.NONE){
            slots[0] = currentColor;
            rotate1Slot(true);
        }
    }
    public void transferUpdate(boolean shootable) {
        if (motor.isBusy() || isFlicking) {
            return;
        }
        if (queue.isEmpty()) {
            return;
        }

        Color desired = queue.peekFirst();

        if (desired == slots[2]) {
            if(shootable){
                startFlick();
                slots[2] = null;
                queue.removeFirst();
            }
        }
        else if (desired == slots[0]) {
            rotate1Slot(true);
        }
        else if (desired == slots[1]) {
            rotate1Slot(false);
        }
        else {
            if (slots[2] != null) {
                if(shootable){
                    startFlick();
                    slots[2] = null;
                    queue.removeFirst();
                }
            }
            else if (slots[0] != null) {
                rotate1Slot(true);
            }
            else if (slots[1] != null) {
                rotate1Slot(false);
            }
        }
    }
    public void sendTelemetry() {
        telemetry.addLine("HARDWARE\n");
        telemetry.addData("SERVO POS", servo.getPosition());
        telemetry.addData("MOTOR POS", motor.getCurrentPosition());
        telemetry.addData("MOTOR TARGET POS", pidController.target);
        telemetry.addData("TARGET TOLERANCE", pidController.tolerance);
        telemetry.addData("MOTOR VEL", motor.getVelocity());
        telemetry.addData("COLOR SENSOR", currentColor.name());

        telemetry.addLine("STORAGE\n");
        telemetry.addData("SLOT 0", slots[0]);
        telemetry.addData("SLOT 1", slots[1]);
        telemetry.addData("SLOT 2", slots[2]);

        telemetry.addLine("FLICKER\n");
        telemetry.addData("FLICKING", isFlicking);
        telemetry.addData("FLICK TIMER (ms)", flickTimer.milliseconds());
    }
}
