package com.example.test_appliances;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddDeviceWindowController {
    @FXML
    private TextField deviceNameField;
    @FXML
    private TextField devicePowerField;
    private ElectroDevicesController electroDevicesController;

    public void setElectroDevicesController(ElectroDevicesController electroDevicesController) {
        this.electroDevicesController = electroDevicesController;
    }

    @FXML
    public void addDevice(ActionEvent event) {
        String name = deviceNameField.getText();
        if (name.isEmpty()) {
            // Show an error message to the user
            System.err.println("Введіть ім'я пристрою");
            return;
        }
        double power;
        try {
            power = Double.parseDouble(devicePowerField.getText());
        } catch (NumberFormatException e) {
            // Show an error message to the user
            System.err.println("Введіть потужність пристрою");
            return;
        }
        ElectroDevice device = new ElectroDevice(name, power);
        electroDevicesController.addDevice(device);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
