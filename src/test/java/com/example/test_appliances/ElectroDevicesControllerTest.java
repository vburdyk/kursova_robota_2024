package com.example.test_appliances;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.sql.*;

public class ElectroDevicesControllerTest {

    private ElectroDevicesController controller;
    private Connection connection;

    @Before
    public void setUp() {
        controller = new ElectroDevicesController();
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/electro_devices", "vburdyk", "vburdyk");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testConnection() {
        assertNotNull("Connection should not be null", connection);
        assertTrue("Connection should be open", isConnectionOpen());
    }

    private boolean isConnectionOpen() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}


























//    @Mock
//    private Connection connection;
//
//    @Mock
//    private PreparedStatement preparedStatement;
//
//    @Mock
//    private ResultSet resultSet;
//
//    @InjectMocks
//    private ElectroDevicesController controller;
//
//    private ObservableList<ElectroDevice> devices;
//
//    @Before
//    public void setUp() throws SQLException {
//        MockitoAnnotations.openMocks(this);
//        controller.setConnection(connection);
//
//        // Mocking JavaFX components
//        setField(controller, "fromPowerField", new TextField());
//        setField(controller, "toPowerField", new TextField());
//        setField(controller, "searchField", new TextField());
//        setField(controller, "totalPowerLabel", new Label());
//
//        devices = FXCollections.observableArrayList();
//        controller.getDevices().addAll(devices);
//
//        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
//        when(preparedStatement.executeQuery()).thenReturn(resultSet);
//        when(resultSet.next()).thenReturn(true, false); // Only one record in result set
//        when(resultSet.getInt("id")).thenReturn(1);
//        when(resultSet.getString("name")).thenReturn("Test Device");
//        when(resultSet.getDouble("power")).thenReturn(100.0);
//        when(resultSet.getBoolean("turnedOn")).thenReturn(true);
//    }
//
//    @Test
//    public void testInitialize() {
//        controller.initialize();
//        assertEquals(1, devices.size());
//        assertEquals("Test Device", devices.get(0).getName());
//    }
//
//    @Test
//    public void testAddDevice() throws SQLException {
//        ElectroDevice device = new ElectroDevice("New Device", 150.0, false);
//        controller.addDevice(device);
//
//        verify(preparedStatement).setString(1, "New Device");
//        verify(preparedStatement).setDouble(2, 150.0);
//        verify(preparedStatement).setBoolean(3, false);
//        verify(preparedStatement).executeUpdate();
//
//        assertEquals(1, devices.size());
//        assertEquals("New Device", devices.get(0).getName());
//    }
//
//    @Test
//    public void testUpdateDevice() throws SQLException {
//        ElectroDevice device = new ElectroDevice(1, "Updated Device", 200.0, true);
//        devices.add(device);
//        controller.updateDevice(device);
//
//        verify(preparedStatement).setString(1, "Updated Device");
//        verify(preparedStatement).setDouble(2, 200.0);
//        verify(preparedStatement).setInt(3, 1);
//        verify(preparedStatement).executeUpdate();
//
//        assertEquals("Updated Device", devices.get(0).getName());
//        assertEquals(200.0, devices.get(0).getPower(), 0.01);
//    }
//
//    @Test
//    public void testDeleteDevice() throws SQLException {
//        ElectroDevice device = new ElectroDevice(1, "Device to Delete", 100.0, false);
//        devices.add(device);
//        controller.deleteDevice();
//
//        verify(preparedStatement).setInt(1, 1);
//        verify(preparedStatement).executeUpdate();
//
//        assertEquals(0, devices.size());
//    }
//
//    @Test
//    public void testTurnOnDevices() {
//        ElectroDevice device = new ElectroDevice(1, "Device to Toggle", 100.0, false);
//        devices.add(device);
//        controller.getDevicesListView().getSelectionModel().select(device);
//        controller.turnOnDevices(new ActionEvent());
//
//        assertTrue(device.isTurnedOn());
//
//        controller.turnOnDevices(new ActionEvent());
//        assertFalse(device.isTurnedOn());
//    }
//
//    @Test
//    public void testFilterDevices() {
//        ElectroDevice device1 = new ElectroDevice(1, "Device1", 100.0, true);
//        ElectroDevice device2 = new ElectroDevice(2, "Device2", 200.0, false);
//        devices.addAll(device1, device2);
//
//        controller.getFromPowerField().setText("150");
//        controller.filterDevices();
//        assertEquals(1, controller.getDevicesListView().getItems().size());
//        assertEquals("Device2", controller.getDevicesListView().getItems().get(0).getName());
//    }



//    @Test
//    public void testUpdateTotalPower() {
//        ElectroDevice device1 = new ElectroDevice(1, "Device1", 100.0, true);
//        ElectroDevice device2 = new ElectroDevice(2, "Device2", 200.0, false);
//        devices.addAll(device1, device2);
//        controller.updateTotalPower();
//
//        assertEquals("Споживана потужність: 100.00 Вт", controller.getTotalPowerLabel().getText());
//    }




