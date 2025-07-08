/*package org.robertomilian.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.robertomilian.system.Main;

import java.sql.Date;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import db.Conexion;
import org.robertomilian.model.Producto;

public class ProductosController implements Initializable {

    private Main principal;

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn colId, colNombre, colApellido, colEmail, colTelefono, colDireccion, colFechaRegistro,
            colContra;

    @FXML private TextField txtId, txtNombre, txtApellido, txtTelefono, txtDireccion, txtCorreo, txtBuscar;
    @FXML private DatePicker dpFechaRegistro;

    @FXML private Button btnNuevo, btnEditar, btnEliminar, btnGuardar, btnCancelar;
    @FXML private Button btnSiguiente, btnAnterior, btnMenu;
/*
    private ObservableList<Clientes> listaClientes;
    private Clientes modeloCliente;

    private enum Operacion {NINGUNA, NUEVO, EDITAR}
    private Operacion tipoOperacion = Operacion.NINGUNA;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarClientes();
        tablaClientes.setOnMouseClicked(e -> cargarClienteSeleccionado());
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellidoCliente"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefonoCliente"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccionCliente"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailCliente"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));
    }

    private void cargarClientes() {
        listaClientes = FXCollections.observableArrayList(obtenerClientes());
        tablaClientes.setItems(listaClientes);
        if (!listaClientes.isEmpty()) {
            tablaClientes.getSelectionModel().selectFirst();
            cargarClienteSeleccionado();
        }
    }

    private ArrayList<Clientes> obtenerClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        try {
            CallableStatement cs = Conexion.getInstancia().getConexion()
                    .prepareCall("CALL sp_listarClientes();");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                lista.add(new Clientes(
                        rs.getInt("ID"),
                        rs.getString("NOMBRE"),
                        rs.getString("APELLIDO"),
                        rs.getString("TELEFONO"),
                        rs.getString("DIRECCION"),
                        rs.getString("EMAIL"),
                        rs.getDate("FECHA").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private void cargarClienteSeleccionado() {
        Clientes c = tablaClientes.getSelectionModel().getSelectedItem();
        if (c != null) {
            txtId.setText(String.valueOf(c.getIdCliente()));
            txtNombre.setText(c.getNombreCliente());
            txtApellido.setText(c.getApellidoCliente());
            txtTelefono.setText(c.getTelefonoCliente());
            txtDireccion.setText(c.getDireccionCliente());
            txtCorreo.setText(c.getEmailCliente());
            dpFechaRegistro.setValue(c.getFechaRegistro());
        }
    }

    private Clientes obtenerDatosFormulario() {
        int id = txtId.getText().isEmpty() ? 0 : Integer.parseInt(txtId.getText());
        return new Clientes(
                id,
                txtNombre.getText(),
                txtApellido.getText(),
                txtTelefono.getText(),
                txtDireccion.getText(),
                txtCorreo.getText(),
                dpFechaRegistro.getValue()
        );
    }

    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtCorreo.clear();
        dpFechaRegistro.setValue(null);
    }

    private void cambiarEstadoCampos(boolean estado) {
        txtNombre.setDisable(!estado);
        txtApellido.setDisable(!estado);
        txtTelefono.setDisable(!estado);
        txtDireccion.setDisable(!estado);
        txtCorreo.setDisable(!estado);
        dpFechaRegistro.setDisable(!estado);
        btnGuardar.setDisable(!estado);
        btnCancelar.setDisable(!estado);
        btnNuevo.setDisable(estado);
        btnEditar.setDisable(estado);
        btnEliminar.setDisable(estado);
    }

    @FXML
    private void clicNuevo() {
        limpiarCampos();
        tipoOperacion = Operacion.NUEVO;
        cambiarEstadoCampos(true);
    }

    @FXML
    private void clicEditar() {
        tipoOperacion = Operacion.EDITAR;
        cambiarEstadoCampos(true);
    }

    @FXML
    private void clicEliminar() {
        try {
            CallableStatement cs = Conexion.getInstancia().getConexion()
                    .prepareCall("CALL sp_eliminarCliente(?);");
            cs.setInt(1, Integer.parseInt(txtId.getText()));
            cs.execute();
            cargarClientes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clicGuardar() {
        Clientes cliente = obtenerDatosFormulario();
        try {
            if (tipoOperacion == Operacion.NUEVO) {
                CallableStatement cs = Conexion.getInstancia().getConexion()
                        .prepareCall("CALL sp_agregarCliente(?,?,?,?,?,?);");
                cs.setString(1, cliente.getNombreCliente());
                cs.setString(2, cliente.getApellidoCliente());
                cs.setString(3, cliente.getTelefonoCliente());
                cs.setString(4, cliente.getDireccionCliente());
                cs.setString(5, cliente.getEmailCliente());
                cs.setDate(6, Date.valueOf(cliente.getFechaRegistro()));
                cs.execute();
            } else if (tipoOperacion == Operacion.EDITAR) {
                CallableStatement cs = Conexion.getInstancia().getConexion()
                        .prepareCall("CALL sp_editarCliente(?,?,?,?,?,?,?);");
                cs.setInt(1, cliente.getIdCliente());
                cs.setString(2, cliente.getNombreCliente());
                cs.setString(3, cliente.getApellidoCliente());
                cs.setString(4, cliente.getTelefonoCliente());
                cs.setString(5, cliente.getDireccionCliente());
                cs.setString(6, cliente.getEmailCliente());
                cs.setDate(7, Date.valueOf(cliente.getFechaRegistro()));
                cs.execute();
            }
            tipoOperacion = Operacion.NINGUNA;
            cambiarEstadoCampos(false);
            cargarClientes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clicCancelar() {
        cargarClienteSeleccionado();
        tipoOperacion = Operacion.NINGUNA;
        cambiarEstadoCampos(false);
    }

    @FXML
    private void clicSiguiente() {
        int index = tablaClientes.getSelectionModel().getSelectedIndex();
        if (index < listaClientes.size() - 1) {
            tablaClientes.getSelectionModel().select(index + 1);
            cargarClienteSeleccionado();
        }
    }

    @FXML
    private void clicAnterior() {
        int index = tablaClientes.getSelectionModel().getSelectedIndex();
        if (index > 0) {
            tablaClientes.getSelectionModel().select(index - 1);
            cargarClienteSeleccionado();
        }
    }

    @FXML
    public void clicRegresar(ActionEvent evento) {
        if (evento.getSource() == btnMenu) {
            principal.escenaVeterinaria();
        }
    }

    @FXML
    private void buscarCliente() {
        String texto = txtBuscar.getText().toLowerCase();
        ArrayList<Clientes> resultados = new ArrayList<>();
        for (Clientes c : listaClientes) {
            if (c.getNombreCliente().toLowerCase().contains(texto)) {
                resultados.add(c);
            }
        }
        tablaClientes.setItems(FXCollections.observableArrayList(resultados));
    }
}

