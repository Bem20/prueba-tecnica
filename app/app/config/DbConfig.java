package app.config;

public final class DbConfig {

    public static final String HOST = "localhost";
    public static final int PORT = 5432;
    public static final String DATABASE = "prueba_tecnica";
    public static final String USER = "postgres";
    public static final String PASSWORD = "1234";

    public static final String URL =
            "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE;

    private DbConfig() {

    }
}