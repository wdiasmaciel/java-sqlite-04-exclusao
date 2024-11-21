import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Conectar ao banco de dados SQLite:
            connection = DriverManager.getConnection("jdbc:sqlite:teste.db");
            System.out.println("Conexão com SQLite estabelecida!");

            // Criar uma tabela (usuario):
            String createTableSQL = 
            """
                CREATE TABLE IF NOT EXISTS usuario (
                    id INTEGER PRIMARY KEY AUTOINCREMENT, 
                    nome VARCHAR(256) NOT NULL, 
                    nascimento TEXT
                );
            """;
            // Criar e executar uma declaração SQL:
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
            System.out.println("Tabela 'usuario' criada ou já existe!");

            // Inserir dados na tabela 'usuario':
            String insertSQL = 
            """
                INSERT INTO usuario (nome, nascimento) VALUES 
                ('Ana', '2000-06-03'), 
                ('Bruna', '2001-02-17'),
                ('Carlos', '2002-04-21'),
                ('Daniel', '2003-10-30');
            """;
            statement.execute(insertSQL);
            System.out.println("Dados inseridos na tabela 'usuario'!");

            // Consultar dados da tabela 'usuario':
            String selectSQL = "SELECT * FROM usuario;";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + 
                                   ", Nome: " + resultSet.getString("nome") +
                                   ", Nascimento: " + resultSet.getString("nascimento"));
            }

            // Excluir registros no banco de dados:
            String deleteSQL = "DELETE FROM usuario WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);

            // Estrutura de repetição para excluir usuários:
            String continuar;
            do {
                // Pedir o ID do usuário que se deseja excluir:
                System.out.print("Informe o ID do usuário que se deseja excluir: ");
                int id = Integer.parseInt(scanner.nextLine());

                // Definir o valor no PreparedStatement:
                preparedStatement.setInt(1, id);
                int rowsDeleted = preparedStatement.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Usuário excluído com sucesso!");
                } else {
                    System.out.println("Nenhum usuário encontrado com o ID especificado.");
                }

                // Perguntar se deseja excluir outro usuário:
                System.out.print("Deseja excluir outro usuário? (sim/não): ");
                continuar = scanner.nextLine();
            } while (continuar.equalsIgnoreCase("sim"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (scanner != null) {
                    scanner.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
