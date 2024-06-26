package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Usuario {
    private int id;
    private String nombre;
    private String password;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String numeroTelefono;
    private String correoElectronico;
    private int idGenero;
    private int idCiudad;
    private static CreateConnection createConn = new CreateConnection();

    public Usuario(int id, String nombre, String password, String apellidoPaterno, String apellidoMaterno, String numeroTelefono, String correoElectronico, int idGenero, int idCiudad) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.numeroTelefono = numeroTelefono;
        this.correoElectronico = correoElectronico;
        this.idGenero = idGenero;
        this.idCiudad = idCiudad;
    }

    public Usuario(int idUsuario) {
        String query = "SELECT " +
                "    iu.id, " +
                "    iu.nombre, " +
                "    p.password, " +
                "    iu.apellido_paterno, " +
                "    iu.apellido_materno, " +
                "    iu.numero_telefono, " +
                "    iu.correo_electronico, " +
                "    iu.id_genero, " +
                "    iu.id_ciudades " +
                "FROM " +
                "    informacion_usuario iu " +
                "    JOIN passwords p ON iu.id = p.id_usuario " +
                "    JOIN genero g ON iu.id_genero = g.id " +
                "    JOIN ciudades c ON iu.id_ciudades = c.id " +
                "WHERE " +
                "    iu.id = ?";
        try (Connection conn = createConn.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, idUsuario);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    this.id = resultSet.getInt("id");
                    this.nombre = resultSet.getString("nombre");
                    this.password = resultSet.getString("password");
                    this.apellidoPaterno = resultSet.getString("apellido_paterno");
                    this.apellidoMaterno = resultSet.getString("apellido_materno");
                    this.numeroTelefono = resultSet.getString("numero_telefono");
                    this.correoElectronico = resultSet.getString("correo_electronico");
                    this.idGenero = resultSet.getInt("id_genero");
                    this.idCiudad = resultSet.getInt("id_ciudades");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    public static boolean registrar(Usuario usr) throws SQLException {
        String query = "INSERT INTO informacion_usuario (id, nombre, apellido_paterno, apellido_materno, numero_telefono, correo_electronico, id_genero, id_ciudades) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        String passwordQuery = "INSERT INTO passwords (password) VALUES (?);";

        Connection conn = createConn.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query);
        PreparedStatement passwordPstmt = conn.prepareStatement(passwordQuery);

        try {
            // Primera consulta para los datos de usuario
            pstmt.setInt(1, usr.getId());
            pstmt.setString(2, usr.getNombre());
            pstmt.setString(3, usr.getApellidoPaterno());
            pstmt.setString(4, usr.getApellidoMaterno());
            pstmt.setString(5, usr.getNumeroTelefono());
            pstmt.setString(6, usr.getCorreoElectronico());
            pstmt.setInt(7, usr.getIdGenero());
            pstmt.setInt(8, usr.getIdCiudad());

            pstmt.executeUpdate();

            // Segunfa consulta para agregar la contraseña
            passwordPstmt.setString(1, usr.getPassword());
            passwordPstmt.executeUpdate();

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            // Cierra los PreparedStatements y la conexión en el bloque finally
            if (pstmt != null) {
                pstmt.close();
            }
            if (passwordPstmt != null) {
                passwordPstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }


    public static boolean usuarioExiste(String correoElectronico) throws SQLException {
        String query = "SELECT id FROM informacion_usuario WHERE correo_electronico = ?";
        Connection conn = createConn.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, correoElectronico);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }


    public static int obtenerIdUsuario(String correoElectronico) throws SQLException {
        String query = "SELECT id FROM informacion_usuario WHERE correo_electronico = ?";
        Connection conn = createConn.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, correoElectronico);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        } else {
            // Devolver un valor negativo (por ejemplo, -1) si no se encontró el usuario
            return -1;
        }
    }


}