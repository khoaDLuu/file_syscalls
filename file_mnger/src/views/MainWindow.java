import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;

import javax.swing.*;

public class MainWindow extends JFrame {
  JPanel main;
  TopPanel top;
  MidPanel mid;
  BotPanel bot;

  public static void main(String[] args) {
    new MainWindow();
  }

  public MainWindow() {
    // DI here
    main = new JPanel(new GridBagLayout());
    top = new TopPanel();
    mid = new MidPanel();
    bot = new BotPanel();

    this.add(main);

    main.add(
        top,
        new LayoutCons()
        .grid(0, 0)
        .weight(1, 0)
        .fill_(GridBagConstraints.VERTICAL)
        .anchor_(GridBagConstraints.FIRST_LINE_START)
    );

    main.add(
        mid,
        new LayoutCons()
        .grid(0, 1)
        .weight(1, 1)
        .ipad(0, 60)
        .fill_(GridBagConstraints.BOTH)
        .anchor_(GridBagConstraints.CENTER)
    );

    main.add(
        bot,
        new LayoutCons()
        .grid(0, 2)
        .weight(1, 0)
        .ipad(0, 60)
        .fill_(GridBagConstraints.BOTH)
        .anchor_(GridBagConstraints.PAGE_END)
    );

    this.configGeometry();
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  private void configGeometry() {
    setBounds(400, 50, 600, 600);
    setMinimumSize(new Dimension(400, 400));
    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent evt) {
        Dimension size = getSize();
        Dimension min = getMinimumSize();
        if (size.getWidth() < min.getWidth()) {
          setSize((int) min.getWidth(), (int) size.getHeight());
        }
        if (size.getHeight() < min.getHeight()) {
          setSize((int) size.getWidth(), (int) min.getHeight());
        }
      }
    });
  }
}

