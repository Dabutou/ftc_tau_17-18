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
        IMUandVu();

        //CALL WHATEVER METHODS HERE:
        closeClaw();
        telemetry.addData("VuValue",getVu());
        updateTelemetry(telemetry);
        lowerJewelServo();
        sleepTau(1000);
        if (getColor() == 10){
            turnDegree(1,30);
        }
        else if(getColor() == 3){
            turnDegree(1,-30);
        }
        else{
            raiseJewelServoSlightly();
            if (getColor() == 10){
                turnDegree(1,30);
            }
            else if(getColor() == 3){
                turnDegree(1,-30);
            }

        }
        sleepTau(3000);
        raiseJewelServo();
        sleepTau(500);
        realign(1);
        sleepTau(1000);
        driveLeftStraightDISTANCE(0.7,0.5);
        sleepTau(3000);
        driveForwardStraightDISTANCE(0.7,1);
        sleepTau(1000);
        openClaw();
        sleepTau(1500);


    }



}
