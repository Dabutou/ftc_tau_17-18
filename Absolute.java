package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/**
 * Created by Lance He on 9/16/2017.
 */
@TeleOp(name = "Tau: Teleop ABSOLUTE", group="Tau")
//Uncomment below to show up after run
//@Disabled
public class Absolute extends OpMode {

    Hardware robot = new Hardware();
    private static final String TAG = "LHActivity";

    //Drive Variables
    private BNO055IMU imu;
    private BNO055IMU.Parameters parameters;

    private double leftGP1Y = 0;
    private double leftGP1X = 0;
    private double rightGP1Y = 0;
    private double rightGP1X = 0;
    private double LTGP1 = 0;
    private double RTGP1 = 0;
    private double frontleftPOWER = 0;
    private double frontrightPOWER = 0;
    private double backleftPOWER = 0;
    private double backrightPOWER = 0;
    private double maxPOWER = 0;
    private final double triggerConstant = 0.75;
    private final double maxPOWERConstant = 0.8;
    private boolean speedToggle = true;
    private double speedToggleMultiplier = 0.6; // Between 0.25 and 0.85
    private double endTimeB = 0;
    private double endTimeS = 0;
    private double endTimeX = 0;
    private double length = 0;
    private double initAngle = 0;
    private double angle = 0;
    private boolean absoluteDrive = false;
    private boolean firstTimeABD = true;
    //private double headinginit = 0;
    private double rollinit = 0;
    private double pitchinit = 0;

    private boolean centerMode = false;
    private boolean centerModeFirstTime = true;
    private double centerModeEndTime = 0;



    //Lift Variables

    private static final double LEFT_LIFT_OPEN = 0.89;
    private static final double LEFT_LIFT_CLOSE = 0.24;
    private static final double RIGHT_LIFT_OPEN = 0.75;
    private static final double RIGHT_LIFT_CLOSE = 0.10;
    private double leftGP2Y = 0;
    private double rightGP2Y = 0;
    private double leftLiftPos = LEFT_LIFT_OPEN;
    private double rightLiftPos = RIGHT_LIFT_OPEN;
    //private double endTime2B = 0;
    //private int clawStage = 2;



    /*
    Controller Layout
      Y
    X   B
      A
     */


    @Override
    public void init()
    {


        robot.initTeleOpDelayIMU(hardwareMap);

        // Set up our telemetry dashboard
        telemetry.addData("Readiness", "Press Play to start");
        telemetry.addData("If you notice this", "You are COOL!!!");
        updateTelemetry(telemetry);
        imu = robot.getImu();
        parameters = robot.getImuProperties();
    }

    @Override
    public void init_loop()
    {

    }


    @Override
    public void start()
    {
        telemetry.addData("Readiness", "NOT READY TO START, PLEASE WAIT");
        updateTelemetry(telemetry);
    }


