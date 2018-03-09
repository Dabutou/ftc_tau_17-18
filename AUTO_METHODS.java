package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by Lance He on 10/7/2017.
 */

class AUTO_METHODS extends LinearOpMode{

    Hardware robot = new Hardware();
    private static final String TAG = "LHActivity";

    //IMU******************************
    private BNO055IMU imu;

    private Orientation angles;
    private Acceleration gravity;

    //private ModernRoboticsI2cRangeSensor frontRangeSensor;
    private ModernRoboticsI2cRangeSensor rightRangeSensor;

    private int backLeftMotorPosition = 0;
    private int backRightMotorPosition = 0;
    private int frontLeftMotorPosition = 0;
    private int frontRightMotorPosition = 0;
    private int frontLeftMotorPositionGradiant = 0;
    private int frontRightMotorPositionGradiant = 0;
    private int backLeftMotorPositionGradiant = 0;
    private int backRightMotorPositionGradiant = 0;
    private int frontLeftMotorPositionFirstGradiant = 0;
    private int frontRightMotorPositionFirstGradiant = 0;
    private int backLeftMotorPositionFirstGradiant = 0;
    private int backRightMotorPositionFirstGradiant = 0;
    private boolean frontLeftGradiantSign;
    private boolean frontRightGradiantSign;
    private boolean backLeftGradiantSign;
    private boolean backRightGradiantSign;
    private int leftLiftPosition = 0;
    private int rightLiftPosition = 0;
    private final int distancetoBlock = 1060;
    private final int blockHeight = -750;
    private double vuMarkEnd = 0;
    private boolean doneOnce = false;
    private double jewelEnd = 0;
    private double speed = 0;


    public AUTO_METHODS(){}


    //private void initialize(){robot.init(hardwareMap);}

    public void runOpMode() throws InterruptedException {}


    public void IMUandVu(){
        telemetry.addData("Readiness", "NOT READY TO START, PLEASE WAIT");
        telemetry.update();

        robot.init(hardwareMap);
        imu = robot.getImu();
        //frontRangeSensor = robot.frontRangeSensor;
        rightRangeSensor = robot.rightRangeSensor;
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
        resetEncoderToReal();
        float heading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        Log.i(TAG,"IMU Heading: "+heading);
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
        resetEncoderToReal();
        speed(speed);
        Log.i(TAG,"IMU Heading: "+imu.getAngularOrientation().firstAngle);
        telemetry.addData("IMU", "" + imu.getAngularOrientation().firstAngle);
        updateTelemetry(telemetry);
        frontLeftMotorPosition += (int)(3520.0*degree/360);
        frontRightMotorPosition += (int)(3520.0*degree/360);
        backLeftMotorPosition += (int)(3520.0*degree/360);
        backRightMotorPosition += (int)(3520.0*degree/360);
        runDistances();
    }
    public void getImuHeading(){
        telemetry.addData("IMU HEADINGS", imu.getAngularOrientation().firstAngle + " : " + imu.getAngularOrientation().secondAngle + " : " + imu.getAngularOrientation().thirdAngle);
        updateTelemetry(telemetry);
    }
    public void turnToDegree(double speed, double degree){
        speed(speed);
        float heading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        Log.i(TAG,"IMU Heading: "+heading);
        telemetry.addData("IMU", "" + heading);
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
            Log.wtf(TAG,"TURN TO DEGREE CLANKED!!!!!!!!!!!!!!!!");
            telemetry.addData("ERROR","SOME INPUT IN TURN TO DEGREE IS BAD");
            updateTelemetry(telemetry);
        }

