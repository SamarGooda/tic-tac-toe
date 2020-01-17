package tictactoe.client.gui;

import com.google.gson.JsonObject;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tictactoe.client.App;

/**
 *
 * @author KeR
 */
public class InvitationScreen extends StackPane {

    private int challengerId;
    private String challengerName;
    private MultiOnlinePlayers multiOnlinePlayers;
    private App app;

    public InvitationScreen(App app) {
        this.app = app;
        multiOnlinePlayers = (MultiOnlinePlayers) app.getScreen("multiOnlinePlayers");

        Region rec = new Region();
        rec.setPrefSize(498, 460);
        rec.setId("regionInvitationScreen");
        DropShadow e = new DropShadow();
        e.setOffsetX(0.0f);
        e.setOffsetY(4.0f);
        e.setBlurType(BlurType.GAUSSIAN);
        e.setColor(Color.BLACK);
        String str = new String("Challenger");
        Button lose = new Button(str + " Challenges you");
        lose.setId("playerWantsToChallengeYou");
        lose.setEffect(e);
        /////////////////accept button//////////////////////
        ToggleButton accept = new ToggleButton("Accept");
        accept.setPrefSize(200, 20);
        accept.setId("acceptInvitation");
        accept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.setScreen("multiOnlinePlayers");
                multiOnlinePlayers.turn = false;
                JsonObject response = new JsonObject();
                JsonObject data = new JsonObject();
                response.add("data", data);
                response.addProperty("type", "accept-invitation");
                data.addProperty("inviting_player_id", challengerId);
                try {
                    app.getDataOutputStream().writeUTF(response.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        ///////////////////////decline button//////////////////////////
        ToggleButton Decline = new ToggleButton("Decline");
        Decline.setPrefSize(200, 20);
        Decline.setId("declineInvitation");
        Decline.setOnAction((t) -> {
            app.setScreen("main");
            JsonObject response = new JsonObject();
            JsonObject data = new JsonObject();
            response.add("data", data);
            response.addProperty("type", "decline-invitation");
            data.addProperty("inviting_player_id", challengerId);
            try {
                app.getDataOutputStream().writeUTF(response.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        ///////////////////////////////////////////////////////////////
        HBox buttonBox = new HBox(20, accept, Decline);

        VBox vbox = new VBox(100, lose, buttonBox);
        vbox.setId("vboxInvitationScreen");

        getChildren().addAll(rec, vbox);
        setId("stackInvitation");
    }

    public void setInvitation(int challengerId, String challengerName) {
        this.challengerId = challengerId;
        this.challengerName = challengerName;
        app.setScreen("invitation");
    }

}
