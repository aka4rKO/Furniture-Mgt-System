package lk.UGNiP;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class RequestMoveController implements Initializable{

    @FXML private ListView selectedToMove;
    @FXML private ComboBox destDept;
    @FXML private ComboBox destFloor;
    @FXML private ComboBox destRoom;
    @FXML private Button confirmMoveBtn;
    @FXML private Button deleteMoveBtn;
    @FXML private Button changeLocationBtn;
    private String deptDb;
    private int floorDb;
    private String roomDb;

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

    //Selecting department to move furniture
    public void deptSelected(ActionEvent ae){
        if(!(this.destDept.getSelectionModel().isEmpty())){
            if(this.destDept.getValue().equals("Science")){
                this.destFloor.setItems(FXCollections.observableArrayList(1,2));
            }else if(this.destDept.getValue().equals("Computing")){
                this.destFloor.setItems(FXCollections.observableArrayList(1,2));
            }
            this.destFloor.setDisable(false);

            //Setting the collection to be accessed by the database
            deptDb = (String) this.destDept.getValue();
        }
    }

    //Selecting floor to move furniture
    public void floorSelected(ActionEvent ae){
        if(!(this.destFloor.getSelectionModel().isEmpty())){
            this.destRoom.setDisable(false);

            //Storing the floor selected by user to query the database
            floorDb = (int) this.destFloor.getValue();
            if(this.destDept.getValue() == "Science"){
                if(this.destFloor.getValue().equals(1)){
                    this.destRoom.setItems(FXCollections.observableArrayList("129I","129J"));
                } else if (this.destFloor.getValue().equals(2)){
                    this.destRoom.setItems(FXCollections.observableArrayList("129K","129L"));
                }
            } else if(this.destDept.getValue() == "Computing"){
                if(this.destFloor.getValue().equals(1)){
                    this.destRoom.setItems(FXCollections.observableArrayList("129E","129F"));
                } else if (this.destFloor.getValue().equals(2)){
                    this.destRoom.setItems(FXCollections.observableArrayList("129G","129H"));
                }

            }
        }
    }

    //Selecting room to move furniture
    public void roomSelected(ActionEvent ae){
        if(!(this.destRoom.getSelectionModel().isEmpty())) {

            //Storing the room selected by user to query the database
            roomDb = (String) this.destRoom.getValue();
            this.confirmMoveBtn.setDisable(false);
            System.out.println(deptDb + " " + roomDb + " " + floorDb);
        }
    }

    //When confirm move button is pressed
    public void confirmMove(ActionEvent ae){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText("Move request was confirmed!");
        alert.show();
        this.destDept.setDisable(true);
        this.destRoom.setDisable(true);
        this.destFloor.setDisable(true);
        this.deleteMoveBtn.setDisable(false);
        this.changeLocationBtn.setDisable(false);
        this.confirmMoveBtn.setDisable(true);
    }

    //When delete move button is pressed
    public void deleteMove(ActionEvent ae){
        this.selectedToMove.setItems(null);
        this.changeLocationBtn.setDisable(true);
        this.deleteMoveBtn.setDisable(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText("Move request was deleted!");
        alert.show();
    }

    //When change location button is pressed
    public void changeLocation(ActionEvent ae){
        this.destDept.setDisable(false);
        this.changeLocationBtn.setDisable(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.selectedToMove.setItems(OfficeMoveController.selectedFurniture);
        this.destDept.setItems(FXCollections.observableArrayList("Science","Computing"));
        this.destFloor.setDisable(true);
        this.destRoom.setDisable(true);
        this.confirmMoveBtn.setDisable(true);
        this.deleteMoveBtn.setDisable(true);
        this.changeLocationBtn.setDisable(true);
    }
}
