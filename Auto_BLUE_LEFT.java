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
        }
        else if(jewelValue == 3){
            turnDegree(0.5,-30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            realign(0.5);
            sleepTau(1000);
        }
        else{
            telemetry.addData("Jewel", "Unknown");
            updateTelemetry(telemetry);
            raiseJewelServo();
            sleepTau(450);
        }
        driveForwardStraightDISTANCE(0.97);
        sleepTau(1500);
        realign(0.5);
        sleepTau(200);
        vuValue = leftGetVu();
        telemetry.addData("VuMark", vuValue);
        updateTelemetry(telemetry);
        sleepTau(100);
        realign(0.5);
        sleepTau(900);
        driveForwardStraightDISTANCE(0.08);
        sleepTau(500);
        driveLeftStraightDISTANCE(1.03);
        sleepTau(1550);
        turnDegree(1,5);
        sleepTau(100);
        turnToDegree(1, 180);
        sleepTau(1600);
        driveForwardStraightDISTANCE(0.65, 2.1);
        sleepTau(2500);
        turnToDegree(0.5,-180);
        sleep(400);
        if(vuValue.equals("CENTER")) {
            driveLeftStraightDISTANCE(0.5,0.54);
        }
        else if(vuValue.equals("LEFT")){
            driveLeftStraightDISTANCE(0.5,0.83);
        }
        else if(vuValue.equals("RIGHT")){
            driveLeftStraightDISTANCE(0.5,0.20);
        }
        else {
            driveLeftStraightDISTANCE(0.5,0.54);
        }
        sleepTau(1600);
        driveForwardStraightDISTANCE(0.7);
        sleepTau(1000);
        openClaw();
        sleepTau(250);
        driveBackwardStraightDISTANCE(0.5);
        sleepTau(1000);
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