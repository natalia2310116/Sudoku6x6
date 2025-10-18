module org.example.sudoku6x6 {
    requires javafx.controls;
    requires javafx.fxml;

    // Exportar paquetes necesarios
    opens org.example.sudoku6x6.Controller to javafx.fxml;
    opens org.example.sudoku6x6.Main to javafx.graphics;

    exports org.example.sudoku6x6.Main;
    exports org.example.sudoku6x6.Controller;
    exports org.example.sudoku6x6.Model;
}