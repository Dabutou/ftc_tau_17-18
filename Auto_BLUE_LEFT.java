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
        lowerJewelServo();
        sleepTau(500);
        raiseLiftSlightly();
        jewelValue = getJewel();
        if (jewelValue == 10) {
            turnToDegree(0.35,8);
            sleepTau(700);
            raiseJewelServo();
            sleepTau(150);
            turnToDegree(0.2,-10);
            sleepTau(1000);
        } else if(jewelValue == 3) {
            turnToDegree(0.35,-8);
            sleepTau(700);
            raiseJewelServo();
            sleepTau(150);
            turnToDegree(0.2,-10);
            sleepTau(1000);
        } else {
            telemetry.addData("Jewel", "Unknown");
            updateTelemetry(telemetry);
            raiseJewelServo();
            sleepTau(450);
            turnToDegree(0.15,-10);
            sleepTau(1000);
        }

        vuValue = rightGetVuSMALL();
        telemetry.addData("VuMark", vuValue);
        updateTelemetry(telemetry);
        turnToDegree(0.35,90);
        sleepTau(1600);
        turnToDegree(0.05,87);
        sleepTau(800);
        driveRightStraightDISTANCE(0.3, 1.2);
        sleepTau(1200);
        glideFindRightWallBlue();
        sleepTau(250);
        turnToDegree(0.1,90);
        sleepTau(800);
        glideFindSpotBACKBL(vuValue);
        //lowerLiftSlightly();
        autoRepositionSIDEBLUE(vuValue);

    }

}
