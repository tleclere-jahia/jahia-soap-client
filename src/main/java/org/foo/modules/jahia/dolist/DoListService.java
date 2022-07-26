package org.foo.modules.jahia.dolist;

import org.foo.modules.jahia.dolist.ws.contact.AuthenticationTokenContext;
import org.jahia.utils.ClassLoaderUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component(service = DoListService.class)
public class DoListService {
    private static final Logger logger = LoggerFactory.getLogger(DoListService.class);

    private DoListContactClient doListContactClient;

    public DoListService() {
        doListContactClient = null;
    }

    @Activate
    private void onActivate(Map<String, ?> configuration) {
        if (configuration.containsKey("accountId") && configuration.containsKey("authenticationKey")) {
            AuthenticationTokenContext authenticationTokenContext = ClassLoaderUtils.executeWith(DoListService.class.getClassLoader(),
                            () -> new DoListAuthenticationClient((String) configuration.get("accountId"), (String) configuration.get("authenticationKey")))
                    .getAuthenticationTokenContext();
            doListContactClient = ClassLoaderUtils.executeWith(DoListService.class.getClassLoader(),
                    () -> new DoListContactClient(authenticationTokenContext));
        } else {
            throw new NullPointerException();
        }
    }

    public List<String> getContacts() {
        if (doListContactClient == null) {
            return Collections.emptyList();
        }
        return doListContactClient.getContacts();
    }
}
