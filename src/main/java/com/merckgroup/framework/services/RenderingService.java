package com.merckgroup.framework.services;

import com.merckgroup.framework.App;
import com.merckgroup.framework.components.GraphicComponent;
import com.merckgroup.framework.components.PhysicComponent;
import com.merckgroup.framework.components.PriorityComponent;
import com.merckgroup.framework.entities.Camera;
import com.merckgroup.framework.entities.Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.merckgroup.framework.App.error;
import static java.util.stream.Collectors.toList;

public class RenderingService extends AbstractService {

    List<Entity> renderingList = new ArrayList<>();
    private Camera cameraActive;
    private BufferedImage renderingBuffer = null;
    private JFrame frame;


    protected RenderingService(App app) {
        super(app);
    }

    @Override
    public String getName() {
        return RenderingService.class.getSimpleName();
    }

    @Override
    public void init(App app, String[] args) {
        ConfigurationService cs = (ConfigurationService) app.getService(ConfigurationService.class.getSimpleName());
        Dimension bufferSize = cs.getValue("app.render.buffer.size");
        renderingBuffer = new BufferedImage(bufferSize.width, bufferSize.height, BufferedImage.TYPE_INT_ARGB);
        Dimension windowSize = cs.getValue("app.render.window.size");
        String windowTitle = cs.getValue("app.window.title");
        int maxBuffers = cs.getValue("app.render.window.max.buffers");
        frame = new JFrame(windowTitle);
        frame.setPreferredSize(windowSize);
        frame.pack();
        frame.setVisible(true);
        frame.createBufferStrategy(maxBuffers);
    }

    @Override
    public void process(App app) {
        Graphics2D g = renderingBuffer.createGraphics();
        g.setRenderingHints(
                Map.of(
                        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON,
                        RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON,
                        RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        renderingList.clear();

        EntityManager entMgr = (EntityManager) app.getService(EntityManager.class.getSimpleName());
        renderingList = entMgr.getEntities().stream().filter(Entity::isActive).toList();

        // render all objects through camera viewport (if an active camera exists)
        if (Optional.ofNullable(cameraActive).isPresent()) {
            renderingList = renderingList.stream().filter(e -> cameraActive.hasEntityInView(e)).toList();
            PhysicComponent pc = cameraActive.getComponent(PhysicComponent.class);
            g.translate(-pc.getPosition().getX(), -pc.getPosition().getY());
        }
        // draw all active sorted entities.
        renderingList.stream().sorted((e1, e2) -> {
            PriorityComponent p1 = e1.getComponent(PriorityComponent.class);
            PriorityComponent p2 = e2.getComponent(PriorityComponent.class);
            return Integer.compare(p1.getPriority(), p1.getPriority());
        }).forEach(e -> drawEntity(g, e));

        // move back to normal position (if an active camera exists)
        if (Optional.ofNullable(cameraActive).isPresent()) {
            PhysicComponent pc = cameraActive.getComponent(PhysicComponent.class);
            g.translate(pc.getPosition().getX(), pc.getPosition().getY());
        }
        drawToFrame(renderingBuffer);
    }

    private void drawToFrame(BufferedImage renderingBuffer) {
        Graphics g = frame.getBufferStrategy().getDrawGraphics();
        g.drawImage(renderingBuffer, 0, 0, frame.getWidth(), frame.getHeight(),
                0, 0, renderingBuffer.getWidth(), renderingBuffer.getHeight(), null);
        g.dispose();
        frame.getBufferStrategy().show();
    }

    private void drawEntity(Graphics2D g, Entity e) {
        GraphicComponent gc = e.getComponent(GraphicComponent.class);
        if (Optional.ofNullable(gc).isPresent()) {
            g.setColor(gc.getFillColor());
            g.fill(gc.getShape());
            g.setColor(gc.getColor());
            g.draw(gc.getShape());
        }
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public void dispose(App app) {

    }

    @Override
    public Map<String, Object> getStats() {
        return Map.of();
    }
}
