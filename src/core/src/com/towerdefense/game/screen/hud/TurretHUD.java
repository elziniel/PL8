package com.towerdefense.game.screen.hud;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.towerdefense.game.Game;
import com.towerdefense.game.entity.Enemy;
import com.towerdefense.game.entity.TurretTile;
import com.towerdefense.game.screen.GameScreen;
import com.towerdefense.game.system.action.LevelUpTurretAction;
import com.towerdefense.game.system.action.SellTurretAction;
import com.towerdefense.game.system.action.UpdateTurretPlayerAction;
import com.towerdefense.game.util.Assets;
import com.towerdefense.game.util.Cst;


public class TurretHUD extends HUD {

    private GameScreen gameScreen;

    private Viewport viewport;

    private Label valueEnemies, valueScore, valueMoney, valueLife;

    private TurretTile selectedTurret;

    private Table tableSelectedTurret;

    private Label selectedTurretType, selectedTurretLevel;

    private TextButton buttonVendreTurret, buttonLevelUpTurret;

    public TurretHUD(final GameScreen gameScreen){
        super();

        selectedTurret = null;

        this.gameScreen = gameScreen;

        viewport = new ScreenViewport(new OrthographicCamera());
        setViewport(viewport);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Table table = new Table(Assets.uiSkin);
        table.setFillParent(true);
        table.top();

        Table tableTurrets = new Table(Assets.uiSkin);
        tableTurrets.setFillParent(false);

        TextButton turretB = new TextButton("Bleu (400)", Assets.uiSkin);
        turretB.setColor(Color.BLUE);
        turretB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameLoop.actionManager.addAction(new UpdateTurretPlayerAction(Cst.BLUE_TYPE));
            }
        });
        tableTurrets.add(turretB).size(32);

        TextButton turretR = new TextButton("Rouge (400)", Assets.uiSkin);
        turretR.setColor(Color.RED);
        turretR.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameLoop.actionManager.addAction(new UpdateTurretPlayerAction(Cst.RED_TYPE));
            }
        });
        tableTurrets.add(turretR).size(32);

        TextButton turretJ = new TextButton("Jaune (400)", Assets.uiSkin);
        turretJ.setColor(Color.YELLOW);
        turretJ.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameLoop.actionManager.addAction(new UpdateTurretPlayerAction(Cst.YELLOW_TYPE));
            }
        });
        tableTurrets.add(turretJ).size(32);
        tableTurrets.row();

        TextButton wall = new TextButton("Mur (50)", Assets.uiSkin);
        wall.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameLoop.actionManager.addAction(new UpdateTurretPlayerAction(Cst.WALL_TYPE));
            }
        });
        tableTurrets.add(wall).width(96).colspan(3);

        selectedTurretLevel = new Label("", Assets.uiSkin);
        selectedTurretType = new Label("", Assets.uiSkin);
        buttonVendreTurret = new TextButton("", Assets.uiSkin);
        buttonVendreTurret.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameLoop.actionManager.addAction(new SellTurretAction((int)selectedTurret.position.x, (int)selectedTurret.position.y));
                setSelectedTurret(null);
            }
        });
        buttonLevelUpTurret = new TextButton("Ameliorer ("+Cst.TURRET_LEVEL_UP_COST+")", Assets.uiSkin);
        buttonLevelUpTurret.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameLoop.actionManager.addAction(new LevelUpTurretAction((int)selectedTurret.position.x, (int)selectedTurret.position.y));
            }
        });

        tableSelectedTurret = new Table(Assets.uiSkin);
        tableSelectedTurret.setFillParent(false);
        tableSelectedTurret.add(new Label("Tourelle selectionnee", Assets.uiSkin)).expandX();
        tableSelectedTurret.row();
        tableSelectedTurret.add(selectedTurretType).expandX();
        tableSelectedTurret.row();
        tableSelectedTurret.add(selectedTurretLevel).expandX();
        tableSelectedTurret.row();
        tableSelectedTurret.add(buttonLevelUpTurret).expandX();
        tableSelectedTurret.row();
        tableSelectedTurret.add(buttonVendreTurret).expandX();
        tableSelectedTurret.row();
        tableSelectedTurret.setVisible(false);

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pm.setColor(Color.DARK_GRAY);
        pm.fill();

        tableSelectedTurret.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pm))));


        table.pad(10);
        table.add(new Label("Ajouter une Tourelle", Assets.uiSkin)).center().pad(10).expandX();
        table.row();
        table.add(turretB).center().width(100).expandX();
        table.row();
        table.add(turretR).center().width(100).expandX();
        table.row();
        table.add(turretJ).center().width(100).expandX();
        table.row();
        table.add(wall).center().width(100).expandX();
        table.row();
        table.add(tableSelectedTurret).expandX().pad(20);
        table.row();

        valueScore = new Label("-1", Assets.uiSkin);
        table.add(new Label("Score : ", Assets.uiSkin));
        table.add(valueScore).left();
        table.row();

        Label labelLife = new Label("Vie : ", Assets.uiSkin);
        valueLife = new Label("-1", Assets.uiSkin);
        table.add(labelLife);
        table.add(valueLife).left();
        table.row();

        Label labelMoney = new Label("Argent : ", Assets.uiSkin);
        valueMoney = new Label("-1", Assets.uiSkin);
        table.add(labelMoney);
        table.add(valueMoney).left();
        table.row();

        valueEnemies = new Label("-1", Assets.uiSkin);
        table.add(new Label("Ennemis : ", Assets.uiSkin));
        table.add(valueEnemies).left();

        addActor(table);
    }

    @Override
    public void draw() {
        getViewport().apply(true);
        setDebugAll(Cst.DEBUG);
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
        valueScore.setText("" + Game.gameLoop.entityManager.getTurretPlayer().getScore());
        valueLife.setText("" + Game.gameLoop.entityManager.getTurretPlayer().getLife());
        valueMoney.setText("" + Game.gameLoop.entityManager.getTurretPlayer().getMoney());

        updateSelectedTurret();
    }

    private void updateSelectedTurret(){
        if(selectedTurret == null){
            selectedTurretType.setText("");
            selectedTurretLevel.setText("");
            tableSelectedTurret.setVisible(false);
            return;
        }

        String m = "Type: ";
        switch (selectedTurret.getColorType()){
            case Cst.BLUE_TYPE:
                m += "bleu";
                break;
            case Cst.RED_TYPE:
                m += "rouge";
                break;
            case Cst.YELLOW_TYPE:
                m += "jaune";
                break;
            default:
                break;
        }
        selectedTurretType.setText(m);

        selectedTurretLevel.setText("Niveau: "+selectedTurret.level);

        int prix = (int) (selectedTurret.level * Cst.TURRET_COST * Cst.TURRET_SELL_PERCENT);
        buttonVendreTurret.setText("Vendre ("+prix+")");

        tableSelectedTurret.setVisible(true);
    }

    public void setSelectedTurret(TurretTile tile){
        selectedTurret = tile;
    }
}
