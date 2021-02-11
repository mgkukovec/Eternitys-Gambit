import java.awt.Rectangle;
import java.awt.Point;

public class CollisionDetection {

	public static Rectangle resolveSpriteObjectCollision(Sprite s, Object o) {

		Point sCenter = new Point(s.x + (s.width / 2), s.y + (s.height / 2));
		Point sCenterPrev = new Point(s.prevX + s.width / 2, s.prevY + s.height / 2);
		
		// Extend the object by half p.width and p.height to check against sCenter
		Rectangle r = new Rectangle(o.x - (s.width / 2) + 1, o.y - (s.height / 2) + 1, o.width + s.width - 2, o.height + s.height - 2);

		// Change in x and y
		double dirX = sCenter.x - sCenterPrev.x;
		double dirY = sCenter.y - sCenterPrev.y;

		// t value of collision between (prevX, prevY) = 0 and (p.x, p.y) = 1
		// If dividing by 0, set to +/- infinity
		double NEARtx = (dirX == 0) ? Integer.MAX_VALUE * Math.signum(r.x - sCenterPrev.x) : (r.x - sCenterPrev.x) / dirX;
		double FARtx = (dirX == 0) ? Integer.MAX_VALUE * Math.signum(r.x + r.width - sCenterPrev.x) : (r.x + r.width - sCenterPrev.x) / dirX;
		double NEARty = (dirY == 0) ? Integer.MAX_VALUE * Math.signum(r.y - sCenterPrev.y) : (r.y - sCenterPrev.y) / dirY;
		double FARty = (dirY == 0) ? Integer.MAX_VALUE * Math.signum(r.y + r.height - sCenterPrev.y) : (r.y + r.height - sCenterPrev.y) / dirY;

		// Sort near and far t values
		if (NEARtx > FARtx) {
			double temp = NEARtx;
			NEARtx = FARtx;
			FARtx = temp;
		}
		if (NEARty > FARty) {
			double temp = NEARty;
			NEARty = FARty;
			FARty = temp;
		}

		// No collision
		if (NEARtx > FARty || NEARty > FARtx) {
			return s.getBoundingBox();
		}

		// t value of the collision along the ray
		double nearCollisionT = Math.max(NEARtx, NEARty);
		double farCollisionT = Math.min(FARtx, FARty);

		// Coordinates of collision
		Point collision = new Point(sCenterPrev.x + (int) (dirX * nearCollisionT), sCenterPrev.y + (int) (dirY * nearCollisionT));

		// No collision, collision is behind the original position or in front of new position
		if (farCollisionT < 0 || nearCollisionT > 1) {
			return s.getBoundingBox();
		}

		// Unit direction adjustment needs to occur
		Point collisionNormal = new Point(0, 0);
		if (NEARtx > NEARty) {
			if (dirX < 0)
				collisionNormal.setLocation(1, 0);
			else
				collisionNormal.setLocation(-1, 0);
		} else if (NEARtx < NEARty) {
			if (dirY < 0)
				collisionNormal.setLocation(0, 1);
			else
				collisionNormal.setLocation(0, -1);
		}

		// Return resolved position, adjusted from center to top-left
		return new Rectangle(sCenter.x + (collisionNormal.x * Math.abs(sCenter.x - collision.x - collisionNormal.x)) - (s.width / 2),
							 sCenter.y + (collisionNormal.y * Math.abs(sCenter.y - collision.y - collisionNormal.y)) - (s.height / 2),
							 s.width,
							 s.height);
	}
}
