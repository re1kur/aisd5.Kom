public class City {
    int id;
    String name;
    double latitude;
    double longitude;

    public City(int id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude / 100.0;
        this.longitude = longitude / 100.0;
    }
}
