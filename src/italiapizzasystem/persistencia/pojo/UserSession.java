/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package italiapizzasystem.persistencia.pojo;

/**
 *
 * @author acrca
 */
public class UserSession {
    private static UserSession instancia;
    private Empleado empleadoConectado;

    private UserSession() {}

    public static UserSession getInstancia() {
        if (instancia == null) {
            instancia = new UserSession();
        }
        return instancia;
    }

    public Empleado getEmpleadoConectado() {
        return empleadoConectado;
    }

    public void setEmpleadoConectado(Empleado empleadoConectado) {
        this.empleadoConectado = empleadoConectado;
    }

    public void limpiarSesion() {
        this.empleadoConectado = null;
    }
}
