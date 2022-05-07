package com.aacoptics.perms.events;

import com.aacoptics.common.web.entity.ResourceDefinition;
import com.aacoptics.perms.service.impl.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BusReceiver {

    @Autowired
    private ResourceService resourceService;

    public void handleMessage(ResourceDefinition resourceDefinition) {
        log.info("Received Message:<{}>", resourceDefinition);
        resourceService.saveResource(resourceDefinition);
    }
}
