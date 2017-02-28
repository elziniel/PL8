package com.towerdefense.game.entity;


import com.towerdefense.game.util.Cst;

public class EnemyPlayer extends Player{

    public static final String PARSE_TAG = "ep";
    public int enemyID;
    public int money;

    public EnemyPlayer(String name){
        super(name);
        money = 0;
    }

    public int getMoney() {
        return money;
    }
    public void setMoney(int m) {
        money = m;
    }

    @Override
    public String encode() {
        return ID+
                Cst.ENTITY_SEPARATOR+
                PARSE_TAG+
                Cst.ENTITY_SEPARATOR+
                name+
                Cst.ENTITY_SEPARATOR+
                money+
                Cst.ENTITY_SEPARATOR+
                enemyID;
    }

    @Override
    public void update(Entity e) {
        EnemyPlayer p = (EnemyPlayer) e;
        name = p.name;
        money = p.money;
        enemyID = p.enemyID;
    }

    public static EnemyPlayer decode(String encoded){
        String[] split = encoded.split(Cst.ENTITY_SEPARATOR);
        if(split.length != 5)
            return null;

        EnemyPlayer e = new EnemyPlayer(split[2]);
        e.setID(Integer.parseInt(split[0]));
        e.money = Integer.parseInt(split[3]);
        e.enemyID = Integer.parseInt(split[4]);

        return e;
    }
}
