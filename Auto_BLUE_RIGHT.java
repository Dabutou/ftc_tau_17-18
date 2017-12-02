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
@Disabled
public class Auto_BLUE_RIGHT extends AUTO_METHODS{

    @Override
    public void runOpMode() throws InterruptedException {

        IMUandVu();

        //CALL WHATEVER METHODS HERE:
        closeClaw();
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