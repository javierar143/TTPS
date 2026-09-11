package ttps.clasificados.model;

public class Usuario {

    private String nombre;
    private String clave;
    private String perfil;


    public Usuario(String nombre, String clave, String perfil) {
        this.nombre = nombre;
        this.clave = clave;
        this.perfil = perfil;
    }


    public String getNombre() {
        return nombre;
    }

    public String getClave() {
        return clave;
    }

    public String getPerfil() {

        return perfil;
    }
}
