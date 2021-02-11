import java.awt.Rectangle;

public class CollisionDetection {

	public static Rectangle rayBasedCollisionAdjustment(Sprite p, int prevX, int prevY, Rectangle o) {

		Sprite s = new Player(p.x + (p.width / 2), p.y + (p.height / 2), p.width, p.height, SpriteID.Player, null);
		Rectangle r = new Rectangle(o.x - (p.width / 2), o.y - (p.height / 2), o.width + p.width, o.height + p.height);

		prevX += p.width / 2;
		prevY += p.height / 2;

		double dirX = (double) s.x - prevX;
		double dirY = (double) s.y - prevY;

		// NOT coordinates
		// t represents the unit distance along the ray, usually between 0 and 1
		double NEARtx = (dirX == 0) ? (r.x - prevX) / 0.000000001 : (r.x - prevX) / dirX;
		double FARtx = (dirX == 0) ? (r.x + r.width - prevX) / 0.000000001 : (r.x + r.width - prevX) / dirX;
		double NEARty = (dirY == 0) ? (r.y - prevY) / 0.000000001 : (r.y - prevY) / dirY;
		double FARty = (dirY == 0) ? (r.y + r.height - prevY) / 0.000000001 : (r.y + r.height - prevY) / dirY;

		if (NEARtx > 1000 || NEARtx < -1000) {
			NEARtx = Integer.MAX_VALUE * Math.signum(NEARtx);
		}
		if (FARtx > 1000 || FARtx < -1000) {
			FARtx = Integer.MAX_VALUE * Math.signum(FARtx);
		}
		if (NEARty > 1000 || NEARty < -1000) {
			NEARty = Integer.MAX_VALUE * Math.signum(NEARty);
		}
		if (FARty > 1000 || FARty < -1000) {
			FARty = Integer.MAX_VALUE * Math.signum(FARty);
		}

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

		if (NEARtx > FARty || NEARty > FARtx) {
			return p.getBoundingBox();
		}

		// the t value of the collision along the ray
		double nearCollisionT = Math.max(NEARtx, NEARty);
		double farCollisionT = Math.min(FARtx, FARty);

		// Coordinates of collision
		int collisionX = prevX + (int) (dirX * nearCollisionT);
		int collisionY = prevY + (int) (dirY * nearCollisionT);

		if (farCollisionT < 0 || nearCollisionT > 1) {
			return p.getBoundingBox();
		}

		int contactNormalX = 0;
		int contactNormalY = 0;

		if (NEARtx > NEARty) {
			if (dirX < 0) {
				contactNormalX = 1;
				contactNormalY = 0;
			} else {
				contactNormalX = -1;
				contactNormalY = 0;
			}
		} else if (NEARtx < NEARty) {
			if (dirY < 0) {
				contactNormalX = 0;
				contactNormalY = 1;
			} else {
				contactNormalX = 0;
				contactNormalY = -1;
			}
		}

		return new Rectangle(s.x + (contactNormalX * Math.abs(s.x - collisionX - contactNormalX)) - (s.width / 2),
							 s.y + (contactNormalY * Math.abs(s.y - collisionY - contactNormalY)) - (s.height / 2),
							 s.width,
							 s.height);
	}

	public static boolean collisionAdjustment(Sprite s, Object o) {

		int playerLeftOverlap = o.x + o.width - s.x;
		int playerRightOverlap = s.x + s.width - o.x;
		int playerBottomOverlap = s.y + s.height - o.y;
		int playerTopOverlap = o.y + o.height - s.y;

		boolean overlappingLeft = false;
		boolean overlappingRight = false;
		boolean overlappingBottom = false;
		boolean overlappingTop = false;

		System.out.println("LEFT: " + playerLeftOverlap);
		System.out.println("RIGHT: " + playerRightOverlap);
		System.out.println("TOP: " + playerTopOverlap);
		System.out.println("BOTTOM: " + playerBottomOverlap);

		// Note: these collisions avoid checking if overlap > 0 because the intersects
		// method guarantees they are all > 0

		// Bottom of Player collision
		if (Math.abs(playerBottomOverlap) < Math.abs(playerTopOverlap)) {// && s.falling == true) {
			System.out.println("BOTTOM collision");
			overlappingBottom = true;
			s.falling = false;
		} else {
			s.falling = true;
		}
		// Top of Player Collision
		if (Math.abs(playerTopOverlap) < Math.abs(playerBottomOverlap)) {
			System.out.println("TOP collision");
			overlappingTop = true;
		}
		// Right of Player Collision
		if (Math.abs(playerRightOverlap) < Math.abs(playerLeftOverlap)) {
			System.out.println("RIGHT collision");
			overlappingRight = true;
		}
		// Left of Player Collision
		if (Math.abs(playerLeftOverlap) < Math.abs(playerRightOverlap)) {
			System.out.println("LEFT collision");
			overlappingLeft = true;
		}

		if (overlappingTop && overlappingLeft) {
			if (playerLeftOverlap < playerTopOverlap) {
				System.out.println("Adjusting RIGHT");
				s.x = o.x + o.width;
			} else {
				System.out.println("Adjusting DOWN");
				s.yVelocity = o.Vy;
				s.y = o.y + o.height;
			}
		} else if (overlappingTop && overlappingRight) {
			if (playerRightOverlap < playerTopOverlap) {
				s.x = o.x - s.width;
				System.out.println("Adjusting LEFT 1");
			} else {
				System.out.println("Adjusting DOWN");
				s.yVelocity = o.Vy;
				s.y = o.y + o.height;
			}
		} else if (overlappingBottom && overlappingLeft) {
			if (playerLeftOverlap < playerBottomOverlap) {
				System.out.println("Adjusting RIGHT");
				s.x = o.x + o.width;
			} else {
				System.out.println("Adjusting UP");
				s.y = o.y - s.height;
			}
		} else {
			if (playerRightOverlap < playerBottomOverlap) {
				System.out.println("Adjusting LEFT 2");
				s.x = o.x - s.width;
			} else {
				System.out.println("Adjusting UP");
				s.y = o.y - s.height;
			}
		}

		System.out.println("");
		if (!overlappingLeft && !overlappingRight && !overlappingTop && !overlappingBottom) {
			return false;
		} else {
			return true;
		}
	}
}
