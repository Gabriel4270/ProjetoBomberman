import Objetos.Caixa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorBanco {

    private static final String URL = "jdbc:postgresql://localhost:5432/estudo";
    private static final String USER = "postgres";
    private static final String PASS = "35246543";

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public void salvarJogo(int x, int y, int bombas, List<Caixa> caixas) {
        StringBuilder sb = new StringBuilder();
        for (Caixa c : caixas) {
            sb.append(c.getX()).append(",").append(c.getY()).append(";");
        }

        String sql = "UPDATE estado_jogo SET bomberman_x=?, bomberman_y=?, bombas_restantes=?, caixas_posicoes=? WHERE id=1";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, x);
            stmt.setInt(2, y);
            stmt.setInt(3, bombas);
            stmt.setString(4, sb.toString());

            stmt.executeUpdate();
            System.out.println("JOGO SALVO COM SUCESSO!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public SaveData carregarJogo() {
        String sql = "SELECT * FROM estado_jogo WHERE id=1";
        SaveData dados = null;

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                dados = new SaveData();
                dados.x = rs.getInt("bomberman_x");
                dados.y = rs.getInt("bomberman_y");
                dados.bombas = rs.getInt("bombas_restantes");
                dados.caixas = rs.getString("caixas_posicoes");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar: " + e.getMessage());
        }
        return dados;
    }

    public class SaveData {
        public int x, y, bombas;
        public String caixas;
    }
}