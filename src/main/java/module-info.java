module ase25.ase {
    requires javafx.controls;
    requires javafx.fxml;


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

}