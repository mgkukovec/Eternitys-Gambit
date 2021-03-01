import java.awt.Rectangle;
import java.awt.Point;

public class CollisionDetection {

	/**
	 * Resolves collision between a moving AABB Rectangle and a stationary AABB
	 * Rectangle.
	 * 
	 * @param currentPos  the current position of the moving AABB Rectangle
	 * @param previousPos the previous position of the moving AABB Rectangle, must
	 *                    not begin in collision
	 * @param immovable   a stationary AABB Rectangle to test against
	 * @return the resolved position for the moving AABB Rectangle
	 */
	public static Rectangle aabbRectCollisionResolution(Rectangle currentPos, Rectangle previousPos, Rectangle immovable) {

		// Collision not possible
		if (!aabbRectCollisionPossible(currentPos, previousPos, immovable)) {
			return currentPos;
		}

		// Centers of rectangles
		Point sCenter = new Point(currentPos.x + (currentPos.width / 2), currentPos.y + (currentPos.height / 2));
		Point sCenterPrev = new Point(previousPos.x + currentPos.width / 2, previousPos.y + currentPos.height / 2);

		// Extend the object by half p.width and p.height to check against sCenter
		Rectangle r = new Rectangle(immovable.x - (currentPos.width / 2) + 1,
									immovable.y - (currentPos.height / 2) + 1,
									immovable.width + currentPos.width - 2,
									immovable.height + currentPos.height - 2);

		// Change in x and y
		double dirX = sCenter.x - sCenterPrev.x;
		double dirY = sCenter.y - sCenterPrev.y;

		// 't' value of collision along ray created by
		// (previousPos.x, previousPos.y) = 0      d and (currentPos.x, currentPos.y) = 1
		// If dividing by 0, set to +/- infinity. Not using MAXVALUE allows more efficient swaps.
		double NEARtx = (dirX == 0) ? 100000 * Math.signum(r.x - sCenterPrev.x) : (r.x - sCenterPrev.x) / dirX;
		double FARtx = (dirX == 0) ? 100000 * Math.signum(r.x + r.width - sCenterPrev.x) : (r.x + r.width - sCenterPrev.x) / dirX;
		double NEARty = (dirY == 0) ? 100000 * Math.signum(r.y - sCenterPrev.y) : (r.y - sCenterPrev.y) / dirY;
		double FARty = (dirY == 0) ? 100000 * Math.signum(r.y + r.height - sCenterPrev.y) : (r.y + r.height - sCenterPrev.y) / dirY;

		// Sort near and far 't' values
		if (NEARtx > FARtx) {
			NEARtx += FARtx;
			FARtx = NEARtx - FARtx;
			NEARtx -= FARtx;
		}
		if (NEARty > FARty) {
			NEARty += FARty;
			FARty = NEARty - FARty;
			NEARty -= FARty;
		}

		// No collision
		if (NEARtx > FARty || NEARty > FARtx || NEARtx == NEARty) {
			return currentPos;
		}

		// 't' value of the collision along the ray
		double nearCollisionT = Math.max(NEARtx, NEARty);
		double farCollisionT = Math.min(FARtx, FARty);

		// No collision, collision occurred behind or in front of ray
		if (farCollisionT < 0 || nearCollisionT > 1) {
			return currentPos;
		}

		// Coordinates of collision
		Point collision = new Point(sCenterPrev.x + (int) (dirX * nearCollisionT),
				sCenterPrev.y + (int) (dirY * nearCollisionT));

		// Unit direction adjustment needs to occur
		Point collisionNormal;
		if (NEARtx > NEARty) {
			collisionNormal = new Point((int) -Math.signum(dirX), 0);
		} else {
			collisionNormal = new Point(0, (int) -Math.signum(dirY));
		}

		// Return resolved position, adjusted from center to top-left
		return new Rectangle(sCenter.x + (collisionNormal.x * Math.abs(sCenter.x - collision.x - collisionNormal.x)) - (currentPos.width / 2),
							 sCenter.y + (collisionNormal.y * Math.abs(sCenter.y - collision.y - collisionNormal.y)) - (currentPos.height / 2),
							 currentPos.width,
							 currentPos.height);
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
		
		s.falling = true;
		s.standingOn = null;
		s.sideCollision = false;

		for (Object o : handler.loadedObjects) {
			Rectangle resolvedPos = CollisionDetection.aabbRectCollisionResolution(s.getBoundingBox(), new Rectangle(s.xPrev, s.yPrev, s.width, s.height), o.getBoundingBox());
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
			if (s.y + s.height == o.y && !((s.x < o.x && s.x + s.width < o.x)
					|| (s.x > o.x + o.width && s.x + s.width > o.x + o.width))) {
				s.falling = false;
				s.standingOn = o.id;
			}
		}
	}
}
