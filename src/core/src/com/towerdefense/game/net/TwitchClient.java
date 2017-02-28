package com.towerdefense.game.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.towerdefense.game.screen.TwitchConnectionScreen;
import com.towerdefense.game.util.Cst;

public class TwitchClient implements Runnable {
    private TwitchConnectionScreen tcs;

    private String username, clientId, redirectUri;
    private boolean connected, running;

    public TwitchClient(TwitchConnectionScreen t) {
        tcs = t;
    }

    @Override
    public void run() {
        String state = "";
        connected = false;
        running = true;

        if (tcs.getOnline()) {
            clientId = "1gtamdxytwzi1ae016q9r2bm0zbbjhb";
            redirectUri = "http://92.163.89.96:49170/";
        }
        else {
            clientId = "plevdhlyl4io4rqla3nxb9y7jioys3s";
            redirectUri = "http://localhost:8080/";
        }

        for (int i = 0; i < 10; i++) {
            state += (int) (Math.random() * 9);
        }

        Gdx.net.openURI("https://api.twitch.tv/kraken/oauth2/authorize?response_type=code&client_id="+clientId+"&redirect_uri="+redirectUri+"&scope=user_read&state="+state);

        while (!connected && running) {
            try {
                Thread.sleep(Cst.TWITCH_CLIENT_REQUEST_INTERVAL);
            }
            catch (Exception e) {
                running = false;
            }
            HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder();
            Net.HttpRequest httpRequest = httpRequestBuilder.newRequest().method(Net.HttpMethods.GET).url(redirectUri+"state/"+state).build();
            Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    String res = httpResponse.getResultAsString();
                    System.out.println(res);
                    if (!res.contains("[KO]")) {
                        username = res.substring(5, res.length());
                        if (username.equals("null")) {
                            tcs.setLabel("Connexion perdu");
                            tcs.setButton("Retour");
                            running = false;
                        }
                        else {
                            tcs.setPlayer(username);
                            tcs.setLabel("Connexion etablie !");
                            tcs.setButton("Accepter");
                            connected = true;
                            running = false;
                        }
                    }
                    else if (running) {
                        tcs.setLabel("Connexion en cours...");
                    }
                }

                @Override
                public void failed(Throwable t) {

                }

                @Override
                public void cancelled() {

                }
            });
        }
    }
}
