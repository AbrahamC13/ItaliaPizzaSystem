
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
