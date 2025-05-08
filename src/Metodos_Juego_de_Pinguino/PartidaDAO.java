package Metodos_Juego_de_Pinguino;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PartidaDAO {
    private Connection con;

    public PartidaDAO(Connection con) {
        this.con = con;
    }

    // Save new game
    public void guardarNuevaPartida(int numPartida, String fecha, String hora, Map<Integer, String> estadoCasillas, Inventario inventario, int idJugador) {
        try {
            // Convert estadoCasillas map to JSON string
            String jsonEstadoCasillas = convertirCasillasAJSON(estadoCasillas);

            // Insert into Partida
            String sqlPartida = "INSERT INTO Partida (num_partida, fecha, hora, estado_casillas) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sqlPartida);
            stmt.setInt(1, numPartida);
            stmt.setDate(2, Date.valueOf(fecha)); // Assuming date format is "YYYY-MM-DD"
            stmt.setTime(3, Time.valueOf(hora));   // Assuming time format is "HH:MM:SS"
            stmt.setString(4, jsonEstadoCasillas);
            stmt.executeUpdate();

            // Insert into Jugador_Partida
            String sqlJugadorPartida = "INSERT INTO Jugador_Partida (id_jugador_partida, id_partida, id_jugador, inventario) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtJugadorPartida = con.prepareStatement(sqlJugadorPartida);
            stmtJugadorPartida.setInt(1, generarIDUnico()); // Implementar método para generar ID único
            stmtJugadorPartida.setInt(2, numPartida);
            stmtJugadorPartida.setInt(3, idJugador);
            stmtJugadorPartida.setString(4, convertirInventarioAJSON(inventario));
            stmtJugadorPartida.executeUpdate();

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
                datosCargados.put("hora", rsPartida.getString("hora"));
                datosCargados.put("estado_casillas", convertirJSONACasillas(rsPartida.getString("estado_casillas")));
            }

            // Load player inventory from Jugador_Partida
            String sqlJugadorPartida = "SELECT inventario FROM Jugador_Partida WHERE id_partida = ? AND id_jugador = ?";
            PreparedStatement stmtJugadorPartida = con.prepareStatement(sqlJugadorPartida);
            stmtJugadorPartida.setInt(1, idPartida);
            stmtJugadorPartida.setInt(2, idJugador);
            ResultSet rsInventario = stmtJugadorPartida.executeQuery();

            if (rsInventario.next()) {
                datosCargados.put("inventario", convertirJSONAInventario(rsInventario.getString("inventario")));
            }

            System.out.println("Partida cargada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al cargar la partida: " + e.getMessage());
        }

        return datosCargados;
    }

    // Helper method: Convert Map<Integer, String> to JSON string
    private String convertirCasillasAJSON(Map<Integer, String> casillasMap) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<Integer, String> entry : casillasMap.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
        }
        if (json.length() > 1) {
            json.setLength(json.length() - 1); // Remove last comma
        }
        json.append("}");
        return json.toString();
    }

    // Helper method: Convert JSON string to Map<Integer, String>
    private Map<Integer, String> convertirJSONACasillas(String json) {
        Map<Integer, String> casillasMap = new HashMap<>();
        json = json.replaceAll("[{}]", "");
        String[] entries = json.split(",");
        for (String entry : entries) {
            String[] keyValue = entry.split(":");
            Integer key = Integer.parseInt(keyValue[0].replaceAll("\"", ""));
            String value = keyValue[1].replaceAll("\"", "");
            casillasMap.put(key, value);
        }
        return casillasMap;
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
        Random rand = new Random();
        return rand.nextInt(900000) + 100000; // 6-digit random number
    }
}
