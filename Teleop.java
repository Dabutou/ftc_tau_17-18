package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

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
    //private double rightGP1 = 0; -- figure out something to do with right stick
    private boolean speedToggle = true;
    private double speedToggleMultiplier = 0.1; // Between 0 and 1


    //Lift Variables

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
        robot.init(hardwareMap);
        telemetry.addData("Say", "If you notice this, you are cool!!!");
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

        //Assign power to each motor based on X and Y vectors
        backleftPOWER = (leftGP1Y - leftGP1X);
        backrightPOWER = -(leftGP1Y + leftGP1X);
        frontleftPOWER = (leftGP1Y + leftGP1X);
        frontrightPOWER = -(leftGP1Y - leftGP1X);

        //Turning
        if (gamepad1.left_trigger > 0.05){
            frontrightPOWER = frontrightPOWER - gamepad1.left_trigger;
            frontleftPOWER = frontleftPOWER - gamepad1.left_trigger;
            backrightPOWER = backrightPOWER - gamepad1.left_trigger;
            backleftPOWER = backleftPOWER - gamepad1.left_trigger;
        }
        if (gamepad1.right_trigger > 0.05){
            frontrightPOWER = frontrightPOWER + gamepad1.right_trigger;
            frontleftPOWER = frontleftPOWER + gamepad1.right_trigger;
            backrightPOWER = backrightPOWER + gamepad1.right_trigger;
            backleftPOWER = backleftPOWER + gamepad1.right_trigger;
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

        if (gamepad1.b) {
            speedToggle = !speedToggle;
        }


        if (speedToggle){
            telemetry.addData("Mode:", "Full Speed");
            updateTelemetry(telemetry);
        }
        else{
            telemetry.addData("Mode:", "Speed Multiplier: " + speedToggleMultiplier);
            updateTelemetry(telemetry);
        }


        //Remove slight touches
        if(Math.abs(leftGP1Y) < 0.05) {
            leftGP1Y = 0.0;
        }
        if(Math.abs(leftGP1X) < 0.05) {
            leftGP1X = 0.0;
        }



        //Changing speedToggleMultiplier
        if (gamepad1.dpad_down)
        {
            if (speedToggleMultiplier > 0.14){
                speedToggleMultiplier = speedToggleMultiplier - 0.01;
            }
        }
        if (gamepad1.dpad_up) {
            if (speedToggleMultiplier < 0.86) {
                speedToggleMultiplier = speedToggleMultiplier + 0.01;
            }
        }


        //Fast or precision movement
        if (speedToggle)
        {
            robot.frontLeftMotor.setPower(frontleftPOWER);
            robot.frontRightMotor.setPower(frontrightPOWER);
            robot.backRightMotor.setPower(backrightPOWER);
            robot.backLeftMotor.setPower(backleftPOWER);

        }
        else
        {
            robot.frontLeftMotor.setPower(speedToggleMultiplier * frontleftPOWER);
            robot.frontRightMotor.setPower(speedToggleMultiplier * frontrightPOWER);
            robot.backRightMotor.setPower(speedToggleMultiplier * backrightPOWER);
            robot.backLeftMotor.setPower(speedToggleMultiplier * backleftPOWER);
        }


        //*****************
        //Game Controller 2
        //*****************

        //Read controller input
        leftGP2Y = gamepad2.left_stick_y;

        robot.leftLiftMotor.setPower(leftGP2Y);
        robot.rightLiftMotor.setPower(-leftGP2Y);


    }

    @Override
    public void stop()
    {

    }

    /*public void waitTau(long milliSec)
    {
        try{
            Thread.sleep(milliSec);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }*/

}
