/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.subsystems.Carriage;
import frc.robot.subsystems.Intake;

public class OuttakeCargo extends CommandGroup {

  public OuttakeCargo(Intake intake, Carriage carriage) {
    addSequential(new OpenIntake(intake, carriage, false));
    addParallel(new RollOutCargo(intake));
  }

}
