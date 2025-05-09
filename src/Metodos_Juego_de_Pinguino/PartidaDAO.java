package Metodos_Juego_de_Pinguino;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PartidaDAO {
    private Connection con;

    public PartidaDAO(Connection con) {
        this.con = con;
    }

    // Save new game
    public int guardarNuevaPartida(String fecha, Map<Integer, String> estadoCasillas, Inventario inventario, int idJugador, int posicionJugador) {
        int numPartida = -1;

        try {
            // Step 1: Get next unique game ID from Oracle sequence
            numPartida = obtenerSiguienteIdPartida();

            // Step 2: Insert into Partida table
            String sqlPartida = "INSERT INTO Partida (num_partida, fecha) VALUES (?, ?)";
            PreparedStatement stmtPartida = con.prepareStatement(sqlPartida);
            stmtPartida.setInt(1, numPartida);
            stmtPartida.setDate(2, Date.valueOf(fecha)); // Assumes format like "2025-04-05"
            stmtPartida.executeUpdate();

            // Step 3: Insert into Jugador_Partida table
            String sqlJugadorPartida = "INSERT INTO Jugador_Partida (id_jugador_partida, id_partida, id_jugador, inventario, posicion) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmtJugadorPartida = con.prepareStatement(sqlJugadorPartida);
            stmtJugadorPartida.setInt(1, obtenerSiguienteIdJugadorPartida()); // From jugador_partida_seq
            stmtJugadorPartida.setInt(2, numPartida);
            stmtJugadorPartida.setInt(3, idJugador);
            stmtJugadorPartida.setString(4, convertirInventarioAJSON(inventario));
            stmtJugadorPartida.setInt(5, posicionJugador);
            stmtJugadorPartida.executeUpdate();

            // Step 4: Insert into Casilla table
            String sqlCasilla = "INSERT INTO Casilla (id_casilla, num_partida, nombre, estado, tipo, posicion, descripcion) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtCasilla = con.prepareStatement(sqlCasilla);

            for (Map.Entry<Integer, String> entry : estadoCasillas.entrySet()) {
                Integer posicion = entry.getKey();
                String estado = entry.getValue();

                int casillaId = obtenerSiguienteIdCasilla(); // ✅ Use sequence
                stmtCasilla.setInt(1, casillaId);           // Assign unique ID
                stmtCasilla.setInt(2, numPartida);
                stmtCasilla.setString(3, "Casilla_" + posicion);
                stmtCasilla.setString(4, estado);
                stmtCasilla.setString(5, "Normal");
                stmtCasilla.setInt(6, posicion);
                stmtCasilla.setString(7, "Descripción de casilla " + posicion);
                stmtCasilla.addBatch();
            }
            stmtCasilla.executeBatch();

            System.out.println("Partida guardada exitosamente con ID: " + numPartida);
            return numPartida;

        } catch (SQLException e) {
            System.out.println("Error al guardar la partida: " + e.getMessage());
            return -1;
        }
    }

    // Load existing game
    public Map<String, Object> cargarPartida(int idPartida, int idJugador) {
        Map<String, Object> datosCargados = new HashMap<>();

        try {
            // Load game state from Partida table
            String sqlPartida = "SELECT * FROM Partida WHERE num_partida = ?";
            PreparedStatement stmt = con.prepareStatement(sqlPartida);
            stmt.setInt(1, idPartida);
            ResultSet rsPartida = stmt.executeQuery();

            if (rsPartida.next()) {
                datosCargados.put("fecha", rsPartida.getString("fecha"));
            }

            // Load player inventory and position from Jugador_Partida
            String sqlJugadorPartida = "SELECT inventario, posicion FROM Jugador_Partida WHERE id_partida = ? AND id_jugador = ?";
            PreparedStatement stmtJugadorPartida = con.prepareStatement(sqlJugadorPartida);
            stmtJugadorPartida.setInt(1, idPartida);
            stmtJugadorPartida.setInt(2, idJugador);
            ResultSet rsInventario = stmtJugadorPartida.executeQuery();

            if (rsInventario.next()) {
                datosCargados.put("inventario", convertirJSONAInventario(rsInventario.getString("inventario")));
                datosCargados.put("posicion", rsInventario.getInt("posicion"));
            }

            // Load board state from Casilla
            String sqlCasillas = "SELECT posicion, estado FROM Casilla WHERE num_partida = ?";
            PreparedStatement stmtCasillas = con.prepareStatement(sqlCasillas);
            stmtCasillas.setInt(1, idPartida);
            ResultSet rsCasillas = stmtCasillas.executeQuery();

            Map<Integer, String> estadoCasillas = new HashMap<>();
            while (rsCasillas.next()) {
                estadoCasillas.put(rsCasillas.getInt("posicion"), rsCasillas.getString("estado"));
            }
            datosCargados.put("estado_casillas", estadoCasillas);

            System.out.println("Partida cargada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al cargar la partida: " + e.getMessage());
        }

        return datosCargados;
    }

    // Helper method: Convert Inventario to JSON string
    private String convertirInventarioAJSON(Inventario inventario) {
        return String.format("{\"peces\":%d,\"dados\":%d,\"bolas_nieve\":%d}",
                inventario.getCantidadPeces(),
                inventario.getCantidadDados(),
                inventario.getCantidadBolasNieve());
    }

    // Helper method: Convert JSON string to Inventario
    private Inventario convertirJSONAInventario(String json) {
        Inventario inventario = new Inventario();
        json = json.replaceAll("[{}]", "");
        String[] entries = json.split(",");
        for (String entry : entries) {
            String[] keyValue = entry.split(":");
            String key = keyValue[0].replaceAll("\"", "");
            int value = Integer.parseInt(keyValue[1]);
            switch (key) {
                case "peces":
                    inventario.setCantidadPeces(value);
                    break;
                case "dados":
                    inventario.setCantidadDados(value);
                    break;
                case "bolas_nieve":
                    inventario.setCantidadBolasNieve(value);
                    break;
            }
        }
        return inventario;
    }

    public int obtenerSiguienteIdPartida() throws SQLException {
        String sql = "SELECT partida_seq.NEXTVAL FROM dual";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("No se pudo obtener un nuevo ID de partida.");
    }
    
    public int obtenerSiguienteIdJugadorPartida() throws SQLException {
    	String sql = "SELECT jugador_partida_seq.NEXTVAL FROM dual";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("No se pudo obtener un nuevo ID de Jugador_Partida.");
    }
    
    public int obtenerSiguienteIdCasilla() throws SQLException {
        String sql = "SELECT casilla_seq.NEXTVAL FROM dual";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("No se pudo obtener un nuevo ID de Casilla.");
    }
}