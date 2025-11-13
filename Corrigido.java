import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Classe responsável por conectar ao banco e validar usuário.
 */
public class Corrigido {

    // ✔ Constante para a URL do banco (melhor prática)
    private static final String DB_URL =
        "jdbc:mysql://127.0.0.1/test?user=lopes&password=123";

    // ✔ Armazena o nome do usuário encontrado
    public String nome = "";

    // ✔ Armazena o resultado da autenticação
    public boolean usuarioValido = false;

    /**
     * Método responsável por abrir conexão com o banco MySQL.
     * @return Connection ativa ou null em caso de erro.
     */
    public Connection conectarBD() {
        Connection conexao = null;

        try {
            // ✔ Driver atualizado e correto
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✔ Tentativa de criar conexão
            conexao = DriverManager.getConnection(DB_URL);

        } catch (Exception e) {
            // ✔ Registra falha na conexão
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
        }

        // ✔ Retorna a conexão (pode ser null se falhou)
        return conexao;
    }

    /**
     * Método que verifica se login e senha existem no banco.
     * Retorna true se o usuário for validado.
     */
    public boolean verificarUsuario(String login, String senha) {

        // ✔ Query protegida contra SQL Injection
        String sql = "SELECT nome FROM usuarios WHERE login = ? AND senha = ?";

        // ✔ try-with-resources: fecha tudo automaticamente (conn, ps, rs)
        try (Connection conn = conectarBD();
             PreparedStatement ps = (conn != null ? conn.prepareStatement(sql) : null)) {

            // ✔ Verifica se conseguiu conectar antes de continuar
            if (conn == null) {
                System.out.println("Conexão não estabelecida!");
                return false;
            }

            // ✔ Preenche os parâmetros da query
            ps.setString(1, login);
            ps.setString(2, senha);

            // ✔ Executa a consulta
            try (ResultSet rs = ps.executeQuery()) {

                // ✔ Se encontrou o usuário, salva nome e valida login
                if (rs.next()) {
                    nome = rs.getString("nome");
                    usuarioValido = true;
                }
            }

        } catch (Exception e) {
            // ✔ Registra erro sem quebrar execução
            System.out.println("Erro ao verificar usuário: " + e.getMessage());
        }

        // ✔ Retorna se a autenticação foi bem-sucedida
        return usuarioValido;
    }

    public static void main(String[] args) {
        Corrigido app = new Corrigido();
        boolean valido = app.verificarUsuario("login_teste", "senha_teste");
        System.out.println("Usuário válido? " + valido);
    }
}
