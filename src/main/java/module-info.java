module ase25.ase {
    requires javafx.controls;
    requires javafx.fxml;


    opens ase25.ase to javafx.fxml;
    exports ase25.ase;
}