//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class ElectroDevicesControllerTest {
//
//    @InjectMocks
//    private ElectroDevicesController controller;
//
//    @Mock
//    private Connection connection;
//
//    @Mock
//    private PreparedStatement preparedStatement;
//
//    @Mock
//    private ResultSet resultSet;
//
//    @Before
//    public void setup() {
//        // Create a mock connection
//        Connection mockConnection = mock(Connection.class);
//
//        // Set the mock connection using the setter method
//        controller.setConnection(mockConnection);
//    }
//
//    @Test
//    public void testInitialize() {
//        // Verify that the devices list is initialized
//        assertNotNull(controller.getDevices());
//        assertEquals(0, controller.getDevices().size());
//
//        // Verify that the table view is initialized
//        assertNotNull(controller.getDevicesListView());
//        assertNotNull(controller.getNameColumn());
//        assertNotNull(controller.getPowerColumn());
//    }
//
//    @Test
//    public void testLoadDevices() throws SQLException {
//        // Mock the result set
//        when(resultSet.next()).thenReturn(true, false);
//        when(resultSet.getInt("id")).thenReturn(1);
//        when(resultSet.getString("name")).thenReturn("Device 1");
//        when(resultSet.getDouble("power")).thenReturn(10.0);
//        when(resultSet.getBoolean("turnedOn")).thenReturn(true);
//
//        // Call the loadDevices method
//        controller.loadDevicesForTesting();
//
//        // Verify that the devices list is populated
//        assertEquals(1, controller.getDevices().size());
//        ElectroDevice device = controller.getDevices().get(0);
//        assertEquals(1, device.getId());
//        assertEquals("Device 1", device.getName());
//        assertEquals(10.0, device.getPower(), 0.01);
//        assertTrue(device.isTurnedOn());
//    }
//
//    @Test
//    public void testTurnOnDevices() {
//        // Create a sample device
//        ElectroDevice device = new ElectroDevice(1, "Device 1", 10.0, false);
//
//        // Add the device to the devices list
//        controller.getDevices().add(device);
//
//        // Call the turnOnDevices method
//        controller.turnOnDevices(null);
//
//        // Verify that the device is turned on
//        assertTrue(device.isTurnedOn());
//    }

//    @Test
//    public void testDeleteDevice() throws SQLException {
//        // Create a sample device
//        ElectroDevice device = new ElectroDevice(1, "Device 1", 10.0, false);
//
//        // Add the device to the devices list
//        controller.getDevices().add(device);
//
//        // Call the deleteDevice method
//        controller.deleteDevice();
//
//        // Verify that the device is removed from the devices list
//        assertEquals(0, controller.getDevices().size());
//
//        // Verify that the delete statement is executed
//        verify(preparedStatement).executeUpdate();
//    }

//    @Test
//    public void testFilterDevices() {
//        // Create sample devices
//        ElectroDevice device1 = new ElectroDevice(1, "Device 1", 10.0, false);
//        ElectroDevice device2 = new ElectroDevice(2, "Device 2", 20.0, true);
//
//        // Add the devices to the devices list
//        controller.getDevices().add(device1);
//        controller.getDevices().add(device2);
//
//        // Set the from power field to 15.0
//        controller.getFromPowerField().setText("15.0");
//
//        // Call the filterDevices method
//        controller.filterDevices();
//
//        // Verify that only device2 is shown in the table view
//        assertEquals(1, controller.getDevicesListView().getItems().size());
//        assertEquals(device2, controller.getDevicesListView().getItems().get(0));
//    }
//
//    @Test
//    public void testAddDevice() throws SQLException {
//        // Create a sample device
//        ElectroDevice device = new ElectroDevice("Device 1", 10.0, false);
//
//        // Call the addDevice method
//        controller.addDevice(device);
//
//        // Verify that the device is added to the devices list
//        assertEquals(1, controller.getDevices().size());
//        assertEquals(device, controller.getDevices().get(0));
//
//        // Verify that the insert statement is executed
//        verify(preparedStatement).executeUpdate();
//    }
//
//    @Test
//    public void testUpdateDevice() throws SQLException {
//        // Create a sample device
//        ElectroDevice device = new ElectroDevice(1, "Device 1", 10.0, false);
//
//        // Add the device to the devices list
//        controller.getDevices().add(device);
//
//        // Update the device
//        device.setName("Device 2");
//        device.setPower(20.0);
//
//        // Call the updateDevice method
//        controller.updateDevice(device);
//
//        // Verify that the device is updated in the devices list
//        assertEquals(1, controller.getDevices().size());
//        assertEquals(device, controller.getDevices().get(0));
//
//        // Verify that the update statement isexecuted
//        verify(preparedStatement).executeUpdate();
//    }
//}