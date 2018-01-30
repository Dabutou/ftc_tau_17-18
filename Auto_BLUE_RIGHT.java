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

@Autonomous(name = "Auto Blue Right", group = "Tau")
//@Disabled
public class Auto_BLUE_RIGHT extends AUTO_METHODS{

    @Override
    public void runOpMode() throws InterruptedException {
        String vuValue = "";
        int jewelValue = 0;
        IMUandVu();
/*
        //CALL WHATEVER METHODS HERE:
        closeClaw();
        getLiftPosition();
        lowerJewelServo();
        sleepTau(1500);
        raiseLiftSlightly();
        jewelValue = getJewel();
        if (jewelValue == 10){
            turnDegree(0.5,30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            realign(0.5);
            sleepTau(500);
        }
        else if(jewelValue == 3){
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
        driveLeftStraightDISTANCE(0.75);
        sleepTau(1500);
        realign(0.5);
        sleepTau(200);
        vuValue = rightGetVu();
        telemetry.addData("VuMark", vuValue);
        updateTelemetry(telemetry);
        turnToDegree(1,-180);
        if(vuValue.equals("CENTER")) {
            driveForwardStraightDISTANCE(1.10);
        }
        else if(vuValue.equals("LEFT")){
            driveForwardStraightDISTANCE(1.39);
        }
        else if(vuValue.equals("RIGHT")){
            driveForwardStraightDISTANCE(0.81);
        }
        else {
            driveForwardStraightDISTANCE(1.10);
        }
        sleepTau(1500);
        turnToDegree(1,90);
        sleepTau(750);
        openClaw();
        sleepTau(1500);

*//*
        turnToDegree(1,0);
        sleepTau(2500);
        turnToDegree(1,90);
        sleepTau(2500);
        turnToDegree(1,-45);
        sleepTau(2500);
        turnToDegree(1, 180);
        sleepTau(2500);
        turnToDegree(1,0);
        sleepTau(2500);
        turnDegree(1,-90);
        sleepTau(2500);
        turnDegree(1,90);
        sleepTau(2500);*/
        ////getPositions();
        openClaw();
        sleepTau(150);
        //getPositions();
        /*driveForwardStraightDISTANCE(1);
        sleepTauCheck(2500);
        getImuHeading();
        driveBackwardStraightDISTANCE(1);
        sleepTauCheck(2500);
        //getPositions();
        getImuHeading();
        getPositions();
        driveForwardStraightDISTANCE(0.5,1);
        sleepTauCheck(2500);
        getImuHeading();
        getPositions();
        driveBackwardStraightDISTANCE(0.5,1);
        sleepTauCheck(2500);
        getImuHeading();
        //getPositions();
        getPositions();
        driveForwardStraightDISTANCE(0.25,1);
        sleepTauCheck(2500);
        getImuHeading();
        getPositions();
        driveBackwardStraightDISTANCE(0.25,1);
        sleepTauCheck(2500);
        getImuHeading();
        //getPositions();
        getPositions();
        driveLeftStraightDISTANCE(1);
        sleepTauCheck(2500);
        getImuHeading();
        getPositions();
        driveRightStraightDISTANCE(1);
        sleepTauCheck(2500);
        getImuHeading();
        //getPositions();
        getPositions();
        driveLeftStraightDISTANCE(0.5,1);
        sleepTauCheck(2500);
        getImuHeading();
        getPositions();
        driveRightStraightDISTANCE(0.5,1);
        sleepTauCheck(2500);
        getImuHeading();
        getPositions();
        //getPositions();
        driveLeftStraightDISTANCE(0.25,1);
        sleepTauCheck(2500);
        getImuHeading();
        getPositions();
        driveRightStraightDISTANCE(0.25,1);
        sleepTauCheck(2500);
        getImuHeading();*/
        //getPositions();
        //getPositions();
        //glideFindSpot();
        glideFindRightWall();


        /*driveLeftStraightDISTANCE(0.5,10);
        sleepTauCheck(20000);
*/
        /*turnDegree(1,180);
        sleepTau(2000);
        getImuHeading();
        turnDegree(1,180);
        sleepTau(2000);
        getImuHeading();
        realign(1);
        sleepTau(1000);
        turnDegree(1,-180);
        sleepTau(2000);
        turnDegree(1,-180);
        sleepTau(2000);
        realign(1);
        sleepTau(1000);
*/
    }

}