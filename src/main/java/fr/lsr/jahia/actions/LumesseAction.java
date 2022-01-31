package fr.lsr.jahia.actions;

import fr.lsr.jahia.ws.LumesseService;
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

@Component(service = Action.class, immediate = true)
public class LumesseAction extends Action {
    private static final Logger logger = LoggerFactory.getLogger(LumesseAction.class);

    private LumesseService lumesseService;

    public LumesseAction() {
        setName("lumesse");
        setRequireAuthenticatedUser(false);
        setRequiredMethods("GET,POST");
    }

    @Reference(service = LumesseService.class)
    public void setLumesseService(LumesseService lumesseService) {
        this.lumesseService = lumesseService;
    }

    @Override
    public ActionResult doExecute(HttpServletRequest httpServletRequest, RenderContext renderContext, Resource resource, JCRSessionWrapper jcrSessionWrapper, Map<String, List<String>> parameters, URLResolver urlResolver) {
        int nbAdvertisements = lumesseService.countAdvertisements();
        logger.info("Nb Advertisements: {}", nbAdvertisements);
        // Set<Advertisement> ads = lumesseService.getAllAdvertisements();
        // logger.info("Advertisements: {}", ads);
        // lumesseService.updateFromLumesse();
        return ActionResult.OK;
    }
}
