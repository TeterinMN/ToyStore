import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ToyStore {
    private final List<Toy> toys;

    public ToyStore() {
        toys = new ArrayList<>();
    }

    public void addToy(int id, String name, int quantity, double weight) {
        toys.add(new Toy(id, name, quantity, weight));
    }

    public void updateWeight(int toyId, double weight) {
        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toy.setWeight(weight);
                return;
            }
        }
        System.out.println("Игрушка с id " + toyId + " не найдена.");
    }

    public Toy drawToy() {
        double totalWeight = toys.stream().mapToDouble(Toy::getWeight).sum();
        double randomWeight = Math.random() * totalWeight;
        double cumulativeWeight = 0;
        for (Toy toy : toys) {
            cumulativeWeight += toy.getWeight();
            if (randomWeight <= cumulativeWeight) {
                if (toy.getQuantity() > 0) {
                    toy.decreaseQuantity();
                    return toy;
                } else {
                    System.out.println("Игрушки " + toy.getName() + " нет в наличии.");
                    return null;
                }
            }
        }
        return null;
    }

    public void saveToyToFile(Toy toy) {
        try (FileWriter writer = new FileWriter("prize_toys.txt", true)) {
            writer.write("Игрушка: " + toy.getName() + "\n");
        } catch (IOException e) {
            System.out.println("Произошла ошибка при сохранении в файл.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ToyStore toyStore = new ToyStore();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите данные игрушки (идентификатор, название, количество, вес) " +
                "или 'выход' чтобы закончить:");
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("выход")) {
                break;
            }
            String[] tokens = input.split(",");
            if (tokens.length != 4) {
                System.out.println("Неверный формат ввода. Пожалуйста, введите идентификатор, имя, количество, вес через запятую.");
                continue;
            }
            try {
                int id = Integer.parseInt(tokens[0].trim());
                String name = tokens[1].trim();
                int quantity = Integer.parseInt(tokens[2].trim());
                double weight = Double.parseDouble(tokens[3].trim());
                toyStore.addToy(id, name, quantity, weight);
            } catch (NumberFormatException e) {
                System.out.println("Недопустимый формат номера. Пожалуйста, введите действительные номера.");
            }
        }

        Toy drawnToy = toyStore.drawToy();
        if (drawnToy != null) {
            System.out.println("Поздравляю! Вы выиграли " + drawnToy.getName());
            toyStore.saveToyToFile(drawnToy);
        } else {
            System.out.println("Извините, игрушек для розыгрыша нет.");
        }

        scanner.close();
    }
}