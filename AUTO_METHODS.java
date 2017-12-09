package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.Locale;

/**
 * Created by Lance He on 10/7/2017.
 */

class AUTO_METHODS extends LinearOpMode{

    Hardware robot = new Hardware();

    //IMU******************************
    private BNO055IMU imu;

    private Orientation angles;
    private Acceleration gravity;

    private int backLeftMotorPosition = 0;
    private int backRightMotorPosition = 0;
    private int frontLeftMotorPosition = 0;
    private int frontRightMotorPosition = 0;
    private int leftLiftPosition = 0;
    private int rightLiftPosition = 0;
    private final int distancetoBlock = 990;
    private final int blockHeight = -750;
    private double vuMarkEnd = 0;
    private boolean doneOnce = false;
    private double jewelEnd = 0;


    public AUTO_METHODS(){}


    //private void initialize(){robot.init(hardwareMap);}

    public void runOpMode() throws InterruptedException{}


    public void IMUandVu(){
        telemetry.addData("Readiness", "NOT READY TO START, PLEASE WAIT");
        telemetry.update();

        robot.init(hardwareMap);
        imu = robot.getImu();

        // Set up our telemetry dashboard
        telemetry.addData("Readiness", "Press Play to start");
        composeTelemetry();
        telemetry.update();

        // Wait until we're told to go
        waitForStart();

        // Start the logging of measured acceleration
        //relicTrackables.activate();
        robot.relicTrackables.activate();
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    }


    //----------------------------------------------------------------------------------------------
    // Telemetry Configuration
    //----------------------------------------------------------------------------------------------


