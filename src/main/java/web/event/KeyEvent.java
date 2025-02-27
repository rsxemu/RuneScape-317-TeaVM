package web.event;

public abstract class KeyEvent extends Event {

    public static final int TYPE_TYPED = 400;
    public static final int TYPE_PRESSED = 401;
    public static final int TYPE_RELEASED = 402;

    public static final int VK_LEFT = 0x25;
    public static final int VK_UP = 0x26;
    public static final int VK_RIGHT = 0x27;
    public static final int VK_DOWN = 0x28;

    public static final int VK_CONTROL = 0x11;
    public static final int VK_ALT = 0x12;
    public static final int VK_DELETE = 0x7F;
    public static final int VK_ENTER = '\n';
    public static final int VK_BACK_SPACE = '\b';
    public static final int VK_TAB = '\t';
    public static final int VK_ESC = 0x1B;
    public static final int VK_SHIFT = 0x10;
    public static final int VK_INSERT = 0x9B;

    public static final int VK_PRINT_SCREEN = 0x9A;
    public static final int VK_PAUSE = 0x13;
    public static final int VK_SCROLL_LOCK = 0x91;

    public static final int VK_F1 = 0x70;
    public static final int VK_F2 = VK_F1 + 1;
    public static final int VK_F3 = VK_F1 + 2;
    public static final int VK_F4 = VK_F1 + 3;
    public static final int VK_F5 = VK_F1 + 4;
    public static final int VK_F6 = VK_F1 + 5;
    public static final int VK_F7 = VK_F1 + 6;
    public static final int VK_F8 = VK_F1 + 7;
    public static final int VK_F9 = VK_F1 + 8;
    public static final int VK_F10 = VK_F1 + 9;
    public static final int VK_F11 = VK_F1 + 10;
    public static final int VK_F12 = VK_F1 + 11;

    public static final int VK_HOME = 0x24;
    public static final int VK_END = 0x23;
    public static final int VK_PAGE_UP = 0x21;
    public static final int VK_PAGE_DOWN = 0x22;

    public static final int VK_AUDIO_MUTE = 0xE000;
    public static final int VK_AUDIO_DOWN = 0xE001;
    public static final int VK_AUDIO_UP = 0xE002;

    @Override
    public final int getType() {
        return TYPE_KEY;
    }

    public abstract int getKeyCode();

    public abstract char getKeyChar();
}
