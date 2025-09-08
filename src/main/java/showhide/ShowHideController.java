package showhide;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;

public class ShowHideController {

    @FXML
    private Button btnShowHide;

    @FXML
    private Rectangle rect;

    @FXML
    private Ellipse ellipse1;

    @FXML
    private Ellipse ellipse2;

    @FXML
    private QuadCurve mouth1;

    @FXML
    private QuadCurve mouth2;

    private boolean visible = true;

    @FXML
    private void btnShowHideClck() {
        visible = !visible; // toggle state

        rect.setVisible(visible);
        ellipse1.setVisible(visible);
        ellipse2.setVisible(visible);
        mouth1.setVisible(visible);
        mouth2.setVisible(visible);

        btnShowHide.setText(visible ? "HIDE" : "SHOW");
    }
}
