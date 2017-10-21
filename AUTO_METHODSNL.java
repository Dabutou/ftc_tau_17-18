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

class AUTO_METHODSNL extends LinearOpMode{

    HardwareNL robot = new HardwareNL();
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



    public AUTO_METHODSNL(){}


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
        frontLeftMotorPosition -= 880;
        frontRightMotorPosition -= 880;
        backLeftMotorPosition -= 880;
        backRightMotorPosition -= 880;
        robot.frontRightMotor.setTargetPosition(frontRightMotorPosition);
        robot.frontLeftMotor.setTargetPosition(frontLeftMotorPosition);
        robot.backRightMotor.setTargetPosition(backRightMotorPosition);
        robot.backLeftMotor.setTargetPosition(backLeftMotorPosition);
    }
    public void right90(double speed){
        speed(speed);
        frontLeftMotorPosition += 880;
        frontRightMotorPosition += 880;
        backLeftMotorPosition += 880;
        backRightMotorPosition += 880;
        robot.frontRightMotor.setTargetPosition(frontRightMotorPosition);
        robot.frontLeftMotor.setTargetPosition(frontLeftMotorPosition);
        robot.backRightMotor.setTargetPosition(backRightMotorPosition);
        robot.backLeftMotor.setTargetPosition(backLeftMotorPosition);
    }

    public void sleepTau(long milliSec){try{Thread.sleep(milliSec);}catch(InterruptedException e){throw new RuntimeException(e);}}

    
    //distance by centimeters
    //FIGURE OUT "number of encoder ticks" to reach 1 meter; follow my example for turndegrees
    public void driveForwardStraightDISTANCE(double distance){}
    public void driveBackwardStraightTIME(long milliSec){}
    public void driveRightStraightTIME(long milliSec){}
    public void driveLeftStraightTIME(long milliSec){}
    public void driveNWStraightTIME(long milliSec){}
    public void driveNEStraightTIME(long milliSec){}
    public void driveSEStraightTIME(long milliSec){}
    public void driveSWStraightTIME(long milliSec){}
    public void driveForwardStraightTIME(double speed, long milliSec){}
    public void driveBackwardStraightTIME(double speed, long milliSec){}
    public void driveRightStraightTIME(double speed, long milliSec){}
    public void driveLeftStraightTIME(double speed, long milliSec) {}
    public void driveNWStraightTIME(double speed, long milliSec){}
    public void driveNEStraightTIME(double speed, long milliSec){}
    public void driveSEStraightTIME(double speed, long milliSec){}
    public void driveSWStraightTIME(double speed, long milliSec){}



    //BEHIND THE SCENES METHODS
    private void speed(double speed){
            robot.frontLeftMotor.setPower(speed);
            robot.frontRightMotor.setPower(speed);
            robot.backLeftMotor.setPower(speed);
            robot.backRightMotor.setPower(speed);
    }
}
