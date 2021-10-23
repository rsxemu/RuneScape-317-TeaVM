package web.impl.js;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.KeyboardEvent;
import org.teavm.jso.dom.events.MouseEvent;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.TextRectangle;
import web.AbstractComponent;
import web.IFont;
import web.IFontMetrics;
import web.IGraphics;
import web.event.*;
import web.util.Dimension;


class JSComponent extends AbstractComponent {

    private static final Logger logger = LoggerFactory.getLogger(JSComponent.class);

    private final HTMLCanvasElement canvas;
    private final CanvasRenderingContext2D context;
    private final JSGraphics graphics;
    private final Dimension dim;

    public JSComponent(HTMLCanvasElement canvas, CanvasRenderingContext2D context) {
        this.canvas = canvas;
        this.context = context;
        this.dim = new Dimension(canvas.getWidth(), canvas.getHeight());
        this.graphics = new JSGraphics(context);
        initEvents();
    }

    @Override
    public void dispatch(web.event.Event e) {
        if(JSConfig.get().logEvents()) {
            logger.info("Client event:" + e);
        }
        super.dispatch(e);
    }

    private void initEvents() {
        //add event listeners

        //context menu (stops system one from appearing)
        canvas.addEventListener("contextmenu", Event::preventDefault);

        //mouse events
        canvas.addEventListener("click", createMouseListener(web.event.MouseEvent.TYPE_CLICKED));
        canvas.addEventListener("mousedown", createMouseListener(web.event.MouseEvent.TYPE_PRESSED));
        canvas.addEventListener("mouseup", createMouseListener(web.event.MouseEvent.TYPE_RELEASED));
        canvas.addEventListener("mousemove", createMouseListener(web.event.MouseEvent.TYPE_MOVED));
        canvas.addEventListener("mouseleave", createMouseListener(web.event.MouseEvent.TYPE_EXITED));
        canvas.addEventListener("mouseenter", createMouseListener(web.event.MouseEvent.TYPE_ENTERED));

        //keyboard events
        canvas.addEventListener("keyup", createKeyListener(KeyEvent.TYPE_RELEASED));
        canvas.addEventListener("keydown", createKeyListener(KeyEvent.TYPE_PRESSED));

        //focus events
        canvas.addEventListener("focus", createFocusListener(FocusEvent.TYPE_GAINED));
        canvas.addEventListener("focusout", createFocusListener(FocusEvent.TYPE_LOST));
    }

    private EventListener<?> createFocusListener(int type) {
        return evt -> dispatch(new ImmutableFocusEvent(type));
    }

    private EventListener<KeyboardEvent> createKeyListener(int type) {
        return evt -> {
            evt.preventDefault(); // stop stuff like F1-F12 affecting the webpage
            String key = evt.getKey();
            int code = evt.getKeyCode();
            char c = key.charAt(0);
            if (key.length() > 1) {
                code += 1000;
                c = '\0';
            }
            ImmutableKeyEvent event = new ImmutableKeyEvent(type,
                    code, c);
            dispatch(event);
        };
    }

    private EventListener<MouseEvent> createMouseListener(int type) {
        return evt -> {
            TextRectangle bounds = canvas.getBoundingClientRect();

            int x = evt.getClientX() - bounds.getLeft();
            int y = evt.getClientY() - bounds.getTop();


            dispatch(new ImmutableMouseEvent(type,
                    x, y, evt.getButton() == 2));
        };
    }

    public CanvasRenderingContext2D getContext() {
        return context;
    }

    @Override
    public IGraphics getGraphics() {
        return graphics;
    }

    @Override
    public void setSize(int width, int height) {
        canvas.setWidth(width);
        canvas.setHeight(height);
        dim.setSize(width, height);
    }

    @Override
    public Dimension getSize() {
        return dim;
    }

    @Override
    public void setPreferredSize(Dimension dimensions) {
        setSize(dimensions.width, dimensions.height);
    }

    @Override
    public void requestFocus() {
        canvas.focus();
    }

    @Override
    public void update(IGraphics g) {
        logger.info("update({})", g);
    }

    @Override
    public void paint(IGraphics g) {
        logger.info("paint({})", g);
    }

    @Override
    public void repaint() {
        logger.info("repaint()");
    }

    @Override
    public IFontMetrics getFontMetrics(IFont font) {
        return new JSFontMetrics(font);
    }
}
