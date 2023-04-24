import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;



public class Main extends Application{
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage){
        Button buttonSave = new Button("Save file");
        TextArea textArea = new TextArea();
        TextField fileName = new TextField();
        fileName.setPromptText("Write file's name, please");
        fileName.setId("fileName");
        File folder = new File("TextsFolder");

        ObservableList<String> observableList = FXCollections.observableArrayList();
        ListView<String> listView = new ListView<>(observableList);
        buttonSave.setOnAction(e -> {
            String textFromArea = textArea.getText();
            String textFromFileName = fileName.getText();
            if(textFromFileName.isEmpty()){
                fileName.setStyle("-fx-border-color: red;");
            }else{
                if(!folder.exists()){
                    folder.mkdirs();
                }
                fileOperation(textFromFileName, textFromArea, String.valueOf(folder));
                listView.getItems().add(textFromFileName + ".txt");
            }
        });

        File[] files = folder.listFiles();
        for(File x : files){
            if(x.isFile()){
                observableList.add(x.getName());
            }
        }

        MultipleSelectionModel<String> multipleSelectionModel = listView.getSelectionModel();
        multipleSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                textArea.clear();
                fileName.clear();
                String line;
                StringBuilder text = new StringBuilder();
                try(BufferedReader bufferedReader = new BufferedReader(new FileReader(folder + "/" + t1))) {
                    line = bufferedReader.readLine();
                    while(line != null){
                        text.append(line);
                        text.append("\n");
                        line = bufferedReader.readLine();
                    }
                    textArea.insertText(0, text.toString());
                    fileName.insertText(0, getFileNameWithoutExtension(t1));
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        HBox belowTextArea = new HBox(fileName, buttonSave);
        VBox fields = new VBox(belowTextArea, textArea);
        HBox root = new HBox(listView, fields);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setWidth(750);
        stage.setHeight(500);
        stage.show();
    }
    public void fileOperation(String textFromFileName, String textFromArea, String folder) {
        try {
            File file = new File(folder + "/" + textFromFileName + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(textFromArea);
            bufferedWriter.flush();
            bufferedWriter.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    public String getFileNameWithoutExtension(String file){
        int dotIndex = file.indexOf(".");
        return (dotIndex == -1) ? file : file.substring(0, dotIndex);
    }
}