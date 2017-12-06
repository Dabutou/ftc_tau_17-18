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
        raiseLiftMoreSlightly();
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
        sleepTau(1000);
        driveForwardStraightDISTANCE(0.08);
        sleepTau(500);
        driveLeftStraightDISTANCE(1.03);
        sleepTau(1750);
        turnDegree(1, 180);
        sleepTau(1500);
        driveForwardStraightDISTANCE(0.65, 2.1);
        sleepTau(2500);
        if(vuValue.equals("CENTER")) {
            driveLeftStraightDISTANCE(0.5,0.39);
        }
        else if(vuValue.equals("LEFT")){
            driveLeftStraightDISTANCE(0.5,0.68);
        }
        else if(vuValue.equals("RIGHT")){
            driveLeftStraightDISTANCE(0.5,0.1);
        }
        else {
            driveLeftStraightDISTANCE(0.5,0.39);
        }
        sleepTau(1600);
        lowerLiftMoreSlightly();
        sleepTau(500);
        openClaw();
        sleepTau(300);
        driveForwardStraightDISTANCE(0.7);
        sleepTau(1000);
        driveBackwardStraightDISTANCE(0.3);
        sleepTau(700);
        turnToDegree(1, -90);
        sleepTau(700);
        autoRepositionSideBLUE(vuValue);
    }

}