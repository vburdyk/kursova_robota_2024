package com.example.test_appliances;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ElectroDevicesController {
    @FXML
    private TextField fromPowerField;
    @FXML
    private TextField toPowerField;
    @FXML
    private TableView<ElectroDevice> devicesListView;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<ElectroDevice, String> nameColumn;
    @FXML
    private TableColumn<ElectroDevice, Double> powerColumn;
    @FXML
    private Label totalPowerLabel;
    private Connection connection;
    private ObservableList<ElectroDevice> devices = FXCollections.observableArrayList();


    public ElectroDevicesController() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/electro_devices", "vburdyk", "vburdyk");
        } catch (SQLException e) {
            System.err.println("Помилка під'єднання до бази даних: " + e.getMessage());
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @FXML
    public void initialize() {
        loadDevices();
        devicesListView.setItems(devices);
        initTableView();

        fromPowerField.textProperty().addListener((obs, oldValue, newValue) -> {
            filterDevices();
        });
        toPowerField.textProperty().addListener((obs, oldValue, newValue) -> {
            filterDevices();
        });

        devicesListView.setRowFactory(tv -> {
            TableRow<ElectroDevice> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null) {
                    row.setStyle(newItem.isTurnedOn() ? "-fx-background-color: #00ff99;" : "-fx-background-color: #e74141;");
                } else {
                    row.setStyle("");
                }
            });
            return row;
        });
        updateTotalPower();
    }

    private void initTableView() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        powerColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPower()).asObject());
        devicesListView.setItems(devices);

    }

    private void updateTotalPower() {
        double totalPower = devices.stream()
                .filter(ElectroDevice::isTurnedOn)
                .mapToDouble(ElectroDevice::getPower)
                .sum();
        totalPowerLabel.setText(String.format("Споживана потужність: %.2f Вт", totalPower));
    }

    @FXML
    public void turnOnDevices(ActionEvent event) {
        ElectroDevice selectedDevice = devicesListView.getSelectionModel().getSelectedItem();
        if (selectedDevice != null) {
            selectedDevice.setTurnedOn(!selectedDevice.isTurnedOn());
            devicesListView.refresh();
            updateTotalPower();
        }
    }

    @FXML
    public void deleteDevice() {
        ElectroDevice selectedDevice = devicesListView.getSelectionModel().getSelectedItem();
        if (selectedDevice != null) {
            devices.remove(selectedDevice);
            devicesListView.setItems(devices);
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM devices WHERE id=?")) {
                statement.setInt(1, selectedDevice.getId());
                statement.executeUpdate();
                updateTotalPower();
            } catch (SQLException e) {
                System.err.println("Помилка видалення приладу з бази даних: " + e.getMessage());
            }
        }
    }

    @FXML
    public void filterDevices() {
        double fromPower = 0;
        double toPower = Double.MAX_VALUE;
        try {
            fromPower = Double.parseDouble(fromPowerField.getText());
        } catch (NumberFormatException e) {
            // Ignore the error if the field doesn't contain a numerical value
        }
        try {
            toPower = Double.parseDouble(toPowerField.getText());
        } catch (NumberFormatException e) {
            // Ignore the error if the field doesn't contain a numerical value
        }

        final double finalFromPower = fromPower;
        final double finalToPower = toPower;
        String searchText = searchField.getText().trim().toLowerCase();

        devicesListView.setItems(devices.filtered(device -> {
            return device.getPower() >= finalFromPower &&
                    device.getPower() <= finalToPower &&
                    device.getName().toLowerCase().contains(searchText);
        }));
    }

    private void loadDevices() {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM devices")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ElectroDevice device = new ElectroDevice(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("power"),
                        resultSet.getBoolean("turnedOn"));
                devices.add(device);
            }
        } catch (SQLException e) {
            System.err.println("Помилка завантаження електроприладів: " + e.getMessage());
        }
    }


    @FXML
    public void openAddDeviceWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addDevice.fxml"));
            Parent root = loader.load();
            AddDeviceWindowController controller = loader.getController();
            controller.setElectroDevicesController(this);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Помилка відкриття вікна додавання приладу: " + e.getMessage());
        }
    }

    public void addDevice(ElectroDevice device) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO devices (name, power, turnedOn) VALUES (?,?,?)")) {
            statement.setString(1, device.getName());
            statement.setDouble(2, device.getPower());
            statement.setBoolean(3, false);
            statement.executeUpdate();
            devices.add(device);
            devicesListView.setItems(devices);
            updateTotalPower();
        } catch (SQLException e) {
            System.err.println("Помилка додавання приладу до бази даних: " + e.getMessage());
        }
    }


    @FXML
    public void openEditDeviceWindow(ActionEvent event) {
        ElectroDevice selectedDevice = devicesListView.getSelectionModel().getSelectedItem();
        if (selectedDevice != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditDeviceWindow.fxml"));
                Parent root = loader.load();
                EditDeviceController controller = loader.getController();
                controller.setDevice(selectedDevice);
                controller.setMainController(this);
                Stage stage = new Stage();
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) event.getSource()).getScene().getWindow());
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } catch (IOException e) {
                System.err.println("Помилка відкриття вікна редагування приладу: " + e.getMessage());
            }
        }
    }

    public void updateDevice(ElectroDevice device) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE devices SET name=?, power=? WHERE id=?")) {
            statement.setString(1, device.getName());
            statement.setDouble(2, device.getPower());
            statement.setInt(3, device.getId());
            statement.executeUpdate();
            devicesListView.refresh();
            updateTotalPower();
        } catch (SQLException e) {
            System.err.println("Помилка оновлення приладу в базі даних: " + e.getMessage());
        }
    }


    public TableColumn<ElectroDevice, String> getNameColumn() {
        return nameColumn;
    }

    public ObservableList<ElectroDevice> getDevices() {
        return devices;
    }

    public TableView<ElectroDevice> getDevicesListView() {
        return devicesListView;
    }

    public TableColumn<ElectroDevice, Double> getPowerColumn() {
        return powerColumn;
    }

    public TextField getFromPowerField() {
        return fromPowerField;
    }

    public TextField getToPowerField() {
        return toPowerField;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public Label getTotalPowerLabel() {
        return totalPowerLabel;
    }

}