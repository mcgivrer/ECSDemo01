package com.snapgames.framework.services;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JFrame;

import com.snapgames.framework.App;
import com.snapgames.framework.components.GraphicComponent;
import com.snapgames.framework.components.PhysicComponent;
import com.snapgames.framework.components.PriorityComponent;
import com.snapgames.framework.entities.Camera;
import com.snapgames.framework.entities.Entity;

public class RenderingService extends AbstractService {

    List<Entity> renderingList = new ArrayList<>();
    private Camera cameraActive;
    private BufferedImage renderingBuffer = null;
    private JFrame frame;
    private SceneManagerService scnMgr;

    public RenderingService(App app) {
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
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                app.requestExit(true);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {
                //app.setPause(true);
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                //app.setPause(false);
                frame.repaint();
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
        scnMgr = app.getService(SceneManagerService.class.getSimpleName());
    }

    @Override
    public void process(App app) {
        Graphics2D g = renderingBuffer.createGraphics();
        cameraActive = scnMgr.getCurrentScene().getCamera();
        // Configure rendering graphics API
        g.setRenderingHints(Map.of(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON,
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON,
                RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));

        // Retrieve required services
        EntityManagerService entMgr = (EntityManagerService) app.getService(EntityManagerService.class.getSimpleName());
        renderingList = entMgr.getEntities().stream().filter(Entity::isActive).toList();

        // Clear the rendering buffer;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, renderingBuffer.getWidth(), renderingBuffer.getHeight());

        // Render all objects through camera viewport (if an active camera exists)
        if (Optional.ofNullable(cameraActive).isPresent()) {
            renderingList = renderingList.stream().filter(e -> cameraActive.hasEntityInView(e)).toList();
            PhysicComponent pc = cameraActive.getComponent(PhysicComponent.class);
            g.translate(-pc.getPosition().getX(), -pc.getPosition().getY());
        }
        // Draw all active sorted entities.
        renderingList.stream().sorted((e1, e2) -> {
            PriorityComponent p1 = e1.getComponent(PriorityComponent.class);
            PriorityComponent p2 = e2.getComponent(PriorityComponent.class);
            return Integer.compare(p1.getPriority(), p1.getPriority());
        }).forEach(e -> drawEntity(g, e));

        // Move back to normal position (if an active camera exists)
        if (Optional.ofNullable(cameraActive).isPresent()) {
            PhysicComponent pc = cameraActive.getComponent(PhysicComponent.class);
            g.translate(pc.getPosition().getX(), pc.getPosition().getY());
        }

        // now copy buffer to window.
        drawToFrame(renderingBuffer);
    }

    private void drawToFrame(BufferedImage renderingBuffer) {
        Graphics2D g = (Graphics2D) frame.getBufferStrategy().getDrawGraphics();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        g.drawImage(renderingBuffer,
                0, 0, frame.getWidth(), frame.getHeight(),
                0, 0, renderingBuffer.getWidth(),renderingBuffer.getHeight(),
                null);
        frame.getBufferStrategy().show();
        g.dispose();

    }

    private void drawEntity(Graphics2D g, Entity e) {
        GraphicComponent gc = e.getComponent(GraphicComponent.class);
        if (Optional.ofNullable(gc).isPresent()) {
            g.setColor(gc.getFillColor());
            g.fill(gc.getShape());
            g.setColor(gc.getColor());
            g.draw(gc.getShape());
            if (app.isDebugLevelGreaterThan(0)) {
                g.setColor(Color.ORANGE);
                g.draw(gc.getShape());
            }
        }
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public void dispose(App app) {
        if (Optional.ofNullable(frame).isPresent()) {
            frame.dispose();
        }
    }

    @Override
    public Map<String, Object> getStats() {
        return Map.of();
    }

    public void addListener(InputService inputService) {
        frame.addKeyListener(inputService);
    }
}
