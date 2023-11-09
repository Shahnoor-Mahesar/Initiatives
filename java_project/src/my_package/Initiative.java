package my_package;

public class Initiative {
    int id;
    String name;
    String date;
    String time;
    String status;
    int userId;

    public Initiative(int id, String name, String date, String time,
            String status, int userId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.status = status;
        this.userId = userId;
    }
}
