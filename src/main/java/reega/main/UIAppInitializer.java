/**
 *
 */
package reega.main;

import java.util.function.Function;
import java.util.function.Supplier;

import javafx.scene.Parent;
import reega.auth.AuthManager;
import reega.auth.RemindableAuthManager;
import reega.controllers.*;
import reega.data.*;
import reega.data.remote.RemoteConnection;
import reega.io.IOController;
import reega.io.IOControllerFactory;
import reega.io.TokenIOController;
import reega.logging.ExceptionHandler;
import reega.logging.SimpleExceptionHandler;
import reega.statistics.DataPlotter;
import reega.statistics.DataPlotterImpl;
import reega.statistics.StatisticsController;
import reega.statistics.StatisticsControllerImpl;
import reega.util.ServiceCollection;
import reega.util.ServiceProvider;
import reega.views.*;
import reega.viewutils.DataTemplate;
import reega.viewutils.DataTemplateManager;
import reega.viewutils.Navigator;
import reega.viewutils.NavigatorImpl;

/**
 * App initializer for the UI main class
 *
 * @author Marco
 *
 */
public class UIAppInitializer implements AppInitializer {

    private ServiceProvider svcProvider;
    private boolean alreadyCalledInitialize;

    private UIAppInitializer() {

    }

    private static UIAppInitializer instance;

    /**
     * Get the only instance of the UI app initializer
     *
     * @return the only instance of the UI app initializer
     */
    public static synchronized UIAppInitializer getInstance() {
        if (UIAppInitializer.instance == null) {
            UIAppInitializer.instance = new UIAppInitializer();
        }
        return UIAppInitializer.instance;
    }

    /**
     * Build the service provider for the app
     *
     * @return the service provider for the app
     */
    private ServiceProvider buildServiceProvider() {
        final ServiceCollection svcCollection = new ServiceCollection();
        svcCollection.addSingleton(Navigator.class, (Function<ServiceProvider, Navigator>) NavigatorImpl::new);
        svcCollection.addSingleton(MasterController.class);
        svcCollection.addSingleton(AuthController.class, AuthControllerFactory.getDefaultAuthController(null));
        svcCollection.addSingleton(IOController.class, IOControllerFactory.getDefaultIOController());
        svcCollection.addSingleton(TokenIOController.class, IOControllerFactory.getDefaultTokenIOController());
        svcCollection.addSingleton(DataController.class,
                DataControllerFactory.getDefaultDataController(new RemoteConnection()));
        svcCollection.addSingleton(ExceptionHandler.class, SimpleExceptionHandler.class);
        svcCollection.addSingleton(reega.data.UserController.class, UserControllerFactory.getDefaultUserController(null));
        svcCollection.addSingleton(AuthManager.class, RemindableAuthManager.class);
        svcCollection.addSingleton(DataFetcher.class, DataFetcherImpl.class);
        svcCollection.addSingleton(OperatorDataFetcher.class, OperatorDataFetcherImpl.class);
        svcCollection.addSingleton(ContractFetcher.class, ContractFetcherImpl.class);
        svcCollection.addSingleton(OperatorContractFetcher.class, OperatorContractFetcherImpl.class);
        svcCollection.addTransient(StatisticsController.class, StatisticsControllerImpl.class);
        svcCollection.addTransient(DataPlotter.class, DataPlotterImpl.class);
        svcCollection.addTransient(LoginController.class, LoginControllerImpl.class);
        svcCollection.addTransient(RegistrationController.class, RegistrationControllerImpl.class);
        svcCollection.addTransient(MainController.class, (svcProvider) -> {
            final StatisticsController statisticsController = svcProvider
                    .getRequiredService(StatisticsController.class);
            final DataPlotter dataPlotter = svcProvider.getRequiredService(DataPlotter.class);
            final ExceptionHandler exceptionHandler = svcProvider.getRequiredService(ExceptionHandler.class);
            final DataFetcher dataFetcher = svcProvider.getRequiredService(DataFetcher.class);
            final ContractFetcher contractFetcher = svcProvider.getRequiredService(ContractFetcher.class);

            dataPlotter.setStatisticController(statisticsController);

            return new MainControllerImpl(statisticsController, dataPlotter, exceptionHandler, dataFetcher,
                    contractFetcher);
        });
        svcCollection.addTransient(OperatorMainController.class, (svcProvider) -> {
            final StatisticsController statisticsController = svcProvider
                    .getRequiredService(StatisticsController.class);
            final DataPlotter dataPlotter = svcProvider.getRequiredService(DataPlotter.class);
            final ExceptionHandler exceptionHandler = svcProvider.getRequiredService(ExceptionHandler.class);
            final OperatorDataFetcher dataFetcher = svcProvider.getRequiredService(OperatorDataFetcher.class);
            final OperatorContractFetcher contractFetcher = svcProvider.getRequiredService(OperatorContractFetcher.class);

            dataPlotter.setStatisticController(statisticsController);

            return new OperatorMainControllerImpl(statisticsController, dataPlotter, exceptionHandler, dataFetcher,
                    contractFetcher);
        });
        svcCollection.addTransient(SearchUserController.class, SearchUserControllerImpl.class);
        svcCollection.addTransient(UserProfileController.class, UserProfileControllerImpl.class);
        svcCollection.addSingleton(BaseLayoutView.class);
        return svcCollection.buildServiceProvider();
    }

