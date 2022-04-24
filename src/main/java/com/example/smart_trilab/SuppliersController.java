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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SuppliersController implements Initializable {

    @FXML
    private TableView<Supplier> supplier_table;

    @FXML
    private Button add_btn;

    @FXML
    private TextField address_field;

    @FXML
    private Button clear_btn;

    @FXML
    private ComboBox<String> country_box;

    @FXML
    private TableColumn<Supplier,String> email_col;

    @FXML
    private TextField email_field;

    @FXML
    private TableColumn<Supplier,String> fax_col;

    @FXML
    private TextField fax_filed;

    @FXML
    private TableColumn<Supplier,String> name_col;

    @FXML
    private TextField name_field;

    @FXML
    private TableColumn<Supplier,String> phone_col;


    @FXML
    private TextField phone_field;


    @FXML
    private TableColumn<Supplier,String> ref_col;

    @FXML
    private TableColumn<Supplier,String> society_col;

    @FXML
    private TextField society_field;

    @FXML
    private TextField search_field;

    @FXML
    private TableColumn<Supplier,String> tax_col;

    @FXML
    private TextField tax_field;

    @FXML
    private TableColumn<Supplier,String> trade_col;

    @FXML
    private TextField trade_field;

    @FXML
    private Button update_btn;

    @FXML
    private ImageView delete_btn;

    @FXML
    private TextField rib_field;

    @FXML
    private TextField bank_field;



    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Supplier supplier = null;
    int myIndex;
    int id;

    ObservableList<Supplier> SupplierList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<String> cities = FXCollections.observableArrayList();


        String[] locales1 = Locale.getISOCountries();
        for (String countrylist : locales1) {
            Locale obj = new Locale("", countrylist);
            String[] city = { obj.getDisplayCountry() };
            for (int x = 0; x < city.length; x++) {
                cities.add(obj.getDisplayCountry());
            }
        }
        country_box.setItems(cities);
        country_box.setValue("Tunisia");

        loadDate();
    }

    @FXML
    private void refreshTable(){

        try {
            SupplierList.clear();
            query = "SELECT * FROM suppliers;";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                SupplierList.add(new Supplier(Integer.parseInt(resultSet.getString("id_suppliers")),
                        resultSet.getString("society"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("country"),
                        resultSet.getString("address"),
                        resultSet.getString("phone"),
                        resultSet.getString("fax"),
                        resultSet.getString("tax_id"),
                        resultSet.getString("trade_register"),
                        resultSet.getString("bank"),
                        resultSet.getString("rib")));
                supplier_table.setItems(SupplierList);
            }

            FilteredList<Supplier> filteredData = new FilteredList<>(SupplierList,b -> true);
            search_field.textProperty().addListener((observableValue, oldValue, newValue) ->{
                filteredData.setPredicate(supplierSearchModel ->{
                    //if no search value then display all records or whatever records it current have. no changes.
                    if (newValue.isEmpty()||newValue.isBlank()||newValue==null){
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();
                    if (supplierSearchModel.getName().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else  if (supplierSearchModel.getReference().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if (supplierSearchModel.getName().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if (supplierSearchModel.getEmail().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if (supplierSearchModel.getSociety().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if (supplierSearchModel.getPhone().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else {
                        return false;
                    }

                });
            });

            SortedList<Supplier> sortedData = new SortedList<>(filteredData);
            //bind sorted result with table view
            sortedData.comparatorProperty().bind(supplier_table.comparatorProperty());

            //apply filtered and sorted data to the table view
            supplier_table.setItems(sortedData);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        supplier_table.setRowFactory( tv->{
            TableRow<Supplier> myRow = new TableRow<>();
            myRow.setOnMouseClicked (event ->
            {
                if (event.getClickCount() == 1 && (!myRow.isEmpty()))
                {
                    myIndex =  supplier_table.getSelectionModel().getSelectedIndex();
                    id = Integer.parseInt(String.valueOf(supplier_table.getItems().get(myIndex).getId()));
                    society_field.setText(supplier_table.getItems().get(myIndex).getSociety());
                    name_field.setText(supplier_table.getItems().get(myIndex).getName());
                    address_field.setText(supplier_table.getItems().get(myIndex).getAddress());
                    email_field.setText(supplier_table.getItems().get(myIndex).getEmail());
                    phone_field.setText(supplier_table.getItems().get(myIndex).getEmail());
                    fax_filed.setText(supplier_table.getItems().get(myIndex).getFax());
                    tax_field.setText(supplier_table.getItems().get(myIndex).getTax_id());
                    trade_field.setText(supplier_table.getItems().get(myIndex).getTrade_register());
                    rib_field.setText(supplier_table.getItems().get(myIndex).getRib());
                    bank_field.setText(supplier_table.getItems().get(myIndex).getBank());
                    country_box.setValue(supplier_table.getItems().get(myIndex).getCountry());


                }
            });
            return myRow;
        });

    }



    private void loadDate() {
        connection = DbConnect.getConnect();
        refreshTable();
        ref_col.setCellValueFactory(new PropertyValueFactory<>("reference"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        email_col.setCellValueFactory(new PropertyValueFactory<>("email"));
        phone_col.setCellValueFactory(new PropertyValueFactory<>("phone"));
        fax_col.setCellValueFactory(new PropertyValueFactory<>("fax"));
        society_col.setCellValueFactory(new PropertyValueFactory<>("society"));
        tax_col.setCellValueFactory(new PropertyValueFactory<>("tax_id"));
        trade_col.setCellValueFactory(new PropertyValueFactory<>("trade_register"));
    }

    @FXML
    void addSupplier(ActionEvent event) throws SQLException {
        String name = name_field.getText();
        String society = society_field.getText();
        String address = address_field.getText();
        String email = email_field.getText();
        String tax = tax_field.getText();
        String fax = fax_filed.getText();
        String phone = phone_field.getText();
        String trade = trade_field.getText();
        String country = country_box.getValue();
        String rib = rib_field.getText();
        String bank = bank_field.getText();

        if (name.isEmpty()||rib.isEmpty()||bank.isEmpty()||society.isEmpty()||address.isEmpty()||tax.isEmpty()||fax.isEmpty()||phone.isEmpty()||trade.isEmpty()||email.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All Data");
            alert.showAndWait();
        }else{
            connection = DbConnect.getConnect();
            query = "INSERT INTO suppliers (name, society, address, email, phone, fax, tax_id, trade_register, country,bank,rib) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?);";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,society);
            preparedStatement.setString(3,address);
            preparedStatement.setString(4,email);
            preparedStatement.setString(5,phone);
            preparedStatement.setString(6,fax);
            preparedStatement.setString(7,tax);
            preparedStatement.setString(8,trade);
            preparedStatement.setString(9,country);
            preparedStatement.setString(10,bank);
            preparedStatement.setString(11,rib);
            try {
                preparedStatement.executeUpdate();
                connection.prepareStatement("update suppliers set reference = CONCAT('SP',LPAD(id_suppliers,5,0)) where id_suppliers>0;").executeUpdate();
                loadDate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Add supplier information");
                alert.setHeaderText("Add supplier information");
                alert.setContentText("Supplier information Added successfully");
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
    void clear(ActionEvent event) {
        name_field.setText("");
        society_field.setText("");
        address_field.setText("");
        email_field.setText("");
        tax_field.setText("");
        fax_filed.setText("");
        phone_field.setText("");
        trade_field.setText("");
        bank_field.setText("");
        rib_field.setText("");
        country_box.setValue("Tunisia");
    }

    @FXML
    void update(ActionEvent event) {
        try {
        myIndex = supplier_table.getSelectionModel().getSelectedIndex();
        id = Integer.parseInt(String.valueOf(supplier_table.getItems().get(myIndex).getId()));

        String name = name_field.getText();
        String society = society_field.getText();
        String address = address_field.getText();
        String email = email_field.getText();
        String tax = tax_field.getText();
        String fax = fax_filed.getText();
        String phone = phone_field.getText();
        String trade = trade_field.getText();
        String bank = bank_field.getText();
        String rib = rib_field.getText();
        String country = country_box.getValue();

            connection = DbConnect.getConnect();
            query = "update suppliers set name= ?, society= ?, address= ?, email= ?, phone= ?, fax= ?, tax_id= ?, trade_register= ?, country= ? ,bank = ?,rib= ? where id_suppliers = ? ";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, society);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, phone);
            preparedStatement.setString(6, fax);
            preparedStatement.setString(7, tax);
            preparedStatement.setString(8, trade);
            preparedStatement.setString(9, country);
            preparedStatement.setString(10, bank);
            preparedStatement.setString(11, rib);
            preparedStatement.setInt(12, id);
            preparedStatement.executeUpdate();
            connection.prepareStatement("update suppliers set reference = CONCAT('SP',LPAD(id_suppliers,5,0)) where id_suppliers>0;").executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Supplier information");

            alert.setHeaderText("Update Supplier information");
            alert.setContentText("Supplier information Updated successfully");

            alert.showAndWait();
            loadDate();
            clear(null);
        } catch (SQLIntegrityConstraintViolationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Email already exist");
            alert.setContentText("Please change  email");
            alert.showAndWait();
        } catch (SQLException ex) {
            Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IndexOutOfBoundsException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Supplier selected yet");

            alert.setHeaderText("Please select Supplier");
            alert.setContentText("Please select Supplier you want to update ");

            alert.showAndWait();
        }
    }

    @FXML
    void delete(MouseEvent event) {
        try
        {
        myIndex =  supplier_table.getSelectionModel().getSelectedIndex();
        id = Integer.parseInt(String.valueOf(supplier_table.getItems().get(myIndex).getId()));



            connection = DbConnect.getConnect();
            query = "delete  from suppliers where id_suppliers = ? ";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Supplier");

            alert.setHeaderText("Delete Supplier");
            alert.setContentText("Supplier deleted successfully!");

            alert.showAndWait();
            loadDate();
            clear(null);
        }
        catch (IndexOutOfBoundsException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Supplier selected yet");

            alert.setHeaderText("Please select Supplier");
            alert.setContentText("Please select Supplier you want to delete ");

            alert.showAndWait();
        }
        catch (SQLException ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Supplier selected yet");

            alert.setHeaderText("Please select Supplier");
            alert.setContentText("Please select Supplier you want to delete ");

            alert.showAndWait();        }
    }


}

