package Gardner;

import battlecode.common.*;

public strictfp class Unit {
    public static RobotController rc;
    public static Team enemy;
    public static Team us;
    public static RobotType myType;
    public static float mySensorRadius;
    public static float bulletVelocity;
    public static MapLocation[] enemyArchons;
    public static MapLocation[] ourArchons;

    public Unit() {
        rc = RobotPlayer.rc;
        us = rc.getTeam();
        enemy = us.opponent();
        myType = rc.getType();
        mySensorRadius = myType.sensorRadius;
        bulletVelocity = myType.bulletSpeed;
        enemyArchons = rc.getInitialArchonLocations(enemy);
        ourArchons = rc.getInitialArchonLocations(us);
    }

    public void loop() throws GameActionException {
        System.out.println("I am a unit!");
        Clock.yield();
    }
}
