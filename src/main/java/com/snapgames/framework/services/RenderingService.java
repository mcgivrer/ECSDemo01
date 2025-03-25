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
import java.util.stream.Collectors;

import javax.swing.JFrame;

import com.snapgames.framework.App;
import com.snapgames.framework.components.*;
import com.snapgames.framework.entities.Camera;
import com.snapgames.framework.entities.Entity;

/**
 * The RenderingService class is responsible for rendering graphical elements of the application.
 * It manages a rendering buffer, active camera, and utilizes a JFrame for displaying the rendered content.
 * The service interacts with various components such as entities, graphics, physics, and input services
 * to display entities and additional visual elements to the user.
 * <p>
 * RenderingService extends the AbstractService and overrides its lifecycle methods including initialization,
 * processing, disposal, and obtaining service statistics.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class RenderingService extends AbstractService {

    /**
     * The {@code renderingList} is a thread-safe list that maintains a collection of
     * {@link Entity} objects to be processed within the rendering pipeline of the
     * {@link RenderingService}. This list is implemented using a {@link CopyOnWriteArrayList}
     * to ensure consistent iteration and modification during concurrent access.
     * <p>
     * It centralizes all entities intended for rendering and is primarily utilized by methods
     * responsible for drawing and rendering operations. The list's thread-safe behavior ensures
     * reliability and accuracy when multiple threads are interacting with the rendering data.
     */
    List<Entity> renderingList = new CopyOnWriteArrayList<>();
    /**
     * Represents the currently active {@link Camera} being used by the {@link RenderingService}
     * for rendering the scene. This camera determines the portion of the game world
     * that is visible on the screen and manages viewport-related operations.
     * It can track a specific target entity and handle adjustments like tweening
     * or viewport resizing.
     * <p>
     * This field is a private member of the {@link RenderingService} class and plays
     * a key role in controlling the rendered perspective of the game world.
     */
    private Camera cameraActive;
    /**
     * The renderingBuffer is an internal buffer used for rendering operations within
     * the RenderingService. It provides a temporary storage space for graphical
     * content to be drawn before being transferred to the final output, ensuring
     * efficient rendering and reducing flickering effects.
     */
    private BufferedImage renderingBuffer = null;
    /**
     * The main application window used for rendering and displaying content.
     * It serves as the primary container for graphical output generated by the service.
     */
    private JFrame frame;
    /**
     * The {@code scnMgr} variable is an instance of {@link SceneManagerService}, responsible
     * for managing and coordinating multiple scenes within the application. It aids in
     * the initialization, activation, processing, and disposal of scenes, serving as a
     * critical component of the rendering and scene management pipeline within the
     * {@code RenderingService}.
     */
    private SceneManagerService scnMgr;


    private long currentTime = 0;
    static double cumulated = 0;
    private int nbRenderedEntities = 0;

    /**
     * Constructor for the RenderingService class.
     * Initializes the rendering service by calling the superclass constructor.
     *
     * @param app the application instance used to configure and manage the service
     */
    public RenderingService(App app) {
        super(app);
    }

    /**
     * Retrieves the name of the service.
     *
     * @return the simple name of the RenderingService class.
     */
    @Override
    public String getName() {
        return RenderingService.class.getSimpleName();
    }

    /**
     * Initializes the rendering service by configuring the rendering context and window properties.
     * Sets up the rendering buffer, application window, and buffer strategy. Additionally,
     * registers the window listener to manage application lifecycle events.
     *
     * @param app  the application instance used to configure and manage services
     * @param args the arguments passed during the application initialization
     */
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

    /**
     * Handles the main rendering process for the current game scene. Renders entities and graphical components
     * onto the rendering buffer, applies camera transformations, and updates the display frame.
     *
     * @param app the application instance containing the required services and context for processing
     */
    @Override
    public void process(App app) {
        double FPS = 60.0;
        long previousTime = currentTime;
        currentTime = System.currentTimeMillis();
        ;
        if (cumulated > 1000.0 / FPS) {
            drawEntitiesToScreen(app);
            cumulated = 0;
        }

        double elapsed = currentTime - previousTime;
        cumulated += elapsed;
    }

    private void drawEntitiesToScreen(App app) {
        Graphics2D g = renderingBuffer.createGraphics();
        cameraActive = scnMgr.getCurrentScene().getCamera();
        nbRenderedEntities = 0;

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
            nbRenderedEntities = nbRenderedEntities + renderingList.size();
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
        nbRenderedEntities = nbRenderedEntities + renderingList.size();
        // Draw all active stuck to viewport entities.
        drawAllEntities(g, renderingList);

        // now copy buffer to window.
        drawToFrame(renderingBuffer);
    }

    /**
     * Renders all entities in the provided list by sorting them according to their
     * priority and invoking the drawing logic for each entity.
     *
     * @param g             the {@code Graphics2D} context used to render the entities
     * @param renderingList the list of {@code Entity} objects to be rendered
     */
    private void drawAllEntities(Graphics2D g, List<Entity> renderingList) {
        renderingList.stream().sorted((e1, e2) -> {
            PriorityComponent p1 = e1.getComponent(PriorityComponent.class);
            PriorityComponent p2 = e2.getComponent(PriorityComponent.class);
            return Integer.compare(p1.getPriority(), p2.getPriority());
        }).forEach(e -> drawEntity(g, e));
    }

    /**
     * Draws the content of the provided rendering buffer onto the application frame.
     * This method clears the frame with a dark gray background before rendering the
     * given buffer image. The rendered content is then displayed using the frame's
     * buffer strategy.
     *
     * @param renderingBuffer the {@link BufferedImage} containing the content to be rendered
     *                        onto the application frame.
     */
    private void drawToFrame(BufferedImage renderingBuffer) {
        Graphics2D g = (Graphics2D) frame.getBufferStrategy().getDrawGraphics();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        g.drawImage(renderingBuffer, 0, 0, frame.getWidth(), frame.getHeight(), 0, 0, renderingBuffer.getWidth(),
                renderingBuffer.getHeight(), null);
        if (app.isDebugLevelGreaterThan(0)) {
            g.setColor(Color.ORANGE);
            String dbgValues = app.getServicesStatistics().entrySet().stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining(" | ", "[", "]"));
            g.drawString(dbgValues, 10, frame.getHeight() - 12);
        }
        frame.getBufferStrategy().show();
        g.dispose();

    }

    /**
     * Renders an entity's graphical components and associated features onto the provided graphics context.
     * This method processes various components like shapes, text, grids, and gauges, and applies them to
     * the {@code Graphics2D} context. Additional debugging visuals may also be drawn based on application settings.
     *
     * @param g the {@code Graphics2D} context used to render the entity
     * @param e the {@code Entity} to be drawn, which contains graphical components to be rendered
     */
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
                g.setColor(gg.getGaugeColor());
                double width = pc.getSize().getX() * (gg.getMax() - gg.getMin()) / gg.getValue();
                g.fill(new Rectangle2D.Double(pc.getPosition().getX() + 2, pc.getPosition().getY() + 2, (int) width - 3, pc.getSize().getY() - 3));
                g.setColor(gc.getColor());
                g.draw(gc.getShape());

            }
            if (app.isDebugLevelGreaterThan(0)) {
                g.setColor(Color.ORANGE);
                g.draw(gc.getShape());
            }
        }

    }

    /**
     * Retrieves the priority of the service for execution order during
     * initialization, processing, and disposing.
     *
     * @return an integer representing the priority level of the service.
     */
    @Override
    public int getPriority() {
        return 5;
    }

    /**
     * Disposes of the resources used by the {@code RenderingService}. Specifically,
     * releases and disposes of the application frame if it is present.
     *
     * @param app the application instance that contains the context and services used
     *            by the {@code RenderingService}
     */
    @Override
    public void dispose(App app) {
        if (Optional.ofNullable(frame).isPresent()) {
            frame.dispose();
        }
    }

    /**
     * Retrieves the current statistics of the rendering service.
     *
     * @return a {@code Map<String, Object>} containing key-value pairs representing
     * the statistics of the rendering service.
     */
    @Override
    public Map<String, Object> getStats() {
        return Map.of("FPS", 60, "rendered", nbRenderedEntities);
    }

    /**
     * Registers an input listener to the application frame to handle key events.
     *
     * @param inputService the {@link InputService} instance to be added as a key listener
     *                     to the application frame.
     */
    public void addListener(InputService inputService) {
        frame.addKeyListener(inputService);
    }
}
