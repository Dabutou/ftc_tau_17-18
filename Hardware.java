package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
/**
 * Created by Lance He on 9/16/2017.
 */

public class Hardware {

    // Motor variable names
    public DcMotor frontLeftMotor = null;
    public DcMotor frontRightMotor = null;
    public DcMotor backLeftMotor = null;
    public DcMotor backRightMotor = null;
    public DcMotor leftLiftMotor = null;
    public DcMotor rightLiftMotor = null;

    // Servo variable names
    public Servo leftLiftServo = null;
    public Servo rightLiftServo = null;

    // Sensor variable names


    // Other variable names
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    // Constant variable names
    public static final double LEFT_LIFT_OPEN = 0;
    public static final double LEFT_LIFT_CLOSE = 0;
    public static final double RIGHT_LIFT_OPEN = 0;
    public static final double RIGHT_LIFT_CLOSE = 0;

    public Hardware()
    {

    }

    public void init(HardwareMap hwMap)
    {

        // Save reference to Hardware map
        period.reset();
        this.hwMap = hwMap;

        // Define Motors
        frontLeftMotor = hwMap.dcMotor.get("left_front");
        frontRightMotor = hwMap.dcMotor.get("right_front");
        backLeftMotor = hwMap.dcMotor.get("left_back");
        backRightMotor = hwMap.dcMotor.get("right_back");
        leftLiftMotor = hwMap.dcMotor.get("left_lift");
        rightLiftMotor = hwMap.dcMotor.get("right_lift");

        // Initialize Motors
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // ******MAY CHANGE *******  Fix Forward/Reverse under testing
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);
        leftLiftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightLiftMotor.setDirection(DcMotor.Direction.FORWARD);

        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        leftLiftMotor.setPower(0);
        rightLiftMotor.setPower(0);

        // May use RUN_USING_ENCODERS if encoders are installed
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftLiftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightLiftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define servos
        //leftLiftServo = hwMap.servo.get("left_lift");
        //rightLiftServo = hwMap.servo.get("right_lift");

        // Initialize servos
        //leftLiftServo.setPosition(LEFT_LIFT_CLOSE);
        //rightLiftServo.setPosition(RIGHT_LIFT_CLOSE);

        // Define sensors

        // Initialize sensors


    }
}


