package tictactoe.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.stage.StageStyle;
import tictactoe.client.gui.*;

public class App extends Application {

    private HashMap<String, Pane> screens = new HashMap<>();
    private Scene mainScene;
    private Socket s;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private JsonHandler jsonHandler;
    private Stage pStage;

    public App() {
        addScreens();
        jsonHandler = new JsonHandler(this);
        try {
            s = new Socket(Config.SERVER_IP, Config.PORT);
            dataInputStream = new DataInputStream(s.getInputStream());
            dataOutputStream = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        String line = dataInputStream.readUTF();
                        if (line != null) {
                            JsonObject obj = JsonParser.parseString(line).getAsJsonObject();
                            System.out.println(obj);
                            jsonHandler.handle(obj);
                        }
                    } catch (IOException ex) {
                        showAlert("You are disconnected!", "Please check your connection and try again.");
                        setScreen("signin");
                        Platform.exit();
                        ex.printStackTrace();
                    }
                }

            }
        }).start();
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    private void addScreens() {
        screens.put("main", new MainScreen(this));
        screens.put("signin", new SigninScreen(this));
        screens.put("signup", new SignupScreen(this));
        screens.put("hardLuck", new HardLuckScreen(this));
        screens.put("invitation", new InvitationScreen(this));
        screens.put("multiPlayerGameBoard", new MultiPlayerGameBoardScreen(this));      //old edited by tharwat
        screens.put("multiOnlinePlayers", new MultiOnlinePlayers(this));        //new kareem's
        screens.put("levels", new LevelsScreen(this));
        screens.put("youWin", new YouWinScreen(this));
        screens.put("playWithComputerEasyGameBoard", new PlayWithComputerEasyGameBoardScreen(this));
        screens.put("playWithComputerNormalGameBoard", new PlayWithComputerNormalGameBoardScreen(this));
        screens.put("playWithComputerHARDGameBoard", new PlayWithComputerHARDGameBoardScreen(this));
        screens.put("nooneIsTheWinner", new NooneIsTheWinnerScreen(this));
        screens.put("playerList", new PlayerListScreen(this));

    }

    public void setScreen(String screenName) {
        mainScene.setRoot(screens.get(screenName));
    }

    public Pane getScreen(String screen) {
        return screens.get(screen);
    }

    /////////////////////error handling methods////////////////////////////////
    public void showAlert(String title, String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle(title);
                a.setHeaderText("");
                a.setContentText(msg);
                a.show();
            }
        });
    }

    public void exit() {
        JsonObject jsonObject = new JsonObject();
        JsonObject data = new JsonObject();
        jsonObject.addProperty("type", "signout");

        try {
            dataOutputStream.writeUTF(jsonObject.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Platform.exit();
        System.exit(0);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        pStage = primaryStage;
        primaryStage.setTitle("TIC TAC TOE!");
        mainScene = new Scene(screens.get("signin"), 1350, 700);
        mainScene.getStylesheets().add(getClass().getResource("/css/style.css").toString());
        primaryStage.setScene(mainScene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        pStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                exit();
            }
        });
    }

    @Override

    public void init() throws Exception {
        super.init();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
