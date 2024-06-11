package com.example.test_appliances;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class EditDeviceController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField powerField;

    private ElectroDevice device;
    private ElectroDevicesController mainController;

    public void setDevice(ElectroDevice device) {
        this.device = device;
        nameField.setText(device.getName());
        powerField.setText(String.valueOf(device.getPower()));
    }

    public void setMainController(ElectroDevicesController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void saveChanges() {
        String name = nameField.getText();
        double power;

        try {
            power = Double.parseDouble(powerField.getText());

            device.setName(name);
            device.setPower(power);

            mainController.updateDevice(device);

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            // Handle invalid input
        }
    }

}