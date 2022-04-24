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

public class ClientsController implements Initializable {

    @FXML
    private TableView<Client> client_table;

    @FXML
    private Button add_btn;

    @FXML
    private TextField address_field;

    @FXML
    private Button clear_btn;

    @FXML
    private ComboBox<String> country_box;

    @FXML
    private TableColumn<Client,String> email_col;

    @FXML
    private TextField email_field;

    @FXML
    private TableColumn<Client,String> fax_col;

    @FXML
    private TextField fax_filed;

    @FXML
    private TableColumn<Client,String> name_col;

    @FXML
    private TextField name_field;

    @FXML
    private TableColumn<Client,String> phone_col;


    @FXML
    private TextField phone_field;

    @FXML
    private TextField search_field;


    @FXML
    private TableColumn<Client,String> ref_col;

    @FXML
    private TableColumn<Client,String> society_col;

    @FXML
    private TextField society_field;

    @FXML
    private TableColumn<Client,String> tax_col;

    @FXML
    private TextField tax_field;

    @FXML
    private TableColumn<Client,String> trade_col;

    @FXML
    private TextField trade_field;

    @FXML
    private Button update_btn;

    @FXML
    private ImageView delete_btn;



    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Client client = null;
    int myIndex;
    int id;

    ObservableList<Client> ClientList = FXCollections.observableArrayList();


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
            ClientList.clear();
            query = "SELECT * FROM clients;";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                ClientList.add(new Client(Integer.parseInt(resultSet.getString("id_clients")),
                        resultSet.getString("society"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("country"),
                        resultSet.getString("address"),
                        resultSet.getString("phone"),
                        resultSet.getString("fax"),
                        resultSet.getString("tax_id"),
                        resultSet.getString("trade_register")));
                client_table.setItems(ClientList);
            }


            FilteredList<Client> filteredData = new FilteredList<>(ClientList, b -> true);
            search_field.textProperty().addListener((observableValue, oldValue, newValue) ->{
                filteredData.setPredicate(clientSearchModel ->{
                    //if no search value then display all records or whatever records it current have. no changes.
                    if (newValue.isEmpty()||newValue.isBlank()||newValue==null){
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();
                    if (clientSearchModel.getName().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else  if (clientSearchModel.getReference().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if (clientSearchModel.getName().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if (clientSearchModel.getEmail().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if (clientSearchModel.getSociety().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if (clientSearchModel.getPhone().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else {
                        return false;
                    }

                });
            });

            SortedList<Client> sortedData = new SortedList<>(filteredData);
            //bind sorted result with table view
            sortedData.comparatorProperty().bind(client_table.comparatorProperty());

            //apply filtered and sorted data to the table view
            client_table.setItems(sortedData);

        } catch (SQLException e) {
            e.printStackTrace();
        }

            client_table.setRowFactory( tv->{
                TableRow<Client> myRow = new TableRow<>();
                myRow.setOnMouseClicked (event ->
                {
                    if (event.getClickCount() == 1 && (!myRow.isEmpty()))
                    {
                        myIndex =  client_table.getSelectionModel().getSelectedIndex();
                        id = Integer.parseInt(String.valueOf(client_table.getItems().get(myIndex).getId()));
                        society_field.setText(client_table.getItems().get(myIndex).getSociety());
                        name_field.setText(client_table.getItems().get(myIndex).getName());
                        address_field.setText(client_table.getItems().get(myIndex).getAddress());
                        email_field.setText(client_table.getItems().get(myIndex).getEmail());
                        phone_field.setText(client_table.getItems().get(myIndex).getEmail());
                        fax_filed.setText(client_table.getItems().get(myIndex).getFax());
                        tax_field.setText(client_table.getItems().get(myIndex).getTax_id());
                        trade_field.setText(client_table.getItems().get(myIndex).getTrade_register());
                        country_box.setValue(client_table.getItems().get(myIndex).getCountry());


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
    void addClient(ActionEvent event) throws SQLException {
        String name = name_field.getText();
        String society = society_field.getText();
        String address = address_field.getText();
        String email = email_field.getText();
        String tax = tax_field.getText();
        String fax = fax_filed.getText();
        String phone = phone_field.getText();
        String trade = trade_field.getText();
        String country = country_box.getValue();

        if (name.isEmpty()||society.isEmpty()||address.isEmpty()||tax.isEmpty()||fax.isEmpty()||phone.isEmpty()||trade.isEmpty()||email.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All Data");
            alert.showAndWait();
        }else{
            connection = DbConnect.getConnect();
            query = "INSERT INTO clients (name, society, address, email, phone, fax, tax_id, trade_register, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
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
            try {
                preparedStatement.executeUpdate();
                connection.prepareStatement("update clients set reference = CONCAT('CL',LPAD(id_clients,5,0)) where id_clients>0;").executeUpdate();
                loadDate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Add client information");
                alert.setHeaderText("Add client information");
                alert.setContentText("Client information Added successfully");
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
         country_box.setValue("Tunisia");
    }

    @FXML
    void update(ActionEvent event) {
        try {
            myIndex = client_table.getSelectionModel().getSelectedIndex();
            id = Integer.parseInt(String.valueOf(client_table.getItems().get(myIndex).getId()));
        } catch(IndexOutOfBoundsException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Client selected yet");

            alert.setHeaderText("Please select Client");
            alert.setContentText("Please select Client you want to update ");

            alert.showAndWait();
        }
            String name = name_field.getText();
            String society = society_field.getText();
            String address = address_field.getText();
            String email = email_field.getText();
            String tax = tax_field.getText();
            String fax = fax_filed.getText();
            String phone = phone_field.getText();
            String trade = trade_field.getText();
            String country = country_box.getValue();

            if (name.isEmpty() || society.isEmpty() || address.isEmpty() || tax.isEmpty() || fax.isEmpty() || phone.isEmpty() || trade.isEmpty() || email.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Please Fill All Data");
                alert.showAndWait();
            } else {

                try {

                connection = DbConnect.getConnect();
                query = "update clients set name= ?, society= ?, address= ?, email= ?, phone= ?, fax= ?, tax_id= ?, trade_register= ?, country= ? where id_clients = ? ";
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
                preparedStatement.setInt(10, id);
                preparedStatement.executeUpdate();
                connection.prepareStatement("update clients set reference = CONCAT('CL',LPAD(id_clients,5,0)) where id_clients>0;").executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update Client information");

                alert.setHeaderText("Update Client information");
                alert.setContentText("Client information Updated successfully");

                alert.showAndWait();
                clear(null);
                loadDate();
            } catch(SQLIntegrityConstraintViolationException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Email already exist");
                alert.setContentText("Please change  email");
                alert.showAndWait();
            } catch(SQLException ex){
                Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);

        }
    }}

    @FXML
    void delete(MouseEvent event) {
        try
        {
        myIndex =  client_table.getSelectionModel().getSelectedIndex();
        id = Integer.parseInt(String.valueOf(client_table.getItems().get(myIndex).getId()));



            connection = DbConnect.getConnect();
            query = "delete  from clients where id_clients = ? ";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Client");

            alert.setHeaderText("Delete Client");
            alert.setContentText("Client deleted successfully!");

            alert.showAndWait();
            loadDate();
            clear(null);
        }
        catch (IndexOutOfBoundsException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Client selected yet");

            alert.setHeaderText("Please select Client");
            alert.setContentText("Please select Client you want to delete ");

            alert.showAndWait();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}

