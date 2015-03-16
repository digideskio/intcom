package nl.hu.v2iac1;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

public class SampleApplication extends ResourceConfig {
    public SampleApplication() {
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(Configuration.class).to(Configuration.class);
            }
        });
    }
}