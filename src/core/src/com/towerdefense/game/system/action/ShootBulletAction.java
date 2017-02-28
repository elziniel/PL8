package com.towerdefense.game.system.action;


import com.towerdefense.game.entity.Bullet;
import com.towerdefense.game.system.GameLoop;

public class ShootBulletAction extends Action {

    public static final String PARSE_VALUE = "ShootBullet";

    public static final String TARGET_ID = "tid";
    public static final String COLOR_TYPE = "clt";
    public static final String POS_X = "x";
    public static final String POS_Y = "y";
    public static final String DAMAGE = "dmg";

    public ShootBulletAction(){
        super();
    }

    public ShootBulletAction(float x, float y, int targetID, int colorType, int damage){
        super(PARSE_VALUE, Send.NONE);

        add(TARGET_ID, targetID+"");
        add(COLOR_TYPE, colorType+"");
        add(POS_X, x+"");
        add(POS_Y, y+"");
        add(DAMAGE, damage+"");
    }

    @Override
    public void execute(GameLoop gameLoop) {
        int targetID = Integer.parseInt(get(TARGET_ID));
        int color = Integer.parseInt(get(COLOR_TYPE));
        float x = Float.parseFloat(get(POS_X));
        float y = Float.parseFloat(get(POS_Y));
        int damage = Integer.parseInt(get(DAMAGE));

        Bullet b = Bullet.bulletPool.obtain();
        b.init(x, y, targetID, color, damage);

        gameLoop.entityManager.add(b);
    }
}
