package com.example.smart_trilab;

        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.collections.transformation.FilteredList;
        import javafx.collections.transformation.SortedList;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.Label;
        import javafx.scene.control.TableColumn;
        import javafx.scene.control.TableRow;
        import javafx.scene.control.TableView;
        import javafx.scene.control.cell.PropertyValueFactory;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;

        import java.io.File;
        import java.net.URL;
        import java.sql.Connection;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private TableColumn<Material, String> color_col;


    @FXML
    private TableView<Material> material_table;

    @FXML
    private TableColumn<Material, ImageView> image_col;

    @FXML
    private TableColumn<Material, String> name_col;

    @FXML
    private TableColumn<Material, String> quantity_col;

    @FXML
    private TableColumn<Material, String> reference_col;

    @FXML
    private Label total_clients;

    @FXML
    private Label total_suppliers;

    @FXML
    private TableColumn<Material, String> type_col;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    ObservableList<Material> materialList = FXCollections.observableArrayList();

    @FXML
    private void refreshTable() throws SQLException {


        materialList.clear();
        query = "SELECT * FROM materials WHERE quantity<300";
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            ImageView image = new ImageView(new Image(new File(resultSet.getString("image")).toURI().toString()));
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


    }
    private void loadDate() throws SQLException {
        connection = DbConnect.getConnect();
        refreshTable();
        reference_col.setCellValueFactory(new PropertyValueFactory<>("reference"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        color_col.setCellValueFactory(new PropertyValueFactory<>("color"));
        image_col.setCellValueFactory((new PropertyValueFactory<>("image")));
        type_col.setCellValueFactory(new PropertyValueFactory<>("type"));
        quantity_col.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            loadDate();
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS clientCount FROM clients");
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            total_clients.setText(String.valueOf(resultSet.getInt("clientCount")));
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS supplierCount FROM suppliers");
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            total_suppliers.setText(String.valueOf(resultSet.getInt("supplierCount")));

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
