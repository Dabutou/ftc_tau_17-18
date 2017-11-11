package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.Locale;


/**
 * Created by Lance He on 9/20/2017.
 */

@Autonomous(name = "Auto Red Left", group = "Tau")
//@Disabled
public class Auto_RED_LEFT extends AUTO_METHODS{


    @Override
    public void runOpMode() throws InterruptedException {
        String vuValue = "";
        IMUandVu();

        //CALL WHATEVER METHODS HERE:
        closeClaw();
        getLiftPosition();
        lowerJewelServo();
        sleepTau(1500);
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
        sleepTau(1500);

        raiseJewelServo();
        sleepTau(150);
        realign(0.5);
        sleepTau(500);
        driveForwardStraightDISTANCE(0.7,0.8);
        sleepTau(1500);
        realign(0.5);
        sleepTau(200);
        vuValue = leftGetVu();
        sleepTau(100);
        realign(0.6);
        sleepTau(700);
        if(vuValue.equals("CENTER")) {
            telemetry.addData("VuMark", "CENTER");
            updateTelemetry(telemetry);
            driveForwardStraightDISTANCE(0.7, 0.65);
        }
        else if(vuValue.equals("LEFT")){
            telemetry.addData("VuMark", "LEFT");
            updateTelemetry(telemetry);
            driveForwardStraightDISTANCE(0.7,1);
        }
        else if(vuValue.equals("RIGHT")){
            telemetry.addData("VuMark", "RIGHT");
            updateTelemetry(telemetry);
            driveForwardStraightDISTANCE(0.7,0.3);
        }
        else {
            telemetry.addData("VuMark", "UNKNOWN AND PROGRAMMERS BAD");
            updateTelemetry(telemetry);
            driveForwardStraightDISTANCE(0.7,0.65);
        }
        sleepTau(1500);
        turnToDegree(1,90.0);
        sleepTau(750);
        openClaw();
        sleepTau(150);
        driveBackwardStraightDISTANCE(0.5,0.3);
        sleepTau(500);
        lowerLiftSlightly();
        sleepTau(150);
        driveForwardStraightDISTANCE(0.5,0.55);
        sleepTau(1200);
        driveBackwardStraightDISTANCE(0.5,0.25);
        sleepTau(750);
        turnToDegree(1,-90);
        sleepTau(750);
        driveBackwardStraightDISTANCE(0.5,0.1);
        sleep(1500);




    }


}