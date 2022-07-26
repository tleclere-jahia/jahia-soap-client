package org.foo.modules.jahia.actions;

import org.foo.modules.jahia.dolist.DoListService;
import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Component(service = Action.class)
public class DoListAction extends Action {
    private static final Logger logger = LoggerFactory.getLogger(DoListAction.class);

    private DoListService doListService;

    public DoListAction() {
        setName("dolist");
        setRequireAuthenticatedUser(false);
        setRequiredMethods("GET,POST");
    }

    @Reference(service = DoListService.class)
    private void setDoListContactService(DoListService doListService) {
        this.doListService = doListService;
    }

    @Override
    public ActionResult doExecute(HttpServletRequest httpServletRequest, RenderContext renderContext, Resource resource, JCRSessionWrapper jcrSessionWrapper, Map<String, List<String>> map, URLResolver urlResolver) throws Exception {
        logger.info("Contacts: {}", doListService.getContacts());
        return ActionResult.OK;
    }
}
