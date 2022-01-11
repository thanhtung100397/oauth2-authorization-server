package com.spring.oauth2_authorization_server.events_handle.resource_server_registration;

import com.spring.oauth2_authorization_server.annotations.event.EventHandler;
import com.spring.oauth2_authorization_server.events_handle.ApplicationEvent;
import com.spring.oauth2_authorization_server.events_handle.ApplicationEventHandle;
import com.spring.oauth2_authorization_server.modules.oauth2_admin.models.entities.OAuth2AdminResourceServer;
import com.spring.oauth2_authorization_server.modules.oauth2_admin.repositories.OAuth2AdminResourceServerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@EventHandler(event = ApplicationEvent.ON_APPLICATION_STARTED_UP)
public class OAuth2AdminResourceServerPersistenceService implements ApplicationEventHandle {
    private static Logger logger = LoggerFactory.getLogger(OAuth2AdminResourceServerPersistenceService.class);

    @Value("${security.oauth2.resource-server.id}")
    private String thisResourceID;

    @Autowired
    private OAuth2AdminResourceServerRepository oAuth2AdminResourceServerRepository;

    @Override
    public String startMessage() {
        return "Start registering this resource server...";
    }

    @Override
    public String successMessage() {
        return "registration this resource server...OK";
    }

    @Override
    public void handleEvent() throws Exception {
        if (oAuth2AdminResourceServerRepository.existsById(thisResourceID)) {
            logger.info("This resource server ["+thisResourceID+"] exists");
        } else {
            oAuth2AdminResourceServerRepository.save(new OAuth2AdminResourceServer(thisResourceID));
            logger.info("This resource server ["+thisResourceID+"] registration");
        }
    }
}
