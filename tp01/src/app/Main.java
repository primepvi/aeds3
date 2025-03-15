package app;

import io.DataIO;
import io.SequentialDataIO;
import models.Movie;
import utils.*;
import parsers.*;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DataIO dataIO = new SequentialDataIO("res/movies.db");

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            makeMainMenu();
            System.out.print("Escolha uma opção: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                running = processMenuChoice(choice);
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
                scanner.nextLine();
            }
        }
    }

    private static boolean processMenuChoice(int choice) {
        switch (choice) {
            case 1 -> loadRegistries();
            case 2 -> createRegistry();
            case 3 -> updateRegistry();
            case 4 -> deleteRegistry();
            case 5 -> searchRegistry();
            case 6 -> sortRegistries();
            case 0 -> {
                System.out.println("Saindo...");
                return false;
            }
            default -> System.out.println("Opção inválida.");
        }
        return true;
    }

    private static void loadRegistries() {
        System.out.println("Fazendo carga de dados...");
        CsvParser.parse("res/dataset.csv", dataIO);
        System.out.println("Carga feita com sucesso...");
    }

    private static void sortRegistries() {
        System.out.println("Digite a quantidade de caminhos:");
        int pathCount = scanner.nextInt();
        System.out.println("Digite a quantidade maxima de registros carregados por operação:");
        int registryCount = scanner.nextInt();
        System.out.println("Ordenando registros...");
        ExternOrdinate ordinator = new ExternOrdinate(dataIO, pathCount, registryCount);
        try {
            System.out.println("Distribuindo registros...");
            ordinator.distribute();
            System.out.println("Intercalando registros...");
            ordinator.intercalate();
            System.out.println("Os registros foram ordenados com sucesso.");
            dataIO.setPath("res/new_movies.db");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void makeMainMenu() {
        System.out.println("====== Menu ======");
        System.out.println("1 - Fazer carga de dados.");
        System.out.println("2 - Criar registro.");
        System.out.println("3 - Editar registro.");
        System.out.println("4 - Deletar registro.");
        System.out.println("5 - Procurar registro.");
        System.out.println("6 - Ordenar registros.");
        System.out.println("0 - Sair.");
        System.out.println("=================");
    }

    private static void createRegistry() {
        System.out.println("Criando um novo registro...");

        Movie movie = new Movie();

        System.out.print("Nome do filme: ");
        movie.setMovieName(scanner.nextLine());

        System.out.print("Home Page: ");
        movie.setHomePage(scanner.nextLine());

        System.out.print("Data de lançamento (AAAA-MM-DD): ");
        movie.setReleaseDate(LocalDate.parse(scanner.nextLine()));

        System.out.print("Duração (minutos): ");
        movie.setRuntimeMinutes(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Gêneros (separados por vírgula): ");
        movie.setGenres(scanner.nextLine().split(","));

        System.out.print("Elenco (separados por vírgula): ");
        movie.setCast(scanner.nextLine().split(","));

        System.out.print("Idioma original (separados por vírgula): ");
        movie.setOriginalLanguage(scanner.nextLine().split(","));

        System.out.print("Sinopse: ");
        movie.setOverview(scanner.nextLine());

        System.out.print("História (Story Line): ");
        movie.setStoryLine(scanner.nextLine());

        System.out.print("Tagline: ");
        movie.setTagLine(scanner.nextLine());

        System.out.print("Empresas de produção (separadas por vírgula): ");
        movie.setProductionCompany(scanner.nextLine().split(","));

        System.out.print("Avaliação média: ");
        movie.setVoteAverage(scanner.nextFloat());
        scanner.nextLine();

        System.out.print("Quantidade de votos: ");
        movie.setVoteCount(scanner.nextLine());

        System.out.print("Orçamento (USD): ");
        movie.setBudgetUsd(scanner.nextLine());

        System.out.print("Receita: ");
        movie.setRevenue(scanner.nextLine());

        System.out.print("País de lançamento: ");
        movie.setReleaseCountry(scanner.nextLine());

        Document doc = new Document(movie);
        BinaryDocument createdDocument = (BinaryDocument) dataIO.createRegistry(doc);
        System.out.println("Registro criado com sucesso!");
        System.out.println("=====================");
        createdDocument.getMovie().write();
    }

    private static void updateRegistry() {
        System.out.print("Informe o ID do registro a ser atualizado: ");
        try {
            short id = scanner.nextShort();
            scanner.nextLine();
            Document existingDoc = dataIO.getRegistry(id);
            if (existingDoc == null) {
                System.out.println("Registro não encontrado.");
                return;
            }
            makeUpdateMenu();
            System.out.print("Escolha um campo para editar: ");
            int option = scanner.nextInt();
            scanner.nextLine();
            processUpdateOption(option, existingDoc);
            dataIO.updateRegistry(id, existingDoc);
            System.out.println("Registro atualizado com sucesso!");
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Certifique-se de inserir um número.");
            scanner.nextLine();
        }
    }

    private static void makeUpdateMenu() {
        System.out.println("====== Editar Registro ======");
        System.out.println("1 - Nome do Filme");
        System.out.println("2 - Home Page");
        System.out.println("3 - Data de Lançamento");
        System.out.println("4 - Duração");
        System.out.println("5 - Gêneros");
        System.out.println("6 - Elenco");
        System.out.println("7 - Idioma Original");
        System.out.println("8 - Sinopse");
        System.out.println("9 - História (Story Line)");
        System.out.println("10 - Tagline");
        System.out.println("11 - Empresas de Produção");
        System.out.println("12 - Avaliação Média");
        System.out.println("13 - Quantidade de Votos");
        System.out.println("14 - Orçamento (USD)");
        System.out.println("15 - Receita");
        System.out.println("16 - País de Lançamento");
        System.out.println("0 - Cancelar");
        System.out.println("=============================");
    }

    private static void processUpdateOption(int option, Document doc) {
        Movie movie = doc.getMovie();
        switch (option) {
            case 1 -> {
                System.out.print("Novo nome do filme: ");
                movie.setMovieName(scanner.nextLine());
            }
            case 2 -> {
                System.out.print("Nova home page: ");
                movie.setHomePage(scanner.nextLine());
            }
            case 3 -> {
                System.out.print("Nova data de lançamento (AAAA-MM-DD): ");
                movie.setReleaseDate(LocalDate.parse(scanner.nextLine()));
            }
            case 4 -> {
                System.out.print("Nova duração (minutos): ");
                movie.setRuntimeMinutes(scanner.nextInt());
                scanner.nextLine();
            }
            case 5 -> {
                System.out.print("Novos gêneros (separados por vírgula): ");
                movie.setGenres(scanner.nextLine().split(","));
            }
            case 6 -> {
                System.out.print("Novo elenco (separados por vírgula): ");
                movie.setCast(scanner.nextLine().split(","));
            }
            case 7 -> {
                System.out.print("Novo idioma original (separados por vírgula): ");
                movie.setOriginalLanguage(scanner.nextLine().split(","));
            }
            case 8 -> {
                System.out.print("Nova sinopse: ");
                movie.setOverview(scanner.nextLine());
            }
            case 9 -> {
                System.out.print("Nova história (Story Line): ");
                movie.setStoryLine(scanner.nextLine());
            }
            case 10 -> {
                System.out.print("Nova tagline: ");
                movie.setTagLine(scanner.nextLine());
            }
            case 11 -> {
                System.out.print("Novas empresas de produção (separadas por vírgula): ");
                movie.setProductionCompany(scanner.nextLine().split(","));
            }
            case 12 -> {
                System.out.print("Nova avaliação média: ");
                movie.setVoteAverage(scanner.nextFloat());
                scanner.nextLine();
            }
            case 13 -> {
                System.out.print("Nova quantidade de votos: ");
                movie.setVoteCount(scanner.nextLine());
            }
            case 14 -> {
                System.out.print("Novo orçamento (USD): ");
                movie.setBudgetUsd(scanner.nextLine());
            }
            case 15 -> {
                System.out.print("Nova receita: ");
                movie.setRevenue(scanner.nextLine());
            }
            case 16 -> {
                System.out.print("Novo país de lançamento: ");
                movie.setReleaseCountry(scanner.nextLine());
            }
            case 0 -> System.out.println("Edição cancelada.");
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void deleteRegistry() {
        System.out.print("Informe o ID do registro a ser deletado: ");
        try {
            short id = scanner.nextShort();
            scanner.nextLine();
            Document deleted = dataIO.deleteRegistry(id);
            if (deleted == null) {
                System.out.println("Registro não encontrado.");
            } else {
                System.out.println("Registro deletado com sucesso!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Certifique-se de inserir um número.");
            scanner.nextLine();
        }
    }

    private static void searchRegistry() {
        System.out.print("Informe o ID do registro a ser buscado: ");
        try {
            short id = scanner.nextShort();
            scanner.nextLine();
            Document doc = dataIO.getRegistry(id);
            if (doc == null) {
                System.out.println("Registro não encontrado.");
            } else {
                System.out.println("Registro encontrado: ");
                System.out.println("=====================");
                doc.getMovie().write();
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Certifique-se de inserir um número.");
            scanner.nextLine();
        }
    }
}
