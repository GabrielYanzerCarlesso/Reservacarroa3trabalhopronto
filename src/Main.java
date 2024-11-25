import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Classe base Modelo
class Modelo {
    String nome;
    int ano;

    public Modelo(String nome, int ano) {
        this.nome = nome;
        this.ano = ano;
    }
}

// Classe base Veiculo
abstract class Veiculo extends Modelo {
    String placa;
    double diaria;
    boolean reservado;
    Agencia agencia; // Agência vinculada

    public Veiculo(String nome, int ano, String placa, double diaria) {
        super(nome, ano);
        this.placa = placa;
        this.diaria = diaria;
        this.reservado = false;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }
}

// Classe Carro
class Carro extends Veiculo {
    public Carro(String nome, int ano, String placa, double diaria) {
        super(nome, ano, placa, diaria);
    }
}

// Classe Moto
class Moto extends Veiculo {
    public Moto(String nome, int ano, String placa, double diaria) {
        super(nome, ano, placa, diaria);
    }
}

// Classe Pagamento
class Pagamento {
    public static double calcular(Veiculo veiculo, LocalDate dataInicio, LocalDate dataFim) {
        long dias = ChronoUnit.DAYS.between(dataInicio, dataFim) + 1; // Incluir o dia inicial
        return veiculo.diaria * dias;
    }

    public static void pagar(double valorTotal) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Escolha a forma de pagamento:");
        System.out.println("1. À vista");
        System.out.println("2. Parcelado");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1 -> System.out.printf("Pagamento à vista realizado: R$ %.2f%n", valorTotal);
            case 2 -> {
                System.out.print("Digite o número de parcelas: ");
                int parcelas = scanner.nextInt();
                double valorParcelado = valorTotal / parcelas;
                System.out.printf("Pagamento parcelado em %d vezes de R$ %.2f%n", parcelas, valorParcelado);
            }
            default -> System.out.println("Opção inválida.");
        }
    }
}

// Classe Agencia
class Agencia {
    String nome;
    String endereco;
    List<Veiculo> veiculos = new ArrayList<>();

    public Agencia(String nome, String endereco) {
        this.nome = nome;
        this.endereco = endereco;
    }
}

// Classe Usuario
class Usuario {
    String nome;
    String email;
    String senha;
    String cnh; // Adicionado CNH
    List<Veiculo> veiculosReservados = new ArrayList<>();

    public Usuario(String nome, String email, String senha, String cnh) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cnh = cnh;
    }
}

public class Main {
    private static final List<Usuario> usuarios = new ArrayList<>();
    private static final List<Veiculo> veiculos = new ArrayList<>();
    private static final List<Agencia> agencias = new ArrayList<>();
    private static Usuario usuarioLogado = null;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("=======================================================");
            System.out.println("1 - Cadastrar Usuário 🙋‍♂️");
            System.out.println("2 - Login 🔐");
            System.out.println("3 - Cadastrar Veículo 🚗🏍️");
            System.out.println("4 - Criar Agência 🏢");
            System.out.println("5 - Vincular Veículo a Agência 🔗");
            System.out.println("6 - Reservar Veículo 🚗🏍️");
            System.out.println("7 - Ver Veículos Reservados 🚗🏍️");
            System.out.println("8 - Sair 🖐️");
            System.out.println("=======================================================");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir nova linha

