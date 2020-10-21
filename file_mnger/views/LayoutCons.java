import java.awt.GridBagConstraints;

public class LayoutCons extends GridBagConstraints {
  public LayoutCons grid(int x, int y) {
    this.gridx = x;
    this.gridy = y;
    return this;
  }

  public LayoutCons weight(int x, int y) {
    this.weightx = x;
    this.weighty = y;
    return this;
  }

  public LayoutCons ipad(int x, int y) {
    this.ipadx = x;
    this.ipady = y;
    return this;
  }

  public LayoutCons fill_(int f) {
    this.fill = f;
    return this;
  } 

  public LayoutCons anchor_(int a) {
    this.anchor = a;
    return this;
  } 
}

