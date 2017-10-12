package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ThreadPool;


/**
 * Created by Lance He on 9/20/2017.
 */

@Autonomous(name = "Auto Red Right", group = "Tau")
//@Disabled
public class Auto_RED_RIGHT extends AUTO_METHODS{

    @Override//CALLS AUTO_METHODS TO RUN
    public void runOpMode() throws InterruptedException{

        IMUandVu();

        turnRobot(1,90,1);
        turnRobot(1,90,0);
        driveForwardStraightTIME(0.5,1500);
        sleepTau(2000);
        driveRightStraightTIME(0.75,1500);
        driveLeftStraightTIME(1,1500);

    }



}