    @Override
    public void loop()
    {

        //*****************
        //Game Controller 1
        //*****************

        //Read controller input
        //Left and right are opposite; front and back are same
        leftGP1Y = -gamepad1.left_stick_y;
        leftGP1X = gamepad1.left_stick_x;
        LTGP1 = gamepad1.left_trigger;
        RTGP1 = gamepad1.right_trigger;

        if (gamepad1.left_bumper && !gamepad1.right_bumper){
            LTGP1 = 0.25;
        }
        if (gamepad1.right_bumper && !gamepad1.left_bumper){
            RTGP1 = 0.25;
        }

        //Remove slight touches
        if (!absoluteDrive) {
            if (Math.abs(leftGP1Y) < 0.40) {
                leftGP1Y = 0;
            }
            if (Math.abs(leftGP1X) < 0.40) {
                leftGP1X = 0;
            }
        }else{
            if (Math.abs(leftGP1Y) < 0.03) {
                leftGP1Y = 0;
            }
            if (Math.abs(leftGP1X) < 0.03) {
                leftGP1X = 0;
            }
        }


        if (firstTimeABD){
            imu.initialize(parameters);
            firstTimeABD = false;
            pitchinit = imu.getAngularOrientation().secondAngle;
            rollinit = imu.getAngularOrientation().thirdAngle;

        }


        //Check if absolute drive is on

       if (absoluteDrive && (Math.abs(leftGP1X) > 0 || Math.abs(leftGP1Y) > 0)) {

            length = Math.sqrt(Math.pow(leftGP1X,2) + Math.pow(leftGP1Y,2));
            if (leftGP1X == 0) {
                if (leftGP1Y > 0){
                    initAngle = 0;
                }
                else{
                    if (imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle > 0){
                        initAngle = Math.toRadians(-180);
                    }
                    else{
                        initAngle = Math.toRadians(180);
                    }

                }
            }
            else if (leftGP1Y == 0){
                if (leftGP1X > 0){
                    initAngle = Math.toRadians(90);
                }
                else{
                    initAngle = Math.toRadians(-90);
                }
            }
            else{
                if (leftGP1Y > 0) {
                    initAngle = Math.atan(leftGP1Y / leftGP1X);
                }
                else{
                    if (leftGP1X > 0){
                        initAngle = Math.atan(leftGP1Y / leftGP1X) + Math.toRadians(180);
                    }
                    else{
                        initAngle = Math.atan(leftGP1Y / leftGP1X) - Math.toRadians(180);
                    }
                }

            }


            //DEBUGGING
            angle = initAngle + Math.toRadians(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
            //telemetry.addData("Angles","Joystick: " + leftGP1X +  " : " + leftGP1Y + "; imu: " + Math.toRadians(imu.getAngularOrientation().firstAngle) + " & " + imu.getAngularOrientation().firstAngle);
            //telemetry.addData("Length", "" + length);
            leftGP1X = length*Math.sin(angle);
            leftGP1Y = length*Math.cos(angle);
            //telemetry.addData("NewJoyStick",leftGP1X + " : " + leftGP1Y);


        }

        if (gamepad1.left_bumper && gamepad1.right_bumper && gamepad1.dpad_down && (endTimeS == 0 || robot.getTime() >= endTimeS)){
            endTimeS = robot.getTime() + 0.1;
            centerMode = true;
            centerModeFirstTime = true;
            centerModeEndTime = 0;
        }
        if (gamepad1.dpad_left && gamepad1.b && (endTimeS == 0 || robot.getTime() >= endTimeS)){
            endTimeS = robot.getTime() + 0.1;
            centerMode = false;
            centerModeFirstTime = false;
            centerModeEndTime = 0;
            //telemetry.addData("CENTERMODE","DEACTIVATED");
        }
        if (gamepad1.x && gamepad1.dpad_right && (endTimeS == 0 || robot.getTime() >= endTimeS)){
            endTimeS = robot.getTime() + 0.1;
            centerMode = true;
            centerModeFirstTime = false;
            centerModeEndTime = 0;
        }

        if (centerMode && (centerModeEndTime == 0 || robot.getTime() < centerModeEndTime)){
            absoluteDrive = false;
            speedToggle = true;
            if (centerModeFirstTime) {
                Log.d(TAG,"CENTER MODE, FIRST TIME");
                //headinginit = imu.getAngularOrientation().firstAngle;
                pitchinit = imu.getAngularOrientation().secondAngle;
                rollinit = imu.getAngularOrientation().thirdAngle;
                driveForward(0.9);
                sleepTau(700);
                driveForward(0.2);
                sleepTau(50);
                driveForward(0);
                sleepTau(20);
                centerModeFirstTime = false;
            }


            double pitch = imu.getAngularOrientation().secondAngle - pitchinit;
            double roll = imu.getAngularOrientation().thirdAngle - rollinit;

            //Log.i(TAG,"IMU PITCH: "+pitch + " : " + pitchinit+  " ROLL: "+roll + " : " + rollinit );

            if (Math.abs(roll) > 0.04){
                leftGP1X = -roll/21;

            }
            if (Math.abs(pitch) > 0.04){
                leftGP1Y = -pitch/22;
            }
        }
        /*if (centerModeEndTime != 0 && robot.getTime() > centerModeEndTime){
            centerMode = false;
            centerModeEndTime = 0;
        }*/
        /*if (centerModeEndTime == 0){
                if (leftGP1X < 0.05 && leftGP1Y < 0.05 && RTGP1 < 0.05 && LTGP1 < 0.05){
                    centerModeEndTime = robot.getTime() + 5;
                }
            }else{
                if (leftGP1X > 0.05 && leftGP1Y > 0.05 && RTGP1 > 0.05 && LTGP1 > 0.05) {
                    centerModeEndTime = robot.getTime() + 5;
                }
            }*/



        //Assign power to each motor based on X and Y vectors
        backleftPOWER = leftGP1Y - leftGP1X;
        backrightPOWER = -leftGP1Y - leftGP1X;
        frontleftPOWER = leftGP1Y + leftGP1X;
        frontrightPOWER = -leftGP1Y + leftGP1X;

        //Turning
        if (LTGP1 > 0.05){
            frontrightPOWER = frontrightPOWER - LTGP1 * triggerConstant;
            frontleftPOWER = frontleftPOWER - LTGP1 * triggerConstant;
            backrightPOWER = backrightPOWER - LTGP1 * triggerConstant;
            backleftPOWER = backleftPOWER - LTGP1 * triggerConstant;
        }
        if (RTGP1 > 0.05){
            frontrightPOWER = frontrightPOWER + RTGP1 * triggerConstant;
            frontleftPOWER = frontleftPOWER + RTGP1 * triggerConstant;
            backrightPOWER = backrightPOWER + RTGP1 * triggerConstant;
            backleftPOWER = backleftPOWER + RTGP1 * triggerConstant;
        }


        //Finding largest value and dividing all numbers by it if it is larger than 1.0; ensures no power greater than 1.0
        maxPOWER = Math.abs(frontleftPOWER);
        if (Math.abs(backleftPOWER) > maxPOWER){
            maxPOWER = Math.abs(backleftPOWER);
        }
        if (Math.abs(backrightPOWER) > maxPOWER){
            maxPOWER = Math.abs(backrightPOWER);
        }
        if (Math.abs(frontrightPOWER) > maxPOWER){
            maxPOWER = Math.abs(frontrightPOWER);
        }

        if (maxPOWER > 1.0) {
            frontrightPOWER = frontrightPOWER / maxPOWER;
            frontleftPOWER = frontleftPOWER / maxPOWER;
            backrightPOWER = backrightPOWER / maxPOWER;
            backleftPOWER = backleftPOWER / maxPOWER;
        }


        rightGP1Y = -gamepad1.right_stick_y; // determine what the right stick is used for
        rightGP1X = gamepad1.right_stick_x;

        telemetry.addData("Center Mode",centerMode);
        telemetry.addData("Center Mode Time Left",centerModeEndTime-robot.getTime());
        telemetry.addData("IMU HEADING", imu.getAngularOrientation().firstAngle);
        Log.d(TAG, "IMU HEADING: "+imu.getAngularOrientation().firstAngle + "; IMU ROLL: " + imu.getAngularOrientation().secondAngle + " : " + rollinit + "; IMU PITCH: " + imu.getAngularOrientation().thirdAngle+" : " + pitchinit);
        telemetry.addData("IMU ROLL", imu.getAngularOrientation().secondAngle+ " : " + rollinit);
        telemetry.addData("IMU PITCH", imu.getAngularOrientation().thirdAngle+ " : " + pitchinit);



        //Speed Toggle
        if (gamepad1.b && (endTimeB == 0 || robot.getTime() >= endTimeB)) {
            endTimeB = robot.getTime() + 0.1;
            speedToggle = !speedToggle;
        }

        if (gamepad1.x && (endTimeX == 0 || robot.getTime() >= endTimeX)) {
            endTimeX = robot.getTime() + 0.25;
            absoluteDrive = !absoluteDrive;
        }
        if (speedToggle && absoluteDrive){
            telemetry.addData("SpeedMode:", "Full Speed ahead!");
            telemetry.addData("DriveMode:", "Absolute Drive");
        }
        else if(speedToggle && !absoluteDrive){
            telemetry.addData("SpeedMode:", "Full Speed ahead!");
            telemetry.addData("DriveMode:", "Regular Drive");
        }
        else if (!speedToggle && absoluteDrive){
            telemetry.addData("SpeedMode:", "Speed Multiplier: " + speedToggleMultiplier);
            telemetry.addData("DriveMode:", "Absolute Drive");
        }
        else{
            telemetry.addData("SpeedMode:", "Speed Multiplier: " + speedToggleMultiplier);
            telemetry.addData("DriveMode:", "Regular Drive");
        }

        if(Math.abs(rightGP1X) > 0.05 && Math.abs(rightGP1Y) > 0.05){
            double r = Math.sqrt(Math.pow(rightGP1X, 2) + Math.pow(rightGP1Y,2));
            double ang = 2*Math.atan2(rightGP1Y,rightGP1X) / Math.PI;
            if (rightGP1Y >= 0){
                frontleftPOWER = frontrightPOWER = r * (0.5 - ang);
                backleftPOWER = backrightPOWER = ang < 0.5 ? -r : r;
            }
            else{
                frontleftPOWER = frontrightPOWER = ang > -0.5 ? r : -r;
                backleftPOWER = backrightPOWER = -r * (0.5 + ang);
            }
        }




        //Changing speedToggleMultiplier
        if (gamepad1.left_bumper && !speedToggle && (endTimeS == 0 || robot.getTime() >= endTimeS))
        {
            if (speedToggleMultiplier > 0.25){
                endTimeS = robot.getTime() + 0.09;
                speedToggleMultiplier = speedToggleMultiplier - 0.1;
            }
        }
        if (gamepad1.right_bumper && !speedToggle && (endTimeS == 0 || robot.getTime() >= endTimeS)) {
            if (speedToggleMultiplier < 0.95) {
                endTimeS = robot.getTime() + 0.09;
                speedToggleMultiplier = speedToggleMultiplier + 0.1;
            }
        }

        if (Math.abs(frontleftPOWER) < 0.03){
            frontleftPOWER = 0;
        }
        if (Math.abs(backleftPOWER) < 0.03){
            backleftPOWER = 0;
        }
        if (Math.abs(frontrightPOWER) < 0.03){
            frontrightPOWER = 0;
        }
        if (Math.abs(backrightPOWER) < 0.03){
            backrightPOWER = 0;
        }


        //Fast or precision movement
        if (speedToggle)
        {
            if (gamepad1.a){
                robot.frontLeftMotor.setPower(0.6*frontleftPOWER * maxPOWERConstant);
                robot.frontRightMotor.setPower(0.6*frontrightPOWER * maxPOWERConstant);
                robot.backRightMotor.setPower(0.6*backrightPOWER * maxPOWERConstant);
                robot.backLeftMotor.setPower(0.6*backleftPOWER * maxPOWERConstant);
            }
            else if (gamepad1.y){
                robot.frontLeftMotor.setPower(frontleftPOWER);
                robot.frontRightMotor.setPower(frontrightPOWER);
                robot.backRightMotor.setPower(backrightPOWER);
                robot.backLeftMotor.setPower(backleftPOWER);
            }
            else if (centerMode && robot.getVoltage() < 11.5){
                if (robot.getVoltage() < 9.5){
                    robot.frontLeftMotor.setPower(frontleftPOWER*0.84);
                    robot.frontRightMotor.setPower(frontrightPOWER*0.84);
                    robot.backRightMotor.setPower(backrightPOWER*0.84);
                    robot.backLeftMotor.setPower(backleftPOWER*0.84);
                }
                else if (robot.getVoltage() < 10.5){
                    robot.frontLeftMotor.setPower(frontleftPOWER*0.82);
                    robot.frontRightMotor.setPower(frontrightPOWER*0.82);
                    robot.backRightMotor.setPower(backrightPOWER*0.82);
                    robot.backLeftMotor.setPower(backleftPOWER*0.82);
                }
                else{
                    robot.frontLeftMotor.setPower(frontleftPOWER*0.81);
                    robot.frontRightMotor.setPower(frontrightPOWER*0.81);
                    robot.backRightMotor.setPower(backrightPOWER*0.81);
                    robot.backLeftMotor.setPower(backleftPOWER*0.81);
                }

            }
            else {
                robot.frontLeftMotor.setPower(frontleftPOWER * maxPOWERConstant);
                robot.frontRightMotor.setPower(frontrightPOWER * maxPOWERConstant);
                robot.backRightMotor.setPower(backrightPOWER * maxPOWERConstant);
                robot.backLeftMotor.setPower(backleftPOWER * maxPOWERConstant);
            }
        }
        else
        {
            if (gamepad1.a){
                robot.frontLeftMotor.setPower(speedToggleMultiplier * 0.6*frontleftPOWER * maxPOWERConstant);
                robot.frontRightMotor.setPower(speedToggleMultiplier * 0.6*frontrightPOWER * maxPOWERConstant);
                robot.backRightMotor.setPower(speedToggleMultiplier * 0.6*backrightPOWER * maxPOWERConstant);
                robot.backLeftMotor.setPower(speedToggleMultiplier * 0.6*backleftPOWER * maxPOWERConstant);
            }
            else if (gamepad1.y){
                robot.frontLeftMotor.setPower(frontleftPOWER);
                robot.frontRightMotor.setPower(frontrightPOWER);
                robot.backRightMotor.setPower(backrightPOWER);
                robot.backLeftMotor.setPower(backleftPOWER);
            }
            else {
                robot.frontLeftMotor.setPower(speedToggleMultiplier * (frontleftPOWER * maxPOWERConstant));
                robot.frontRightMotor.setPower(speedToggleMultiplier * (frontrightPOWER * maxPOWERConstant));
                robot.backRightMotor.setPower(speedToggleMultiplier * (backrightPOWER * maxPOWERConstant));
                robot.backLeftMotor.setPower(speedToggleMultiplier * (backleftPOWER * maxPOWERConstant));
            }

        }







        //*****************
        //Game Controller 2
        //*****************

        //Read controller input
        leftGP2Y = gamepad2.left_stick_y;
        rightGP2Y = gamepad2.right_stick_y;

        if(Math.abs(leftGP2Y) < 0.05) {
            leftGP1Y = 0;
        }
        if(Math.abs(rightGP2Y) < 0.05) {
            rightGP2Y = 0;
        }
        //Limit extension of lift

        if (gamepad2.x) {
            robot.leftLiftMotor.setPower(Math.pow(leftGP2Y, 3));
            robot.rightLiftMotor.setPower(Math.pow(leftGP2Y, 3));
        }else{
            robot.leftLiftMotor.setPower(0.7 * Math.pow(leftGP2Y, 3));
            robot.rightLiftMotor.setPower(0.7 * Math.pow(leftGP2Y, 3));
        }
        robot.relicMotor.setPower(Math.pow(rightGP2Y,3));


        if (gamepad2.left_bumper){
            leftLiftPos = LEFT_LIFT_CLOSE;
            rightLiftPos = RIGHT_LIFT_CLOSE;
        }
        if (gamepad2.right_bumper){
            leftLiftPos = LEFT_LIFT_OPEN;
            rightLiftPos = RIGHT_LIFT_OPEN;
        }

        if (gamepad2.dpad_up){
            robot.relicServo.setPosition(0);
        }
        else if (gamepad2.dpad_down){
            robot.relicServo.setPosition(1);
        }
        else{
            robot.relicServo.setPosition(0.5);
        }
        //Open and close claw servos
        /*if (gamepad2.left_bumper && clawStage < 3 && (endTime2B == 0 || robot.getTime() >= endTime2B)) {
            endTime2B = robot.getTime() + 0.15;
            clawStage++;
            clawStage %= 3;

        }
        if (gamepad2.right_bumper && (endTime2B == 0 || robot.getTime() >= endTime2B)) {
            endTime2B = robot.getTime() + 0.15;
            clawStage--;
            if(clawStage == -1){
                clawStage = 2;
            }
        }
        if(clawStage == 0){
            robot.leftLiftServo.setPosition(LEFT_LIFT_CLOSE);
            robot.rightLiftServo.setPosition(RIGHT_LIFT_CLOSE);

        }
        else if(clawStage == 1){
            robot.leftLiftServo.setPosition(LEFT_LIFT_CLOSE + 0.3);
            robot.rightLiftServo.setPosition(RIGHT_LIFT_CLOSE + 0.3);
        }
        else if(clawStage == 2){
            robot.leftLiftServo.setPosition(LEFT_LIFT_OPEN);
            robot.rightLiftServo.setPosition(RIGHT_LIFT_OPEN);
        }
        else{
            telemetry.addData("CLAW STAGE","OUT OF BOUNDS: " + clawStage);
            updateTelemetry(telemetry);[
        }*/

        //Slight Adjust of CLAW
        if (gamepad2.left_trigger > 0.1){
            if (robot.leftLiftServo.getPosition() <= LEFT_LIFT_CLOSE || robot.rightLiftServo.getPosition() <= RIGHT_LIFT_CLOSE){
                leftLiftPos = LEFT_LIFT_CLOSE;
                rightLiftPos = RIGHT_LIFT_CLOSE;
            }
            else{
                leftLiftPos -= 0.025*gamepad2.left_trigger;
                rightLiftPos -= 0.025*gamepad2.left_trigger;
            }
        }
        if (gamepad2.right_trigger > 0.1){
            if (robot.leftLiftServo.getPosition() >= LEFT_LIFT_OPEN || robot.rightLiftServo.getPosition() >= RIGHT_LIFT_OPEN){
                leftLiftPos = LEFT_LIFT_OPEN;
                rightLiftPos = RIGHT_LIFT_OPEN;
            }
            else{
                leftLiftPos += 0.025*gamepad2.right_trigger;
                rightLiftPos += 0.025*gamepad2.right_trigger;
            }
        }
        robot.leftLiftServo.setPosition(leftLiftPos);
        robot.rightLiftServo.setPosition(rightLiftPos);

        if(gamepad2.y){robot.jewelServo.setPosition(0);}
        if(gamepad2.a){robot.jewelServo.setPosition(1);}

        /*telemetry.addData("Left Servo", robot.leftLiftServo.getPosition());
        telemetry.addData("Right Servo", robot.rightLiftServo.getPosition());
        updateTelemetry(telemetry);
        */
        //telemetry.addData("IMU", "" + imu.getAngularOrientation().firstAngle);
        updateTelemetry(telemetry);

    }

    @Override
    public void stop()
    {

    }


    public void sleepTau(long milliSec){try{Thread.sleep(milliSec);}catch(InterruptedException e){throw new RuntimeException(e);}}
    public void driveForward(double speed){
        robot.frontLeftMotor.setPower(speed*0.95);
        robot.frontRightMotor.setPower(-speed);
        robot.backLeftMotor.setPower(speed*0.95);
        robot.backRightMotor.setPower(-speed);
    }


}
