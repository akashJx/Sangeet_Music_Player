package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


import javax.management.Notification;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;



public class Controller  implements Initializable {


    Notifications notifications;
    String songName = "";
    String songNameatLabel = "";


    @FXML
    private Label minimizeButton;

    @FXML
    private JFXSlider volumeSlider;

    @FXML
    ImageView volumeIco;

    Image volume = new Image("iconsImages/volicon.png");
    Image mute = new Image("iconsImages/muteIco.png");

    @FXML
    ImageView BIGLOGO;
    Image GreyLogo = new Image("iconsImages/BIGLOGO.png");
    Image RedLogo = new Image("iconsImages/REDBIGLOGO.png");

    @FXML
    Label nowPlaying;

    @FXML
    AnchorPane musicDDpane;

    @FXML
    ImageView lovedIt;
    Image redHeart = new Image("iconsImages/heartRed.png");
    Image blackHeart = new Image("iconsImages/heartBlack.png");

    @FXML
    ImageView playpauseImg;
    Image pauseImg = new Image("iconsImages/icons8_Pause_96.png");
    Image playImg = new Image("iconsImages/icons8_Play_96.png");
    Image MusicRecIMG = new Image("iconsImages/MusicRecord_96.png");


    static int play_back_status = 0;
    private Double totalTimeOfMusic ;
    private int i = 0;
    private int j = 0;
    private String songpath="";
    private HashMap<Integer, String> songList  = new HashMap<>();
    private HashMap<Integer, String> songNameList = new HashMap<>();

    private static MediaPlayer mediaPlayer;
    private MediaPlayer.Status playBtatus = MediaPlayer.Status.STOPPED;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        volumeIco.setImage(volume);

        musicDDpane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.consume();
                }
            }
        });

        musicDDpane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    String filePath = null;
                    for (File file:db.getFiles()) {
                        filePath = file.getAbsolutePath(); // ABSOLUTE FILE PATH

                        if(file!=null && playBtatus == MediaPlayer.Status.PLAYING){
                            System.out.println(" PLAYBACK STATUS CHECKED ");
                              mediaPlayer.stop();
                        }

                        play_back_status = 1;
                   
                        playpauseImg.setImage(pauseImg);
                        lovedIt.setImage(blackHeart);
                        BIGLOGO.setImage(RedLogo);
                        //-----------------------------

                        songpath = filePath;
                        songList.put(i++,songpath);
                        playthisSong(songpath);

                        volumeSlider.setValue(mediaPlayer.getVolume() * 100);
                        volumeSlider.valueProperty().addListener(new InvalidationListener() {
                            @Override
                            public void invalidated(Observable observable) {
                                mediaPlayer.setVolume(volumeSlider.getValue() / 100);
                            }
                        });
                        //------------------------------------------------------------------
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });

        minimizeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ((Stage)  minimizeButton.getScene().getWindow()).setIconified(true);
            }
        });
    } //--@End of initialize-----------------------------------------


    @FXML
    public void changePlayPauseImg(){ // changePlayPauseImg
        System.out.println("Play / Pause Icon Changed.");
        if(playpauseImg.getImage() == pauseImg){
            mediaPlayer.pause();
            BIGLOGO.setImage(GreyLogo);
            playBtatus = MediaPlayer.Status.PAUSED;
            System.out.println("Status should be PAUSED here STATUS : " + playBtatus);
            playpauseImg.setImage(playImg);

        } else if(playpauseImg.getImage() == playImg){
            mediaPlayer.play();
            playBtatus = MediaPlayer.Status.PLAYING;
            BIGLOGO.setImage(RedLogo);
            System.out.println("Status should be PLAYING here STATUS : " + playBtatus);
            playpauseImg.setImage(pauseImg);

        }
    }


    @FXML
    private void closeButton(){
        System.exit(0);
    }

    @FXML
    private void prevSong(){

        if(i>0){
            songpath = songList.get(--i);
            j = i;
            System.out.println("\nSucessfull Prev Song");
            songName = songNameList.get(j);
            mediaPlayer.stop();
            Media song = new Media(new File(songpath).toURI().toString());
            myPlayerNotification(songName);
            notifications.show();
            playthisSong(song);
        }
    }

    @FXML
    private void nextSong(){
        System.out.println("VALUE OF i : " + i);
        songpath = songList.get(++i);
        j = i;
        System.out.println("VALUE OF i : " + j );
        songName = songNameList.get(j);
        System.out.println("GOT THE SONG : " + songName);
        System.out.println("\nSucessfull Next Song");
        mediaPlayer.stop();
        Media song = new Media(new File(songpath).toURI().toString());

        myPlayerNotification(songName);
        notifications.show();
        playthisSong(song);
    }

    @FXML
    private void LovedItClicked(){
        if(lovedIt.getImage() == blackHeart){
            lovedIt.setImage(redHeart);
        } else if(lovedIt.getImage() == redHeart){
            lovedIt.setImage(blackHeart);
        }

    }

    @FXML
    private void setsongNameatLabel(String s){
        nowPlaying.setText(" Now Playing \n" +
                " " + s);
    }


    @FXML
    private void changeVolico(){

        if(volumeIco.getImage() == volume){
            volumeIco.setImage(mute);
            volumeSlider.setValue(0);
        } else if(volumeIco.getImage() == mute){
            volumeIco.setImage(volume);
            volumeSlider.setValue(80);
        }

    }

    private void playthisSong(String songFile){
        Media song = new Media(new File(songFile).toURI().toString());
        System.out.println("FILE INPUT SUCESSFULL.!!" + songFile );
        mediaPlayer = new MediaPlayer(song);
        // mediaPlayer.play();

       mediaPlayer.setOnReady(new Runnable() {
           @Override
           public void run() {
               totalTimeOfMusic = song.getDuration().toSeconds();
               System.out.println("TOTAL DURATION OF SONG: " + totalTimeOfMusic);

               /* // To get the metaData out of the song.
               for (Map.Entry<String, Object> entry : song.getMetadata().entrySet()){
                   System.out.println(entry.getKey() + ": " + entry.getValue());
               }
               */

               songName = (String) song.getMetadata().get("title");
               myPlayerNotification(songName);
               notifications.show();

               songNameList.put(j++,songName);
               mediaPlayer.play();
           }
       });

        playBtatus = MediaPlayer.Status.PLAYING;

        System.out.println("MEDIAPLAYER STATUS : " + playBtatus );

    }

    private void playthisSong(Media newsong){
        mediaPlayer = new MediaPlayer(newsong);
        System.out.println("FILE INPUT SUCESSFULL.!!" + songpath);
        mediaPlayer.play();
        playBtatus = MediaPlayer.Status.PLAYING;
        System.out.println("MEDIAPLAYER STATUS : " + playBtatus );
    }


    private void myPlayerNotification(String songNoti){

        songNameatLabel = songNoti;
        setsongNameatLabel(songNameatLabel);

        notifications = Notifications.create()
                .title("NOW PLAYING")
                .text("SONG : " + songNoti)
                .graphic(new ImageView(MusicRecIMG))
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .darkStyle()
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("NOTIFICATION WORKING PERFECTLY");
                    }
                });
    }




} //--@End of Controller------------------------------------

// TODO MAKE A PLAYBACK SEEKER SLIDER
// TODO MAKE CURRENT DURATION AND TOTAL DURATION LABEL
// TODO album = (String) music.getMetadata().get("album");
// TODO Icon of the software