        runDistances();
    }
    public void glideFindSpotFL(String vuMark){
        double glideEnd = robot.getTime() + 6.5;
        double minTimeBetween = 0;
        int cryptoCounter = 0;
        double distValues = rightRangeSensor.getDistance(DistanceUnit.CM);
        double tempValues = distValues;
        driveForwardStraightDISTANCE(0.11,1.1);
        boolean done = false;


        while (glideEnd > robot.getTime()){
            tempValues = rightRangeSensor.getDistance(DistanceUnit.CM);
            if (tempValues < 1000 ){
                if (distValues - tempValues > 2 && robot.getTime() > minTimeBetween){
                    cryptoCounter++;
                    minTimeBetween = robot.getTime()+0.30;
                }
                distValues = tempValues;}
            Log.i(TAG,"RIGHT RANGE " + rightRangeSensor.getDistance(DistanceUnit.CM) + " CRYPTO: " + cryptoCounter);
            telemetry.addData("RANGE","RIGHT: " + distValues+" CRYPTO: " + cryptoCounter);
            updateTelemetry(telemetry);
            if (vuMark.equals("RIGHT") && cryptoCounter>=1 &&  !done){
                stopRobot();
                sleepTau(250);
                driveLeftStraightDISTANCE(0.3,0.25);
                sleepTau(1300);
                turnToDegree(0.45,90);
                sleepTau(1050);
                driveForwardStraightDISTANCE(0.3,1);
                sleepTau(1500);
                stopRobot();
                /*pokeClaw();
                sleepTau(100);
                driveBackwardStraightDISTANCE(0.6,0.45);
                sleepTau(700);
                turnToDegree(0.6,-90);
                sleepTau(1500);
                lowerLiftSlightly();
                driveForwardStraightDISTANCE(0.6,1.1);
                sleepTau(1200);
                closeClaw();
                sleepTau(200);
                raiseLiftEntireBlock();
                driveBackwardStraightDISTANCE(0.6,0.4);
                sleepTau(700);
                turnToDegree(0.6,90);
                sleepTau(1300);
                driveForwardStraightDISTANCE(0.3,1);
                sleepTau(1500);*/
                openClaw();
                sleepTau(750);
                driveBackwardStraightDISTANCE(0.3,0.54);
                sleepTau(1100);
                turnDegree(0.55,-180);
                sleepTau(1250);
                lowerLiftMoreSlightly();

                done = true;
                break;
            }
            if ((vuMark.equals("CENTER") || vuMark.equals("CG")) && cryptoCounter >= 2 && !done){
                stopRobot();
                sleepTau(250);
                driveLeftStraightDISTANCE(0.3,0.25);
                sleepTau(1300);
                turnToDegree(0.45,90);
                sleepTau(1050);
                driveForwardStraightDISTANCE(0.3,1);
                sleepTau(1500);
                stopRobot();
                /*pokeClaw();
                sleepTau(100);
                driveBackwardStraightDISTANCE(0.6,0.45);
                sleepTau(700);
                turnToDegree(0.6,-90);
                sleepTau(1500);
                lowerLiftSlightly();
                driveForwardStraightDISTANCE(0.6,1.1);
                sleepTau(1200);
                closeClaw();
                sleepTau(200);
                raiseLiftEntireBlock();
                driveBackwardStraightDISTANCE(0.6,0.4);
                sleepTau(700);
                turnToDegree(0.6,90);
                sleepTau(1300);
                driveForwardStraightDISTANCE(0.3,1);
                sleepTau(1500);*/
                openClaw();
                sleepTau(750);
                driveBackwardStraightDISTANCE(0.3,0.54);
                sleepTau(1100);
                turnDegree(0.55,-180);
                sleepTau(1250);
                lowerLiftMoreSlightly();

                done = true;
                break;
            }
            if (vuMark.equals("LEFT") && cryptoCounter >= 3 && !done){
                stopRobot();
                sleepTau(250);
                driveLeftStraightDISTANCE(0.3,0.25);
                sleepTau(1300);
                turnToDegree(0.45,90);
                sleepTau(1050);
                driveForwardStraightDISTANCE(0.3,1);
                sleepTau(1500);
                stopRobot();
                /*pokeClaw();
                sleepTau(100);
                driveBackwardStraightDISTANCE(0.6,0.45);
                sleepTau(700);
                turnToDegree(0.6,-90);
                sleepTau(1500);
                lowerLiftSlightly();
                driveForwardStraightDISTANCE(0.6,1.1);
                sleepTau(1200);
                closeClaw();
                sleepTau(200);
                raiseLiftEntireBlock();
                driveBackwardStraightDISTANCE(0.6,0.4);
                sleepTau(700);
                turnToDegree(0.6,90);
                sleepTau(1300);
                driveForwardStraightDISTANCE(0.3,1);
                sleepTau(1500);*/
                openClaw();
                sleepTau(750);
                driveBackwardStraightDISTANCE(0.3,0.54);
                sleepTau(1100);
                turnDegree(0.55,-180);
                sleepTau(1250);
                lowerLiftMoreSlightly();

                done = true;
                break;
            }

        }
        if (!done) {
            glideEnd = robot.getTime() + 6;
            cryptoCounter = 0;
            distValues = rightRangeSensor.getDistance(DistanceUnit.CM);
            tempValues = distValues;
            driveBackwardStraightDISTANCE(0.11,1.1);
            while (glideEnd > robot.getTime()){
                tempValues = rightRangeSensor.getDistance(DistanceUnit.CM);
                if (tempValues < 1000 ){
                    if (distValues - tempValues > 2 && robot.getTime() > minTimeBetween){
                        cryptoCounter++;
                        minTimeBetween = robot.getTime()+0.45;
                    }
                    distValues = tempValues;
                }
                Log.i(TAG,"RIGHT RANGE " + rightRangeSensor.getDistance(DistanceUnit.CM) + " CRYPTO: " + cryptoCounter);
                if (vuMark.equals("LEFT") && cryptoCounter>=1 &&  !done){
                    stopRobot();
                    sleepTau(250);
                    driveLeftStraightDISTANCE(0.3,0.25);
                    sleepTau(1300);
                    turnToDegree(0.45,90);
                    sleepTau(1050);
                    driveForwardStraightDISTANCE(0.3,1);
                    sleepTau(1500);
                    stopRobot();
                    openClaw();
                    sleepTau(50);
                    driveBackwardStraightDISTANCE(0.3,0.45);
                    sleepTau(1000);
                    break;
                }
                if ((vuMark.equals("CENTER") || vuMark.equals("CG")) && cryptoCounter >= 2 && !done){
                    stopRobot();
                    sleepTau(250);
                    driveLeftStraightDISTANCE(0.3,0.25);
                    sleepTau(1300);
                    turnToDegree(0.45,90);
                    sleepTau(1050);
                    driveForwardStraightDISTANCE(0.3,1);
                    sleepTau(1500);
                    stopRobot();
                    openClaw();
                    sleepTau(50);
                    driveBackwardStraightDISTANCE(0.3,0.45);
                    sleepTau(1000);
                    break;
                }
                if (vuMark.equals("RIGHT") && cryptoCounter >= 3 && !done){
                    stopRobot();
                    sleepTau(250);
                    driveLeftStraightDISTANCE(0.3,0.25);
                    sleepTau(1300);
                    turnToDegree(0.45,90);
                    sleepTau(1050);
                    driveForwardStraightDISTANCE(0.3,1);
                    sleepTau(1500);
                    stopRobot();
                    openClaw();
                    sleepTau(50);
                    driveBackwardStraightDISTANCE(0.3,0.45);
                    sleepTau(1000);
                    break;
                }

            }
        }

    }
    public void glideFindSpotFR(String vuMark){
        double glideEnd = robot.getTime() + 6.5;
        double minTimeBetween = 0;
        int cryptoCounter = 0;
        double distValues = rightRangeSensor.getDistance(DistanceUnit.CM);
        double tempValues = distValues;
        driveForwardStraightDISTANCE(0.11,1.1);
        boolean done = false;


        while (glideEnd > robot.getTime()){
            tempValues = rightRangeSensor.getDistance(DistanceUnit.CM);
            if (tempValues < 1000 ){
                if (distValues - tempValues > 2 && robot.getTime() > minTimeBetween){
                    cryptoCounter++;
                    minTimeBetween = robot.getTime()+0.45;
                }
                distValues = tempValues;}
            Log.i(TAG,"RIGHT RANGE " + rightRangeSensor.getDistance(DistanceUnit.CM) + " CRYPTO: " + cryptoCounter);
            telemetry.addData("RANGE","RIGHT: " + distValues+" CRYPTO: " + cryptoCounter);
            updateTelemetry(telemetry);
            if (vuMark.equals("RIGHT") && cryptoCounter>=1 &&  !done){
                done = sequenceRR();
                break;
            }
            if ((vuMark.equals("CENTER") || vuMark.equals("CG")) && cryptoCounter >= 2 && !done){
                done = sequenceRR();
                break;
            }
            if (vuMark.equals("LEFT") && cryptoCounter >= 3 && !done){
                done = sequenceRR();
                break;
            }

        }
        if (!done) {
            glideEnd = robot.getTime() + 6;
            distValues = rightRangeSensor.getDistance(DistanceUnit.CM);
            tempValues = distValues;
            driveBackwardStraightDISTANCE(0.1,1.1);
            cryptoCounter = 0;
            while (glideEnd > robot.getTime()){
                tempValues = rightRangeSensor.getDistance(DistanceUnit.CM);
                if (tempValues < 1000 ){

                    if (distValues - tempValues > 2 && robot.getTime() > minTimeBetween){
                        cryptoCounter++;
                        minTimeBetween = robot.getTime()+0.45;
                    }
                    distValues = tempValues;}
                Log.i(TAG,"RIGHT RANGE " + rightRangeSensor.getDistance(DistanceUnit.CM) + " CRYPTO: " + cryptoCounter);
                if (vuMark.equals("LEFT") && cryptoCounter>=1 &&  !done){
                    sequenceRR();
                    break;
                }
                if ((vuMark.equals("CENTER") || vuMark.equals("CG")) && cryptoCounter >= 2 && !done){
                    sequenceRR();
                    break;
                }
                if (vuMark.equals("RIGHT") && cryptoCounter >= 3 && !done){
                    sequenceRR();
                    break;
                }

            }
        }

    }
    public void glideFindSpotBACKBL(String vuMark){
        double glideEnd = robot.getTime() + 6.5;
        double minTimeBetween = 0;
        int cryptoCounter = 0;
        double distValues = rightRangeSensor.getDistance(DistanceUnit.CM);
        double tempValues = distValues;
        driveBackwardStraightDISTANCE(0.11,1.1);
        boolean done = false;


        while (glideEnd > robot.getTime()){
            tempValues = rightRangeSensor.getDistance(DistanceUnit.CM);
            if (tempValues < 1000 ){
                if (distValues - tempValues > 2 && robot.getTime() > minTimeBetween){
                    cryptoCounter++;
                    minTimeBetween = robot.getTime()+0.45;
                }
                distValues = tempValues;}
            Log.i(TAG,"RIGHT RANGE " + rightRangeSensor.getDistance(DistanceUnit.CM) + " CRYPTO: " + cryptoCounter);
            //telemetry.addData("RANGE","RIGHT: " + distValues+" CRYPTO: " + cryptoCounter);
            //updateTelemetry(telemetry);
            if (vuMark.equals("LEFT") && cryptoCounter>=1 &&  !done){
                done = sequenceBL();
                break;
            }
            if ((vuMark.equals("CENTER") || vuMark.equals("CG")) && cryptoCounter >= 2 && !done){
                done = sequenceBL();
                break;
            }
            if (vuMark.equals("RIGHT") && cryptoCounter >= 3 && !done){
                done = sequenceBL();
                break;
            }

        }
        if (!done) {
            glideEnd = robot.getTime() + 6;
            distValues = rightRangeSensor.getDistance(DistanceUnit.CM);
            tempValues = distValues;
            driveForwardStraightDISTANCE(0.1,1.1);
            cryptoCounter = 0;
            while (glideEnd > robot.getTime()){
                tempValues = rightRangeSensor.getDistance(DistanceUnit.CM);
                if (tempValues < 1000 ){
                    if (distValues - tempValues > 2 && robot.getTime() > minTimeBetween){
                        cryptoCounter++;
                        minTimeBetween = robot.getTime()+0.45;
                    }
                    distValues = tempValues;}
                Log.i(TAG,"RIGHT RANGE " + rightRangeSensor.getDistance(DistanceUnit.CM) + " CRYPTO: " + cryptoCounter);
                if (vuMark.equals("RIGHT") && cryptoCounter>=1 &&  !done){
                    sequenceBL();
                    break;
                }
                if ((vuMark.equals("CENTER") || vuMark.equals("CG")) && cryptoCounter >= 2 && !done){
                    sequenceBL();
                    break;
                }
                if (vuMark.equals("LEFT") && cryptoCounter >= 3 && !done){
                    sequenceBL();
                    break;
                }

            }
        }

    }
    public void glideFindSpotBACKBR(String vuMark){
        double glideEnd = robot.getTime() + 6.5;
        double minTimeBetween = 0;
        int cryptoCounter = 0;
        double distValues = rightRangeSensor.getDistance(DistanceUnit.CM);
        double tempValues = distValues;
        driveBackwardStraightDISTANCE(0.11,1.1);
        boolean done = false;


        while (glideEnd > robot.getTime()){
            tempValues = rightRangeSensor.getDistance(DistanceUnit.CM);
            if (tempValues < 1000 ){
                if (distValues - tempValues > 2 && robot.getTime() > minTimeBetween){
                    cryptoCounter++;
                    minTimeBetween = robot.getTime()+0.30;
                }
                distValues = tempValues;}
            Log.i(TAG,"RIGHT RANGE " + rightRangeSensor.getDistance(DistanceUnit.CM) + " CRYPTO: " + cryptoCounter);
            //telemetry.addData("RANGE","RIGHT: " + distValues+" CRYPTO: " + cryptoCounter);
            //updateTelemetry(telemetry);
            if (vuMark.equals("LEFT") && cryptoCounter>=1 &&  !done){
                stopRobot();
                sleepTau(250);
                driveLeftStraightDISTANCE(0.3,0.25);
                sleepTau(1100);
                turnToDegree(0.45,86);
                sleepTau(1050);
                driveForwardStraightDISTANCE(0.3,1);
                sleepTau(1300);
                stopRobot();
                openClaw();
                sleepTau(650);
                driveBackwardStraightDISTANCE(0.3,0.54);
                sleepTau(1100);
                turnDegree(0.55,-180);
                sleepTau(1250);
                lowerLiftMoreSlightly();
                done = true;
                break;

            }
            if ((vuMark.equals("CENTER") || vuMark.equals("CG")) && cryptoCounter >= 2 && !done){
                stopRobot();
                sleepTau(250);
                driveLeftStraightDISTANCE(0.3,0.25);
                sleepTau(1100);
                turnToDegree(0.45,86);
                sleepTau(1050);
                driveForwardStraightDISTANCE(0.3,1);
                sleepTau(1300);
                stopRobot();
                openClaw();
                sleepTau(650);
                driveBackwardStraightDISTANCE(0.3,0.54);
                sleepTau(1100);
                turnDegree(0.55,-180);
                sleepTau(1250);
                lowerLiftMoreSlightly();
                done = true;
                break;
            }
            if (vuMark.equals("RIGHT") && cryptoCounter >= 3 && !done){
                stopRobot();
                sleepTau(250);
                driveLeftStraightDISTANCE(0.3,0.25);
                sleepTau(1100);
                turnToDegree(0.45,86);
                sleepTau(1050);
                driveForwardStraightDISTANCE(0.3,1);
                sleepTau(1300);
                stopRobot();
                openClaw();
                sleepTau(650);
                driveBackwardStraightDISTANCE(0.3,0.54);
                sleepTau(1100);
                turnDegree(0.55,-180);
                sleepTau(1250);
                lowerLiftMoreSlightly();
                done = true;
                break;
            }

        }
        if (!done) {
            glideEnd = robot.getTime() + 6;
            distValues = rightRangeSensor.getDistance(DistanceUnit.CM);
            tempValues = distValues;
            driveForwardStraightDISTANCE(0.1,1.1);
            cryptoCounter = 0;
            while (glideEnd > robot.getTime()){
                tempValues = rightRangeSensor.getDistance(DistanceUnit.CM);
                if (tempValues < 1000 ){
                    if (distValues - tempValues > 2 && robot.getTime() > minTimeBetween){
                        cryptoCounter++;
                        minTimeBetween = robot.getTime()+0.45;
                    }
                    distValues = tempValues;}
                Log.i(TAG,"RIGHT RANGE " + rightRangeSensor.getDistance(DistanceUnit.CM) + " CRYPTO: " + cryptoCounter);
                if (vuMark.equals("RIGHT") && cryptoCounter>=1 &&  !done){
                    stopRobot();
                    sleepTau(250);
                    driveLeftStraightDISTANCE(0.3,0.25);
                    sleepTau(1300);
                    turnToDegree(0.45,86);
                    sleepTau(1050);
                    driveForwardStraightDISTANCE(0.3,1);
                    sleepTau(1500);
                    stopRobot();
                    openClaw();
                    sleepTau(50);
                    driveBackwardStraightDISTANCE(0.3,0.45);
                    sleepTau(1000);
                    break;
                }
                if ((vuMark.equals("CENTER") || vuMark.equals("CG")) && cryptoCounter >= 2 && !done){
                    stopRobot();
                    sleepTau(250);
                    driveLeftStraightDISTANCE(0.3,0.25);
                    sleepTau(1300);
                    turnToDegree(0.45,86);
                    sleepTau(1050);
                    driveForwardStraightDISTANCE(0.3,1);
                    sleepTau(1500);
                    stopRobot();
                    openClaw();
                    sleepTau(50);
                    driveBackwardStraightDISTANCE(0.3,0.45);
                    sleepTau(1000);
                    break;
                }
                if (vuMark.equals("LEFT") && cryptoCounter >= 3 && !done){
                    sleepTau(250);
                    stopRobot();
                    sleepTau(250);
                    driveLeftStraightDISTANCE(0.3,0.25);
                    sleepTau(1300);
                    turnToDegree(0.45,86);
                    sleepTau(1050);
                    driveForwardStraightDISTANCE(0.3,1);
                    sleepTau(1500);
                    stopRobot();
                    openClaw();
                    sleepTau(50);
                    driveBackwardStraightDISTANCE(0.3,0.45);
                    sleepTau(1000);
                    break;
                }

            }
        }

    }

    /*public void glideFindFrontWall(){
        double glideEnd = robot.getTime() + 10;
        driveForwardStraightDISTANCE(0.11,3);
        while (glideEnd > robot.getTime()){
            if (frontGetRange() <= 35){
                break;
            }
            telemetry.addData("FRONT RANGE",frontGetRange());
            updateTelemetry(telemetry);
        }
        stopRobot();

    }*/
    public void glideFindRightWall(){
        double glideEnd = robot.getTime() + 2;
        driveRightStraightDISTANCE(0.1,3);

        while (glideEnd > robot.getTime()){
            if (rightRangeSensor.getDistance(DistanceUnit.CM) <= 23){
                break;
            }
            Log.i(TAG,"RIGHT RANGE " + rightRangeSensor.getDistance(DistanceUnit.CM));
            telemetry.addData("RIGHT RANGE",rightRangeSensor.getDistance(DistanceUnit.CM));
            updateTelemetry(telemetry);

        }
        stopRobot();

    }
    public void glideFindRightWallBlue(){
        double glideEnd = robot.getTime() + 10;
        driveRightStraightDISTANCE(0.15,3);
        while (glideEnd > robot.getTime()){
            if (rightRangeSensor.getDistance(DistanceUnit.CM) <= 27){
                break;
            }
            Log.i(TAG,"RIGHT RANGE " + rightRangeSensor.getDistance(DistanceUnit.CM));
            telemetry.addData("RIGHT RANGE",rightRangeSensor.getDistance(DistanceUnit.CM));
            updateTelemetry(telemetry);

        }
        stopRobot();

    }
    /*public String leftGetVu(){

        RelicRecoveryVuMark vumark = RelicRecoveryVuMark.from(robot.relicTemplate);
        vuMarkEnd = robot.getTime() + 5.2;
        while(vuMarkEnd > robot.getTime()) {
            if(vumark != RelicRecoveryVuMark.UNKNOWN) {
                return "" + vumark;
            }
            else{
                if(!doneOnce){turnDegree(0.06,36);doneOnce = !doneOnce;}
                vumark = RelicRecoveryVuMark.from(robot.relicTemplate);
            }
        }
        return "UNKNOWN AND PROGRAMMERS BAD";
    }*/
    public String rightGetVu(){

        RelicRecoveryVuMark vumark = RelicRecoveryVuMark.from(robot.relicTemplate);
        vuMarkEnd = robot.getTime() + 4;
        while(vuMarkEnd > robot.getTime()) {
            if(vumark != RelicRecoveryVuMark.UNKNOWN) {
                return "" + vumark;
            }
            else{
                if(!doneOnce){turnDegree(0.14,-17.5);doneOnce = !doneOnce;}
                vumark = RelicRecoveryVuMark.from(robot.relicTemplate);
            }
        }
        return "CG";
    }
    public String rightGetVuSMALL(){
        RelicRecoveryVuMark vumark = RelicRecoveryVuMark.from(robot.relicTemplate);
        vuMarkEnd = robot.getTime() + 2;
        while(vuMarkEnd > robot.getTime()) {
            if(vumark != RelicRecoveryVuMark.UNKNOWN) {
                Log.i(TAG,"Vumark: "+vumark+"; Heading "+imu.getAngularOrientation().firstAngle + "; roll "+imu.getAngularOrientation().thirdAngle);
                return "" + vumark;
            }
            else{
                if(!doneOnce){turnDegree(0.14,-7.5);doneOnce = !doneOnce;}
                vumark = RelicRecoveryVuMark.from(robot.relicTemplate);
            }
        }
        Log.i(TAG,"Vumark: didn't get; Heading "+imu.getAngularOrientation().firstAngle + "; roll "+imu.getAngularOrientation().thirdAngle);
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
        liftSpeed(-0.65);
        sleepTau(340);
        liftSpeed(0);
    }
    public void lowerLiftSlightly(){
        liftSpeed(0.65);
        sleepTau(290);
        liftSpeed(0);
    }
    public void raiseLiftMoreSlightly(){
        liftSpeed(-0.45);
        sleepTau(450);
        liftSpeed(0);
    }
    public void lowerLiftMoreSlightly(){
        liftSpeed(0.40);
        sleepTau(290);
        liftSpeed(0);
    }
    public void raiseLiftEntireBlock(){
        liftSpeed(-1);
        sleepTau(1000);
        liftSpeed(0);
    }
    public void lowerLiftEntireBlock(){
        liftSpeed(0.8);
        sleepTau(450);
        liftSpeed(0);
    }
    public void autoRepositionFRONT(String vuMark){

        if ((vuMark.equals("CENTER") || vuMark.equals("CG"))){
            return;
        }
        else if (vuMark.equals("LEFT")){
            driveLeftStraightDISTANCE(0.5,0.35);
        }
        else if (vuMark.equals("RIGHT")){
            driveRightStraightDISTANCE(0.5,0.35);
        }
        else{
            driveRightStraightDISTANCE(0.5,0.35);
        }
        sleepTau(1500);
    }
    public void autoRepositionFRONT2(String vuMark){
        if ((vuMark.equals("CENTER") || vuMark.equals("CG"))){
            return;
        }
        else if (vuMark.equals("RIGHT")){
            driveLeftStraightDISTANCE(0.5,0.35);
        }
        else if (vuMark.equals("LEFT")){
            driveRightStraightDISTANCE(0.5,0.35);
        }
        else{
            driveRightStraightDISTANCE(0.5,0.35);
        }
        sleepTau(1500);
    }
    public void autoRepositionSIDEBLUE(String vuMark){
        if ((vuMark.equals("CENTER") || vuMark.equals("CG"))){
            return;
        }
        else if (vuMark.equals("LEFT")){
            driveForwardStraightDISTANCE(0.5,0.35);
        }
        else if (vuMark.equals("RIGHT")){
            driveBackwardStraightDISTANCE(0.5,0.35);
        }
        else{
            driveBackwardStraightDISTANCE(0.5,0.35);
        }
        sleepTau(1500);
    }
    public void autoRepositionSideRED(String vuMark){
        if ((vuMark.equals("CENTER") || vuMark.equals("CG"))){
            return;
        }
        else if (vuMark.equals("LEFT")){
            driveBackwardStraightDISTANCE(0.5,0.35);
        }
        else if (vuMark.equals("RIGHT")){
            driveForwardStraightDISTANCE(0.5,0.35);
        }
        else{
            driveBackwardStraightDISTANCE(0.5,0.35);
        }
        sleepTau(1500);
    }

    public void sleepTau(long milliSec){try{Thread.sleep(milliSec);Log.i(TAG,"IMU Heading: "+imu.getAngularOrientation().firstAngle);}catch(InterruptedException e){throw new RuntimeException(e);}}
    public void sleepTauCheck(long milliSec)
    {
        long time = 0;
        try{
            if (speed > 0.25) {
                speedNoChange(speed * 0.55);
            }
            else{
                speedNoChange(0.15);
            }
            runDistances();
            while (time < milliSec){
                Thread.sleep(3);
                if (checkInitGradiants()){
                    speed(speed);
                    runDistances();
                    break;
                }
                time += 3;
            }
            while (time < milliSec){
                Thread.sleep(3);
                if (checkEndGradiants()){
                    telemetry.addData("Speed", speed);
                    updateTelemetry(telemetry);
                    if (speed > 0.1){
                        speed-=0.3;
                        speed(speed);
                    }
                    else{
                        break;
                    }
                    runDistances();
                    time+=1;
                }else{
                    time+=3;
                }


            }
            if (time < milliSec){
                Thread.sleep(milliSec-time);
            }
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void pokeClaw(){
        robot.leftLiftServo.setPosition(0.54);
        robot.rightLiftServo.setPosition(0.40);
    }

    public void openClaw(){
        robot.leftLiftServo.setPosition(0.89);
        robot.rightLiftServo.setPosition(0.75);
    }
    public void closeClaw(){
        robot.leftLiftServo.setPosition(0.29);
        robot.rightLiftServo.setPosition(0.15);
    }
    public int getJewel(){
        jewelEnd = robot.getTime() + 1;
        int jewelValue = 0;
        while(jewelEnd > robot.getTime() && robot.jewelServo.getPosition() > 0.63) {
            jewelValue = getColor();
            if(jewelValue == 3 || jewelValue == 10) {
                return jewelValue;
            }
            else{
                robot.jewelServo.setPosition(robot.jewelServo.getPosition() - 0.0012);
            }
        }
        return 69;
    }

    public void lowerJewelServo(){
        robot.jewelServo.setPosition(0.95);
    }
    public void raiseJewelServoSlightly(){robot.jewelServo.setPosition(0.93);}
    public void raiseJewelServo(){
        robot.jewelServo.setPosition(0);
    }


    public void driveForwardStraightDISTANCE(double distance){
        resetEncoderToReal();
        speed(0.55);
        resetGradiants();
        int distances = (int)(distancetoBlock*distance);
        int distancegradiant = (int)(distances-distancetoBlock*1.6*speed);
        frontLeftGradiantSign = true;
        frontRightGradiantSign = false;
        backLeftGradiantSign = true;
        backRightGradiantSign = false;
        setDistances(distances,-distances,distances,-distances);
        setGradiants(distancegradiant,-distancegradiant,distancegradiant,-distancegradiant);
        runDistances();

    }
    public void driveBackwardStraightDISTANCE(double distance){
        resetEncoderToReal();
        speed(0.55);
        resetGradiants();
        int distances = (int)(distancetoBlock*distance);
        int distancegradiant = (int)(distances-distancetoBlock*1.6*speed);
        frontLeftGradiantSign = false;
        frontRightGradiantSign = true;
        backLeftGradiantSign = false;
        backRightGradiantSign = true;
        setDistances(-distances,distances,-distances, distances);
        setGradiants(-distancegradiant,distancegradiant,-distancegradiant,distancegradiant);
        runDistances();

    }
    public void driveRightStraightDISTANCE(double distance){
        resetEncoderToReal();
        speed(0.55);
        resetGradiants();
        int distances = (int)(distancetoBlock*distance);
        int distancegradiant = (int)(distances-distancetoBlock*1.6*speed);
        frontLeftGradiantSign = true;
        frontRightGradiantSign = true;
        backLeftGradiantSign = false;
        backRightGradiantSign = false;
        setDistances(distances,distances,-distances, -distances);
        setGradiants(distancegradiant,distancegradiant,-distancegradiant,-distancegradiant);
        runDistances();

    }
    public void driveLeftStraightDISTANCE(double distance){
        resetEncoderToReal();
        speed(0.55);
        resetGradiants();
        int distances = (int)(distancetoBlock*distance);
        int distancegradiant = (int)(distances-distancetoBlock*1.6*speed);
        frontLeftGradiantSign = false;
        frontRightGradiantSign = false;
        backLeftGradiantSign = true;
        backRightGradiantSign = true;
        setDistances(-distances,-distances,distances, distances);
        setGradiants(-distancegradiant,-distancegradiant,distancegradiant,distancegradiant);
        runDistances();
    }
    public void driveNWStraightDISTANCE(double distance){
        resetEncoderToReal();
        speed(0.55);
        int distances = (int)(distancetoBlock*distance);
        setDistances(0,-distances,distances, 0);
        runDistances();
    }
    public void driveNEStraightDISTANCE(double distance){
        resetEncoderToReal();
        speed(0.55);
        int distances = (int)(distancetoBlock*distance);
        setDistances(distances,0,0, -distances);
        runDistances();
    }
    public void driveSEStraightDISTANCE(double distance){
        resetEncoderToReal();
        speed(0.55);
        int distances = (int)(distancetoBlock*distance);
        setDistances(0,distances,-distances, 0);
        runDistances();
    }
    public void driveSWStraightDISTANCE(double distance){
        resetEncoderToReal();
        speed(0.55);
        int distances = (int)(distancetoBlock*distance);
        setDistances(-distances,0,0, distances);
        runDistances();
    }
    //same with speed
    public void driveForwardStraightDISTANCE(double speed, double distance){
        resetEncoderToReal();
        speed(speed);
        resetGradiants();
        int distances = (int)(distancetoBlock*distance);
        int distancegradiant = (int)(distances-distancetoBlock*1.6*speed);
        frontLeftGradiantSign = true;
        frontRightGradiantSign = false;
        backLeftGradiantSign = true;
        backRightGradiantSign = false;
        setDistances(distances,-distances,distances,-distances);
        setGradiants(distancegradiant,-distancegradiant,distancegradiant,-distancegradiant);
        runDistances();
    }
    public void driveBackwardStraightDISTANCE(double speed, double distance){
        resetEncoderToReal();
        speed(speed);
        resetGradiants();
        int distances = (int)(distancetoBlock*distance);
        int distancegradiant = (int)(distances-distancetoBlock*1.6*speed);
        frontLeftGradiantSign = false;
        frontRightGradiantSign = true;
        backLeftGradiantSign = false;
        backRightGradiantSign = true;
        setDistances(-distances,distances,-distances, distances);
        setGradiants(-distancegradiant,distancegradiant,-distancegradiant,distancegradiant);
        runDistances();
    }
    public void driveRightStraightDISTANCE(double speed, double distance){
        resetEncoderToReal();
        speed(speed);
        resetGradiants();
        int distances = (int)(distancetoBlock*distance);
        int distancegradiant = (int)(distances-distancetoBlock*1.6*speed);
        frontLeftGradiantSign = true;
        frontRightGradiantSign = true;
        backLeftGradiantSign = false;
        backRightGradiantSign = false;
        setDistances(distances,distances,-distances, -distances);
        setGradiants(distancegradiant,distancegradiant,-distancegradiant,-distancegradiant);
        runDistances();
    }
    public void driveLeftStraightDISTANCE(double speed, double distance){
        resetEncoderToReal();
        speed(speed);
        resetGradiants();
        int distances = (int)(distancetoBlock*distance);
        int distancegradiant = (int)(distances-distancetoBlock*1.6*speed);
        frontLeftGradiantSign = false;
        frontRightGradiantSign = false;
        backLeftGradiantSign = true;
        backRightGradiantSign = true;
        setDistances(-distances,-distances,distances, distances);
        setGradiants(-distancegradiant,-distancegradiant,distancegradiant,distancegradiant);
        runDistances();
    }
    public void driveNWStraightDISTANCE(double speed, double distance){
        resetEncoderToReal();
        speed(speed);
        int distances = (int)(distancetoBlock*distance);
        setDistances(0,-distances,distances, 0);
        runDistances();
    }
    public void driveNEStraightDISTANCE(double speed, double distance){
        resetEncoderToReal();
        speed(speed);
        int distances = (int)(distancetoBlock*distance);
        setDistances(distances,0,0, -distances);
        runDistances();
    }
    public void driveSEStraightDISTANCE(double speed, double distance){
        resetEncoderToReal();
        speed(speed);
        int distances = (int)(distancetoBlock*distance);
        setDistances(0,distances,-distances, 0);
        runDistances();
    }
    public void driveSWStraightDISTANCE(double speed, double distance){
        resetEncoderToReal();
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
    public void getPositions() {
        telemetry.addData("FRONT LEFT", frontLeftMotorPosition + " : " + robot.frontLeftMotor.getCurrentPosition());
        telemetry.addData("FRONT RIGHT", frontRightMotorPosition + " : " + robot.frontRightMotor.getCurrentPosition());
        telemetry.addData("BACK LEFT", backLeftMotorPosition + " : " + robot.backLeftMotor.getCurrentPosition());
        telemetry.addData("BACK RIGHT", backRightMotorPosition + " : " + robot.backRightMotor.getCurrentPosition());
        updateTelemetry(telemetry);
    }

    private boolean sequenceBL(){
        stopRobot();
        sleepTau(250);
        driveLeftStraightDISTANCE(0.3,0.25);
        sleepTau(1500);
        turnToDegree(0.45,176);
        sleepTau(1000);
        driveForwardStraightDISTANCE(0.3,1);
        sleepTau(1500);
        stopRobot();
        openClaw();
        driveBackwardStraightDISTANCE(0.3,0.45);
        sleepTau(1400);
        turnToDegree(0.6,-90);
        sleepTau(1500);
        return true;
    }
    private boolean sequenceBR(){
        stopRobot();
        sleepTau(250);
        driveLeftStraightDISTANCE(0.3,0.25);
        sleepTau(1100);
        turnToDegree(0.45,86);
        sleepTau(1050);
        driveForwardStraightDISTANCE(0.3,1);
        sleepTau(1300);
        stopRobot();
        pokeClaw();
        sleepTau(100);
        driveBackwardStraightDISTANCE(0.6,0.45);
        sleepTau(1000);
        turnToDegree(0.8,-90);
        sleepTau(1400);
        lowerLiftSlightly();
        driveForwardStraightDISTANCE(0.6,1.1);
        sleepTau(1200);
        closeClaw();
        sleepTau(200);
        raiseLiftEntireBlock();
        driveBackwardStraightDISTANCE(0.6,0.4);
        sleepTau(500);
        turnToDegree(0.7,90);
        sleepTau(1100);
        driveForwardStraightDISTANCE(0.3,1);
        sleepTau(1800);
        openClaw();
        sleepTau(650);
        driveBackwardStraightDISTANCE(0.3,0.3);
        sleepTau(900);
        lowerLiftEntireBlock();
        return true;
    }
    private boolean sequenceRL(){
        stopRobot();
        sleepTau(250);
        driveLeftStraightDISTANCE(0.3,0.25);
        sleepTau(1100);
        turnToDegree(0.45,90);
        sleepTau(1050);
        driveForwardStraightDISTANCE(0.3,1);
        sleepTau(1300);
        stopRobot();
        pokeClaw();
        sleepTau(100);
        driveBackwardStraightDISTANCE(0.6,0.45);
        sleepTau(1000);
        turnToDegree(0.8,-90);
        sleepTau(1400);
        lowerLiftSlightly();
        driveForwardStraightDISTANCE(0.6,1.1);
        sleepTau(1200);
        closeClaw();
        sleepTau(200);
        raiseLiftEntireBlock();
        driveBackwardStraightDISTANCE(0.6,0.4);
        sleepTau(500);
        turnToDegree(0.7,90);
        sleepTau(1100);
        driveForwardStraightDISTANCE(0.3,1);
        sleepTau(1800);
        openClaw();
        sleepTau(650);
        driveBackwardStraightDISTANCE(0.3,0.3);
        sleepTau(900);
        lowerLiftEntireBlock();
        return true;
    }
    private boolean sequenceRR(){
        stopRobot();
        sleepTau(250);
        driveLeftStraightDISTANCE(0.3,0.25);
        sleepTau(1300);
        realign(0.45);
        sleepTau(1050);
        driveForwardStraightDISTANCE(0.3,1);
        sleepTau(1500);
        stopRobot();
        openClaw();
        driveBackwardStraightDISTANCE(0.3,0.45);
        sleepTau(1400);
        turnToDegree(0.6,-90);
        sleepTau(1500);
        return true;
    }



    //BEHIND THE SCENES METHODS
    private void speed(double speed){
        this.speed = speed;
        robot.frontLeftMotor.setPower(speed);
        robot.frontRightMotor.setPower(speed);
        robot.backLeftMotor.setPower(speed);
        robot.backRightMotor.setPower(speed);
    }
    private void stopRobot(){
        speed(0);
        resetEncoderToReal();
    }
    private void speedNoChange(double speed){
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
   /* private double frontGetRange(){

        return frontRangeSensor.getDistance(DistanceUnit.CM);}
    */private double rightGetRange(){
        //double num = rightRangeSensor.getDistance(DistanceUnit.CM);
        return rightRangeSensor.getDistance(DistanceUnit.CM);
       
    }
    private void setDistances(int fl, int fr, int bl, int br){
        frontLeftMotorPosition += fl;
        frontRightMotorPosition += fr;
        backLeftMotorPosition += bl;
        backRightMotorPosition += br;
    }
    private void setGradiants(int fl, int fr, int bl, int br){
        frontLeftMotorPositionGradiant += fl;
        frontRightMotorPositionGradiant += fr;
        backLeftMotorPositionGradiant += bl;
        backRightMotorPositionGradiant += br;
        frontLeftMotorPositionFirstGradiant += fl/Math.abs(fl)*50;
        frontRightMotorPositionFirstGradiant += fr/Math.abs(fr)*50;
        backLeftMotorPositionFirstGradiant += bl/Math.abs(bl)*50;
        backRightMotorPositionFirstGradiant += br/Math.abs(br)*50;
    }
    private void resetGradiants(){
        frontLeftMotorPositionGradiant = frontLeftMotorPosition;
        frontRightMotorPositionGradiant = frontRightMotorPosition;
        backLeftMotorPositionGradiant = backLeftMotorPosition;
        backRightMotorPositionGradiant = backRightMotorPosition;
        frontLeftMotorPositionFirstGradiant = frontLeftMotorPosition;
        frontRightMotorPositionFirstGradiant = frontRightMotorPosition;
        backLeftMotorPositionFirstGradiant = backLeftMotorPosition;
        backRightMotorPositionFirstGradiant = backRightMotorPosition;
    }
    private boolean checkEndGradiants(){
        if (frontLeftGradiantSign){
            if (robot.frontLeftMotor.getCurrentPosition() < frontLeftMotorPositionGradiant){
                return false;
            }
        }
        else{
            if (robot.frontLeftMotor.getCurrentPosition() > frontLeftMotorPositionGradiant){
                return false;
            }
        }
        if (frontRightGradiantSign){
            if (robot.frontRightMotor.getCurrentPosition() < frontRightMotorPositionGradiant){
                return false;
            }
        }
        else{
            if (robot.frontRightMotor.getCurrentPosition() > frontRightMotorPositionGradiant){
                return false;
            }
        }
        if (backLeftGradiantSign){
            if (robot.backLeftMotor.getCurrentPosition() < backLeftMotorPositionGradiant){
                return false;
            }
        }
        else{
            if (robot.backLeftMotor.getCurrentPosition() > backLeftMotorPositionGradiant){
                return false;
            }
        }
        if (backRightGradiantSign){
            if (robot.backRightMotor.getCurrentPosition() < backRightMotorPositionGradiant){
                return false;
            }
        }
        else{
            if (robot.backRightMotor.getCurrentPosition() > backRightMotorPositionGradiant){
                return false;
            }
        }


        return true;

    }
    private boolean checkInitGradiants(){
        if (frontLeftGradiantSign){
            if (robot.frontLeftMotor.getCurrentPosition() < frontLeftMotorPositionFirstGradiant){
                return false;
            }
        }
        else{
            if (robot.frontLeftMotor.getCurrentPosition() > frontLeftMotorPositionFirstGradiant){
                return false;
            }
        }
        if (frontRightGradiantSign){
            if (robot.frontRightMotor.getCurrentPosition() < frontRightMotorPositionFirstGradiant){
                return false;
            }
        }
        else{
            if (robot.frontRightMotor.getCurrentPosition() > frontRightMotorPositionFirstGradiant){
                return false;
            }
        }
        if (backLeftGradiantSign){
            if (robot.backLeftMotor.getCurrentPosition() < backLeftMotorPositionFirstGradiant){
                return false;
            }
        }
        else{
            if (robot.backLeftMotor.getCurrentPosition() > backLeftMotorPositionFirstGradiant){
                return false;
            }
        }
        if (backRightGradiantSign){
            if (robot.backRightMotor.getCurrentPosition() < backRightMotorPositionFirstGradiant){
                return false;
            }
        }
        else{
            if (robot.backRightMotor.getCurrentPosition() > backRightMotorPositionFirstGradiant){
                return false;
            }
        }


        return true;
    }
    private void runDistances(){
        robot.frontRightMotor.setTargetPosition(frontRightMotorPosition);
        robot.frontLeftMotor.setTargetPosition(frontLeftMotorPosition);
        robot.backRightMotor.setTargetPosition(backRightMotorPosition);
        robot.backLeftMotor.setTargetPosition(backLeftMotorPosition);
    }
    private void resetEncoderToReal(){
        frontLeftMotorPosition = robot.frontLeftMotor.getCurrentPosition();
        frontRightMotorPosition = robot.frontRightMotor.getCurrentPosition();
        backLeftMotorPosition = robot.backLeftMotor.getCurrentPosition();
        backRightMotorPosition = robot.backRightMotor.getCurrentPosition();
        runDistances();
    }
}
