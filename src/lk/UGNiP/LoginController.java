package lk.UGNiP;

import com.mongodb.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LoginController {

    static DB db;

    //Static variable to disable "generate report" button if logged in by authorized staff
    //Only allows specific staff to generate reports
    static String collection;
    @FXML private Label lblStatus;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    public void login(ActionEvent ae) throws  Exception{

        if(isAuthenticated("Login")){
            lblStatus.setText("Login Successful!");
            collection = "Login";

            //Opens the Office Move FXML file
            Parent root = FXMLLoader.load(getClass().getResource("../../FXML/Office Move.fxml"));
            Scene successfulLoginScene = new Scene(root);

            //Getting current stage information
            Stage window = (Stage)((Node)ae.getSource()).getScene().getWindow();
            window.setTitle("Office Move");
            window.setScene(successfulLoginScene);
            window.show();
        } else if (isAuthenticated("SpecificLogin")){
            lblStatus.setText("Login Successful!");
            collection = "SpecificLogin";

            //Opens the Office Move FXML file
            Parent root = FXMLLoader.load(getClass().getResource("../../FXML/Office Move.fxml"));
            Scene successfulLoginScene = new Scene(root);

            //Getting current stage information
            Stage window = (Stage)((Node)ae.getSource()).getScene().getWindow();
            window.setTitle("Office Move");
            window.setScene(successfulLoginScene);
            window.show();
        }
        lblStatus.setText("Login failed!");
    }

    private boolean isAuthenticated(String collection) throws Exception{
        //Setting the values entered by the user to the local variables
        String un = txtUsername.getText();
        String pwd = txtPassword.getText();

        //Connecting to the database
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        db = mongoClient.getDB("UGNiP");

        //Accessing the Login collection
        DBCollection coll = db.getCollection(collection);

        //Setting two database objects with the user inputs and finding if they are valid
        BasicDBObject query = new BasicDBObject("username",un).append("password",pwd);

        DBCursor dbCursor = coll.find(query);
        return dbCursor.hasNext();
    }

}
