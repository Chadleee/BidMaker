import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BidMakerMain extends Application{

    BorderPane layout = new BorderPane();
    public TableView<MakeShift> shiftTable;
    GridPane scheduleGrid = new GridPane();
    TabPane body = new TabPane();
    Tab tabA = new Tab();
    Tab tabB = new Tab();
    public TextField clockingIn, clockingOut, monies, fileNameA, fileNameB, bidTolerance, numberOfBids, threeDay;
    public ChoiceBox<String> casinoBars = new ChoiceBox<>();
    public ChoiceBox<String> weekdays = new ChoiceBox<>();
    public int shiftCounter = 0;
    public MakeShift shift[] = new MakeShift[500];
    public BidGenerator bids = new BidGenerator();
    public OpenClose fileManager = new OpenClose();
    TableColumn<MakeShift, Integer> tipsCol;
    TableColumn<MakeShift, String> grading;
    TableColumn<MakeShift, Double> hours;
    TableColumn<MakeShift, String> day;
    TableColumn<MakeShift, String> bar;
    TableColumn<MakeShift, Integer> inTime;
    TableColumn<MakeShift, Integer> outTime;
    TableColumn<MakeShift, String> available;
    TableColumn<MakeShift, String> shiftType;

    public static void main(String[] args){
        launch(args);

    }
    //UI Formatting
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Bid Maker");
        BorderPane tableLayout = new BorderPane();
        BorderPane bidLayout = new BorderPane();
        layout.setCenter(body);
        ScrollPane bidScroll = new ScrollPane();

        //Tab set up

        tabA.setText("               Shifts               ");
        tabB.setText("                Bids                ");
        Tab tabC = new Tab();
        tabC.setText("               Stats                ");
        body.getTabs().addAll(tabA, tabB);
        tabA.setContent(tableLayout);
        tabB.setContent(bidLayout);


        //Column Sizes
        int a = 150;
        int b = 75;
        int c = 75;
        int d = 50;

        //Day column
        day = new TableColumn<>("Day");
        day.setPrefWidth(a);
        day.setResizable(false);
        day.setCellValueFactory(new PropertyValueFactory<>("day"));

        //Bar column
        bar = new TableColumn<>("Bar");
        bar.setPrefWidth(a);
        bar.setResizable(false);
        bar.setCellValueFactory(new PropertyValueFactory<>("bar"));

        //Shift Start Time column
        inTime = new TableColumn<>("In Time");
        inTime.setPrefWidth(b);
        inTime.setResizable(false);
        inTime.setCellValueFactory(new PropertyValueFactory<>("inTime"));


        //Shift End Time column
        outTime = new TableColumn<>("Out Time");
        outTime.setPrefWidth(b);
        outTime.setResizable(false);
        outTime.setCellValueFactory(new PropertyValueFactory<>("outTime"));


        //Number of Hours column
        hours = new TableColumn<>("Hrs");
        hours.setPrefWidth(c);
        hours.setResizable(false);
        hours.setCellValueFactory(new PropertyValueFactory<>("hours"));

        //Tips column
        tipsCol = new TableColumn<>("Tips");
        tipsCol.setPrefWidth(b);
        tipsCol.setResizable(false);
        tipsCol.setCellValueFactory(new PropertyValueFactory<>("tips"));

        //AM/PM column
        shiftType = new TableColumn<>("AM/PM");
        shiftType.setPrefWidth(b);
        shiftType.setResizable(false);
        shiftType.setCellValueFactory(new PropertyValueFactory<>("shiftType"));

        //Grade column
        grading = new TableColumn<>("Grade");
        grading.setPrefWidth(c);
        grading.setResizable(false);
        grading.setCellValueFactory(new PropertyValueFactory<>("shiftGrade"));
        grading.setVisible(false);

        //Availability column
        available = new TableColumn<>("Availability");
        available.setPrefWidth(b);
        available.setResizable(false);
        available.setCellValueFactory(new PropertyValueFactory<>("Available"));
        available.setVisible(true);

        //ChoiceBox Size and Items
        weekdays.setPrefWidth(a);
        casinoBars.setPrefWidth(a);
        weekdays.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        casinoBars.getItems().addAll("Blackjack1", "Blackjack2", "Lucky7", "CasinoKing1", "CasinoKing2", "CircleKing", "CircleQueen");
        casinoBars.getItems().addAll("Breaker1", "Breaker2", "ServiceWell", "Depot9", "Cabaret");


        //Clock In
        clockingIn = new TextField();
        clockingIn.setPromptText("In Time");
        clockingIn.setPrefWidth(b);

        //Clock Out
        clockingOut = new TextField();
        clockingOut.setPromptText("Out Time");
        clockingOut.setPrefWidth(b);

        //Tips
        monies = new TextField();
        monies.setPromptText("Tips");
        monies.setPrefWidth(b);

        //File Name TabA
        fileNameA = new TextField();
        fileNameA.setPromptText("Filename");
        fileNameA.setPrefWidth(200);

        //File Name TabB
        fileNameB = new TextField();
        fileNameB.setPromptText("Filename");
        fileNameB.setPrefWidth(200);

        //Sets Bid Tolerance
        bidTolerance = new TextField();
        bidTolerance.setPromptText("Tolerance");
        bidTolerance.setPrefWidth(100);

        //Sets Number of Bids
        numberOfBids = new TextField();
        numberOfBids.setPromptText("Number of Bids");
        numberOfBids.setPrefWidth(100);

        //Sets Three Days Off
        threeDay = new TextField();
        threeDay.setPromptText("Three Day Weekends");
        threeDay.setPrefWidth(100);

        //TabA Buttons
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addButtonClicked());
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteButtonClicked());
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> refreshButtonClicked());
        Button picButtonA = new Button("Snapshot");
        picButtonA.setOnAction(e -> saveAsPng());

        shiftTable = new TableView<>();
        tableLayout.setCenter(shiftTable);
        shiftTable.getColumns().addAll(day, bar, tipsCol, inTime, outTime, hours, shiftType, available, grading);
        shiftTable.setEditable(true);

        HBox hboxTabA = new HBox();
        hboxTabA.setPadding(new Insets(10, 10, 10, 0));
        hboxTabA.setSpacing(0);
        hboxTabA.setAlignment(Pos.BOTTOM_LEFT);
        hboxTabA.getChildren().addAll(weekdays, casinoBars, monies, clockingIn, clockingOut,
                addButton, deleteButton, fileNameA, picButtonA);
        tableLayout.setBottom(hboxTabA);


        //TabB Buttons
        Button picButtonB = new Button("Snapshot");
        picButtonB.setOnAction(e -> saveAsPng());
        Button generateButton = new Button("Generate Bids");
        generateButton.setOnAction(e -> generateButtonClicked());

        //Text bidNumber = new Text("Bid\nNumber");
        Text monday = new Text("Monday");
        Text tuesday = new Text("Tuesday");
        Text wednesday = new Text("Wednesday");
        Text thursday = new Text("Thursday");
        Text friday = new Text("Friday");
        Text saturday = new Text("Saturday");
        Text sunday = new Text("Sunday");
        Text otherInfo = new Text("Other Info");

        scheduleGrid.setPadding(new Insets(20, 20, 20, 20));
        scheduleGrid.setHgap(75);
        scheduleGrid.setVgap(50);


        GridPane columnLabels = new GridPane();
        columnLabels.setPadding(new Insets(20, 20, 20, 20));
        columnLabels.setHgap(90);
        columnLabels.setVgap(50);
        columnLabels.add(monday, 0, 0);
        columnLabels.add(tuesday, 1, 0);
        columnLabels.add(wednesday, 2, 0);
        columnLabels.add(thursday, 3, 0);
        columnLabels.add(friday, 4, 0);
        columnLabels.add(saturday, 5, 0);
        columnLabels.add(sunday, 6, 0);
        columnLabels.add(otherInfo, 7, 0);

        HBox hboxTabB = new HBox();
        hboxTabB.setPadding(new Insets(10, 10, 10, d));
        hboxTabB.setSpacing(0);
        hboxTabB.setAlignment(Pos.BOTTOM_LEFT);
        hboxTabB.getChildren().addAll(numberOfBids, bidTolerance, threeDay, generateButton, fileNameB, picButtonB);
        bidLayout.setCenter(bidScroll);
        bidLayout.setTop(columnLabels);
        bidScroll.setContent(scheduleGrid);
        bidLayout.setBottom(hboxTabB);

        tipsCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tipsCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<MakeShift, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<MakeShift, Integer> t) {
                        shift[shiftCounter] = new MakeShift(shiftCounter);
                        shift[shiftCounter] = shiftTable.getSelectionModel().getSelectedItem();
                        shift[shiftCounter].setTips(t.getNewValue());
                        shiftTable.getItems().add(shift[shiftCounter]);
                        shiftCounter++;
                        deleteButtonClicked();
                        refreshButtonClicked();
                    }
                });
        inTime.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        inTime.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<MakeShift, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<MakeShift, Integer> t) {
                        shift[shiftCounter] = new MakeShift(shiftCounter);
                        shift[shiftCounter] = shiftTable.getSelectionModel().getSelectedItem();
                        shift[shiftCounter].setInTime(t.getNewValue());
                        shiftTable.getItems().add(shift[shiftCounter]);
                        shiftCounter++;
                        deleteButtonClicked();
                        refreshButtonClicked();
                    }
                });
        outTime.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        outTime.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<MakeShift, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<MakeShift, Integer> t) {
                        shift[shiftCounter] = new MakeShift(shiftCounter);
                        shift[shiftCounter] = shiftTable.getSelectionModel().getSelectedItem();
                        shift[shiftCounter].setOutTime(t.getNewValue());
                        shiftTable.getItems().add(shift[shiftCounter]);
                        shiftCounter++;
                        deleteButtonClicked();
                        refreshButtonClicked();
                    }
                });
        available.setCellFactory(TextFieldTableCell.forTableColumn());
        available.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<MakeShift, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<MakeShift, String> t) {
                        shift[shiftCounter] = new MakeShift(shiftCounter);
                        shift[shiftCounter] = shiftTable.getSelectionModel().getSelectedItem();
                        shift[shiftCounter].setAvailable(t.getNewValue());
                        shiftTable.getItems().add(shift[shiftCounter]);
                        shiftCounter++;
                        deleteButtonClicked();
                        refreshButtonClicked();
                    }
                });

        Scene scene = new Scene(layout, 1080, 720);
        primaryStage.setScene(scene);
        primaryStage.show();

        try {
            importInfo();
            refreshButtonClicked();
            bids.fillArray();
        }
        catch (Exception e) {
            System.out.println("Error");
        }
    }
    //Edit Cell Values
    public void editCell(){
        //shiftTable.getSelectionModel().getSelectedItem().setAvailable(shiftTable.getSelectionModel().getSelectedItem().available.getNewValue());
        shift[shiftCounter] = new MakeShift(shiftCounter);
        shiftCounter++;
        shift[shiftCounter] = shiftTable.getSelectionModel().getSelectedItem();
        System.out.printf("%s %s %s %s %s %s %s %n",
                shift[shiftCounter].getDay(),
                shift[shiftCounter].getBar(),
                shift[shiftCounter].getInTime(),
                shift[shiftCounter].getOutTime(),
                shift[shiftCounter].getHours(),
                shift[shiftCounter].getTips(),
                shift[shiftCounter].getAvailable());

    }

    //Add Button Clicked
    public void addButtonClicked() {
        shift[shiftCounter] = new MakeShift(shiftCounter);
        shift[shiftCounter].setDay(weekdays.getValue());
        shift[shiftCounter].setBar(casinoBars.getValue());
        shift[shiftCounter].setInTime(Integer.parseInt(clockingIn.getText()));
        shift[shiftCounter].setOutTime(Integer.parseInt(clockingOut.getText()));
        shift[shiftCounter].setShiftType();
        shift[shiftCounter].setTips(Integer.parseInt(monies.getText()));
        shiftTable.getItems().add(shift[shiftCounter]);
        fileManager.openWriteFile();
        fileManager.shiftWriter.format("%s %s %s %s %s %s %s %s %n",
                shift[shiftCounter].getDay(),
                shift[shiftCounter].getBar(),
                shift[shiftCounter].getInTime(),
                shift[shiftCounter].getOutTime(),
                shift[shiftCounter].getHours(),
                shift[shiftCounter].getShiftType(),
                shift[shiftCounter].getTips(),
                shift[shiftCounter].getAvailable());
        fileManager.closeWriteFile();
        shiftCounter++;
        clockingIn.clear();
        clockingOut.clear();
        monies.clear();
        refreshButtonClicked();
    }

    //Refresh Button Clicked
    public void refreshButtonClicked(){
            File file = new File("shifts.txt");
            file.delete();
            fileManager.openWriteFile();
        if (tipsCol != null) {
            shiftTable.getSortOrder().removeAll(day, bar, tipsCol, inTime, outTime, hours, shiftType, available, grading);
            shiftTable.getSortOrder().add(tipsCol);
            tipsCol.setSortType(TableColumn.SortType.DESCENDING);
            tipsCol.setSortable(true);
        }
            for (int i = 0; shiftCounter > i; i++) {
                shift[shiftCounter] = new MakeShift(shiftCounter);
                shiftTable.requestFocus();
                shiftTable.getSelectionModel().select(i);
                shiftTable.getFocusModel().focus(i);
                shift[shiftCounter] = shiftTable.getSelectionModel().getSelectedItem();
                fileManager.shiftWriter.format("%s %s %s %s %s %s %s %s %n",
                        shift[shiftCounter].getDay(),
                        shift[shiftCounter].getBar(),
                        shift[shiftCounter].getInTime(),
                        shift[shiftCounter].getOutTime(),
                        shift[shiftCounter].getHours(),
                        shift[shiftCounter].getShiftType(),
                        shift[shiftCounter].getTips(),
                        shift[shiftCounter].getAvailable());
            }
            fileManager.closeWriteFile();
            for (int j = 0; shiftCounter > j; j++) {
                ObservableList<MakeShift> shiftSelected, allShifts;
                allShifts = shiftTable.getItems();
                shiftSelected = shiftTable.getSelectionModel().getSelectedItems();
                shiftSelected.forEach(allShifts::remove);
            }
        shiftCounter = 0;
        importInfo();
    }

    //Delete Button Clicked
    public void deleteButtonClicked() {
        ObservableList<MakeShift> shiftSelected, allShifts;
        allShifts = shiftTable.getItems();
        shiftSelected = shiftTable.getSelectionModel().getSelectedItems();
        shiftSelected.forEach(allShifts::remove);
        shiftCounter--;
        refreshButtonClicked();
    }

    //Randomizes Bids Into File
    public void randomizeBids(){
        body.getSelectionModel().select(tabA);
        File file = new File("bids.txt");
        file.delete();
        fileManager.openWriteBidsFile();
        if (grading != null) {
            shiftTable.getSortOrder().removeAll(day, bar, tipsCol, inTime, outTime, hours, shiftType, available, grading);
            shiftTable.getSortOrder().add(grading);
            tipsCol.setSortType(TableColumn.SortType.DESCENDING);
            tipsCol.setSortable(true);
        }
        for (int i = 0; shiftCounter > i; i++) {
            shift[shiftCounter] = new MakeShift(shiftCounter);
            shiftTable.requestFocus();
            shiftTable.getSelectionModel().select(i);
            shiftTable.getFocusModel().focus(i);
            shift[shiftCounter] = shiftTable.getSelectionModel().getSelectedItem();
            fileManager.shiftWriter.format("%s %s %s %s %s %s %s %s %n",
                    shift[shiftCounter].getDay(),
                    shift[shiftCounter].getBar(),
                    shift[shiftCounter].getInTime(),
                    shift[shiftCounter].getOutTime(),
                    shift[shiftCounter].getHours(),
                    shift[shiftCounter].getShiftType(),
                    shift[shiftCounter].getTips(),
                    shift[shiftCounter].getAvailable());
        }
        fileManager.closeWriteFile();
        for (int j = 0; shiftCounter > j; j++) {
            ObservableList<MakeShift> shiftSelected, allShifts;
            allShifts = shiftTable.getItems();
            shiftSelected = shiftTable.getSelectionModel().getSelectedItems();
            shiftSelected.forEach(allShifts::remove);
        }
        shiftCounter = 0;
        fileManager.openReadBidsFile();
        String dow, theBar, startTime, endTime, totalHours, timeOfDay, expectedTips, openAvailable;
        try {
            while (fileManager.shiftReader.hasNextLine()) {

                shift[shiftCounter] = new MakeShift(shiftCounter);
                dow = fileManager.shiftReader.next();
                theBar = fileManager.shiftReader.next();
                startTime = fileManager.shiftReader.next();
                endTime = fileManager.shiftReader.next();
                totalHours = fileManager.shiftReader.next();
                timeOfDay = fileManager.shiftReader.next();
                expectedTips = fileManager.shiftReader.next();
                openAvailable = fileManager.shiftReader.next();
                fileManager.shiftReader.nextLine();

                shift[shiftCounter].setDay(dow);
                shift[shiftCounter].setBar(theBar);
                shift[shiftCounter].setInTime(Integer.parseInt(startTime));
                shift[shiftCounter].setOutTime(Integer.parseInt(endTime));
                shift[shiftCounter].setHours(Double.parseDouble(totalHours));
                shift[shiftCounter].setTips(Integer.parseInt(expectedTips));
                shift[shiftCounter].setShiftType();
                shift[shiftCounter].setAvailable(openAvailable);
                shiftTable.getItems().addAll(shift[shiftCounter]);
                shiftCounter++;
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
        fileManager.closeReadFile();
        bids.setTipsAverage();
        body.getSelectionModel().select(tabB);

    }

    //Reads Info from file
    public void importInfo() {
        fileManager.openReadFile();
        String dow, theBar, startTime, endTime, totalHours, expectedTips, openAvailable;
        try {
            while (fileManager.shiftReader.hasNextLine()) {
                shift[shiftCounter] = new MakeShift(shiftCounter);
                dow = fileManager.shiftReader.next();
                theBar = fileManager.shiftReader.next();
                startTime = fileManager.shiftReader.next();
                endTime = fileManager.shiftReader.next();
                totalHours = fileManager.shiftReader.next();
                fileManager.shiftReader.next();
                expectedTips = fileManager.shiftReader.next();
                openAvailable = fileManager.shiftReader.next();
                fileManager.shiftReader.nextLine();

                shift[shiftCounter].setDay(dow);
                shift[shiftCounter].setBar(theBar);
                shift[shiftCounter].setInTime(Integer.parseInt(startTime));
                shift[shiftCounter].setOutTime(Integer.parseInt(endTime));
                shift[shiftCounter].setHours(Double.parseDouble(totalHours));
                shift[shiftCounter].setShiftType();
                shift[shiftCounter].setTips(Integer.parseInt(expectedTips));
                shift[shiftCounter].setAvailable(openAvailable);
                shiftTable.getItems().addAll(shift[shiftCounter]);
                shiftCounter++;
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
        fileManager.closeReadFile();
        bids.setTipsAverage();
    }

    public void saveAsPng() {

        String fNameA = fileNameA.getText();
        String fNameB = fileNameB.getText();

        WritableImage image = layout.snapshot(new SnapshotParameters(), null);

        // TODO: probably use a file chooser here
        File fileA = new File(fNameA+".png");
        File fileB = new File(fNameB+".png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", fileA);
        } catch (IOException e) {
            // TODO: handle exception here
        }
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", fileB);
        } catch (IOException e) {
            // TODO: handle exception here
        }
    }

    public void generateButtonClicked(){
        randomizeBids();
        bids.setCheck(true);
        scheduleGrid.getChildren().clear();
        int tipTolerance = (Integer.parseInt(bidTolerance.getText()));
        int numberOfFullTimeBids = (Integer.parseInt(numberOfBids.getText()));
        int numberOfThreeDayOffBids = (Integer.parseInt(threeDay.getText()));
        bids.setTolerance(tipTolerance);
        bids.fillArray();
        bids.generateBidSchedule(numberOfFullTimeBids, numberOfThreeDayOffBids);
        if(bids.getCheck() == false){
            generateButtonClicked();
        }
        if (bids.bids<=(bids.usableBids/4)) {
            printBids();
        }
        body.getSelectionModel().select(tabA);
        refreshButtonClicked();
        body.getSelectionModel().select(tabB);
    }

   public void printBids() {
        int bidID = 0;
        int day = 0;
        bids.setArrayLength();
        double tableLength = bids.getArrayLength();

        for (int i = 0; i < tableLength; i++) {
            bidID++;
            for (int j = 0; j < 4; j++) {
                int a = Integer.parseInt(bids.bidSchedule[i][j]);

                String weekDay = (bids.shifts[a][1]);

                switch (weekDay) {
                    case "Monday":
                        day = 0;
                        break;
                    case "Tuesday":
                        day = 1;
                        break;
                    case "Wednesday":
                        day = 2;
                        break;
                    case "Thursday":
                        day = 3;
                        break;
                    case "Friday":
                        day = 4;
                        break;
                    case "Saturday":
                        day = 5;
                        break;
                    case "Sunday":
                        day = 6;
                        break;
                }

                Text label = new Text((bids.shifts[a][2]) + System.lineSeparator() +
                        (bids.shifts[a][3]) + System.lineSeparator() +
                        (bids.shifts[a][4]));
                scheduleGrid.add(label, day, bidID);
                //System.out.print(weekDay);
                Text label2 = new Text("WeeklyAvg\n$" + (bids.bidSchedule[i][4])+
                        System.lineSeparator() + (bids.bidSchedule[i][5])+" Hrs");
                scheduleGrid.add(label2, 7, bidID);
            }
        }
    }
}

