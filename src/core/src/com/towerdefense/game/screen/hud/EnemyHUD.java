package com.towerdefense.game.screen.hud;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.towerdefense.game.Game;
import com.towerdefense.game.entity.Enemy;
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.screen.GameScreen;
import com.towerdefense.game.system.action.UpdateEnemyLifeAction;
import com.towerdefense.game.system.action.UpdateEnemySpeedAction;
import com.towerdefense.game.system.action.UpdateTurretPlayerAction;
import com.towerdefense.game.util.Assets;
import com.towerdefense.game.util.Cst;


public class EnemyHUD extends HUD {

    private GameScreen gameScreen;

    private Viewport viewport;

    private Label valueEnemies, valueType, valueLife, valueMoney, valueTurretLife;

    public EnemyHUD(GameScreen gameScreen){
        super();

        this.gameScreen = gameScreen;

        viewport = new ScreenViewport(new OrthographicCamera());
        setViewport(viewport);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Table table = new Table(Assets.uiSkin);
        table.setFillParent(true);
        table.top();

        Label labelType = new Label("Vous etes ", Assets.uiSkin);
        valueType = new Label("inconnu", Assets.uiSkin);
        table.add(labelType);
        table.row();
        table.add(valueType);
        table.row();

        Table tableEnemies = new Table(Assets.uiSkin);
        tableEnemies.setFillParent(false);

        Label labelEnemy = new Label("Amelioration", Assets.uiSkin);
        tableEnemies.add(labelEnemy);
        tableEnemies.row();

        TextButton enemyL = new TextButton("vie ("+Cst.ENEMY_LIFE_COST+")", Assets.uiSkin);
        enemyL.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameLoop.actionManager.addAction(new UpdateEnemyLifeAction(50, Game.gameLoop.player.getID()));
            }
        });
        tableEnemies.add(enemyL).width(100);
        tableEnemies.row();

        TextButton enemyS = new TextButton("Vitesse ("+Cst.ENEMY_SPEED_COST+")", Assets.uiSkin);
        enemyS.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameLoop.actionManager.addAction(new UpdateEnemySpeedAction(0.5f, Game.gameLoop.player.getID()));
            }
        });
        tableEnemies.add(enemyS).width(100);
        tableEnemies.row();

        table.add(tableEnemies).expandY();
        table.row();

        Label labelTurretLife = new Label("Vie tourelle : ", Assets.uiSkin);
        valueTurretLife = new Label("-1", Assets.uiSkin);
        table.add(labelTurretLife);
        table.add(valueTurretLife);
        table.row();

        Label labelLife = new Label("Vie : ", Assets.uiSkin);
        valueLife = new Label("-1", Assets.uiSkin);
        table.add(labelLife);
        table.add(valueLife);
        table.row();

        Label labelMoney = new Label("Argent : ", Assets.uiSkin);
        valueMoney = new Label("-1", Assets.uiSkin);
        table.add(labelMoney);
        table.add(valueMoney);
        table.row();

        Label labelEnemies = new Label("Ennemis : ", Assets.uiSkin);
        valueEnemies = new Label("-1", Assets.uiSkin);
        table.add(labelEnemies);
        table.add(valueEnemies);

        addActor(table);
    }

    @Override
    public void draw() {
        setDebugAll(Cst.DEBUG);

        getViewport().apply(true);
        super.draw();
    }

    @Override
    public void resize(int width, int height) {
        getViewport().setScreenBounds(width - Cst.HUD_WIDTH, 0, Cst.HUD_WIDTH, height);
        getViewport().setWorldSize(viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        int enemies = 0;
        for(Enemy e : Game.gameLoop.entityManager.getEnemies()) {
            if (e.isAlive()) {
                enemies++;
            }
        }
        valueEnemies.setText("" + enemies);
        valueTurretLife.setText(""+Game.gameLoop.entityManager.getTurretPlayer().getLife());

        if(Game.gameLoop.player == null){
            valueLife.setText("0");
            valueType.setText("mort");
        }
        else {
            Enemy enemy = (Enemy) Game.gameLoop.entityManager.getEntityByID(((EnemyPlayer) Game.gameLoop.player).enemyID);

            if(enemy == null){
                valueLife.setText("0");
                valueType.setText("mort");
            }
            else {
                valueMoney.setText(""+((EnemyPlayer)Game.gameLoop.player).getMoney());
                valueLife.setText(""+enemy.getLife());

                switch(enemy.getColor()){
                    case Cst.BLUE_TYPE:
                        valueType.setText("bleu");
                        break;
                    case Cst.RED_TYPE:
                        valueType.setText("rouge");
                        break;
                    case Cst.YELLOW_TYPE:
                        valueType.setText("jaune");
                        break;
                }
            }
        }
    }
}
