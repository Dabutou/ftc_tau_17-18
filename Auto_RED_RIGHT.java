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
        //rough draft

        String vuValue = "";
        IMUandVu();

        //CALL WHATEVER METHODS HERE:
        closeClaw();
        getLiftPosition();
        lowerJewelServo();
        sleepTau(1000);
        raiseLiftSlightly();

        if (getColor() == 10){
            turnDegree(0.5,30);
        }
        else if(getColor() == 3){
            turnDegree(0.5,-30);
        }
        else{
            raiseJewelServoSlightly();
            if (getColor() == 10){
                turnDegree(0.5,30);
            }
            else if(getColor() == 3){
                turnDegree(0.5,-30);
            }
            else{
                telemetry.addData("Jewel", "Unknown");
                updateTelemetry(telemetry);
            }

        }
        sleepTau(3000);

        raiseJewelServo();
        sleepTau(500);
        realign(0.5);
        sleepTau(1000);
        driveForwardStraightDISTANCE(0.7,0.8);
        sleepTau(2000);
        turnToDegree(0.5,-10);
        sleepTau(500);

        vuValue = getVu();
        sleepTau(2000);
        realign(0.6);
        sleepTau(2000);
        driveForwardStraightDISTANCE(0.5, 0.1);
        sleepTau(500);
        if(vuValue.equals("CENTER")) {
            telemetry.addData("VuMark", "CENTER");
            updateTelemetry(telemetry);
            driveLeftStraightDISTANCE(0.7, 0.9);
        }
        else if(vuValue.equals("LEFT")){
            telemetry.addData("VuMark", "LEFT");
            updateTelemetry(telemetry);
            driveLeftStraightDISTANCE(0.7,1.2);
        }
        else if(vuValue.equals("RIGHT")){
            telemetry.addData("VuMark", "RIGHT");
            updateTelemetry(telemetry);
            driveLeftStraightDISTANCE(0.7,0.65);
        }
        else {
            telemetry.addData("VuMark", "UNKNOWN AND PROGRAMMERS BAD");
            updateTelemetry(telemetry);
            driveLeftStraightDISTANCE(0.7,0.9);
        }

        sleepTau(2500);
        openClaw();
        sleepTau(250);
        driveBackwardStraightDISTANCE(0.5,0.3);
        sleepTau(1500);
        lowerLiftSlightly();
        sleepTau(250);
        driveForwardStraightDISTANCE(0.5,1.2);
        sleepTau(2500);

    }



}
