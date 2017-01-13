package NavTest;

import battlecode.common.*;

public strictfp class Gardner extends Unit {
    static boolean initialized = false;
    static boolean farmer;
    static MapLocation idealSpot;
    static boolean[] treesPlanted = new boolean[8];

    public void loop() throws GameActionException {

        if (!initialized) {
            int count = rc.readBroadcast(0);

            if (count % 2 == 0) {
                farmer = false;
            } else {
                farmer = false;
            }

            rc.broadcast(0, ++count);

//            Direction dir = Util.randomDirection();

            idealSpot = rc.getLocation();

            initialized = true;
        }


        if (farmer) {
            Util.waterTrees();
             if (rc.getLocation().distanceSquaredTo(idealSpot) <= 1) {
                 boolean moveToWaterTrees = true;
                 if (rc.hasTreeBuildRequirements()) {
                     Direction dir;

                     dir = Direction.getNorth();

                     do {
                         if (rc.canPlantTree(dir)) {
                             rc.plantTree(dir);
                             break;
                         }
                         dir = dir.rotateRightDegrees(5);

                     } while (dir.getAngleDegrees() < 350);

//                     for (int i = 0; i < 8; i++) {
//                         dir = Util.getDirectionForInt(i);
//
//                         if (treesPlanted[i]) continue;
//                         moveToWaterTrees = false;
//
//                         if (!rc.isLocationOccupiedByTree(rc.getLocation().add(dir, 2))) {
//                             Util.tryMove(dir);
//                             if (rc.canPlantTree(dir)) {
//                                 rc.plantTree(dir);
//                                 treesPlanted[i] = true;
//                                 break;
//                             }
//                         }
//                     }
                 } else {
                     moveToWaterTrees = false;
                     Util.moveToWaterTrees();
                 }

                 if (!moveToWaterTrees) {
                     Util.moveToWaterTrees();
                 }


             } else {
                 Direction dir = rc.getLocation().directionTo(idealSpot);
                 if (Util.tryMove(dir));
                 else if (Util.tryMove(dir.rotateRightDegrees(45)));
                 else if (Util.tryMove(dir.rotateLeftDegrees(45)));
                 else {
                     TreeInfo[] trees = rc.senseNearbyTrees();

                     for (int i = trees.length; --i>=0;) {
                         if (rc.canChop(trees[i].getID())) {
                             rc.chop(trees[i].getID());
                         }
                     }
                 }
             }
        } else {
            if (rc.hasRobotBuildRequirements(RobotType.LUMBERJACK)) {
                for (int i = 0; i < 8; i++) {
                    Direction dir = Util.getDirectionForInt(i);

                    if (rc.canBuildRobot(RobotType.LUMBERJACK, dir)) {
                        rc.buildRobot(RobotType.LUMBERJACK, dir);
                        farmer = true;
                    }
                }
            }

            Direction dir = Util.randomDirection();
            MapLocation next = rc.getLocation().add(dir);
            TreeInfo[] trees = rc.senseNearbyTrees();

            TreeInfo treeToShake = null;
            float closestTreeToShake = Float.MAX_VALUE;

            TreeInfo treeToWater = null;
            float closestTreeToWater = Float.MAX_VALUE;

            for (int i = 0; i < trees.length; i++) {
                TreeInfo tree = trees[i];

                // look to shake
                if (tree.getTeam() == Team.NEUTRAL) {
                    if (rc.canShake(tree.getID())) {
                        rc.shake(tree.getID());
                    } else {
                        float dist = rc.getLocation().distanceSquaredTo(tree.location);

                        if (dist < closestTreeToShake) {
                            closestTreeToShake = dist;
                            treeToShake = tree;
                        }
                    }
                } else if (tree.getTeam() == rc.getTeam()) {
                    if (tree.getHealth() < 40) {
                        if (rc.canWater(tree.getID())) {
                            rc.water(trees[i].getID());
                        } else {
                            float dist = rc.getLocation().distanceSquaredTo(tree.location);

                            if (dist < closestTreeToWater) {
                                closestTreeToWater = dist;
                                treeToWater = tree;
                            }
                        }
                    }
                }
            }


            if (treeToWater != null) {
                dir = rc.getLocation().directionTo(treeToWater.location);
            } else if (treeToShake != null) {
                dir = rc.getLocation().directionTo(treeToShake.location);
            }
            Util.tryMove(dir);
        }



//        System.out.println("I'm a gardener!");
//
//                Direction dir = Util.randomDirection();
//
//                MapLocation next = rc.getLocation().add(dir);
//
//                // search for trees in need of watering
//
//                // search for location to place trees
//
//                if (next.x % 3 < 1 && next.y % 3 < 1) {
//                    if (rc.canPlantTree(dir)) {
//                        rc.plantTree(dir);
//                    }
//                } else {
//                    TreeInfo[] trees = rc.senseNearbyTrees(10, rc.getTeam());
//                    for (int i = 0; i < trees.length; i++) {
//                        if (trees[i].getHealth() < 40 && rc.canWater(trees[i].getID())) {
//                            rc.water(trees[i].getID());
//                        }
//                    }
//                    Util.tryMove(dir);
//                }
    }
}
