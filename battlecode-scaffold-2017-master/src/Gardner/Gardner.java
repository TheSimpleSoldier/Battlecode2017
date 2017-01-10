package Gardner;

import battlecode.common.*;

public class Gardner extends Unit {
    public static void loop() throws GameActionException {
        System.out.println("I'm a gardener!");

                Direction dir = Util.randomDirection();

                MapLocation next = rc.getLocation().add(dir);

                // search for trees in need of watering

                // search for location to place trees

                if (next.x % 3 < 1 && next.y % 3 < 1) {
                    if (rc.canPlantTree(dir)) {
                        rc.plantTree(dir);
                    }
                } else {
                    TreeInfo[] trees = rc.senseNearbyTrees(10, rc.getTeam());
                    for (int i = 0; i < trees.length; i++) {
                        if (trees[i].getHealth() < 40 && rc.canWater(trees[i].getID())) {
                            rc.water(trees[i].getID());
                        }
                    }
                    Util.tryMove(dir);
                }
    }
}
