package org.springframework.roo.addon.jms;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.classpath.ModuleFeatureName;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.converters.JavaPackageConverter;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CliOptionAutocompleteIndicator;
import org.springframework.roo.shell.CommandMarker;
import org.springframework.roo.shell.Completion;
import org.springframework.roo.shell.Converter;
import org.springframework.roo.shell.ShellContext;
import org.springframework.roo.shell.converters.StaticFieldConverter;
import org.springframework.roo.support.osgi.ServiceInstaceManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Commands for the 'JMS' add-on to be used by the ROO shell.
 *
 * @author Stefan Schmidt
 * @author Manuel Iborra
 * @since 1.0
 */
@Component
@Service
public class JmsCommands implements CommandMarker {

  @Reference
  private JmsOperations jmsOperations;
  @Reference
  private StaticFieldConverter staticFieldConverter;
  @Reference
  private TypeLocationService typeLocationService;
  @Reference
  private ProjectOperations projectOperations;
  @Reference
  private PathResolver pathResolver;

  private ServiceInstaceManager serviceManager = new ServiceInstaceManager();

  // ------------ OSGi component attributes ----------------
  private BundleContext context;

  protected void activate(final ComponentContext context) {
    this.context = context.getBundleContext();
    this.serviceManager.activate(this.context);
  }

  protected void deactivate(final ComponentContext context) {
    this.context = null;
    this.serviceManager.deactivate();
  }

  /**
   * Returns {@link JavaPackageConverter} if available.
   *
   * @return a list with {@link Converter} that match with
   *         JavaPackageConverter (usually one).
   */
  private List<Converter> getJavaPackageConverterService() {
    return this.serviceManager.getServiceInstance(this, Converter.class,
        new ServiceInstaceManager.Matcher<Converter>() {

          @Override
          public boolean match(Converter service) {
            if (service instanceof JavaPackageConverter) {
              return true;
            }
            return false;
          }

        });
  }

  @CliAvailabilityIndicator("jms receiver")
  public boolean isInstallJmsReceiverAvailable() {
    return jmsOperations.isJmsInstallationPossible();
  }

  @CliOptionAutocompleteIndicator(
      command = "jms receiver",
      param = "service",
      validate = false,
      includeSpaceOnFinish = false,
      help = "--service parameter parameter is the service where will be added the support to send emails")
  public List<String> returnApplicationPackages(ShellContext shellContext) {

    List<String> applicationPackages = new ArrayList<String>();

    // Get only application modules
    StringBuffer matcher = new StringBuffer("feature[");
    matcher.append(ModuleFeatureName.APPLICATION).append("]");

    JavaPackageConverter converter = (JavaPackageConverter) getJavaPackageConverterService().get(0);
    List<Completion> completions = new ArrayList<Completion>();
    converter.getAllPossibleValues(completions, String.class,
        shellContext.getParameters().get("service"), matcher.toString(), null);

    for (Completion completion : completions) {
      applicationPackages.add(completion.getValue());
    }

    return applicationPackages;
  }

  @CliCommand(value = "jms receiver", help = "Create an JMS receiver")
  public void addJmsReceiver(
      @CliOption(key = {"destinationName"}, mandatory = true,
          help = "The name of the JMS destination") final String destinationName,
      @CliOption(key = "service", mandatory = true,
          help = "The service where include the method that receive JMS messages") final JavaType service,
      @CliOption(key = {"jndiConnectionFactory"}, mandatory = true,
          help = "The jndi name where the JMS receiver configuration has been defined") final String jndiConnectionFactory,
      @CliOption(key = {"profile"}, mandatory = false,
          help = "The profile where the properties will be set") final String profile,
      ShellContext shellContext) {
    jmsOperations.addJmsReceiver(destinationName, service, jndiConnectionFactory, profile,
        shellContext.isForce());
  }

  @CliOptionAutocompleteIndicator(command = "jms sender", param = "destinationName",
      validate = false, includeSpaceOnFinish = false,
      help = "--destinationName parameter composed by application module and destination name")
  public List<String> returnApplicationModules(ShellContext shellContext) {

    List<String> applicationModules = new ArrayList<String>();

    Collection<String> moduleNames =
        typeLocationService.getModuleNames(ModuleFeatureName.APPLICATION);

    for (String moduleName : moduleNames) {
      applicationModules.add(moduleName.concat(":"));
    }

    return applicationModules;
  }

  @CliCommand(value = "jms sender", help = "Create an JMS receiver")
  public void addJmsSender(
      @CliOption(
          key = {"destinationName"},
          mandatory = true,
          help = "The name of the JMS destination. Composed by application module and destination name") final String destinationName,
      @CliOption(key = "service", mandatory = true,
          help = "The service where include the method that receive JMS messages") final JavaType service,
      @CliOption(key = {"jndiConnectionFactory"}, mandatory = true,
          help = "The jndi name where the JMS receiver configuration has been defined") final String jndiConnectionFactory,
      @CliOption(key = {"profile"}, mandatory = false,
          help = "The profile where the properties will be set") final String profile,
      ShellContext shellContext) {

    jmsOperations.addJmsSender(destinationName, service, jndiConnectionFactory, profile,
        shellContext.isForce());
  }

}