            switch (opcao) {
                case 1 -> cadastrarUsuario();
                case 2 -> login();
                case 3 -> cadastrarVeiculo();
                case 4 -> criarAgencia();
                case 5 -> vincularVeiculoAgencia();
                case 6 -> reservarVeiculo();
                case 7 -> verVeiculosReservados();
                case 8 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 8);
    }

    private static void cadastrarUsuario() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("CNH: ");
        String cnh = scanner.nextLine();
        usuarios.add(new Usuario(nome, email, senha, cnh));
        System.out.println("Usuário cadastrado com sucesso!");
    }

    private static void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        usuarioLogado = usuarios.stream()
                .filter(u -> u.email.equals(email) && u.senha.equals(senha))
                .findFirst().orElse(null);
        if (usuarioLogado != null) {
            System.out.println("Login realizado com sucesso!");
        } else {
            System.out.println("Credenciais inválidas.");
        }
    }

    private static void cadastrarVeiculo() {
        if (verificarLogin()) return;

        System.out.println("1 - Cadastrar Carro");
        System.out.println("2 - Cadastrar Moto");
        System.out.print("Escolha o tipo de veículo: ");
        int tipo = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha

        System.out.print("Nome do Modelo: ");
        String nomeModelo = scanner.nextLine();
        System.out.print("Ano do Modelo: ");
        int anoModelo = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha

        System.out.print("Placa: ");
        String placa = scanner.nextLine();
        System.out.print("Valor da diária: ");
        double diaria = scanner.nextDouble();

        if (tipo == 1) {
            veiculos.add(new Carro(nomeModelo, anoModelo, placa, diaria));
            System.out.println("Carro cadastrado com sucesso!");
        } else if (tipo == 2) {
            veiculos.add(new Moto(nomeModelo, anoModelo, placa, diaria));
            System.out.println("Moto cadastrada com sucesso!");
        } else {
            System.out.println("Tipo de veículo inválido.");
        }
    }

    private static void criarAgencia() {
        System.out.print("Nome da Agência: ");
        String nome = scanner.nextLine();
        System.out.print("Endereço da Agência: ");
        String endereco = scanner.nextLine();
        agencias.add(new Agencia(nome, endereco));
        System.out.println("Agência criada com sucesso!");
    }

    private static void vincularVeiculoAgencia() {
        System.out.println("Escolha uma agência:");
        for (int i = 0; i < agencias.size(); i++) {
            System.out.printf("%d - %s (%s)%n", i + 1, agencias.get(i).nome, agencias.get(i).endereco);
        }
        int escolhaAgencia = scanner.nextInt() - 1;
        if (escolhaAgencia < 0 || escolhaAgencia >= agencias.size()) {
            System.out.println("Agência inválida.");
            return;
        }

        Agencia agencia = agencias.get(escolhaAgencia);

        System.out.println("Escolha um veículo:");
        for (int i = 0; i < veiculos.size(); i++) {
            Veiculo veiculo = veiculos.get(i);
            System.out.printf("%d - %s (%d) - Placa: %s%n", i + 1, veiculo.nome, veiculo.ano, veiculo.placa);
        }
        int escolhaVeiculo = scanner.nextInt() - 1;
        if (escolhaVeiculo < 0 || escolhaVeiculo >= veiculos.size()) {
            System.out.println("Veículo inválido.");
            return;
        }

        Veiculo veiculo = veiculos.get(escolhaVeiculo);
        veiculo.setAgencia(agencia);
        agencia.veiculos.add(veiculo);
        System.out.println("Veículo vinculado à agência com sucesso!");
    }

    private static void reservarVeiculo() {
        if (verificarLogin()) return;

        System.out.println("Veículos disponíveis:");
        List<Veiculo> disponiveis = veiculos.stream().filter(v -> !v.reservado).toList();
        if (disponiveis.isEmpty()) {
            System.out.println("Nenhum veículo disponível para reserva.");
            return;
        }

        for (int i = 0; i < disponiveis.size(); i++) {
            Veiculo v = disponiveis.get(i);
            System.out.printf("%d. Modelo: %s (%d) | Placa: %s | Diária: R$ %.2f%n", i + 1, v.nome, v.ano, v.placa, v.diaria);
        }

        System.out.print("Escolha um veículo: ");
        int escolha = scanner.nextInt() - 1;
        if (escolha < 0 || escolha >= disponiveis.size()) {
            System.out.println("Escolha inválida.");
            return;
        }

        Veiculo veiculo = disponiveis.get(escolha);

        System.out.print("Data de início (dd/MM/yyyy): ");
        LocalDate dataInicio = LocalDate.parse(scanner.next(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.print("Data de fim (dd/MM/yyyy): ");
        LocalDate dataFim = LocalDate.parse(scanner.next(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        double valorTotal = Pagamento.calcular(veiculo, dataInicio, dataFim);
        Pagamento.pagar(valorTotal);

        veiculo.reservado = true;
        usuarioLogado.veiculosReservados.add(veiculo);

        System.out.printf("Retire seu veículo na agência %s, localizada em %s, no dia %s.%n",
                veiculo.agencia.nome, veiculo.agencia.endereco, dataInicio);
        System.out.printf("Devolva seu veículo até as 18:00 na agência %s, localizada em %s, no dia %s.%n",
                veiculo.agencia.nome, veiculo.agencia.endereco, dataFim);
    }

    private static void verVeiculosReservados() {
        if (verificarLogin()) return;

        if (usuarioLogado.veiculosReservados.isEmpty()) {
            System.out.println("Nenhum veículo reservado.");
        } else {
            System.out.println("Veículos Reservados:");
            usuarioLogado.veiculosReservados.forEach(v ->
                    System.out.printf("Modelo: %s (%d) | Placa: %s%n", v.nome, v.ano, v.placa));
        }
    }

    private static boolean verificarLogin() {
        if (usuarioLogado == null) {
            System.out.println("Por favor, faça login primeiro.");
            return true;
        }
        return false;
    }
}