    private void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() { @Override public void run()
        {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
        }
        });

        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override public String value() {
                        return imu.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override public String value() {
                        return imu.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });

        telemetry.addLine()
                .addData("grvty", new Func<String>() {
                    @Override public String value() {
                        return gravity.toString();
                    }
                })
                .addData("mag", new Func<String>() {
                    @Override public String value() {
                        return String.format(Locale.getDefault(), "%.3f",
                                Math.sqrt(gravity.xAccel*gravity.xAccel
                                        + gravity.yAccel*gravity.yAccel
                                        + gravity.zAccel*gravity.zAccel));
                    }
                });
    }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    private String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    private String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }





    //METHODS YOU CALL FOR AUTO

    //IMU clockwise is negative
        //counter-clockwise is positive

    //realigns to initial setup
    public void realign(double speed){
        float heading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        telemetry.addData("IMU", "" + heading);
        updateTelemetry(telemetry);
        speed(speed);
        frontLeftMotorPosition += (int)(3520.0*heading/360);
        frontRightMotorPosition += (int)(3520.0*heading/360);
        backLeftMotorPosition += (int)(3520.0*heading/360);
        backRightMotorPosition += (int)(3520.0*heading/360);
        runDistances();

    }

    //negative degree is left turn, positive is right turn
    public void turnDegree(double speed, double degree){
        speed(speed);
        telemetry.addData("IMU", "" + imu.getAngularOrientation().firstAngle);
        updateTelemetry(telemetry);
        frontLeftMotorPosition += (int)(3520.0*degree/360);
        frontRightMotorPosition += (int)(3520.0*degree/360);
        backLeftMotorPosition += (int)(3520.0*degree/360);
        backRightMotorPosition += (int)(3520.0*degree/360);
        runDistances();
    }
    public float getImuHeading(){
        return imu.getAngularOrientation().firstAngle;
    }
    public void turnToDegree(double speed, double degree){
        speed(speed);
        float heading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        telemetry.addData("IMU", "" + imu.getAngularOrientation().firstAngle);
        updateTelemetry(telemetry);

        if (heading == degree){
            return;
        }
        else if (heading == 0){
            turnDegree(speed,degree);
            return;
        }
        else if (degree == 0){
            realign(speed);
            return;
        }
        degree = -degree;
        double totaldistance = heading - degree;
        double finaldistance;
        if ((heading > 0 && degree > 0) || (heading < 0 && degree < 0)){

            frontLeftMotorPosition += (int)(3520.0*totaldistance/360);
            frontRightMotorPosition += (int)(3520.0*totaldistance/360);
            backLeftMotorPosition += (int)(3520.0*totaldistance/360);
            backRightMotorPosition += (int)(3520.0*totaldistance/360);
        }
        else if (heading > 0 && degree < 0){
            finaldistance = Math.min(totaldistance, 360-totaldistance);
            if (finaldistance == totaldistance){
                frontLeftMotorPosition += (int)(3520.0*finaldistance/360);
                frontRightMotorPosition += (int)(3520.0*finaldistance/360);
                backLeftMotorPosition += (int)(3520.0*finaldistance/360);
                backRightMotorPosition += (int)(3520.0*finaldistance/360);
            }
            else{
                frontLeftMotorPosition -= (int)(3520.0*finaldistance/360);
                frontRightMotorPosition -= (int)(3520.0*finaldistance/360);
                backLeftMotorPosition -= (int)(3520.0*finaldistance/360);
                backRightMotorPosition -= (int)(3520.0*finaldistance/360);
            }
        }
        else if (heading < 0 && degree > 0){
            totaldistance = Math.abs(totaldistance);
            finaldistance = Math.min(totaldistance, 360-totaldistance);
            if (finaldistance == totaldistance){
                frontLeftMotorPosition -= (int)(3520.0*finaldistance/360);
                frontRightMotorPosition -= (int)(3520.0*finaldistance/360);
                backLeftMotorPosition -= (int)(3520.0*finaldistance/360);
                backRightMotorPosition -= (int)(3520.0*finaldistance/360);
            }
            else{
                frontLeftMotorPosition += (int)(3520.0*finaldistance/360);
                frontRightMotorPosition += (int)(3520.0*finaldistance/360);
                backLeftMotorPosition += (int)(3520.0*finaldistance/360);
                backRightMotorPosition += (int)(3520.0*finaldistance/360);
            }
        }
        else{
            telemetry.addData("ERROR","SOME INPUT IN TURN TO DEGREE IS BAD");
            updateTelemetry(telemetry);
        }

        runDistances();
    }
    public String leftGetVu(){

        RelicRecoveryVuMark vumark = RelicRecoveryVuMark.from(robot.relicTemplate);
        vuMarkEnd = robot.getTime() + 5;
        while(vuMarkEnd > robot.getTime()) {
            if(vumark != RelicRecoveryVuMark.UNKNOWN) {
                return "" + vumark;
            }
            else{
                if(!doneOnce){turnDegree(0.06,33);doneOnce = !doneOnce;}
                vumark = RelicRecoveryVuMark.from(robot.relicTemplate);
            }
        }
        return "UNKNOWN AND PROGRAMMERS BAD";
    }

    public String rightGetVu(){

        RelicRecoveryVuMark vumark = RelicRecoveryVuMark.from(robot.relicTemplate);
        vuMarkEnd = robot.getTime() + 5;
        while(vuMarkEnd > robot.getTime()) {
            if(vumark != RelicRecoveryVuMark.UNKNOWN) {
                return "" + vumark;
            }
            else{
                if(!doneOnce){turnDegree(0.06,-33);doneOnce = !doneOnce;}
                vumark = RelicRecoveryVuMark.from(robot.relicTemplate);
            }
        }
        return "CG";
    }

    public int getColor(){
        return robot.color.colorNumber();
    }

    public void getLiftPosition(){
        telemetry.addData("Left Lift Position", robot.leftLiftMotor.getCurrentPosition()+ " : " + leftLiftPosition);
        telemetry.addData("Right Lift Position",  robot.rightLiftMotor.getCurrentPosition() + " : " + rightLiftPosition);
        updateTelemetry(telemetry);
    }
    public void raiseLiftSlightly(){
        liftSpeed(-0.45);
        sleepTau(250);
        liftSpeed(0);
    }
    public void lowerLiftSlightly(){
        liftSpeed(0.45);
        sleepTau(248);
        liftSpeed(0);
    }
    public void raiseLiftMoreSlightly(){
        liftSpeed(-0.45);
        sleepTau(500);
        liftSpeed(0);
    }
    public void lowerLiftMoreSlightly(){
        liftSpeed(0.45);
        sleepTau(498);
        liftSpeed(0);
    }
    public void autoReposition(String vuMark){
        driveBackwardStraightDISTANCE(0.5,0.25);
        sleepTau(800);
        if (vuMark.equals("CENTER")){
            driveForwardStraightDISTANCE(0.5,0.1);
        }
        else if (vuMark.equals("LEFT")){
            driveNWStraightDISTANCE(0.5,0.2);
        }
        else if (vuMark.equals("RIGHT")){
            driveNEStraightDISTANCE(0.5,0.2);
        }
        else{
            driveForwardStraightDISTANCE(0.5,0.1);
        }
        sleepTau(1500);
    }
    public void autoRepositionSideBLUE(String vuMark){
        driveLeftStraightDISTANCE(0.5,0.25);
        sleepTau(800);
        if (vuMark.equals("CENTER")){
            driveRightStraightDISTANCE(0.5,0.1);
        }
        else if (vuMark.equals("LEFT")){
            driveNEStraightDISTANCE(0.5,0.2);
        }
        else if (vuMark.equals("RIGHT")){
            driveSEStraightDISTANCE(0.5,0.2);
        }
        else{
            driveRightStraightDISTANCE(0.5,0.1);
        }
        sleepTau(1500);
    }
    public void autoRepositionSideRED(String vuMark){
        driveRightStraightDISTANCE(0.5,0.25);
        sleepTau(800);
        if (vuMark.equals("CENTER")){
            driveLeftStraightDISTANCE(0.5,0.1);
        }
        else if (vuMark.equals("LEFT")){
            driveSWStraightDISTANCE(0.5,0.2);
        }
        else if (vuMark.equals("RIGHT")){
            driveNWStraightDISTANCE(0.5,0.2);
        }
        else{
            driveLeftStraightDISTANCE(0.5,0.1);
        }
        sleepTau(1500);
    }
   /* public void setLiftStage1(double speed){
        liftSpeed(speed);
        leftLiftPosition = 0;
        rightLiftPosition = 0;
        liftDistances();
    }
    public void setLiftStage2(double speed){
        liftSpeed(speed);
        leftLiftPosition = blockHeight;
        rightLiftPosition = blockHeight;
        liftDistances();
    }
    public void setLiftStage3(double speed){
        liftSpeed(speed);
        leftLiftPosition = 2*blockHeight;
        rightLiftPosition = 2*blockHeight;
        liftDistances();
    }
    public void setLiftStage4(double speed){
        liftSpeed(speed);
        leftLiftPosition = 3*blockHeight;
        rightLiftPosition = 3*blockHeight;
        liftDistances();
    }
    public void raiseLiftStage(double speed){
        liftSpeed(speed);
        leftLiftPosition += blockHeight;
        rightLiftPosition += blockHeight;
        liftDistances();
    }
    public void lowerLiftStage(double speed){
        liftSpeed(speed);
        leftLiftPosition -= blockHeight;
        rightLiftPosition -= blockHeight;
        liftDistances();
    }*/


    public void sleepTau(long milliSec){try{Thread.sleep(milliSec);}catch(InterruptedException e){throw new RuntimeException(e);}}

    public void openClaw(){
        robot.leftLiftServo.setPosition(0.93);
        robot.rightLiftServo.setPosition(0.88);
    }

    public void closeClaw(){
        robot.leftLiftServo.setPosition(0.03);
        robot.rightLiftServo.setPosition(0);
    }
    public int getJewel(){
        jewelEnd = robot.getTime() + 3;
        int jewelValue = 0;
        while(jewelEnd > robot.getTime() && robot.jewelServo.getPosition() > 0.9) {
            jewelValue = getColor();
            if(jewelValue == 3 || jewelValue == 10) {
                return jewelValue;
            }
            else{
                robot.jewelServo.setPosition(robot.jewelServo.getPosition() - 0.001);
            }
        }
        return 69;
    }

    public void lowerJewelServo(){
        robot.jewelServo.setPosition(0.97);
    }
    public void raiseJewelServoSlightly(){robot.jewelServo.setPosition(0.93);}
    public void raiseJewelServo(){
        robot.jewelServo.setPosition(0);
    }

    public void readEncoders(){
        telemetry.addData("Front Left", "" + frontLeftMotorPosition + " : " + robot.frontLeftMotor.getCurrentPosition());
        telemetry.addData("Front Right", "" + frontRightMotorPosition + " : " + robot.frontRightMotor.getCurrentPosition());
        telemetry.addData("Front Left", "" + backLeftMotorPosition + " : " + robot.backLeftMotor.getCurrentPosition());
        telemetry.addData("Front Left", "" + backRightMotorPosition + " : " + robot.backRightMotor.getCurrentPosition());
        updateTelemetry(telemetry);
    }

    public void driveForwardStraightDISTANCE(double distance){
        speed(0.6);
        int distancesr = (int)(distancetoBlock*distance);
        int distancesl = (int)((distancetoBlock-13)*distance);
        setDistances(distancesr,-distancesl,distancesl,-distancesr);
        runDistances();
    }
    public void driveBackwardStraightDISTANCE(double distance){
        speed(0.6);
        int distancesr = (int)(distancetoBlock*distance);
        int distancesl = (int)((distancetoBlock-13)*distance);
        setDistances(-distancesr,distancesl,-distancesl, distancesr);
        runDistances();
    }
    public void driveRightStraightDISTANCE(double distance){
        speed(0.6);
        int distances = (int)(distancetoBlock*distance);
        setDistances(distances,distances,-distances, -distances);
        runDistances();
    }
    public void driveLeftStraightDISTANCE(double distance){
        speed(0.6);
        int distances = (int)(distancetoBlock*distance);
        setDistances(-distances,-distances,distances, distances);
        runDistances();
    }
    public void driveNWStraightDISTANCE(double distance){
        speed(0.6);
        int distances = (int)(distancetoBlock*distance);
        setDistances(0,-distances,distances, 0);
        runDistances();
    }
    public void driveNEStraightDISTANCE(double distance){
        speed(0.6);
        int distances = (int)(distancetoBlock*distance);
        setDistances(distances,0,0, -distances);
        runDistances();
    }
    public void driveSEStraightDISTANCE(double distance){
        speed(0.6);
        int distances = (int)(distancetoBlock*distance);
        setDistances(0,distances,-distances, 0);
        runDistances();
    }
    public void driveSWStraightDISTANCE(double distance){
        speed(0.6);
        int distances = (int)(distancetoBlock*distance);
        setDistances(-distances,0,0, distances);
        runDistances();
    }
    //same with speed
    public void driveForwardStraightDISTANCE(double speed, double distance){
        speed(speed);
        int distancesr = (int)(distancetoBlock*distance);
        int distancesl = (int)((distancetoBlock-13)*distance);
        setDistances(distancesr,-distancesl,distancesl,-distancesr);
        runDistances();
    }
    public void driveBackwardStraightDISTANCE(double speed, double distance){
        speed(speed);
        int distancesr = (int)(distancetoBlock*distance);
        int distancesl = (int)((distancetoBlock-13)*distance);
        setDistances(-distancesr,distancesl,-distancesl, distancesr);
        runDistances();
    }
    public void driveRightStraightDISTANCE(double speed, double distance){
        speed(speed);
        int distances = (int)(distancetoBlock*distance);
        setDistances(distances,distances,-distances, -distances);
        runDistances();
    }
    public void driveLeftStraightDISTANCE(double speed, double distance){
        speed(speed);
        int distances = (int)(distancetoBlock*distance);
        setDistances(-distances,-distances,distances, distances);
        runDistances();
    }
    public void driveNWStraightDISTANCE(double speed, double distance){
        speed(speed);
        int distances = (int)(distancetoBlock*distance);
        setDistances(0,-distances,distances, 0);
        runDistances();
    }
    public void driveNEStraightDISTANCE(double speed, double distance){
        speed(speed);
        int distances = (int)(distancetoBlock*distance);
        setDistances(distances,0,0, -distances);
        runDistances();
    }
    public void driveSEStraightDISTANCE(double speed, double distance){
        speed(speed);
        int distances = (int)(distancetoBlock*distance);
        setDistances(0,distances,-distances, 0);
        runDistances();
    }
    public void driveSWStraightDISTANCE(double speed, double distance){
        speed(speed);
        int distances = (int)(distancetoBlock*distance);
        setDistances(-distances,0,0, distances);
        runDistances();
    }
    public void testColor(){
        while(1==1){
            telemetry.addData("Color",""+ robot.color.colorNumber());
            updateTelemetry(telemetry);
        }
    }

    //BEHIND THE SCENES METHODS
    private void speed(double speed){
        robot.frontLeftMotor.setPower(speed);
        robot.frontRightMotor.setPower(speed);
        robot.backLeftMotor.setPower(speed);
        robot.backRightMotor.setPower(speed);
    }
    private void liftSpeed(double speed){
        robot.leftLiftMotor.setPower(speed);
        robot.rightLiftMotor.setPower(speed);
    }
    private void liftDistances(){
        robot.leftLiftMotor.setTargetPosition(leftLiftPosition);
        robot.rightLiftMotor.setTargetPosition(rightLiftPosition);
    }

    private void setDistances(int fl, int fr, int bl, int br){
        frontLeftMotorPosition += fl;
        frontRightMotorPosition += fr;
        backLeftMotorPosition += bl;
        backRightMotorPosition += br;
    }
    private void runDistances(){
        robot.frontRightMotor.setTargetPosition(frontRightMotorPosition);
        robot.frontLeftMotor.setTargetPosition(frontLeftMotorPosition);
        robot.backRightMotor.setTargetPosition(backRightMotorPosition);
        robot.backLeftMotor.setTargetPosition(backLeftMotorPosition);
    }
}