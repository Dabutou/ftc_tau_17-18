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

    private double leftGP1Y = 0;
    private double leftGP1X = 0;
    private double frontleftPOWER = 0;
    private double frontrightPOWER = 0;
    private double backleftPOWER = 0;
    private double backrightPOWER = 0;
    private double maxPOWER = 0;
    private final double triggerConstant = 0.75;
    private final double maxPOWERConstant = 0.8;
    private double Lift_POS = 0;
    //private double rightGP1 = 0; -- figure out something to do with right stick
    private boolean speedToggle = true;
    private double speedToggleMultiplier = 0.6; // Between 0.25 and 0.85
    private double startTimeB = 0;
    private double endTimeB = 0;
    private double startTimeS = 0;
    private double endTimeS = 0;

    //Lift Variables

    private static final double LEFT_LIFT_OPEN = 0.95;
    private static final double LEFT_LIFT_CLOSE = 0.35;
    private static final double RIGHT_LIFT_OPEN = 0.8;
    private static final double RIGHT_LIFT_CLOSE = 0;
    private double clawPosLeft = LEFT_LIFT_OPEN;
    private double clawPosRight = RIGHT_LIFT_OPEN;
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
        telemetry.update();

        robot.initTeleOp(hardwareMap);

        // Set up our telemetry dashboard
        telemetry.addData("Readiness", "Press Play to start");
        telemetry.addData("If you notice this", "You are COOL!!!");
        updateTelemetry(telemetry);
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
        leftGP1Y = gamepad1.left_stick_y;
        leftGP1X = gamepad1.left_stick_x;

        //Remove slight touches
        if(Math.abs(leftGP1Y) < 0.25) {
            leftGP1Y = 0.0;
        }
        if(Math.abs(leftGP1X) < 0.25) {
            leftGP1X = 0.0;
        }

        //Assign power to each motor based on X and Y vectors
        backleftPOWER = -(leftGP1Y + leftGP1X);
        backrightPOWER = (leftGP1Y - leftGP1X);
        frontleftPOWER = -(leftGP1Y - leftGP1X);
        frontrightPOWER = (leftGP1Y + leftGP1X);

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
            startTimeB = robot.getTime();
            endTimeB = startTimeB + 0.1;
            speedToggle = !speedToggle;
        }


        if (speedToggle){
            telemetry.addData("Mode:", "Full Speed ahead!");
            updateTelemetry(telemetry);
        }
        else{
            telemetry.addData("Mode:", "Speed Multiplier: " + speedToggleMultiplier);
            updateTelemetry(telemetry);
        }






        //Changing speedToggleMultiplier
        if (gamepad1.left_bumper && !speedToggle && (endTimeS == 0 || robot.getTime() >= endTimeS))
        {
            if (speedToggleMultiplier > 0.25){
                startTimeS = robot.getTime();
                endTimeS = startTimeS + 0.09;
                speedToggleMultiplier = speedToggleMultiplier - 0.1;
            }
        }
        if (gamepad1.right_bumper && !speedToggle && (endTimeS == 0 || robot.getTime() >= endTimeS)) {
            if (speedToggleMultiplier < 0.95) {
                startTimeS = robot.getTime();
                endTimeS = startTimeS + 0.09;
                speedToggleMultiplier = speedToggleMultiplier + 0.1;
            }
        }

        if (gamepad1.x){
            BNO055IMU imu = robot.getImu();
            telemetry.addData("FIRST_ANGLE",""+imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
            updateTelemetry(telemetry);
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
        leftGP2Y = gamepad2.left_stick_y;

        //Limit extension of lift
        liftLevel -= leftGP2Y;
        telemetry.addData("Lift Level",liftLevel);
        updateTelemetry(telemetry);

        if (leftGP2Y > 0 || liftLevel > 0) {
            robot.leftLiftMotor.setPower(0.6 * leftGP2Y);
            robot.rightLiftMotor.setPower(0.3 * leftGP2Y);
        }
        else{
            liftLevel = 0;
        }
        //Open and close claw servos
        if (gamepad2.left_bumper){
            robot.leftLiftServo.setPosition(LEFT_LIFT_OPEN);
            robot.rightLiftServo.setPosition(RIGHT_LIFT_OPEN);
            clawPosLeft = LEFT_LIFT_OPEN;
            clawPosRight = RIGHT_LIFT_OPEN;
        }
        if (gamepad2.right_bumper){
            robot.leftLiftServo.setPosition(LEFT_LIFT_CLOSE);
            robot.rightLiftServo.setPosition(RIGHT_LIFT_CLOSE);
            clawPosLeft = LEFT_LIFT_CLOSE;
            clawPosRight = RIGHT_LIFT_CLOSE;
        }
        if(clawPosLeft <= 1) {
            clawPosLeft += gamepad2.left_trigger / 100;
            clawPosRight += gamepad2.left_trigger / 100;
        }
        if(clawPosRight >= 0) {
            clawPosLeft -= gamepad2.right_trigger / 100;
            clawPosRight -= gamepad2.right_trigger / 100;
        }    
        
        robot.leftLiftServo.setPosition(clawPosLeft);
        robot.rightLiftServo.setPosition(clawPosRight);

        if(gamepad2.a){
            robot.jewelServo.setPosition(0);
        }

    }

    @Override
    public void stop()
    {

    }



}
