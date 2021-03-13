import java.awt.Rectangle;
import java.awt.Point;
import java.util.LinkedList;

public class CollisionDetection {
	
	/**
	 * Resolves collision between moving and static Rectangles
	 * 
	 * @param s		The moving Rectangle's current position
	 * @param sPrev	The moving Rectangle's previous position
	 * @param o		The static Rectangle
	 * @return 		the resolved position of the moving Rectangle
	 */
	public static Rectangle aabbCollisionResolution(Rectangle s, Rectangle sPrev, Rectangle o) {

		if (!aabbRectCollisionPossible(s, sPrev, o)) {
			return s;
		}

		Point sCenter = new Point(s.x + (s.width / 2), s.y + (s.height / 2));
		Point sCenterPrev = new Point(sPrev.x + s.width / 2, sPrev.y + s.height / 2);

		// Extend the object by half p.width and p.height to check against sCenter
		Rectangle r = new Rectangle(o.x - (s.width / 2) + 1, o.y - (s.height / 2) + 1, o.width + s.width - 2, o.height + s.height - 2);

		// Change in x and y
		double dirX = sCenter.x - sCenterPrev.x;
		double dirY = sCenter.y - sCenterPrev.y;

		// t value of collision between (xPrev, yPrev) = 0 and (p.x, p.y) = 1
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
		if (NEARtx > FARty || NEARty > FARtx || NEARtx == NEARty) {
			return s;
		}

		// t value of the collision along the ray
		double nearCollisionT = Math.max(NEARtx, NEARty);
		double farCollisionT = Math.min(FARtx, FARty);
		
		// No collision, collision is behind the original position or in front of new position
		if (farCollisionT < 0 || nearCollisionT > 1) {
			return s;
		}

		// Coordinates of collision
		Point collision = new Point(sCenterPrev.x + (int) (dirX * nearCollisionT), sCenterPrev.y + (int) (dirY * nearCollisionT));


		// Unit direction adjustment needs to occur
		Point collisionNormal;
		if (NEARtx > NEARty) {
			collisionNormal = new Point((int) -Math.signum(dirX), 0);
		} else {
			collisionNormal = new Point(0, (int) -Math.signum(dirY));
		}

		// Return resolved position, adjusted from center to top-left
		return new Rectangle(sCenter.x + (collisionNormal.x * Math.abs(sCenter.x - collision.x - collisionNormal.x)) - (s.width / 2),
							 sCenter.y + (collisionNormal.y * Math.abs(sCenter.y - collision.y - collisionNormal.y)) - (s.height / 2),
							 s.width,
							 s.height);
	}

	/**
	 * A lightweight initial collision test. Creates a large rectangle surrounding s
	 * and sPrev, and checks for intersection against o.
	 * 
	 * @param s     current position
	 * @param sPrev previous position
	 * @param o     rectangle to test collision against
	 * @return if collision between s and o is possible
	 */
	private static boolean aabbRectCollisionPossible(Rectangle s, Rectangle sPrev, Rectangle o) {
		Rectangle combined = new Rectangle(Math.min(s.x, sPrev.x),
				   						   Math.min(s.y, sPrev.y),
				   						   Math.abs(s.x - sPrev.x) + s.width,
				   						   Math.abs(s.y - sPrev.y) + s.height);
		return (combined.intersects(o));
	}
	
	public static void collisionWithObjects(Sprite s, Handler handler) {
		
		// Sprite hasn't moved, no collision
		if (s.x == s.xPrev && s.y == s.yPrev) {
			return;
		}
		
		s.standingOn = null;
		s.sideCollision = false;

		for (Object o : handler.loadedObjects) {
			Rectangle resolvedPos = CollisionDetection.aabbCollisionResolution(s.getCollider(), new Rectangle(s.xPrev, s.yPrev, s.width, s.height), o.getBoundingBox());
			if (s.x != resolvedPos.x || s.y != resolvedPos.y) {

				// Ceiling collision
				if (s.y < resolvedPos.y) {
					s.yVel = 0;
				}

				// Side collision
				if (resolvedPos.x == o.x + o.width || resolvedPos.x + s.width == o.x) {
					s.sideCollision = true;
				}

				s.x = resolvedPos.x;
				s.y = resolvedPos.y;
			}

			// On top of tempObject
			if (s.yVel >= 0 && s.y + s.height == o.y && !((s.x < o.x && s.x + s.width < o.x) || (s.x > o.x + o.width && s.x + s.width > o.x + o.width))) {
				s.standingOn = o.id;
			}
		}
	}
	
	public static LinkedList<Sprite> collisionWithSprites(Rectangle r, Handler handler) {
		LinkedList<Sprite> collidedWith = new LinkedList<>();
		for (Sprite s : handler.loadedSprites) {
			if (r.intersects(s.getCollider())) {
				collidedWith.add(s);
			}
		}
		return collidedWith;
	}
}
