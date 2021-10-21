package ui.awt.impl;

import ui.*;
import ui.awt.event.AWTEventAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class JVMWindowEngine extends WindowEngine {

    private static final JVMAllocator alloc = new JVMAllocator();

    @Override
    public IComponent createComponent() {
        Canvas res = new Canvas();
        IComponent component = new AWTComponent(res);
        AWTEventAdapter adapter = new AWTEventAdapter(component);
        res.addFocusListener(adapter);
        res.addKeyListener(adapter);
        res.addMouseListener(adapter);
        res.addMouseMotionListener(adapter);
        return component;
    }

    @Override
    public IFont getFont(String name, int style, int size) {
        return new AWTFont(name, style, size);
    }

    @Override
    public IImage createImage(byte[] data) {
        try {
            BufferedImage br = ImageIO.read(new ByteArrayInputStream(data));
            if (br.getType() != BufferedImage.TYPE_INT_RGB) {
                BufferedImage tmp = new BufferedImage(br.getWidth(), br.getHeight(), BufferedImage.TYPE_INT_RGB);
                tmp.getGraphics().drawImage(br, 0, 0, null);
                br = tmp;
            }
            return
                    new AWTImage(br);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IImage createImage(int width, int height, int type) {
        int t = 0;
        switch (type) {
            case IImage.TYPE_INT_RGB:
                t = BufferedImage.TYPE_INT_RGB;
                break;
            default:
                throw new RuntimeException("Bad type:" + type);
        }
        return new AWTImage(new BufferedImage(width, height, t));
    }

    @Override
    public ISocket openSocket(String server, int port) throws IOException {
        return new JVMSocket(new Socket(InetAddress.getByName(server), port));
    }

    @Override
    public IAllocator alloc() {
        return alloc;
    }

    @Override
    public void schedule(Runnable r, int updateInterval) {
        throw new UnsupportedOperationException("Unimplemented");
    }
}
