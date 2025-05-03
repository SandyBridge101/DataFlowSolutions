

package ui;

import exceptions.InvalidInputException;
import exceptions.MatchNotFoundException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CustomDataObject;
import service.DataManager;
import service.FileHandler;
import service.RegexProcessor;
import service.TextAnalyzer;

import java.io.IOException;
import java.util.Map;
import java.io.File;

public class MainApp extends Application {
    private DataManager dataManager=new DataManager();
    private RegexProcessor processor=new RegexProcessor();
    private FileHandler fileHandler=new FileHandler();
    private ObservableList<CustomDataObject> dataObjects= FXCollections.observableArrayList();
    private ListView<CustomDataObject> dataObjectView=new ListView<>(dataObjects);
    private TextArea inputArea = new TextArea();
    private TextArea replaceTextArea = new TextArea();
    private TextField patternField = new TextField();
    private Button matchButton = new Button("Find Matches");
    private Button refreshButton = new Button("Refresh");
    private Button replaceAllButton = new Button("Replace All");
    private Button replaceAtButton = new Button("Replace Selected");
    private Button deleteAllButton = new Button("Delete All");
    private Button deleteAtButton = new Button("Delete Selected");
    private Button analyzeButton = new Button("Analyze Text");
    private Label selectedFileLabel;
    private Button openButton = new Button("Select File");
    private Button exportButton = new Button("Export Text");

    static public String fileName;
    static public String filePath;
    static public String extension;
    static public String fileParent;

    TextArea outputTextArea = new TextArea();
    @Override
    public void start(Stage primaryStage) {


        outputTextArea.setEditable(false);  // Make it read-only
        outputTextArea.setWrapText(true);

        inputArea.setPromptText("Your Input Text Goes Here");
        replaceTextArea.setPromptText("Enter the Replacement Text");
        patternField.setPromptText("Enter the Regular Expression pattern or a simple text");
        outputTextArea.setPromptText("Text Analysis and Results");



        selectedFileLabel = new Label("No file selected");






        HBox inputBox = new HBox(
                10.0,
                new VBox(
                        10,
                        new Label("Input Text"),
                        inputArea,
                        new HBox(10,
                                openButton,
                                exportButton
                                ),
                        new Label("Regex Pattern"),
                        patternField,
                        new Label("Replacement Text"),
                        replaceTextArea,
                        matchButton,
                        refreshButton
                )
        );

        HBox outputBox = new HBox(
                20,
                new VBox(
                        5,
                        dataObjectView
                ),
                new VBox(
                        5,
                        outputTextArea,
                        new HBox(5,replaceAllButton,replaceAtButton),
                        new HBox(5,deleteAllButton,deleteAtButton),
                        analyzeButton
                )
                );



        VBox root = new VBox(10,
                inputBox,
                outputBox
                //,new Label("Match Results"), resultArea
        );



        matchButton.setOnAction(e -> {
            try {
                String input = inputArea.getText();
                String pattern = patternField.getText();
                processor.findMatches(pattern,input);
                refreshUI(input,false);
            }catch (MatchNotFoundException e1) {
                showAlert("Error","Matching Issues",e1.getMessage());
            }
            //resultArea.setText(String.join("\n", processor.findMatches(pattern, input)));
        });

        replaceAllButton.setOnAction(e -> {
            try {
                String pattern= patternField.getText();
                String input = inputArea.getText();
                String replacement = replaceTextArea.getText();
                String newInput=processor.replaceMatches(pattern,replacement,input);
                refreshUI(newInput,false);
            }catch (InvalidInputException e1) {
                showAlert("Error","Input Exception",e1.getMessage());
            }
        });

        replaceAtButton.setOnAction(e -> {
            try {
                CustomDataObject selected=dataObjectView.getSelectionModel().getSelectedItem();
                String input=inputArea.getText();
                String replacement=replaceTextArea.getText();
                String newInput=processor.replaceAt(input,selected.getStart(),selected.getEnd(),selected.getId(),replacement);
                refreshUI(newInput,false);
            }catch (InvalidInputException e1) {
                showAlert("Error","Input Exception",e1.getMessage());
            }
        });
        deleteAllButton.setOnAction(e -> {
            try {
                String pattern= patternField.getText();
                String input = inputArea.getText();
                String newInput=processor.deleteMatches(pattern,input);
                refreshUI(newInput,false);
            }catch (InvalidInputException e1) {
                showAlert("Error","Input Exception",e1.getMessage());
            }
        });
        deleteAtButton.setOnAction(e -> {
            try {
                CustomDataObject selected=dataObjectView.getSelectionModel().getSelectedItem();
                String input = inputArea.getText();
                String newInput=processor.deleteAt(input,selected.getStart(),selected.getEnd(),selected.getId());
                refreshUI(newInput,false);
            }catch (InvalidInputException e1) {
                showAlert("Error","Input Exception",e1.getMessage());
            }
        });
        analyzeButton.setOnAction(e -> {
           String summary = TextAnalyzer.generateSummary(inputArea.getText(),inputArea.getText().length()/4);
            Map<String, Long> wordFrequency= TextAnalyzer.analyzeWordFrequency(inputArea.getText());
            outputTextArea.appendText("Summary: \n");
            outputTextArea.appendText(summary+"\n \n");
            outputTextArea.appendText("Word Frequency: \n");
            for(String word: wordFrequency.keySet()){
                outputTextArea.appendText(word+" "+wordFrequency.get(word)+"\n");
            }


        });
        refreshButton.setOnAction(e -> {
           refreshUI(inputArea.getText(),true);
        });
        openButton.setOnAction(e -> handleFileSelection(primaryStage));
        exportButton.setOnAction(e -> {handleFileExport(primaryStage);});


        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.setTitle("Text Processor Tool");
        primaryStage.show();
    }



    private void refreshUI(String inputAreaText, boolean clear) {
        if(clear){
            dataManager.clearAllEntries();
        }
        dataObjects.setAll(dataManager.getDataObjects());
        inputArea.setText(inputAreaText);

    }


    private void handleFileSelection(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter(
                "Text Files", "*.txt");
        FileChooser.ExtensionFilter docxFilter = new FileChooser.ExtensionFilter(
                "All Files", "*.docx");
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter(
                "All Files", "*.pdf");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter(
                "All Files", "*.csv");
        FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter(
                "All Files", "*.*");

        fileChooser.getExtensionFilters().addAll(txtFilter,docxFilter,csvFilter,pdfFilter, allFilter);

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            selectedFileLabel.setText("Selected File: " + selectedFile.getAbsolutePath());
            System.out.println("Selected File: " + selectedFile.getAbsolutePath()+" "+selectedFile.getParent());
            fileName = selectedFile.getName();
            filePath = selectedFile.getAbsolutePath();
            fileParent=selectedFile.getParent();


            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                extension = fileName.substring(dotIndex + 1);
            }

            try {
                inputArea.setText(fileHandler.readFile(filePath));
            }catch (IOException e){
                showAlert("Error","File Error","There was an error in parsing the file");
            }
        }
    }

    private void handleFileExport(Stage stage) {
        String newFileName="new_"+fileName;
        System.out.println(filePath);
        try {
            fileHandler.writeFile(fileParent+"\\"+newFileName,inputArea.getText());
        } catch (IOException e) {
            showAlert("Error","File Error","There was an error in parsing the file");
        }



    }


    private void showAlert(String title,String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
