import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class ToyStore {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Создание игрушек
        PriorityQueue<Toy> toyQueue = new PriorityQueue<>((t1, t2) -> t2.weight - t1.weight);

        System.out.println("Введите данные игрушек в формате: id название вес");
        System.out.println("Пример: 1 Машинка 2");
        System.out.println("Для завершения ввода введите 'выход'");

        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equals("выход")) {
                break;
            }
            String[] parts = input.split(" ");
            if (parts.length != 3) {
                System.out.println("Неверный формат ввода. Повторите попытку.");
                continue;
            }
            String id = parts[0];
            String name = parts[1];
            int weight;
            try {
                weight = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат веса. Повторите попытку.");
                continue;
            }
            Toy toy = new Toy(id, name, weight);
            toyQueue.add(toy);
        }
        scanner.close();

        // Получение 10 игрушек из очереди и запись результатов в файл
        try {
            FileWriter writer = new FileWriter("output.txt");
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                Toy toy = getRandomToy(toyQueue, random);
                if (toy != null) {
                    writer.write("ID: " + toy.id + ", Название: " + toy.name + "\n");
                } else {
                    writer.write("Очередь пуста\n");
                }
            }
            writer.close();
            System.out.println("Результаты записаны в файл output.txt");
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    private static Toy getRandomToy(PriorityQueue<Toy> toyQueue, Random random) {
        int totalWeight = toyQueue.stream().mapToInt(t -> t.weight).sum();
        int randomWeight = random.nextInt(totalWeight);
        int currentWeight = 0;
        for (Toy toy : toyQueue) {
            currentWeight += toy.weight;
            if (randomWeight < currentWeight) {
                return toy;
            }
        }
        return null;
    }
}