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
        lowerJewelServo();
        sleepTau(500);
        raiseLiftSlightly();
        jewelValue = getJewel();
        if (jewelValue == 3) {
            turnToDegree(0.2,30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            turnToDegree(0.2,-10);
            sleepTau(1000);
        } else if(jewelValue == 10) {
            turnDegree(0.2,-30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            turnToDegree(0.2,-10);
            sleepTau(1000);
        } else {
            telemetry.addData("Jewel", "Unknown");
            updateTelemetry(telemetry);
            raiseJewelServo();
            sleepTau(450);
            turnToDegree(0.2,-10);
            sleepTau(1000);
        }

        vuValue = rightGetVuSMALL();
        telemetry.addData("VuMark", vuValue);
        updateTelemetry(telemetry);
        turnToDegree(0.35,-90);
        sleepTau(1600);
        turnToDegree(0.05,-87);
        sleepTau(800);
        driveRightStraightDISTANCE(0.3, 1.2);
        sleepTau(1500);
        turnToDegree(0.1,-90);
        sleepTau(700);
        glideFindRightWall();
        sleepTau(250);
        turnToDegree(0.1,-90);
        sleepTau(800);
        glideFindSpotFR(vuValue);
        lowerLiftSlightly();
        autoRepositionSideRED(vuValue);



    }



}
