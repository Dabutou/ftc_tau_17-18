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
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;package org.firstinspires.ftc.teamcode;

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

@Autonomous(name = "Auto Blue Left", group = "Tau")
//@Disabled
public class Auto_BLUE_LEFT extends AUTO_METHODS{

    @Override
    public void runOpMode() throws InterruptedException {
        String vuValue = "";
        int jewelValue = 0;
        IMUandVu();

        //CALL WHATEVER METHODS HERE:
        closeClaw();
        sleepTau(400);
        raiseLiftSlightly();
        vuValue = rightGetVu();
        telemetry.addData("VuMark", vuValue);
        updateTelemetry(telemetry);
        realign(0.2);
        sleepTau(1000);
        realign(0.1);
        sleepTau(750);
        lowerJewelServo();
        sleepTau(450);
        jewelValue = getJewel();
        if (jewelValue == 10) {
            turnToDegree(0.2,90);
            sleepTau(2000);
            raiseJewelServo();
            sleepTau(150);
            realign(0.2);
            sleepTau(1100);
            realign(0.1);
            sleepTau(600);
        } else if(jewelValue == 3) {
            turnDegree(0.2,-30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            turnToDegree(0.15, 90);
            sleepTau(3000);
            realign(0.2);
            sleepTau(1100);
            realign(0.1);
            sleepTau(600);
        } else {
            telemetry.addData("Jewel", "Unknown");
            updateTelemetry(telemetry);
            raiseJewelServo();
            sleepTau(450);
            turnToDegree(0.2, 90);
            sleepTau(2000);
        }

        driveRightStraightDISTANCE(0.2, 1.5);
        sleepTau(2000);
        realign(0.2);
        glideFindRightWall();
        sleepTau(3000);






        /*closeClaw();
        getLiftPosition();
        lowerJewelServo();
        sleepTau(1000);
        raiseLiftSlightly();
        jewelValue = getJewel();
        if (jewelValue == 10){
            turnDegree(0.5,30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            realign(0.5);
            sleepTau(1000);
            realign(0.1);
            sleepTau(700);
        }
        else if(jewelValue == 3){
            turnDegree(0.5,-30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            realign(0.5);
            sleepTau(1000);
            realign(0.1);
            sleepTau(700);
        }
        else{
            telemetry.addData("Jewel", "Unknown");
            updateTelemetry(telemetry);
            raiseJewelServo();
            sleepTau(450);
        }
        driveForwardStraightDISTANCE(0.4,0.97);
        sleepTauCheck(1750);
        realign(0.5);
        sleepTau(200);
        vuValue = leftGetVu();
        telemetry.addData("VuMark", vuValue);
        updateTelemetry(telemetry);
        sleepTau(100);
        realign(0.5);
        sleepTau(900);
        driveForwardStraightDISTANCE(0.08);
        sleepTauCheck(500);
        driveLeftStraightDISTANCE(1.10);
        sleepTauCheck(1550);
        turnDegree(1,5);
        sleepTau(250);
        turnToDegree(0.45, 176);
        sleepTau(2500);
        driveForwardStraightDISTANCE(0.6, 2.1);
        sleepTauCheck(2800);
        turnToDegree(0.5,-180);
        sleep(400);
        if(vuValue.equals("CENTER")) {
            driveLeftStraightDISTANCE(0.5,0.47);
        }
        else if(vuValue.equals("LEFT")){
            driveLeftStraightDISTANCE(0.5,0.91);
        }
        else if(vuValue.equals("RIGHT")){
            driveLeftStraightDISTANCE(0.5,0.14);
        }
        else {
            driveLeftStraightDISTANCE(0.5,0.47);
        }
        sleepTauCheck(1600);
        turnToDegree(0.3,-180);
        sleepTau(500);
        driveForwardStraightDISTANCE(0.7);
        sleepTauCheck(1000);
        openClaw();
        sleepTau(250);
        driveBackwardStraightDISTANCE(0.5);
        sleepTauCheck(1000);
        lowerLiftSlightly();
        sleepTau(500);
        turnToDegree(1, -90);
        sleepTau(700);
        autoRepositionSideBLUE(vuValue);
        sleepTau(150);
        turnToDegree(0.5,-90);
        sleepTau(1500);*/

    }

}
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

@Autonomous(name = "Auto Blue Left", group = "Tau")
//@Disabled
public class Auto_BLUE_LEFT extends AUTO_METHODS{

    @Override
    public void runOpMode() throws InterruptedException {
        String vuValue = "";
        int jewelValue = 0;
        IMUandVu();

        //CALL WHATEVER METHODS HERE:
        closeClaw();
        getLiftPosition();
        lowerJewelServo();
        sleepTau(1000);
        raiseLiftSlightly();
        jewelValue = getJewel();
        if (jewelValue == 10){
            turnDegree(0.5,30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            realign(0.5);
            sleepTau(1000);
            realign(0.1);
            sleepTau(700);
        }
        else if(jewelValue == 3){
            turnDegree(0.5,-30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            realign(0.5);
            sleepTau(1000);
            realign(0.1);
            sleepTau(700);
        }
        else{
            telemetry.addData("Jewel", "Unknown");
            updateTelemetry(telemetry);
            raiseJewelServo();
            sleepTau(450);
        }
        driveForwardStraightDISTANCE(0.4,0.97);
        sleepTauCheck(1750);
        realign(0.5);
        sleepTau(200);
        vuValue = leftGetVu();
        telemetry.addData("VuMark", vuValue);
        updateTelemetry(telemetry);
        sleepTau(100);
        realign(0.5);
        sleepTau(900);
        driveForwardStraightDISTANCE(0.08);
        sleepTauCheck(500);
        driveLeftStraightDISTANCE(1.10);
        sleepTauCheck(1550);
        turnDegree(1,5);
        sleepTau(250);
        turnToDegree(0.45, 176);
        sleepTau(2500);
        driveForwardStraightDISTANCE(0.6, 2.1);
        sleepTauCheck(2800);
        turnToDegree(0.5,-180);
        sleep(400);
        if(vuValue.equals("CENTER")) {
            driveLeftStraightDISTANCE(0.5,0.47);
        }
        else if(vuValue.equals("LEFT")){
            driveLeftStraightDISTANCE(0.5,0.91);
        }
        else if(vuValue.equals("RIGHT")){
            driveLeftStraightDISTANCE(0.5,0.14);
        }
        else {
            driveLeftStraightDISTANCE(0.5,0.47);
        }
        sleepTauCheck(1600);
        turnToDegree(0.3,-180);
        sleepTau(500);
        driveForwardStraightDISTANCE(0.7);
        sleepTauCheck(1000);
        openClaw();
        sleepTau(250);
        driveBackwardStraightDISTANCE(0.5);
        sleepTauCheck(1000);
        lowerLiftSlightly();
        sleepTau(500);
        turnToDegree(1, -90);
        sleepTau(700);
        autoRepositionSideBLUE(vuValue);
        sleepTau(150);
        turnToDegree(0.5,-90);
        sleepTau(1500);

    }

}
