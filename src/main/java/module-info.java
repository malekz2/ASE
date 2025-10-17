module ase25.ase {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;
    requires java.desktop;


    opens ase25.ase to javafx.fxml;
    exports ase25.ase;

    opens CollegeApp to javafx.fxml;
    exports CollegeApp;

    exports showhide;
    opens showhide to javafx.fxml;

    opens piggyBank to javafx.fxml;
    exports piggyBank;

    opens billCoinGenerator to javafx.fxml;
    exports billCoinGenerator;

    opens pizzaApp to javafx.fxml;
    exports pizzaApp;

    opens productivityApp to javafx.fxml;
    exports productivityApp;

    opens textOrNumber to javafx.fxml;
    exports textOrNumber;

    opens gradebook to javafx.fxml;
    exports gradebook;

    opens app to javafx.fxml;
    exports app;

}