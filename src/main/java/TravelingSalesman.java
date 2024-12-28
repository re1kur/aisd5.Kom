import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TravelingSalesman {

    private static final String INPUT_FILE = "src/main/resources/data.csv";
    private static final String OUTPUT_FILE = "src/main/resources/solution.csv";
    private static final int START_CITY_ID = 3753;

    public static void main(String[] args) {
        List<City> cities = readCitiesFromFile(INPUT_FILE);
        List<String> distances = findShortestRoute(cities);
        writeDistancesToFile(distances, OUTPUT_FILE);

        System.out.println(calculateTotalDistance(distances));
    }

    private static List<City> readCitiesFromFile(String filename) {
        List<City> cities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int id = Integer.parseInt(values[0]);
                String name = values[3];
                double latitude = Integer.parseInt(values[5]);
                double longitude = Integer.parseInt(values[6]);
                cities.add(new City(id, name, latitude, longitude));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cities;
    }

    private static List<String> findShortestRoute(List<City> cities) {
        List<String> distances = new ArrayList<>();
        int startIndex = -1;

        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).id == START_CITY_ID) {
                startIndex = i;
                break;
            }
        }

        if (startIndex == -1) {
            throw new IllegalArgumentException("Start city not found");
        }

        boolean[] visited = new boolean[cities.size()];
        int currentCityIndex = startIndex;
        visited[currentCityIndex] = true;

        for (int i = 0; i < cities.size() - 1; i++) {
            double minDistance = Double.MAX_VALUE;
            int nextCityIndex = -1;

            for (int j = 0; j < cities.size(); j++) {
                if (!visited[j] && j != startIndex) {
                    double distance = euclideanDistance(
                            cities.get(currentCityIndex).latitude,
                            cities.get(currentCityIndex).longitude,
                            cities.get(j).latitude,
                            cities.get(j).longitude
                    );

                    if (distance < minDistance) {
                        minDistance = distance;
                        nextCityIndex = j;
                    }
                }
            }

            distances.add(currentCityIndex + ";" + minDistance);
            visited[nextCityIndex] = true;
            currentCityIndex = nextCityIndex;
        }

        double returnDistance = euclideanDistance(
                cities.get(currentCityIndex).latitude,
                cities.get(currentCityIndex).longitude,
                cities.get(startIndex).latitude,
                cities.get(startIndex).longitude
        );
        distances.add(startIndex + ";" + returnDistance);

        return distances;
    }

    private static double euclideanDistance(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2));
    }

    private static void writeDistancesToFile(List<String> distances, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("Step;Id;Predicted\n");
            for (int i = 0; i < distances.size(); i++) {
                String[] city = distances.get(i).split(";");
                double distance = Double.parseDouble(city[1]);
//                if (distance < 0) {
//                    distance = 0;
//                }
                bw.write(i + ";" + city[0] + ";" + distance + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static double calculateTotalDistance(List<String> distances) {
        double totalDistance = 0.0;
        for (String city : distances) {
            String[] split = city.split(";");
            double distance = Double.parseDouble(split[1]);
            totalDistance += distance;
        }
        return totalDistance;
    }
}