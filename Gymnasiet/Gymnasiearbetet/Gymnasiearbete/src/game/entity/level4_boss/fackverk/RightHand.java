package game.entity.level4_boss.fackverk;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import game.entity.Effect;
import game.entity.Player;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.tileMap.TileMap;

public class RightHand extends Hand{

	public RightHand(TileMap tm, ArrayList<EnemyProjectile> ep, Player player, ArrayList<Effect> ef) {
		super(tm, ep, player, ef);
		
		restingPos = new Point(29*30, 14*30+15);
		facingRight = true;
		
		isLeft = false;
		
		pointStrafeShoot2 = new Point2D.Double(19*30, 15*30+15);
		pointStrafeShoot1 = new Point2D.Double(520+460-50, 15*30+15);
		
		rightSpeed = 0.51363636363636;
		leftSpeed = -1.875;
		
		randStart = 190;
	}

}
