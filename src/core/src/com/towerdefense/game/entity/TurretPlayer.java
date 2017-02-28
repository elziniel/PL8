package com.towerdefense.game.entity;


import com.towerdefense.game.util.Cst;

public class TurretPlayer extends Player{

    public static final String PARSE_TAG = "tp";

    private int score, money, turretType, turretLife;

    public TurretPlayer(String name){
        super(name);
        score = 0;
        money = 2000;
        turretLife = 10;
    }

    @Override
    public String encode() {
        return ID+
                Cst.ENTITY_SEPARATOR+
                PARSE_TAG+
                Cst.ENTITY_SEPARATOR+
                name+
                Cst.ENTITY_SEPARATOR+
                turretType+
                Cst.ENTITY_SEPARATOR+
                money+
                Cst.ENTITY_SEPARATOR+
                turretLife;
    }

    @Override
    public void update(Entity e) {
        TurretPlayer p = (TurretPlayer) e;

        setID(p.getID());
        name = p.name;
        turretType = p.turretType;
        money = p.money;
        turretLife = p.turretLife;
    }

    public static TurretPlayer decode(String encoded){
        String[] split = encoded.split(Cst.ENTITY_SEPARATOR);

        if(split.length != 6)
            return null;

        TurretPlayer t = new TurretPlayer(split[2]);
        t.setID(Integer.parseInt(split[0]));
        t.setType(Integer.parseInt(split[3]));
        t.setMoney(Integer.parseInt(split[4]));
        t.setLife(Integer.parseInt(split[5]));

        return t;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int s) {
        score = s;
    }

    public int getMoney() {
        return money;
    }
    public void setMoney(int m) {
        money = m;
    }

    public int getType() {
        return turretType;
    }
    public void setType(int type) {
        turretType = type;
    }

    public int getLife() {
        return turretLife;
    }
    public void setLife(int l) {
        turretLife = l;
    }
    public void addLife(int l) {
        turretLife += l;
    }
}
