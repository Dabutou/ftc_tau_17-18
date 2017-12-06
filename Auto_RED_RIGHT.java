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
        String vuValue = "";
        int jewelValue = 0;
        IMUandVu();

        //CALL WHATEVER METHODS HERE:
        closeClaw();
        getLiftPosition();
        lowerJewelServo();
        sleepTau(1500);
        raiseLiftSlightly();
        jewelValue = getJewel();
        if (jewelValue == 3){
            turnDegree(0.5,30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            realign(0.5);
            sleepTau(500);
        }
        else if(jewelValue == 10){
            turnDegree(0.5,-30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            realign(0.5);
            sleepTau(500);
        }
        else{
            telemetry.addData("Jewel", "Unknown");
            updateTelemetry(telemetry);
            raiseJewelServo();
            sleepTau(450);
        }
        driveForwardStraightDISTANCE(0.8);
        sleepTau(1500);
        realign(0.5);
        sleepTau(200);
        vuValue = leftGetVu();
        telemetry.addData("VuMark", vuValue);
        updateTelemetry(telemetry);
        sleepTau(100);
        realign(0.6);
        sleepTau(700);
        driveForwardStraightDISTANCE(0.5);
        if(vuValue.equals("CENTER")) {
            driveLeftStraightDISTANCE(0.5);
        }
        else if(vuValue.equals("LEFT")){
           driveLeftStraightDISTANCE(0.79);
        }
        else if(vuValue.equals("RIGHT")){
            driveLeftStraightDISTANCE(0.21);
        }
        else {
            driveForwardStraightDISTANCE(0.5);
        }
        sleepTau(1200);
        driveForwardStraightDISTANCE(0.5);
        sleepTau(1000);
        openClaw();
        sleepTau(250);
        lowerLiftSlightly();
        driveBackwardStraightDISTANCE(0.5,0.2);
        sleepTau(500);
        driveForwardStraightDISTANCE(0.5);
        sleepTau(2500);



    }



}
