package ui.tea;

import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.KeyboardEvent;
import org.teavm.jso.dom.events.MouseEvent;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.TextRectangle;
import ui.AbstractComponent;
import ui.IFont;
import ui.IFontMetrics;
import ui.IGraphics;
import ui.event.*;
import ui.poly.Dimension;


class HtmlComponent extends AbstractComponent {

    private final HTMLCanvasElement canvas;
    private final CanvasRenderingContext2D context;
    private final double ratio;
    private final HtmlGraphics graphics;
    private final Dimension dim;

    public HtmlComponent(HTMLCanvasElement canvas, CanvasRenderingContext2D context, double ratio) {
        this.canvas = canvas;
        this.context = context;
        this.ratio = ratio;
        this.dim = new Dimension(canvas.getWidth(), canvas.getHeight());
        this.graphics = new HtmlGraphics(context);
        initEvents();
    }

    @Override
    public void dispatch(ui.event.Event e) {
        //TODO hooks for external tools to get events
        System.out.println(e);
        super.dispatch(e);
    }

    private void initEvents() {
        //add even listeners

        //context menu (stops system one from appearing)
        canvas.addEventListener("contextmenu", Event::preventDefault);

        //mouse events
        canvas.addEventListener("click", createMouseListener(ui.event.MouseEvent.TYPE_CLICKED));
        canvas.addEventListener("mousedown", createMouseListener(ui.event.MouseEvent.TYPE_PRESSED));
        canvas.addEventListener("mouseup", createMouseListener(ui.event.MouseEvent.TYPE_RELEASED));
        canvas.addEventListener("mousemove", createMouseListener(ui.event.MouseEvent.TYPE_MOVED));
        canvas.addEventListener("mouseleave", createMouseListener(ui.event.MouseEvent.TYPE_EXITED));
        canvas.addEventListener("mouseenter", createMouseListener(ui.event.MouseEvent.TYPE_ENTERED));

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
            if (key.length() > 1) {
                code += 1000;
            }
            char c = key.charAt(0);
            ImmutableKeyEvent event = new ImmutableKeyEvent(type,
                    code, c);
            dispatch(event);
        };
    }

    private EventListener<MouseEvent> createMouseListener(int type) {
        return evt -> {
            TextRectangle bounds = canvas.getBoundingClientRect();
            double sX = canvas.getWidth() / bounds.getWidth();
            double sY = canvas.getHeight() / bounds.getHeight();

            int x = evt.getClientX() - bounds.getLeft();
            int y = evt.getClientY() - bounds.getTop();

            //x *= sX;
            //y *= sY;

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
        JSMethods.setSize(canvas, width, height);
        //canvas.getStyle().setProperty("width", width + "px");
        //canvas.getStyle().setProperty("height", height + "px");
        //context.setTransform(ratio, 0, 0, ratio, 0, 0);
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
        System.out.println("update");
    }

    @Override
    public void paint(IGraphics g) {
        System.out.println("paint");
    }

    @Override
    public void repaint() {
        System.out.println("repaint");
    }

    @Override
    public IFontMetrics getFontMetrics(IFont font) {
        return new HtmlFontMetrics(font);
    }
}
