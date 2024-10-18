// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.xrp.XRPMotor;

public class XRPDrivetrain {
  private long lastEncoderTimerL = System.currentTimeMillis();
  private long lastEncoderTimerR = System.currentTimeMillis();

  private static final double kGearRatio =
      (30.0 / 14.0) * (28.0 / 16.0) * (36.0 / 9.0) * (26.0 / 8.0); // 48.75:1
  private static final double kCountsPerMotorShaftRev = 12.0;
  private static final double kCountsPerRevolution = kCountsPerMotorShaftRev * kGearRatio; // 585.0
  private static final double kWheelDiameterInch = 2.3622; // 60 mm

  // The XRP has the left and right motors set to
  // channels 0 and 1 respectively
  private final XRPMotor m_leftMotor = new XRPMotor(0);
  private final XRPMotor m_rightMotor = new XRPMotor(1);

  private Ultrasonic m_Ultrasonic = new Ultrasonic(0,1);

  // The XRP has onboard encoders that are hardcoded
  // to use DIO pins 4/5 and 6/7 for the left and right
  private final Encoder m_leftEncoder = new Encoder(4, 5);
  private final Encoder m_rightEncoder = new Encoder(6, 7);

  private double lastDistanceLeftEncoder = m_leftEncoder.getDistance();
  private double lastDistanceRightEncoder = m_rightEncoder.getDistance();

  // Set up the differential drive controller
  private final DifferentialDrive m_diffDrive =
      new DifferentialDrive(m_leftMotor::set, m_rightMotor::set);

  /** Creates a new XRPDrivetrain. */
  public XRPDrivetrain() {
    // Use inches as unit for encoder distances
    m_leftEncoder.setDistancePerPulse((Math.PI * kWheelDiameterInch) / kCountsPerRevolution);
    m_rightEncoder.setDistancePerPulse((Math.PI * kWheelDiameterInch) / kCountsPerRevolution);
    resetEncoders();

    // Invert right side since motor is flipped
    m_rightMotor.setInverted(true);
  }

  public void arcadeDrive(double xaxisSpeed, double zaxisRotate) {
    m_diffDrive.arcadeDrive(xaxisSpeed, zaxisRotate);
  }

  public void resetEncoders() {
    m_leftEncoder.reset();
    m_rightEncoder.reset();
  }

  public double getLeftDistanceInch() {
    return m_leftEncoder.getDistance();
  }

  public double getRightDistanceInch() {
    return m_rightEncoder.getDistance();
  }
  
  public double encoderRateTestLeft(){
    long currentTimeL= System.currentTimeMillis();

    long deltaTime = currentTimeL-lastEncoderTimerL;
    double deltaDistance = getLeftDistanceInch()-lastDistanceLeftEncoder;
    lastEncoderTimerL = currentTimeL;
    lastDistanceLeftEncoder = getLeftDistanceInch();
    return deltaDistance/deltaTime;
  }
  public double encoderRateTestRight(){
    long currentTimeR= System.currentTimeMillis();

    long deltaTime = currentTimeR-lastEncoderTimerR;
    double deltaDistance = getRightDistanceInch()-lastDistanceRightEncoder;
    lastEncoderTimerR = currentTimeR;
    lastDistanceRightEncoder = getRightDistanceInch();
    return deltaDistance/deltaTime;
  }
  public void setLeftMotorSpeed(double speed){
    m_leftMotor.set(speed);
  }
  public void setRightMotorSpeed(double speed){
    m_rightMotor.set(speed);
  }
  public void pingUltrasonic(){
    m_Ultrasonic.ping();
  }
  public double getUltrasonicDist(){

    return m_Ultrasonic.getRangeInches();
  }
  public void setUltrasonicChannels(int a, int b)
  {
    m_Ultrasonic = new Ultrasonic(a,b );
  }
}
