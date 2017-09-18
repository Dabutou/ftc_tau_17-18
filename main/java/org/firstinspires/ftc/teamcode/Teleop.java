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
