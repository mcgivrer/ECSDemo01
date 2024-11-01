package com.snapgames.framework.services;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;

import com.snapgames.framework.App;
import com.snapgames.framework.components.*;
import com.snapgames.framework.entities.Camera;
import com.snapgames.framework.entities.Entity;

public class RenderingService extends AbstractService {

    List<Entity> renderingList = new CopyOnWriteArrayList<>();
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
                // app.setPause(true);
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                // app.setPause(false);
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

        PhysicEngineService pes = app.getService(PhysicEngineService.class.getSimpleName());

        // Configure rendering graphics API
        g.setRenderingHints(Map.of(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON,
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON,
                RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));

        // Retrieve required services
        EntityManagerService entMgr = (EntityManagerService) app.getService(EntityManagerService.class.getSimpleName());
        renderingList = entMgr.getEntities().stream().filter(e -> {
            GraphicComponent gc = e.getComponent(GraphicComponent.class);
            return e.isActive() && !gc.isStickToViewport();
        }).toList();

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
        drawAllEntities(g, renderingList);

        // draw world limit in debug mode
        if (app.isDebugLevelGreaterThan(0)) {
            g.setColor(Color.DARK_GRAY);
            g.draw(pes.getWorld().getPlayArea());
        }

        // Move back to normal position (if an active camera exists)
        if (Optional.ofNullable(cameraActive).isPresent()) {
            PhysicComponent pc = cameraActive.getComponent(PhysicComponent.class);
            g.translate(pc.getPosition().getX(), pc.getPosition().getY());
        }
        renderingList = entMgr.getEntities().stream().filter(e -> {
            GraphicComponent gc = e.getComponent(GraphicComponent.class);
            return e.isActive() && gc.isStickToViewport();
        }).toList();
        // Draw all active stuck to viewport entities.
        drawAllEntities(g, renderingList);

        // now copy buffer to window.
        drawToFrame(renderingBuffer);
    }

    private void drawAllEntities(Graphics2D g, List<Entity> renderingList) {
        renderingList.stream().sorted((e1, e2) -> {
            PriorityComponent p1 = e1.getComponent(PriorityComponent.class);
            PriorityComponent p2 = e2.getComponent(PriorityComponent.class);
            return Integer.compare(p1.getPriority(), p1.getPriority());
        }).forEach(e -> drawEntity(g, e));
    }

    private void drawToFrame(BufferedImage renderingBuffer) {
        Graphics2D g = (Graphics2D) frame.getBufferStrategy().getDrawGraphics();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        g.drawImage(renderingBuffer, 0, 0, frame.getWidth(), frame.getHeight(), 0, 0, renderingBuffer.getWidth(),
                renderingBuffer.getHeight(), null);
        frame.getBufferStrategy().show();
        g.dispose();

    }

    private void drawEntity(Graphics2D g, Entity e) {
        GraphicComponent gc = e.getComponent(GraphicComponent.class);
        if (Optional.ofNullable(gc).isPresent()) {
            if (gc.getShape() != null) {
                if (gc.getFillColor() != null) {
                    g.setColor(gc.getFillColor());
                    g.fill(gc.getShape());
                }
                if (gc.getColor() != null) {
                    g.setColor(gc.getColor());
                    g.draw(gc.getShape());
                }
            }
            if (e.containsComponent(TextComponent.class)) {
                TextComponent tc = e.getComponent(TextComponent.class);
                PhysicComponent pc = e.getComponent(PhysicComponent.class);
                g.setColor(tc.getTextColor());
                g.setFont(tc.getTextFont());
                g.drawString(tc.getText(), (int) pc.getPosition().getX(), (int) pc.getPosition().getY());
                int textHeight = g.getFontMetrics().getHeight();
                int textWidth = g.getFontMetrics().stringWidth(tc.getText());
                pc.setSize(textWidth, textHeight);
                gc.setShape(
                        new Rectangle2D.Double(
                                pc.getPosition().getX(),
                                pc.getPosition().getY() - textHeight,
                                textWidth, textHeight));
            }
            if (e.containsComponent(GridComponent.class)) {
                GridComponent gridC = e.getComponent(GridComponent.class);
                g.setColor(gc.getColor());
                gc.setShape(gridC.getBox());
                for (double ix = gridC.getBox().getX();
                     ix < gridC.getBox().getX() + gridC.getBox().getWidth();
                     ix += gridC.getTileWidth()) {
                    g.drawRect((int) ix, 0, gridC.getTileWidth(), (int) gridC.getBox().getHeight());
                }
                for (double iy = gridC.getBox().getY();
                     iy < gridC.getBox().getY() + gridC.getBox().getHeight();
                     iy += gridC.getTileHeight()) {
                    g.drawRect(0, (int) iy, (int) gridC.getBox().getWidth(), gridC.getTileHeight());
                }
            }
            if (e.containsComponent(GaugeComponent.class)) {
                PhysicComponent pc = e.getComponent(PhysicComponent.class);
                GaugeComponent gg = e.getComponent(GaugeComponent.class);
                g.setColor(gc.getFillColor());
                gc.setShape(pc.getBBox());
                g.fill(gc.getShape());
                g.setColor(gc.getColor());
                double width = pc.getSize().getX() * (gg.getMax() - gg.getMin()) / gg.getValue();
                g.fill(new Rectangle2D.Double(pc.getPosition().getX() + 2, pc.getPosition().getY() + 2, (int) width-4, pc.getSize().getY() - 3));
            }
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
