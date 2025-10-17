package pizzaApp;

import app.SceneNavigator;
import app.SecurityContext;
import gradebook.models.UserRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class ProducerController {

    @FXML private TableView<OrderEntity> tblOrders;
    @FXML private TableColumn<OrderEntity, Integer> colId;
    @FXML private TableColumn<OrderEntity, String> colUser;
    @FXML private TableColumn<OrderEntity, String> colSize;
    @FXML private TableColumn<OrderEntity, String> colCrust;
    @FXML private TableColumn<OrderEntity, String> colToppings;
    @FXML private TableColumn<OrderEntity, Integer> colQty;
    @FXML private TableColumn<OrderEntity, Double> colTotal;
    @FXML private TableColumn<OrderEntity, String> colWhen;
    @FXML private TableColumn<OrderEntity, Void> colActions;

    private final OrderService orderService = new OrderService();
    private final ObservableList<OrderEntity> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Only producers allowed
        if (SecurityContext.get() == null || SecurityContext.get().getRole() != UserRole.PRODUCER) {
            new Alert(Alert.AlertType.ERROR) {{ setTitle("Access Denied"); setHeaderText(null); setContentText("Login as PRODUCER to access."); }}.showAndWait();
            try { SceneNavigator.goToLogin(); } catch (Exception ignored) {}
            return;
        }

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colCrust.setCellValueFactory(new PropertyValueFactory<>("crust"));
        colToppings.setCellValueFactory(new PropertyValueFactory<>("toppings"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colWhen.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        addActionButtons();
        tblOrders.setItems(data);
        refresh();
    }

    private void addActionButtons() {
        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnDone = new Button("Complete");
            private final Button btnCancel = new Button("Cancel");
            private final HBox box = new HBox(8, btnDone, btnCancel);

            {
                btnDone.setOnAction(e -> {
                    OrderEntity order = getTableView().getItems().get(getIndex());
                    orderService.updateStatus(order.getId(), OrderStatus.COMPLETED);
                    refresh();
                });
                btnCancel.setOnAction(e -> {
                    OrderEntity order = getTableView().getItems().get(getIndex());
                    orderService.updateStatus(order.getId(), OrderStatus.CANCELLED);
                    refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void refresh() {
        data.setAll(orderService.listActiveOrders());
    }

    @FXML private void onRefresh() { refresh(); }

    @FXML private void onLogout() {
        SecurityContext.clear();
        try { SceneNavigator.goToLogin(); } catch (Exception ignored) {}
    }
}
