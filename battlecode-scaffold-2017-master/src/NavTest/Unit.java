package NavTest;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.Team;

public strictfp class Unit {
    public static RobotController rc;
    public static Team enemy;

    public Unit() {
        rc = RobotPlayer.rc;
        enemy = rc.getTeam().opponent();
    }

    public void loop() throws GameActionException {
        System.out.println("I am a unit!");
        Clock.yield();
    }
}
