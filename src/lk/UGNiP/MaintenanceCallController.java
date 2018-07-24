package lk.UGNiP;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class MaintenanceCallController implements Initializable{

    @FXML private CheckBox offOpen;
    @FXML private CheckBox lightReplace;
    @FXML private CheckBox carpetClean;
    @FXML private Button action;
    @FXML private Button log;
    @FXML private Button cancel;

    //Goes to the office move scene when clicked on the Back button
    public void officeMove(ActionEvent ae) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../../FXML/Office Move.fxml"));
        Scene maintenanceCallScene = new Scene(root);

        //Getting current stage information
        Stage window = (Stage) ((Node) ae.getSource()).getScene().getWindow();
        window.setScene(maintenanceCallScene);
        window.setTitle("Office Move");
        window.show();
    }

    //Office to be opened checkbox
    public void offOpenBox(ActionEvent ae){
        if(this.offOpen.isSelected()){
            this.action.setDisable(false);
            this.log.setDisable(false);
        }
    }

    //Light replacement checkbox
    public void lightReplaceBox(ActionEvent ae){
        if(this.lightReplace.isSelected()){
            this.action.setDisable(false);
            this.log.setDisable(false);
        }
    }

    //Carpet to be cleaned checkbox
    public void carpetCleanBox(ActionEvent ae){
        if(this.carpetClean.isSelected()){
            this.action.setDisable(false);
            this.log.setDisable(false);
        }
    }

    //When "action call" button is pressed
    public void actionMethod(ActionEvent ae){
        this.cancel.setDisable(false);
        this.log.setDisable(true);
        this.action.setDisable(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText("Maintenance call will be actioned!");
        alert.show();
    }

    //When "log call" button is pressed
    public void logMethod(ActionEvent ae){
        this.cancel.setDisable(false);
        this.log.setDisable(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText("Maintenance call is logged!");
        alert.show();
    }

    //When "cancel call" button is pressed
    public void cancelMethod(ActionEvent ae){
        this.action.setDisable(true);
        this.log.setDisable(true);
        this.cancel.setDisable(true);
        this.offOpen.setSelected(false);
        this.carpetClean.setSelected(false);
        this.lightReplace.setSelected(false);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText("Maintenance call was cancelled!");
        alert.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.action.setDisable(true);
        this.log.setDisable(true);
        this.cancel.setDisable(true);
    }
}
