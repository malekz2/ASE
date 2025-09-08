package CollegeApp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 * @author malekzuhdi on 03/09/2025
 */
public class CollegeAppController {
    @FXML
    private CheckBox chkIB;
    @FXML
    private CheckBox chkAP;

    @FXML
    private RadioButton radNA;
    @FXML
    private RadioButton radEU;
    @FXML
    private RadioButton radAS;

    @FXML
    private ToggleGroup regionGroup;

    @FXML
    private TextArea txtResult;

    // Holds the current plan
    private CollegePlan currentPlan;

    /**
     * Called by the FXMLLoader after the fields are injected.
     */
    @FXML
    private void initialize() {
        // initialize model
        currentPlan = new CollegePlan("None", "Not selected");

        // wire checkbox listeners so the UI updates when user toggles them
        if (chkIB != null) {
            chkIB.selectedProperty().addListener((obs, oldV, newV) -> {
                updateProgramFromCheckboxes();
                updateResultDisplay();
            });
        }
        if (chkAP != null) {
            chkAP.selectedProperty().addListener((obs, oldV, newV) -> {
                updateProgramFromCheckboxes();
                updateResultDisplay();
            });
        }

        // wire toggle group listener (preferred way to track radio selection)
        if (regionGroup != null) {
            regionGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                @Override
                public void changed(ObservableValue<? extends Toggle> obs, Toggle oldToggle, Toggle newToggle) {
                    if (newToggle == null) {
                        // no selection
                        currentPlan.setTargetLocation("Not selected");
                    } else if (newToggle == radNA) {
                        currentPlan.setTargetLocation("North America");
                    } else if (newToggle == radEU) {
                        currentPlan.setTargetLocation("Europe");
                    } else if (newToggle == radAS) {
                        currentPlan.setTargetLocation("Asia");
                    } else {
                        // fallback: try to infer text if possible
                        Object userObj = newToggle.getUserData();
                        if (userObj instanceof String) {
                            currentPlan.setTargetLocation((String) userObj);
                        } else {
                            currentPlan.setTargetLocation("Unknown");
                        }
                    }
                    updateResultDisplay();
                }
            });

            // Optionally set a sensible default (choose North America by default)
            if (radNA != null) {
                regionGroup.selectToggle(radNA);
            }
        }

        // ensure program field matches initial checkbox states
        updateProgramFromCheckboxes();
        updateResultDisplay();
    }

    /**
     * Called by FXML when IB checkbox is clicked (keeps backward compatibility with onAction handlers).
     */
    @FXML
    public void chkIBSelect() {
        updateProgramFromCheckboxes();
        updateResultDisplay();
    }

    /**
     * Called by FXML when AP checkbox is clicked (keeps backward compatibility with onAction handlers).
     */
    @FXML
    public void chkAPselect() {
        updateProgramFromCheckboxes();
        updateResultDisplay();
    }

    /**
     * The radio onAction handlers are retained for compatibility with the FXML.
     * They simply select the corresponding toggle in the group and update the model.
     */
    @FXML
    public void radNASelect() {
        if (regionGroup != null && radNA != null) {
            regionGroup.selectToggle(radNA);
            // listener on the toggle group will update the model and UI
        }
    }

    @FXML
    public void radEUSelect() {
        if (regionGroup != null && radEU != null) {
            regionGroup.selectToggle(radEU);
        }
    }

    @FXML
    public void radASSelect() {
        if (regionGroup != null && radAS != null) {
            regionGroup.selectToggle(radAS);
        }
    }

    /**
     * Read the checkbox states and update the programEnrolled field in the model.
     */
    private void updateProgramFromCheckboxes() {
        StringBuilder sb = new StringBuilder();
        if (chkIB != null && chkIB.isSelected()) {
            sb.append("IB");
        }
        if (chkAP != null && chkAP.isSelected()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("AP");
        }
        String program = sb.length() > 0 ? sb.toString() : "None";
        currentPlan.setProgramEnrolled(program);
    }

    /**
     * Push the current model state to the TextArea in a readable format.
     */
    private void updateResultDisplay() {
        if (txtResult == null || currentPlan == null) return;

        StringBuilder out = new StringBuilder();
        out.append("College Plan Summary\n");
        out.append("---------------------\n");
        out.append("Program(s) enrolled: ").append(currentPlan.getProgramEnrolled()).append("\n");
        out.append("Target location: ").append(currentPlan.getTargetLocation()).append("\n\n");
        out.append("Raw model:\n").append(currentPlan.toString());

        txtResult.setText(out.toString());
    }
}
