package Gardner;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class Unit {
    static RobotController rc;

    public void loop() throws GameActionException {
        System.out.println("I am a unit!");
        Clock.yield();
    }
}
