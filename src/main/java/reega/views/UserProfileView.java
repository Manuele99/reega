package reega.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import org.apache.commons.lang3.StringUtils;
import reega.controllers.UserProfileController;
import reega.users.User;
import reega.viewcomponents.Card;
import reega.viewcomponents.FlexibleGridPane;
import reega.viewcomponents.WrappableLabel;
import reega.viewutils.ViewUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class UserProfileView extends VBox {

    @FXML
    private WrappableLabel userName;
    @FXML
    private WrappableLabel userSurname;
    @FXML
    private WrappableLabel userRole;
    @FXML
    private WrappableLabel userEmail;
    @FXML
    private WrappableLabel userFiscalCode;
    @FXML
    private FlexibleGridPane userContracts;

    public UserProfileView(UserProfileController controller) {
        final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("views/UserProfile.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        this.setUserProperties(controller.getUser(), controller);
    }

    private void setUserProperties(User user, UserProfileController controller) {
        this.userName.setText("Name: " + StringUtils.capitalize(user.getName()));
        this.userSurname.setText("Surname: " + StringUtils.capitalize(user.getSurname()));
        this.userRole.setText("Role: " + StringUtils.capitalize(user.getRole().getRoleName()));
        this.userEmail.setText("Email: " + user.getEmail());
        this.userFiscalCode.setText("Fiscal code: " + user.getFiscalCode());
        this.userContracts.getChildren().clear();
        this.userContracts.getChildren().addAll(controller.getUserContracts().stream().map(contract -> {
            Card contractCard = ViewUtils.wrapNodeWithStyleClasses(new Card(), "contract-card");
            WrappableLabel contractAddress = ViewUtils.wrapNodeWithStyleClasses(new WrappableLabel("Address: " + contract.getAddress()), "contract-label");
            String contractServicesString = contract.getServices().stream().map(svcType -> StringUtils.capitalize(svcType.getName())).collect(Collectors.joining(", "));
            WrappableLabel contractServices = ViewUtils.wrapNodeWithStyleClasses(new WrappableLabel("Services: " + contractServicesString), "contract-label");
            String contractStartDateString = new SimpleDateFormat("yyyy/MM/dd").format(contract.getStartDate());
            WrappableLabel contractStartDate = ViewUtils.wrapNodeWithStyleClasses(new WrappableLabel("Start date: " + contractStartDateString), "contract-label");
            contractCard.getChildren().addAll(contractAddress, contractServices, contractStartDate);
            return contractCard;
        }).collect(Collectors.toList()));
    }
}
