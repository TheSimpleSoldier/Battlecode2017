package Gardner;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.Team;

public class Unit {
    static RobotController rc;
    static Team enemy = rc.getTeam().opponent();

    public void loop() throws GameActionException {
        System.out.println("I am a unit!");
        Clock.yield();
    }
}
