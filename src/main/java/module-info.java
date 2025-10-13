module org.example.sudoku6x6 {
    requires javafx.controls;
    requires javafx.fxml;

    // Abrir los paquetes que contienen controladores para FXML
    opens org.example.sudoku6x6.Controller to javafx.fxml;
    opens org.example.sudoku6x6.View to javafx.fxml;

    // Exportar los paquetes necesarios para que JavaFX pueda encontrarlos
    exports org.example.sudoku6x6.Main;
    exports org.example.sudoku6x6.Model;
    exports org.example.sudoku6x6.View;
}
