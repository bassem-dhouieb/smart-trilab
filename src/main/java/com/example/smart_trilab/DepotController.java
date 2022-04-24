package com.example.smart_trilab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepotController implements Initializable {

    @FXML
    private Button add_btn;

    @FXML
    private TextField quantity_field;

    @FXML
    private Label total_quantity;


    @FXML
    private Button clear_btn;

    @FXML
    private ImageView up_arrow;

    @FXML
    private ImageView down_arrow;

    @FXML
    private TableView<Material> material_table;

    @FXML
    private TextField color_field;

    @FXML
    private ImageView delete_btn;

    @FXML
    private TableColumn<Material, ImageView> image_col;

    @FXML
    private TableColumn<Material, String> name_col;

    @FXML
    private TableColumn<Material, String> color_col;

    @FXML
    private TextField name_field;

    @FXML
    private ImageView material_image;

    @FXML
    private TableColumn<Material, String> quantity_col;

    @FXML
    private TableColumn<Material, String> reference_col;

    @FXML
    private TextField search_field;

    @FXML
    private ComboBox<String> supplier_box;

    @FXML
    private TableColumn<Material, String> supplier_col;

    @FXML
    private ComboBox<String> type_box;

    @FXML
    private TableColumn<Material, String> type_col;

    @FXML
    private Button update_btn;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Material material = null;
    int myIndex;
    int id;

    ObservableList<Material> materialList = FXCollections.observableArrayList();

    @FXML
    private void refreshTable(){

        try {
            materialList.clear();
            query = "SELECT * FROM materials;";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                ImageView image =  new ImageView(new Image(new File( resultSet.getString("image")).toURI().toString()));
                image.setFitWidth(100);
                image.setFitHeight(100);

                materialList.add(new Material(Integer.parseInt(resultSet.getString("id_materials")),
                        resultSet.getInt("quantity"),
                        resultSet.getString("supplier"),
                        resultSet.getString("name"),
                        resultSet.getString("color"),
                       image,
                        resultSet.getString("type")
                       ));
                material_table.setItems(materialList);
            }


            FilteredList<Material> filteredData = new FilteredList<>(materialList, b -> true);
            search_field.textProperty().addListener((observableValue, oldValue, newValue) ->{
                filteredData.setPredicate(materialSearchModel ->{
                    //if no search value then display all records or whatever records it current have. no changes.
                    if (newValue.isEmpty()||newValue.isBlank()||newValue==null){
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();
                    if (materialSearchModel.getName().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else  if (materialSearchModel.getReference().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if (materialSearchModel.getName().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if (materialSearchModel.getColor().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if (materialSearchModel.getSupplier().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if (materialSearchModel.getType().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else {
                        return false;
                    }

                });
            });

            SortedList<Material> sortedData = new SortedList<>(filteredData);
            //bind sorted result with table view
            sortedData.comparatorProperty().bind(material_table.comparatorProperty());

            //apply filtered and sorted data to the table view
            material_table.setItems(sortedData);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        material_table.setRowFactory( tv->{
            TableRow<Material> myRow = new TableRow<>();
            myRow.setOnMouseClicked (event ->
            {
                if (event.getClickCount() == 1 && (!myRow.isEmpty()))
                {
                    myIndex =  material_table.getSelectionModel().getSelectedIndex();
                    id = Integer.parseInt(String.valueOf(material_table.getItems().get(myIndex).getId()));
                    color_field.setText(material_table.getItems().get(myIndex).getColor());
                    name_field.setText(material_table.getItems().get(myIndex).getName());
                    type_box.setValue(material_table.getItems().get(myIndex).getType());
                    System.out.println(material_table.getItems().get(myIndex).getImage().getImage().getUrl().substring(5));
                    material_image.setImage(new Image(new File(material_table.getItems().get(myIndex).getImage().getImage().getUrl().substring(5)).toURI().toString()));
                    total_quantity.setText(String.valueOf(material_table.getItems().get(myIndex).getQuantity()));
                   supplier_box.setValue(material_table.getItems().get(myIndex).getSupplier());


                }
            });
            return myRow;
        });

    }

    
    private void loadDate() {
        connection = DbConnect.getConnect();
        refreshTable();
        reference_col.setCellValueFactory(new PropertyValueFactory<>("reference"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        color_col.setCellValueFactory(new PropertyValueFactory<>("color"));
        image_col.setCellValueFactory((new PropertyValueFactory<>("image")));
        type_col.setCellValueFactory(new PropertyValueFactory<>("type"));
        supplier_col.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        quantity_col.setCellValueFactory(new PropertyValueFactory<>("quantity"));
         }

    @FXML
    void addMaterial(ActionEvent event) throws SQLException, FileNotFoundException {
        String name = name_field.getText();
        String color = color_field.getText();
        String quantity = total_quantity.getText();
       String image = material_image.getImage().getUrl().substring(5);

        String supplier = supplier_box.getValue();
        String type = type_box.getValue();

        if (name.isEmpty()||type.isEmpty()||color.isEmpty()||quantity.isEmpty()|| supplier == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All Data");
            alert.showAndWait();
        }else{
            connection = DbConnect.getConnect();
            query = "INSERT INTO materials (quantity, supplier, name, color, image, type) VALUES (?, ?, ?, ?, ?, ?);";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(quantity));
            preparedStatement.setString(2,supplier);
            preparedStatement.setString(3,name);
            preparedStatement.setString(4,color);
            preparedStatement.setString(5,image);
            preparedStatement.setString(6,type);
          
            try {
                preparedStatement.executeUpdate();
                connection.prepareStatement("update materials set reference = CONCAT(type,LPAD(id_materials,4,0)) where id_materials>0;").executeUpdate();
                loadDate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Add material information");
                alert.setHeaderText("Add material information");
                alert.setContentText("Material information Added successfully");
                alert.showAndWait();
                clear(null);
            }catch (SQLIntegrityConstraintViolationException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Email already exist");
                alert.setContentText("Please change  email");
                alert.showAndWait();
            }




        }
    }

    @FXML
    void add_quantity(MouseEvent event) {
        try {


            total_quantity.setText(String.valueOf(Integer.parseInt(quantity_field.getText()) + Integer.parseInt(total_quantity.getText())));
           } catch (NumberFormatException e){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Enter valid number");

        alert.setHeaderText("Please enter a valid number");
        alert.setContentText("Please enter a valid number to add quantity to depot");

        alert.showAndWait();
    }
    }

    @FXML
    void browse_image(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            System.out.println("File Was Selected");
            URL url = null;
            try {
                url = file.toURI().toURL();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            material_image.setImage(new Image(url.toExternalForm()));

            System.out.println(url.toExternalForm());
            System.out.println(material_image.getImage().getUrl());



        }

    }

    @FXML
    void clear(ActionEvent event) {
        name_field.setText("");
        color_field.setText("");
        quantity_field.setText("");
        total_quantity.setText("0");
        supplier_box.getSelectionModel().clearSelection();
        type_box.getSelectionModel().clearSelection();
        material_image.setImage(new Image(new File("src/main/resources/com/example/smart_trilab/images/add-image.png").toURI().toString()));
    }

    @FXML
    void delete(MouseEvent event) {
        try
        {
            myIndex =  material_table.getSelectionModel().getSelectedIndex();
            id = Integer.parseInt(String.valueOf(material_table.getItems().get(myIndex).getId()));



            connection = DbConnect.getConnect();
            query = "delete  from materials where id_materials = ? ";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Material");

            alert.setHeaderText("Delete Material");
            alert.setContentText("Material deleted successfully!");

            alert.showAndWait();
            loadDate();
            clear(null);
        }
        catch (IndexOutOfBoundsException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Material selected yet");

            alert.setHeaderText("Please select Material");
            alert.setContentText("Please select Material you want to delete ");

            alert.showAndWait();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DepotController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void reduce_quantity(MouseEvent event) {
        try{


        if(Integer.parseInt(total_quantity.getText())-Integer.parseInt(quantity_field.getText())>0) {
            total_quantity.setText(String.valueOf(Integer.parseInt(total_quantity.getText()) - Integer.parseInt(quantity_field.getText())));

        }        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Enter valid number");

            alert.setHeaderText("Please enter a valid number");
            alert.setContentText("Please enter a valid number to add quantity to depot");

            alert.showAndWait();
        }


    }

    @FXML
    void update(ActionEvent event) throws FileNotFoundException {
        try {
            myIndex = material_table.getSelectionModel().getSelectedIndex();
            id = Integer.parseInt(String.valueOf(material_table.getItems().get(myIndex).getId()));
        } catch(IndexOutOfBoundsException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Material selected yet");

            alert.setHeaderText("Please select Material");
            alert.setContentText("Please select Material you want to update ");

            alert.showAndWait();
        }
            String name = name_field.getText();
            String color = color_field.getText();
            String quantity = total_quantity.getText();
        String image = material_image.getImage().getUrl().substring(5);
            String supplier = supplier_box.getValue();
            String type = type_box.getValue();

        if (name.isEmpty() || color.isEmpty() || supplier.isEmpty() || type.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All Data");
            alert.showAndWait();
        } else {

            try {

            connection = DbConnect.getConnect();
            query = "update materials set name= ?, color= ?, quantity= ?, image= ?, supplier= ?, type= ? where id_materials = ? ";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, color);
            preparedStatement.setString(3, quantity);
            preparedStatement.setString(4, image);
            preparedStatement.setString(5, supplier);
            preparedStatement.setString(6, type);
            preparedStatement.setInt(7, id);
            preparedStatement.executeUpdate();
            connection.prepareStatement("update materials set reference = CONCAT(type,LPAD(id_materials,4,0)) where id_materials>0;").executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Material information");

            alert.setHeaderText("Update Material information");
            alert.setContentText("Material information Updated successfully");

            alert.showAndWait();
            loadDate();

            clear(null);
        } catch (SQLIntegrityConstraintViolationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Email already exist");
            alert.setContentText("Please change  email");
            alert.showAndWait();
        } catch (SQLException ex) {
            Logger.getLogger(DepotController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> types = FXCollections.observableArrayList();
        types.add("PLA");
        types.add("TPU");
        types.add("ABS");
        types.add("AVC");
        types.add("PETG");
        type_box.setItems(types);

        ObservableList<String> suppliers_name = FXCollections.observableArrayList();
        connection = DbConnect.getConnect();
        query = "SELECT name FROM suppliers;";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                suppliers_name.add(
                        resultSet.getString("name")
                       );

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        supplier_box.setItems(suppliers_name);
        loadDate();



    }
}
