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
    public void guardarNuevaPartida(int numPartida, String fecha, Map<Integer, String> estadoCasillas, Inventario inventario, int idJugador, int posicionJugador) {
        try {
            // Insert into Partida
            String sqlPartida = "INSERT INTO Partida (num_partida, fecha) VALUES (?, ?)";
            PreparedStatement stmt = con.prepareStatement(sqlPartida);
            stmt.setInt(1, numPartida);
            stmt.setDate(2, Date.valueOf(fecha)); // Assuming date format is "YYYY-MM-DD"
            stmt.executeUpdate();

            // Insert into Jugador_Partida
            String sqlJugadorPartida = "INSERT INTO Jugador_Partida (id_jugador_partida, id_partida, id_jugador, inventario, posicion) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmtJugadorPartida = con.prepareStatement(sqlJugadorPartida);
            stmtJugadorPartida.setInt(1, generarIDUnico());
            stmtJugadorPartida.setInt(2, numPartida);
            stmtJugadorPartida.setInt(3, idJugador);
            stmtJugadorPartida.setString(4, convertirInventarioAJSON(inventario));
            stmtJugadorPartida.setInt(5, posicionJugador);
            stmtJugadorPartida.executeUpdate();

            // Insert into Casilla table
            String sqlCasilla = "INSERT INTO Casilla (id_casilla, num_partida, nombre, estado, tipo, posicion, descripcion) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtCasilla = con.prepareStatement(sqlCasilla);

            int casillaId = 1; // Example ID generation
            for (Map.Entry<Integer, String> entry : estadoCasillas.entrySet()) {
                Integer posicion = entry.getKey();
                String estado = entry.getValue();
                stmtCasilla.setInt(1, casillaId++);
                stmtCasilla.setInt(2, numPartida);
                stmtCasilla.setString(3, "Casilla_" + posicion);
                stmtCasilla.setString(4, estado);
                stmtCasilla.setString(5, "Normal"); // Placeholder for type
                stmtCasilla.setInt(6, posicion);
                stmtCasilla.setString(7, "Descripción de casilla " + posicion);
                stmtCasilla.addBatch();
            }
            stmtCasilla.executeBatch();

            System.out.println("Partida guardada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al guardar la partida: " + e.getMessage());
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

    // Generate unique ID for jugador_partida
    private int generarIDUnico() {
        return (int) (Math.random() * 900000 + 100000); // 6-digit random number
    }
}