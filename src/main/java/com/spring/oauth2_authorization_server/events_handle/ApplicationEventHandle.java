package com.spring.oauth2_authorization_server.events_handle;

public interface ApplicationEventHandle {
    void handleEvent() throws Exception;
    String startMessage();
    String successMessage();
}
