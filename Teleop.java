package org.firstinspires.ftc.teamcode;

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
@TeleOp(name = "Tau: Teleop", group="Tau")
//Uncomment below to show up after run
//@Disabled
public class Teleop extends OpMode {

    Hardware robot = new Hardware();

    //Drive Variables
    private BNO055IMU imu;
    private double leftGP1Y = 0;
    private double leftGP1X = 0;
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

    //Lift Variables

    private static final double LEFT_LIFT_OPEN = 0.95;
    private static final double LEFT_LIFT_CLOSE = 0.2;
    private static final double RIGHT_LIFT_OPEN = 0.8;
    private static final double RIGHT_LIFT_CLOSE = 0;
    private double leftGP2Y = 0;
    private double liftLevel = 0;



    /*
    Controller Layout
      Y
    X   B
      A
     */


    @Override
    public void init()
    {
        telemetry.addData("Readiness", "NOT READY TO START, PLEASE WAIT");
        updateTelemetry(telemetry);

        robot.initTeleOp(hardwareMap);

        // Set up our telemetry dashboard
        telemetry.addData("Readiness", "Press Play to start");
        telemetry.addData("If you notice this", "You are COOL!!!");
        updateTelemetry(telemetry);
        imu = robot.getImu();
    }

    @Override
    public void init_loop()
    {

    }


    @Override
    public void start()
    {

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

        //Remove slight touches
        if(Math.abs(leftGP1Y) < 0.40) {
            leftGP1Y = 0;
        }
        if(Math.abs(leftGP1X) < 0.40) {
            leftGP1X = 0;
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
                        initAngle = Math.toRadians(180);
                    }
                    else{
                        initAngle = Math.toRadians(-180);
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
                initAngle = Math.atan(leftGP1Y / leftGP1X);
            }

            angle = initAngle - Math.toRadians(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle)-Math.toRadians(90);

            leftGP1X = length*Math.cos(angle);
            leftGP1Y = -length*Math.sin(angle);


        }

        //Assign power to each motor based on X and Y vectors
        backleftPOWER = leftGP1Y - leftGP1X;
        backrightPOWER = -leftGP1Y - leftGP1X;
        frontleftPOWER = leftGP1Y + leftGP1X;
        frontrightPOWER = -leftGP1Y + leftGP1X;

        //Turning
        if (gamepad1.left_trigger > 0.05){
            frontrightPOWER = frontrightPOWER - gamepad1.left_trigger * triggerConstant;
            frontleftPOWER = frontleftPOWER - gamepad1.left_trigger * triggerConstant;
            backrightPOWER = backrightPOWER - gamepad1.left_trigger * triggerConstant;
            backleftPOWER = backleftPOWER - gamepad1.left_trigger * triggerConstant;
        }
        if (gamepad1.right_trigger > 0.05){
            frontrightPOWER = frontrightPOWER + gamepad1.right_trigger * triggerConstant;
            frontleftPOWER = frontleftPOWER + gamepad1.right_trigger * triggerConstant;
            backrightPOWER = backrightPOWER + gamepad1.right_trigger * triggerConstant;
            backleftPOWER = backleftPOWER + gamepad1.right_trigger * triggerConstant;
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


        //rightGP1 = -gamepad1.right_stick_y;  -- determine what the right stick is used for

        //Speed Toggle
        if (gamepad1.b && (endTimeB == 0 || robot.getTime() >= endTimeB)) {
            endTimeB = robot.getTime() + 0.1;
            speedToggle = !speedToggle;
        }

        if (gamepad1.x && (endTimeX == 0 || robot.getTime() >= endTimeX)) {
            endTimeX = robot.getTime() + 0.2;
            absoluteDrive = !absoluteDrive;
        }
        if (speedToggle && absoluteDrive){
            telemetry.addData("SpeedMode:", "Full Speed ahead!");
            telemetry.addData("DriveMode:", "Absolute Drive");
            updateTelemetry(telemetry);
        }
        else if(speedToggle && !absoluteDrive){
            telemetry.addData("SpeedMode:", "Full Speed ahead!");
            telemetry.addData("DriveMode:", "Regular Drive");
            updateTelemetry(telemetry);
        }
        else if (!speedToggle && absoluteDrive){
            telemetry.addData("SpeedMode:", "Speed Multiplier: " + speedToggleMultiplier);
            telemetry.addData("DriveMode:", "Absolute Drive");
            updateTelemetry(telemetry);
        }
        else{
            telemetry.addData("SpeedMode:", "Speed Multiplier: " + speedToggleMultiplier);
            telemetry.addData("DriveMode:", "Regular Drive");
            updateTelemetry(telemetry);
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


        //Fast or precision movement
        if (speedToggle)
        {
            robot.frontLeftMotor.setPower(frontleftPOWER * maxPOWERConstant);
            robot.frontRightMotor.setPower(frontrightPOWER * maxPOWERConstant);
            robot.backRightMotor.setPower(backrightPOWER * maxPOWERConstant);
            robot.backLeftMotor.setPower(backleftPOWER * maxPOWERConstant);

        }
        else
        {
            robot.frontLeftMotor.setPower(speedToggleMultiplier * (frontleftPOWER * maxPOWERConstant));
            robot.frontRightMotor.setPower(speedToggleMultiplier * (frontrightPOWER * maxPOWERConstant));
            robot.backRightMotor.setPower(speedToggleMultiplier * (backrightPOWER * maxPOWERConstant));
            robot.backLeftMotor.setPower(speedToggleMultiplier * (backleftPOWER * maxPOWERConstant));
        }


        //*****************
        //Game Controller 2
        //*****************

        //Read controller input
        leftGP2Y = -gamepad2.left_stick_y;

        if(Math.abs(leftGP2Y) < 0.05) {
            leftGP1Y = 0;
        }
        //Limit extension of lift

        robot.leftLiftMotor.setPower(0.3 * leftGP2Y);
        robot.rightLiftMotor.setPower(0.3 * leftGP2Y);


        //Open and close claw servos
        if(gamepad1.right_bumper && clawStage < 3){clawStage++;}
        if(gamepad1.left_bumper && clawStage > 1){clawStage--;}

        if(clawStage == 1){
            clawPosLeft = LEFT_LIFT_CLOSE;
            clawPosRight = LEFT_LIFT_CLOSE;

        }
        if(clawStage == 2){
            clawPosLeft = LEFT_LIFT_CLOSE + 0.1;
            clawPosRight = LEFT_LIFT_CLOSE + 0.1;
        }
        if(clawStage == 3){
            clawPosLeft = LEFT_LIFT_OPEN;
            clawPosRight = LEFT_LIFT_OPEN;
        }

        if (gamepad2.left_trigger > 0.1 && robot.leftLiftServo.getPosition() <= 0.95 && robot.rightLiftServo.getPosition() < 0.8){
            clawPosLeft -= gamepad2.left_trigger / 40;
            clawPosRight -= gamepad2.left_trigger / 40;
        }
        if (gamepad2.right_trigger > 0.1 && robot.leftLiftServo.getPosition() >= 0.2 && robot.rightLiftServo.getPosition() >= 0.01){
            clawPosLeft += gamepad2.right_trigger / 40;
            clawPosRight += gamepad2.right_trigger / 40;
        }

        robot.leftLiftServo.setPosition(clawPosLeft);
        robot.rightLiftServo.setPosition(clawPosRight);

        if(gamepad2.a){robot.jewelServo.setPosition(0);}
        if(gamepad2.b){robot.jewelServo.setPosition(1);}
    }
    
    @Override
    public void stop()
    {

    }



}
