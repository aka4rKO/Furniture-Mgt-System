package lk.UGNiP;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mongodb.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class OfficeMoveController implements Initializable{

    static String deptDb;
    static int floorDb;
    static String roomDb;
    @FXML private ComboBox dept;
    @FXML private ComboBox floor;
    @FXML private ComboBox room;
    @FXML private Button generateReportBtn;
    @FXML private ListView<String> movableList;
    @FXML private ListView<String> selectedList;
    static ObservableList<String> selectedFurniture = FXCollections.observableArrayList();

    //Goes to the maintenance call scene when clicked on the Maintenance Call button
    public void maintenanceCall(ActionEvent ae) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../../FXML/Maintenance Call.fxml"));
        Scene maintenanceCallScene = new Scene(root);

        //Getting current stage information
        Stage window = (Stage)((Node)ae.getSource()).getScene().getWindow();
        window.setTitle("Maintenance Call");
        window.setScene(maintenanceCallScene);
        window.show();
    }

    //Goes to the request move scene when clicked on the Request Move button
    public void requestMove(ActionEvent ae) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../../FXML/Request Move.fxml"));
        Scene maintenanceCallScene = new Scene(root);

        //Getting current stage information
        Stage window = (Stage)((Node)ae.getSource()).getScene().getWindow();
        window.setTitle("Request Move");
        window.setScene(maintenanceCallScene);
        window.show();
    }

    //Goes back to the Log In scene
    public void logout(ActionEvent ae) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../../FXML/Login.fxml"));
        Scene maintenanceCallScene = new Scene(root);

        //Getting current stage information
        Stage window = (Stage)((Node)ae.getSource()).getScene().getWindow();
        window.setTitle("UGNiP Login");
        window.setScene(maintenanceCallScene);
        window.show();
    }

    //Selects the source department to move the furniture
    public void deptSelected(ActionEvent ae){

        //Setting the movable list to null when the selection is changed
        this.movableList.setItems(null);
        if(!(this.dept.getSelectionModel().isEmpty())){
            if(this.dept.getValue().equals("Science")){
                this.floor.setItems(FXCollections.observableArrayList(1,2));
            }else if(this.dept.getValue().equals("Computing")){
                this.floor.setItems(FXCollections.observableArrayList(1,2));
            }
            this.floor.setDisable(false);

            //Setting the collection to be accessed by the database
            deptDb = (String) this.dept.getValue();
        }
    }

    //Selects the source floor to move the furniture
    public void floorSelected(ActionEvent ae){

        //Setting the movable list to null when the selection is changed
        this.movableList.setItems(null);
        if(!(this.floor.getSelectionModel().isEmpty())){
            this.room.setDisable(false);

            //Storing the floor selected by user to query the database
            floorDb = (int) this.floor.getValue();
            if(this.dept.getValue() == "Science"){
                if(this.floor.getValue().equals(1)){
                    this.room.setItems(FXCollections.observableArrayList("129I","129J"));
                } else if (this.floor.getValue().equals(2)){
                    this.room.setItems(FXCollections.observableArrayList("129K","129L"));
                }
            } else if(this.dept.getValue() == "Computing"){
                if(this.floor.getValue().equals(1)){
                    this.room.setItems(FXCollections.observableArrayList("129E","129F"));
                } else if (this.floor.getValue().equals(2)){
                    this.room.setItems(FXCollections.observableArrayList("129G","129H"));
                }

            }
        }
    }

    //Selects the source room to move the furniture
    public void roomSelected(ActionEvent ae){
        if(!(this.room.getSelectionModel().isEmpty())){

            //Storing the room selected by user to query the database
            roomDb = (String) this.room.getValue();
            System.out.println(deptDb+" "+roomDb+" "+floorDb);
            DBCollection coll = LoginController.db.getCollection(deptDb);

            //Querying the database
            BasicDBObject query = new BasicDBObject("floor",floorDb).append("room",roomDb);
            DBCursor cursor = coll.find(query);
            ObservableList<String> observableFurniture = FXCollections.observableArrayList();
            BasicDBList furniture = (BasicDBList) cursor.next().get("furniture");

            //Adding all the furniture items in the selected room in an
            // observable list to display to the user
            for (int i = 0; i < furniture.size(); i++){
                BasicDBObject type = (BasicDBObject) furniture.get(i);
                observableFurniture.add((String) type.get("type"));
                System.out.println(type.get("type"));
            }

            //Displaying the list
            this.movableList.setItems(observableFurniture);
        }
    }

    //Generates FMO report on click
    public void generateReport(ActionEvent ae){
        DBCollection coll1 = LoginController.db.getCollection("Computing");
        DBCollection coll2 = LoginController.db.getCollection("Science");
        BasicDBObject obj = new BasicDBObject();

        //Cursor to find all objects in the computing dept
        DBCursor cursor1 = coll1.find(obj);

        //Cursor to find all objects in the science dept
        DBCursor cursor2 = coll2.find(obj);

        //Creating new document
        Document doc = new Document();
        try{
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("FMO report.pdf"));
            doc.open();
            Paragraph date = new Paragraph("Report date: "+new Date().toString());
            date.setAlignment(Element.ALIGN_RIGHT);
            Paragraph heading = new Paragraph("FMO Report", FontFactory.getFont
                    (FontFactory.TIMES_BOLD,15, Font.UNDERLINE));
            heading.setAlignment(Element.ALIGN_CENTER);

            Paragraph startPara = new Paragraph("Campus : Wellawatte\nBuilding : 19");

            //Adding date, heading and initial paragraph
            doc.add(date);
            doc.add(heading);
            doc.add(startPara);

            //Creating an array to display the rooms in the computing dept
            String[] compRooms = {"129E","129F","129G","129H"};
            for(int i = 0; cursor1.hasNext(); i++){
                Paragraph preTablePara = new Paragraph("\n\nRoom : "+compRooms[i]+
                        "\nSpace type : Office\nOccupying dept : Computing\nOccupant : Vacant\n\n");
                doc.add(preTablePara);

                //Creating list of furniture in each room
                BasicDBList roomFurniture = (BasicDBList) cursor1.next().get("furniture");
                PdfPTable room = new PdfPTable(4);
                room.getDefaultCell().setBorder(0);

                //Setting the headings of the columns
                PdfPCell col1 = new PdfPCell(new Paragraph("Barcode",
                        FontFactory.getFont(FontFactory.TIMES_BOLD,14, Font.BOLD)));
                PdfPCell col2 = new PdfPCell(new Paragraph("Type",
                        FontFactory.getFont(FontFactory.TIMES_BOLD,14, Font.BOLD)));
                PdfPCell col3 = new PdfPCell(new Paragraph("Date",
                        FontFactory.getFont(FontFactory.TIMES_BOLD,14, Font.BOLD)));
                PdfPCell col4 = new PdfPCell(new Paragraph("Amount",
                        FontFactory.getFont(FontFactory.TIMES_BOLD,14, Font.BOLD)));
                col1.setBorder(0);
                col2.setBorder(0);
                col3.setBorder(0);
                col4.setBorder(0);

                //Adding the column headings
                room.addCell(col1);
                room.addCell(col2);
                room.addCell(col3);
                room.addCell(col4);

                //Adding the furniture details into the table
                for(int j = 0; j < roomFurniture.size(); j++){
                    BasicDBObject fur1 = (BasicDBObject) roomFurniture.get(j);
                    room.addCell(fur1.getString("barcode"));
                    room.addCell(fur1.getString("type"));
                    room.addCell(fur1.getString("date"));
                    room.addCell(fur1.getString("amount"));
                }
                doc.add(room);
            }

            //Creating an array to display the rooms in the science dept
            String[] sciRooms = {"129I","129J","129K","129L"};
            for(int i = 0; cursor2.hasNext(); i++){
                Paragraph preTablePara = new Paragraph("\n\nRoom : "+sciRooms[i]+
                        "\nSpace type : Office\nOccupying dept : Science\nOccupant : Vacant\n\n");
                doc.add(preTablePara);

                //Creating list of furniture in each room
                BasicDBList roomFurniture = (BasicDBList) cursor2.next().get("furniture");
                PdfPTable room = new PdfPTable(4);
                room.getDefaultCell().setBorder(0);

                //Setting the headings of the columns
                PdfPCell col1 = new PdfPCell(new Paragraph("Barcode",
                        FontFactory.getFont(FontFactory.TIMES_BOLD,14, Font.BOLD)));
                PdfPCell col2 = new PdfPCell(new Paragraph("Type",
                        FontFactory.getFont(FontFactory.TIMES_BOLD,14, Font.BOLD)));
                PdfPCell col3 = new PdfPCell(new Paragraph("Date",
                        FontFactory.getFont(FontFactory.TIMES_BOLD,14, Font.BOLD)));
                PdfPCell col4 = new PdfPCell(new Paragraph("Amount",
                        FontFactory.getFont(FontFactory.TIMES_BOLD,14, Font.BOLD)));
                col1.setBorder(0);
                col2.setBorder(0);
                col3.setBorder(0);
                col4.setBorder(0);

                //Adding the column headings
                room.addCell(col1);
                room.addCell(col2);
                room.addCell(col3);
                room.addCell(col4);

                //Adding the furniture details into the table
                for(int j = 0; j < roomFurniture.size(); j++){
                    BasicDBObject fur1 = (BasicDBObject) roomFurniture.get(j);
                    room.addCell(fur1.getString("barcode"));
                    room.addCell(fur1.getString("type"));
                    room.addCell(fur1.getString("date"));
                    room.addCell(fur1.getString("amount"));
                }
                doc.add(room);
            }
            doc.close();
            writer.close();
        }catch(DocumentException e){
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        //Displaying the report
        if(Desktop.isDesktopSupported()){
            try {
                File report = new File("FMO report.pdf");
                Desktop.getDesktop().open(report);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    //Adding the furniture to be moved
    public void addItem(ActionEvent ae){
        if(!(this.room.getSelectionModel().isEmpty())){
            selectedFurniture = this.movableList.getItems();
            this.selectedList.setItems(selectedFurniture);
        }

    }

    //Removing the furniture to be moved
    public void removeItem(ActionEvent ae){
        this.selectedList.setItems(null);
    }

    //Initializing the GUI
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.dept.setItems(FXCollections.observableArrayList("Science","Computing"));
        this.floor.setDisable(true);
        this.room.setDisable(true);
        if(LoginController.collection == "Login"){
            this.generateReportBtn.setDisable(true);
        }else if(LoginController.collection == "SpecificLogin"){
            this.generateReportBtn.setDisable(false);
        }
    }
}
