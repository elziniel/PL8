var express = require("express");
var net = require("net");
var twitch_api = require("twitch-api");

var app = express();
var table = [];

app.get("/", function(req, res){
  console.log("Request auth by " + req.query.state);

  var twitch = new twitch_api({
    clientId: '1gtamdxytwzi1ae016q9r2bm0zbbjhb',
    clientSecret: '808g7r535v7mocgt9jbffrh84912p2p',
    redirectUri: 'http://92.163.89.96:49170/',
    scopes: 'user_read'
  });

  twitch.getAccessToken(req.query.code, function(err, body) {
    if (err) {
      console.log("getAccessToken: " + err);
      res.send("Erreur lors de l'authentification avec twitch");
      var row = [];
      row["state"] = req.query.state;
      row["username"] = "null";
      table.push(row);
    }
    else {
      twitch.getAuthenticatedUser(body.access_token, function(err2, body2) {
        if (err2) {
          console.log("getAuthentificatedUser: " + err2);
          res.send("Erreur lors de la récupération de l'username");
          var row = [];
          row["state"] = req.query.state;
          row["username"] = "null";
          table.push(row);
        }
        else {
          var row = [];
          row["state"] = req.query.state;
          row["username"] = body2.display_name;
          table.push(row);
          console.log("Adding " + body2.display_name + " to the table");

          res.send("Bienvenue " + body2.display_name);
        }
      })
    }
  })
});

app.get("/state/:state", function(req, res){
  for (var i = 0; i < table.length; i++) {
    if (table[i]["state"] == req.params.state) {
      res.send("[OK] " + table[i]["username"]);
      console.log("Removing " + table[i]["username"] + " from the table");
      table.splice(i, 1);
      return;
    }
  }
  res.send("[KO] waiting");
  return;
});

app.listen(8080, function() {
  console.log("Listening on port 8080");
});