    /**
     * Initialize the static template manager
     */
    private void initializeTemplateManager() {
        final DataTemplateManager templateManager = DataTemplateManager.instance;
        templateManager.addTemplate(new DataTemplate<RegistrationControllerImpl>() {
            @Override
            public Class<RegistrationControllerImpl> getDataObjectClass() {
                return RegistrationControllerImpl.class;
            }

            @Override
            public Supplier<? extends Parent> getControlFactory(final RegistrationControllerImpl controller) {
                return () -> new RegistrationView(controller);
            }
        });
        templateManager.addTemplate(new DataTemplate<LoginControllerImpl>() {
            @Override
            public Class<LoginControllerImpl> getDataObjectClass() {
                return LoginControllerImpl.class;
            }

            @Override
            public Supplier<? extends Parent> getControlFactory(final LoginControllerImpl controller) {
                return () -> new LoginView(controller);
            }
        });
        templateManager.addTemplate(new DataTemplate<MainControllerImpl>() {
            @Override
            public Class<MainControllerImpl> getDataObjectClass() {
                return MainControllerImpl.class;
            }

            @Override
            public Supplier<? extends Parent> getControlFactory(final MainControllerImpl controller) {
                return () -> new UserMainView(controller);
            }
        });
        templateManager.addTemplate(new DataTemplate<OperatorMainControllerImpl>() {

            @Override
            public Class<OperatorMainControllerImpl> getDataObjectClass() {
                return OperatorMainControllerImpl.class;
            }

            @Override
            public Supplier<? extends Parent> getControlFactory(OperatorMainControllerImpl controller) {
                return () -> new OperatorMainView(controller);
            }

        });
        templateManager.addTemplate(new DataTemplate<SearchUserControllerImpl>() {

            @Override
            public Class<SearchUserControllerImpl> getDataObjectClass() {
                return SearchUserControllerImpl.class;
            }

            @Override
            public Supplier<? extends Parent> getControlFactory(SearchUserControllerImpl controller) {
                return () -> new UserSearchView(controller);
            }
        });
        templateManager.addTemplate(new DataTemplate<UserProfileControllerImpl>() {

            @Override
            public Class<UserProfileControllerImpl> getDataObjectClass() {
                return UserProfileControllerImpl.class;
            }

            @Override
            public Supplier<? extends Parent> getControlFactory(UserProfileControllerImpl controller) {
                return () -> new UserProfileView(controller);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() throws Exception {
        if (this.alreadyCalledInitialize) {
            throw new IllegalStateException("The initialize() method has already been created");
        }
        this.initializeTemplateManager();
        this.svcProvider = this.buildServiceProvider();
        this.alreadyCalledInitialize = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceProvider getServiceProvider() {
        return this.svcProvider;
    }

}
