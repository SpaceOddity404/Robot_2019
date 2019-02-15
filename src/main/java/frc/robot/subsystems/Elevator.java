/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import badlog.lib.*;

import com.ctre.phoenix.motorcontrol.*;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.InvertType;

import frc.robot.subsystems.IgniteSubsystem;

public class Elevator extends IgniteSubsystem {

  private WPI_TalonSRX elevatorMaster;
  private WPI_VictorSPX elevatorFollower;

  private Command defaultCommand;
  
  private final double kF  = 0;
  private final double kP = 0;
  private final double kI = 0;
  private final double kD = 0;

  private final int MAX_ACCELERATION = 0;
  private final int CRUISE_VELOCITY = 0;

  public Elevator(int elevatorMasterID, int elevatorFollowerID) {

    elevatorMaster = new WPI_TalonSRX(elevatorMasterID);
    elevatorFollower = new WPI_VictorSPX(elevatorFollowerID);
    
    elevatorMaster.setNeutralMode(NeutralMode.Brake);
    elevatorFollower.setNeutralMode(NeutralMode.Brake);

    elevatorFollower.follow(elevatorMaster);

    elevatorMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 20);
    elevatorMaster.setStatusFramePeriod(StatusFrame.Status_10_MotionMagic, 10, 20);
    // elevatorMaster.setInverted(false); //TODO: set me
    // elevatorFollower.setInverted(InvertType.FollowMaster);
    // elevatorMaster.setSensorPhase(false);
    
    elevatorMaster.selectProfileSlot(0, 0);
    elevatorMaster.config_kF(0, kF, 10);
    elevatorMaster.config_kP(0, kP, 10);
    elevatorMaster.config_kI(0, kI, 10);
    elevatorMaster.config_kD(0, kD, 10);
    
    elevatorMaster.configMotionCruiseVelocity(CRUISE_VELOCITY, 10);
    elevatorMaster.configMotionAcceleration(MAX_ACCELERATION, 10);

    writeToLog();

  }

  public void establishDefaultCommand(Command command) {
    this.defaultCommand = command;
    initDefaultCommand();
  }

  public boolean checkSystem() {
    return true;
  }

  public void writeToLog() {
    BadLog.createTopic("Elevator/Master Percent Output", BadLog.UNITLESS, () -> this.getPercentOutput(), "hide", "join:Elevator/Output percents");
    BadLog.createTopic("Elevator/Master Voltage", "V", () -> this.getMasterVoltage(), "hide", "join:Elevator/Output Voltages");
    BadLog.createTopic("Elevator/Follower Voltage", "V", () -> this.getFollowerVoltage(), "hide", "join:Elevator/Output Voltages");
    BadLog.createTopic("Elevator/Master Current", "A", () -> this.getMasterCurrent(), "hide", "join:Elevator/Output Current");
    BadLog.createTopicStr("Elevator/Lower limit", "bool", () -> Boolean.toString(this.isLowerLimitTripped()));
    BadLog.createTopicStr("Elevator/Upper limit", "bool", () -> Boolean.toString(this.isUpperLimitTripped()));
  }

  public void outputTelemetry() {
    SmartDashboard.putNumber("Pos", this.getEncoderPos());
    SmartDashboard.putNumber("Vel", this.getEncoderVel());
    SmartDashboard.putNumber("Master voltage", this.getMasterVoltage());
    SmartDashboard.putNumber("Follower voltage", this.getFollowerVoltage());
    SmartDashboard.putNumber("Master current", this.getMasterCurrent());
    SmartDashboard.putNumber("Percent out", this.getPercentOutput());
    SmartDashboard.putBoolean("Is upper limit switch tripped?", this.isUpperLimitTripped());
    SmartDashboard.putBoolean("Is lower limit switch tripped?", this.isLowerLimitTripped());
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(this.defaultCommand);  
  }

  public void setOpenLoop(double percentage) {
    elevatorMaster.set(ControlMode.PercentOutput, percentage);
  }

  public void setMotionMagicPosition(double position) {
    elevatorMaster.set(ControlMode.MotionMagic, position);
  }

  public int getEncoderPos() {
    return elevatorMaster.getSensorCollection().getQuadraturePosition();
  }

  public double getEncoderVel() {
    return elevatorMaster.getSensorCollection().getQuadratureVelocity();
  }

  public double getMasterVoltage() {
    return elevatorMaster.getMotorOutputVoltage();
  }

  public double getFollowerVoltage() {
    return elevatorFollower.getMotorOutputVoltage();
  }
  
  public double getPercentOutput() {
    return elevatorMaster.getMotorOutputPercent();
  }
  
  public double getMasterCurrent() {
    return elevatorMaster.getOutputCurrent();
  }

  public void zeroSensors() {
    elevatorMaster.getSensorCollection().setQuadraturePosition(0, 10);
  }
  
  public boolean isUpperLimitTripped() {
    return elevatorMaster.getSensorCollection().isFwdLimitSwitchClosed();
  }

  public boolean isLowerLimitTripped() {
    return elevatorMaster.getSensorCollection().isRevLimitSwitchClosed();
  }

  public void stop() {
    elevatorMaster.stopMotor();
  }

}
