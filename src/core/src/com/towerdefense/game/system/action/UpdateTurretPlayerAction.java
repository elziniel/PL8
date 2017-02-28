package com.towerdefense.game.system.action;

import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Log;

public class UpdateTurretPlayerAction extends Action {

    public static final String PARSE_VALUE = "UpdateTurretPlayer";

    public static final String TURRET_TYPE = "tt";

    public UpdateTurretPlayerAction(){
        super();
    }

    public UpdateTurretPlayerAction (int type) {
        super(PARSE_VALUE, Send.CLIENT);

        add(TURRET_TYPE, type+"");
    }

    @Override
    public void execute(GameLoop gameLoop) {
        int turretType = Integer.parseInt(get(TURRET_TYPE));

        Log.e(PARSE_VALUE, "Changement couleur turret : " + turretType);
        gameLoop.entityManager.getTurretPlayer().setType(turretType);
    }
}
