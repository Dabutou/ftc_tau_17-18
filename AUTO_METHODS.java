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

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }






    //METHODS YOU CALL FOR AUTO

    public void realign(){
        float heading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        int direction = 0;
        turnRobot(1,heading,direction);
    }
    public void sleepTau(long milliSec){try{Thread.sleep(milliSec);}catch(InterruptedException e){throw new RuntimeException(e);}}
    public void driveForwardStraightTIME(long milliSec){driveRobotTIME(1,milliSec,1,-1,1,-1);}
    public void driveBackwardStraightTIME(long milliSec){
        driveRobotTIME(1,milliSec,-1,1,-1,1);
    }
    public void driveRightStraightTIME(long milliSec){driveRobotTIME(1,milliSec,1,1,-1,-1);}
    public void driveLeftStraightTIME(long milliSec){driveRobotTIME(1,milliSec,-1,-1,1,1);}
    public void driveNWStraightTIME(long milliSec){driveRobotTIME(1,milliSec,0,-1,0,1);}
    public void driveNEStraightTIME(long milliSec){driveRobotTIME(1,milliSec,1,0,-1,0);}
    public void driveSEStraightTIME(long milliSec){driveRobotTIME(1,milliSec,0,1,0,-1);}
    public void driveSWStraightTIME(long milliSec){driveRobotTIME(1,milliSec,-1,0,1,0);}
    public void driveForwardStraightTIME(double speed, long milliSec){driveRobotTIME(speed,milliSec,1,-1,1,-1);}
    public void driveBackwardStraightTIME(double speed, long milliSec){driveRobotTIME(speed,milliSec,-1,1,-1,1);}
    public void driveRightStraightTIME(double speed, long milliSec){driveRobotTIME(speed,milliSec,1,1,-1,-1);}
    public void driveLeftStraightTIME(double speed, long milliSec) {driveRobotTIME(speed, milliSec, -1, -1, 1, 1);}
    public void driveNWStraightTIME(double speed, long milliSec){driveRobotTIME(speed,milliSec,0,-1,0,1);}
    public void driveNEStraightTIME(double speed, long milliSec){driveRobotTIME(speed,milliSec,1,0,-1,0);}
    public void driveSEStraightTIME(double speed, long milliSec){driveRobotTIME(speed,milliSec,0,1,0,-1);}
    public void driveSWStraightTIME(double speed, long milliSec){driveRobotTIME(speed,milliSec,-1,0,1,0);}
    
    public void openClaw(){
        robot.leftLiftServo.setPosition(0);
        robot.rightLiftServo.setPosition(1);
    }
     public void closeClaw(){
        robot.leftLiftServo.setPosition(1);
        robot.rightLiftServo.setPosition(0);
    }
    



    //BEHIND THE SCENES METHODS
    private void driveRobotTIME(double speed, long milliSec, double flPower, double frPower, double blPower, double brPower){
        try{
            powerMotors(speed,flPower,frPower,blPower,brPower);
            Thread.sleep(milliSec);
            powerMotors(0,0,0,0,0);
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }
    /*private void driveRobotDISTANCE(double speed, double distance, double flPower, double frPower, double blPower, double brPower){

    }
    public void reAlign(){

    }*/
    public void turnRobot(double speed, double degrees, int direction){
        final double timeToSpin = 1500;
        try{
            //direction = 0: left   direction = 1: right
            if (direction==0){powerMotors(speed,-1,-1,-1,-1);}
            else {powerMotors(speed,1,1,1,1);}
            Thread.sleep((long)(timeToSpin*degrees/360));
            powerMotors(0,0,0,0,0);
        }catch(InterruptedException e){throw new RuntimeException(e);}
    }
    private void powerMotors(double speed, double flPower, double frPower, double blPower, double brPower){
        try {
            robot.frontLeftMotor.setPower(flPower * speed);
            robot.frontRightMotor.setPower(frPower * speed);
            robot.backLeftMotor.setPower(blPower * speed);
            robot.backRightMotor.setPower(brPower * speed);
        } catch(NullPointerException e){
            throw new NullPointerException(e.toString());
        }
    }
}
