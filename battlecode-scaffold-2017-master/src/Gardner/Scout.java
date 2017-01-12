package Gardner;

import battlecode.common.*;
import scala.Int;

public class Scout extends Unit {
    public static MapLocation target;

    public void loop() throws GameActionException {
        BulletInfo[] bullets = rc.senseNearbyBullets();


        if (bullets.length > 0) {
            int smallestBulletCount = Int.MaxValue();
            Direction bestDir = null;
            MapLocation bestLoc = null;

            for (int i = 0; i < 16; i++) {
                int bulletCount = 0;
                Direction dir = new Direction(i / 8 * (float)Math.PI);

                MapLocation next = rc.getLocation().add(dir, rc.getType().strideRadius);

                for (int j = 0; j < bullets.length; j++) {
                    BulletInfo bullet = bullets[j];
                    float velocity = bullet.getSpeed();
                    Direction bulletDir = bullet.getDir();

                    int turn = Util.turnBulletWillHitLocation(bullet.location, next, velocity, bulletDir, 1);

                    if (turn == 1) {
                        bulletCount++;
                    }
                }


                if (bulletCount < smallestBulletCount) {
                    bestLoc = next;
                    bestDir = dir;
                    smallestBulletCount = bulletCount;
                } else if (bulletCount == smallestBulletCount) {
                    if (bestLoc.distanceSquaredTo(target) > next.distanceSquaredTo(target)) {
                        bestLoc = next;
                        bestDir = dir;
                    }
                }
            }

            if (smallestBulletCount == 0) {
                Util.tryMove(bestDir);
            } else {
                int bulletCount = 0;
                for (int i = 0; i < bullets.length; i++) {
                    BulletInfo bullet = bullets[i];
                    float velocity = bullet.getSpeed();
                    Direction dir = bullet.getDir();

                    int turn = Util.turnBulletWillHitLocation(bullet.location, rc.getLocation(), velocity, dir, 1);

                    if (turn == 1) {
                        bulletCount++;
                    }
                }

                if (bulletCount == 0) {
                    // do not move
                } else {
                    Util.tryMove(bestDir);
                }
            }
        } else {

        }
    }
}
