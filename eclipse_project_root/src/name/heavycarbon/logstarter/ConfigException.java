package name.heavycarbon.logstarter;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 *******************************************************************************
 * Signal that something went wrong with Configuration
 * 
 * 2011.10.26 - Created
 ******************************************************************************/

@SuppressWarnings("serial")
public class ConfigException extends Exception {

    public ConfigException() {
        super();
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }

}
