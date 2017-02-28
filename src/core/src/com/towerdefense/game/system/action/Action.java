package com.towerdefense.game.system.action;


import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Cst;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Action {

    protected static final String PARSE_KEY = "parse";

    private HashMap<String, String> container;

    public enum Send{
        SERVER,
        CLIENT,
        SERVER_BROADCAST,
        NONE
    };

    protected Send requireSend;

    protected String pseudo;

    /**
     * Constructeur avec container vide et aucun send
     */
    public Action(){
        container = new HashMap<String, String>();
        this.requireSend = Send.NONE;
    }

    /**
     * Contructeur qui ajoute le parse_key en fonction du type d'action et
     * définie le requireSend
     * @param parseValue valeur du parse_key
     * @param requireSend demande d'envoi de l'action server ou client side
     */
    public Action(String parseValue, Send requireSend){
        container = new HashMap<String, String>();
        this.requireSend = requireSend;

        add(PARSE_KEY, parseValue);
    }

    /**
     * Constructeur qui ajoute le parse_key en fonction du type d'action et
     * spécifie à quel client doit etre envoyé l'action
     * @param parseTag valeur du parse_key
     * @param pseudoToSendTo pseudo du client à qui doit être envoyé l'action
     */
    public Action(String parseTag, String pseudoToSendTo){
        this(parseTag, Send.SERVER);
        this.pseudo = pseudoToSendTo;
    }

    /**
     * Exeéute l'action
     * @param gameLoop
     */
    public abstract void execute(GameLoop gameLoop);

    /**
     * Ajoute une variable
     * @param key clef de la variable
     * @param value valeur de la variable
     */
    protected void add(String key, String value){
        container.put(key, value);
    }

    /**
     * Met à jour une variable, l'ajoute si elle n'existe pas
     * @param key clef de la variable
     * @param value nouvelle valeur de la variable
     */
    protected void update(String key, String value){
        if(container.containsKey(key)){
            container.remove(key);
        }
        add(key, value);
    }

    /**
     * Récupère la valeur d'une variable
     * @param key clef de la variable
     * @return valeur de la variable
     */
    public String get(String key){
        if(container.containsKey(key))
            return container.get(key);
        return null;
    }

    /**
     * Encode en String l'action
     * @return action encodé
     */
    public String encode(){
        String encoded = "";

        Iterator<Map.Entry<String, String>> iterator = container.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            encoded += entry.getKey() + Cst.ACTION_SEPARATOR_VARIABLE + entry.getValue();

            if(iterator.hasNext())
                encoded += Cst.ACTION_SEPARATOR;
        }

        return encoded;
    }

    /**
     * Decode une action à partir d'un String
     * @param message action encodé
     * @return action
     */
    public static Action decode(String message){
        String[] split = message.split(Cst.ACTION_SEPARATOR);

        String parseTag = null;
        for(String s : split){
            String[] ssplit = s.split(Cst.ACTION_SEPARATOR_VARIABLE);
            if(ssplit.length == 2 && ssplit[0].equals(PARSE_KEY)){
                parseTag = ssplit[1];
                break;
            }
        }

        if(parseTag == null)
            return null;

        Action action = getAction(parseTag);

        if(action == null)
            return null;

        for(String s : split){
            String[] ssplit = s.split(Cst.ACTION_SEPARATOR_VARIABLE);
            if(ssplit.length == 2){
                action.add(ssplit[0], ssplit[1]);
            }
        }

        return action;
    }

    private static Action getAction(String parsetag){
        if(parsetag.equals(AddTurretAction.PARSE_VALUE))
            return new AddTurretAction();
        if(parsetag.equals(ConnectAskAction.PARSE_VALUE))
            return new ConnectAskAction();
        if(parsetag.equals(ConnectGrantedAction.PARSE_VALUE))
            return new ConnectGrantedAction();
        if(parsetag.equals(ConnectRefusedAction.PARSE_VALUE))
            return new ConnectRefusedAction();
        if(parsetag.equals(RTTAskAction.PARSE_VALUE))
            return new RTTAskAction();
        if(parsetag.equals(RTTRespondAction.PARSE_VALUE))
            return new RTTRespondAction();
        if(parsetag.equals(ShootBulletAction.PARSE_VALUE))
            return new ShootBulletAction();
        if(parsetag.equals(SpawnEnemyAction.PARSE_VALUE))
            return new SpawnEnemyAction();
        if(parsetag.equals(UpdateAction.PARSE_VALUE))
            return new UpdateAction();
        if(parsetag.equals(UpdateEnemyLifeAction.PARSE_VALUE))
            return new UpdateEnemyLifeAction();
        if(parsetag.equals(UpdateEnemySpeedAction.PARSE_VALUE))
            return new UpdateEnemySpeedAction();
        if(parsetag.equals(UpdateTurretPlayerAction.PARSE_VALUE))
            return new UpdateTurretPlayerAction();
        if(parsetag.equals(UpdateVelocityAction.PARSE_VALUE))
            return new UpdateVelocityAction();
        if(parsetag.equals(LaunchGameAction.PARSE_VALUE))
            return new LaunchGameAction();
        if(parsetag.equals(RequestLaunchGameAction.PARSE_VALUE))
            return new RequestLaunchGameAction();
        if(parsetag.equals(DisconnectAction.PARSE_VALUE))
            return new DisconnectAction();
        if(parsetag.equals(SellTurretAction.PARSE_VALUE))
            return new SellTurretAction();
        if(parsetag.equals(LevelUpTurretAction.PARSE_VALUE))
            return new LevelUpTurretAction();

        return null;
    }

    public static String getActionType(String encoded){
        String[] split = encoded.split(Cst.ACTION_SEPARATOR);

        for(String s : split){
            String[] ssplit = s.split(Cst.ACTION_SEPARATOR_VARIABLE);
            if(ssplit.length == 2 && ssplit[0].equals(PARSE_KEY))
                return ssplit[1];
        }

        return null;
    }

    /**
     * Pseudo du client qui doit recevoir cette action
     * @return
     */
    public String getPseudo(){
        return pseudo;
    }

    public Send requireSending(){
        return requireSend;
    }
}
