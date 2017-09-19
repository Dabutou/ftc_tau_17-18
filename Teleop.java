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

    double leftGP1 = 0;
    double rightGP1 = 0;
    boolean speedToggle = false;


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
        telemetry.addData("Say", "Wowza!!!");
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
        //Read controller input
        leftGP1 = -gamepad1.left_stick_y;
        rightGP1 = -gamepad1.right_stick_y;
        if (gamepad1.b){
            telemetry.addData("Debug:","B");
            speedToggle = !speedToggle;
        }

        //Remove slight touches
        if(Math.abs(leftGP1) < 0.05) {
            leftGP1 = 0.0;
        }
        if(Math.abs(rightGP1) < 0.05) {
            rightGP1 = 0.0;
        }

        //Fast or precision movement
        if (speedToggle)
        {
            robot.frontLeftMotor.setPower(leftGP1);
            robot.frontRightMotor.setPower(rightGP1);

        }
        else
        {
            robot.frontLeftMotor.setPower(0.25 *leftGP1);
            robot.frontRightMotor.setPower(0.25 * rightGP1);
        }

    }

    @Override
    public void stop()
    {

    }

    public void waitTau(long milliSec)
    {
        try{
            Thread.sleep(milliSec);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
