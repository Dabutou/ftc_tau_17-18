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
    /*
        //VuForia**************************
        OpenGLMatrix lastLocation = null;
        private VuforiaLocalizer vuforia;
    */
    //IMU******************************
    private BNO055IMU imu;

    private Orientation angles;
    private Acceleration gravity;

    private int backLeftMotorPosition = 0;
    private int backRightMotorPosition = 0;
    private int frontLeftMotorPosition = 0;
    private int frontRightMotorPosition = 0;
    private final int distancetoBlock = 990;


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

    //realigns to initial setup
    public void realign(double speed){
        float heading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        speed(speed);
        frontLeftMotorPosition += (int)(3520.0*heading/360);
        frontRightMotorPosition += (int)(3520.0*heading/360);
        backLeftMotorPosition += (int)(3520.0*heading/360);
        backRightMotorPosition += (int)(3520.0*heading/360);
        robot.frontRightMotor.setTargetPosition(frontRightMotorPosition);
        robot.frontLeftMotor.setTargetPosition(frontLeftMotorPosition);
        robot.backRightMotor.setTargetPosition(backRightMotorPosition);
        robot.backLeftMotor.setTargetPosition(backLeftMotorPosition);
    }

    //negative degree is left turn, positive is right turn
    public void turnDegree(double speed, double degree){
        speed(speed);
        frontLeftMotorPosition += (int)(3520.0*degree/360);
        frontRightMotorPosition += (int)(3520.0*degree/360);
        backLeftMotorPosition += (int)(3520.0*degree/360);
        backRightMotorPosition += (int)(3520.0*degree/360);
        robot.frontRightMotor.setTargetPosition(frontRightMotorPosition);
        robot.frontLeftMotor.setTargetPosition(frontLeftMotorPosition);
        robot.backRightMotor.setTargetPosition(backRightMotorPosition);
        robot.backLeftMotor.setTargetPosition(backLeftMotorPosition);
    }

    //probably don't need left90 and right90
    public void left90(double speed){
        speed(speed);
        setDistances(-880,-880,-880,-880);
        runDistances();
    }
    public void right90(double speed){
        speed(speed);
        setDistances(880,880,880,880);
        runDistances();
    }

    public void sleepTau(long milliSec){try{Thread.sleep(milliSec);}catch(InterruptedException e){throw new RuntimeException(e);}}

    public void closeClaw(){
        robot.leftLiftServo.setPosition(0.35);
        robot.rightLiftServo.setPosition(0);
    }

    public void openClaw(){
        robot.leftLiftServo.setPosition(1);
        robot.rightLiftServo.setPosition(0.65);
    }

    public void readEncoders(){
        telemetry.addData("Front Left", "" + frontLeftMotorPosition + " : " + robot.frontLeftMotor.getCurrentPosition());
        telemetry.addData("Front Right", "" + frontRightMotorPosition + " : " + robot.frontRightMotor.getCurrentPosition());
        telemetry.addData("Front Left", "" + backLeftMotorPosition + " : " + robot.backLeftMotor.getCurrentPosition());
        telemetry.addData("Front Left", "" + backRightMotorPosition + " : " + robot.backRightMotor.getCurrentPosition());
    }

    public void driveForwardStraightDISTANCE(double distance){
        speed(0.6);
        int distancesr = (int)(distancetoBlock*distance);
        int distancesl = (int)((distancetoBlock-5)*distance);
        setDistances(distancesr,-distancesl,distancesl,-distancesr);
        runDistances();
    }
    public void driveBackwardStraightDISTANCE(double distance){
        speed(0.6);
        int distancesr = (int)(distancetoBlock*distance);
        int distancesl = (int)((distancetoBlock-5)*distance);
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
        int distancesl = (int)((distancetoBlock-5)*distance);
        setDistances(distancesr,-distancesl,distancesl,-distancesr);
        runDistances();
    }
    public void driveBackwardStraightDISTANCE(double speed, double distance){
        speed(speed);
        int distancesr = (int)(distancetoBlock*distance);
        int distancesl = (int)((distancetoBlock-5)*distance);
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
    public void driveForwardStraightTIME(double speed, long milliSec){}
    public void driveBackwardStraightTIME(double speed, long milliSec){}
    public void driveRightStraightTIME(double speed, long milliSec){}
    public void driveLeftStraightTIME(double speed, long milliSec) {}
    public void driveNWStraightTIME(double speed, long milliSec){}
    public void driveNEStraightTIME(double speed, long milliSec){}
    public void driveSEStraightTIME(double speed, long milliSec){}
    public void driveSWStraightTIME(double speed, long milliSec){}
    
    /*public boolen isRed(){
        return robot.colorx.colorNumber() == 10;
    }*/


    //BEHIND THE SCENES METHODS
    private void speed(double speed){
        robot.frontLeftMotor.setPower(speed);
        robot.frontRightMotor.setPower(speed);
        robot.backLeftMotor.setPower(speed);
        robot.backRightMotor.setPower(speed);
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
